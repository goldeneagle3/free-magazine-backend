package com.serbest.magazine.backend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;


    public Like() {
    }

    public Like(Post post, Comment comment,Author author) {
        this.post = post;
        this.comment = comment;
        this.author = author;

    }

    public Like(UUID id, Post post, Comment comment, Author author) {
        this.id = id;
        this.post = post;
        this.comment = comment;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
