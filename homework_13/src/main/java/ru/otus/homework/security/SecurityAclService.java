package ru.otus.homework.security;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Service
public class SecurityAclService {
    private final DataSource dataSource;
    private final JdbcMutableAclService jdbcMutableAclService;

    public SecurityAclService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcMutableAclService = getJdbcAclService();
        this.jdbcMutableAclService.setSidIdentityQuery("SELECT currval('acl_sid_id_seq')");
        this.jdbcMutableAclService.setClassIdentityQuery("SELECT currval('acl_class_id_seq')");
    }

    public AclService getJdbcMutableAclService() {
        return jdbcMutableAclService;
    }

/*    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(jdbcMutableAclService);
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(jdbcMutableAclService));
        return expressionHandler;
    }*/

    @Transactional
    public void addSecurityUserRight(Class<?> clas, Serializable objectId, Permission permission, String user, boolean granting) {
        addSecurityRight(clas, objectId, permission, new PrincipalSid(user), granting);
    }

    @Transactional
    public void addSecurityRoleRight(Class<?> clas, Serializable objectId, Permission permission, String role, boolean granting) {
        addSecurityRight(clas, objectId, permission, new GrantedAuthoritySid(role), granting);
    }

    private JdbcMutableAclService getJdbcAclService() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setCacheNames(List.of("aclCache"));
        Cache cache = concurrentMapCacheManager.getCache("aclCache");
        AclAuthorizationStrategyImpl aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
        DefaultPermissionGrantingStrategy defaultPermissionGrantingStrategy = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
        AclCache aclCache = new SpringCacheBasedAclCache(cache, defaultPermissionGrantingStrategy, aclAuthorizationStrategy);
        BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, new ConsoleAuditLogger());
        return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }

    private void addSecurityRight(Class<?> clas, Serializable objectId, Permission permission, Sid sid, boolean granting) {
        Sid owner = getOwnerSid();
        ObjectIdentity oid = getOid(clas, objectId);
        MutableAcl acl = getMutableAcl(owner, sid, oid, permission, granting);
        this.jdbcMutableAclService.updateAcl(acl);
    }

    private Sid getOwnerSid() {
        return (Sid) new GrantedAuthoritySid("ROLE_ADMIN");
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new PrincipalSid(authentication);*/
    }

    private ObjectIdentity getOid(Class<?> clas, Serializable objectId) {
        return new ObjectIdentityImpl(clas, objectId);
    }

    private MutableAcl getMutableAcl(Sid owner, Sid sid, ObjectIdentity oid, Permission permission, boolean granting) {
        MutableAcl acl = this.jdbcMutableAclService.createAcl(oid);
        acl.setOwner(owner);
        acl.insertAce(acl.getEntries().size(), permission, sid, granting);
        return acl;
    }
}
