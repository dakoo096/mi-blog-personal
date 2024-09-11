package com.proyectoblog.my_personal_blog.repository;

import com.proyectoblog.my_personal_blog.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long>{
    
    //creamos los metodos
    //trae una lista de comentarios que se encuentra en X post
    List<CommentEntity> findByPostId(Long postId);
    
}
