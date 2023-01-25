package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Comment;
import com.serbest.magazine.backend.entity.Like;
import com.serbest.magazine.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    Like findByAuthorAndPost(Author author, Post post);

    @Modifying
    @Transactional
    @Query(value = "delete from likes l where l.post=:postId and l.author=:authorId",nativeQuery = true)
    void deleteByPostAndAuthor(UUID postId, UUID authorId);

    Like findByAuthorAndComment(Author author, Comment comment);

    @Modifying
    @Transactional
    @Query(value = "delete from likes l where l.comment=:commentId and l.author=:authorId",nativeQuery = true)
    void deleteByCommentAndAuthor(UUID commentId, UUID authorId);

    @Query(value = "SELECT username FROM likes l JOIN authors a ON a.id = l.author WHERE l.post=:postId",nativeQuery = true)
    List<String> findLikedAuthorsByPostId(UUID postId);

    @Query(value = "SELECT username FROM likes l JOIN authors a ON a.id = l.author WHERE l.comment=:commentId",nativeQuery = true)
    List<String> findLikedAuthorsByCommentId(UUID commentId);
}
