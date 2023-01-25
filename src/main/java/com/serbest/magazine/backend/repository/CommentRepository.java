package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query(
            value = "select * from comments c where c.post=:postId order by c.create_date_time desc",
            nativeQuery = true
    )
    List<Comment> findAll(UUID postId);

}
