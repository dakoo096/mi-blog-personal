
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.PostEntity;
import java.util.List;
import java.util.Optional;

public interface PostService {
    
    //trae toda la lista de post
    List<PostEntity> getAllPost();
    
    //obtener un post por el id
    Optional<PostEntity> getPostById(Long id);
    
    //trae una lista de post que realizo ese usuario
    List<PostEntity> getPostByUserId(Long userId);
    
    //crea un post
    void createPost(PostEntity post);
    
    //actualiza un post
    void updatePost(Long id,PostEntity post);
    
    //elimina un post
    void deletePostById(Long id);
    
    //buscar un post por titulo
    List<PostEntity> searchPostByTitle(String title);

}
