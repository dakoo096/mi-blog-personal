
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.CommentEntity;
import java.util.Optional;

public interface CommentService {
    
    Optional<CommentEntity> getCommentById(Long id);
    
    void createComment(CommentEntity comment);
    
    void updateComment(Long id,CommentEntity comment);
    
    void deleteComment(Long id);
}
