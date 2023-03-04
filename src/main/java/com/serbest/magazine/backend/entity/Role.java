package com.serbest.magazine.backend.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Author> authors;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(UUID id, String role) {
        this.id = id;
        this.name = role;
    }

    public Role(UUID id, String name, Set<Author> authors) {
        this.id = id;
        this.name = name;
        this.authors = authors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
