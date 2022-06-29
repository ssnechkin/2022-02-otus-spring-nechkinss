package ru.otus.homework.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.Serializable;

@Service
@Configuration
public class SecurityAclService {
    private final DataSource dataSource;
    private final JdbcMutableAclService jdbcMutableAclService;

    public SecurityAclService(DataSource dataSource, JdbcMutableAclService jdbcMutableAclService) {
        this.dataSource = dataSource;
        //this.jdbcMutableAclService = getJdbcMutableAclService();
        this.jdbcMutableAclService = jdbcMutableAclService;
    }

    public AclService getAclService() {
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

    private void addSecurityRight(Class<?> clas, Serializable objectId, Permission permission, Sid sid, boolean granting) {
        Sid owner = getOwnerSid();
        ObjectIdentity oid = getOid(clas, objectId);
        MutableAcl acl = getMutableAcl(owner, sid, oid, permission, granting);
        this.jdbcMutableAclService.updateAcl(acl);
    }

    /*private JdbcMutableAclService getJdbcMutableAclService() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setCacheNames(List.of("aclCache"));
        Cache cache = concurrentMapCacheManager.getCache("aclCache");
        AclAuthorizationStrategyImpl aclAuthorizationStrategy
                = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
        DefaultPermissionGrantingStrategy defaultPermissionGrantingStrategy
                = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
        AclCache aclCache
                = new SpringCacheBasedAclCache(cache, defaultPermissionGrantingStrategy, aclAuthorizationStrategy);
        BasicLookupStrategy lookupStrategy
                = new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, new ConsoleAuditLogger());
        return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }*/

    private Sid getOwnerSid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new PrincipalSid(authentication);
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
