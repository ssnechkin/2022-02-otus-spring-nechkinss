package ru.dpec.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int position;
    private String title;
    private Boolean alt;
    private String method;
    private String link;

    public Menu setMethod(String method) {
        this.method = method;
        return this;
    }

    public Menu setLink(String link) {
        this.link = link;
        return this;
    }

    public Menu setPosition(int position) {
        this.position = position;
        return this;
    }

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public Menu setAlt(Boolean alt) {
        this.alt = alt;
        return this;
    }
}
