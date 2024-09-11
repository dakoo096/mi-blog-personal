package com.proyectoblog.my_personal_blog.repository;

import com.proyectoblog.my_personal_blog.entity.PostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    
    //creamos los metodos
    //trae una lista de post que realizo X usuario
    List<PostEntity> findByUserId(Long userId);

    //trae una lista de post que coincidan con un titulo
    List<PostEntity> findByTitleContainingIgnoreCase(String title);//convencion de nomenclaturas de spring
    
    
}
