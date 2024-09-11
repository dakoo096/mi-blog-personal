
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.CommentEntity;
import com.proyectoblog.my_personal_blog.repository.CommentRepository;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    
    
    @Override
    public Optional<CommentEntity> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void createComment(CommentEntity comment) {
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentEntity comment) {
        //idem explicacion que post
        CommentEntity commentDB = getCommentById(id).orElseThrow(()-> new InvalidParameterException("Invalid comment id"));
        commentDB.setContent(comment.getContent());
        commentRepository.save(commentDB);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    
}
