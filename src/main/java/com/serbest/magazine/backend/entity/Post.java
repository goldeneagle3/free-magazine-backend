package com.serbest.magazine.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postId;

    private String title;
    private String subtitle;

    @Lob
    private String content;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "post_images",
            joinColumns = {
                    @JoinColumn(name = "post_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private ImageModel postImage;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;


    public Post(UUID postId, String title, String subtitle, String content,Boolean active, Category category,Author author, ImageModel postImage, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        this.postId = postId;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.active = active;
        this.category = category;
        this.author = author;
        this.postImage = postImage;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
    }

    public Post() {
    }

    private Post(Builder builder) {
        setPostId(builder.postId);
        setTitle(builder.title);
        setSubtitle(builder.subtitle);
        setContent(builder.content);
        setActive(builder.active);
        setCategory(builder.category);
        setAuthor(builder.author);
        setPostImage(builder.postImage);
    }


    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public ImageModel getPostImage() {
        if (postImage == null){
            return new ImageModel();
        };
        return postImage;
    }

    public void setPostImage(ImageModel postImage) {
        this.postImage = postImage;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public static final class Builder {
        private UUID postId;
        private String title;
        private String subtitle;
        private String content;
        private Boolean active;
        private Category category;
        private Author author;
        private ImageModel postImage;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder postId(UUID val) {
            postId = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder subtitle(String val) {
            subtitle = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder active(Boolean val) {
            active = val;
            return this;
        }

        public Builder category(Category val) {
            category = val;
            return this;
        }

        public Builder author(Author val) {
            author = val;
            return this;
        }

        public Builder postImage(ImageModel val) {
            postImage = val;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }
}
