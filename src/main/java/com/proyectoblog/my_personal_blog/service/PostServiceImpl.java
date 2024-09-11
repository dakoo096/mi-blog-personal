package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.repository.PostRepository;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {//implementamos todos los metodos que creamos en la interface postService

   
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostEntity> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public Optional<PostEntity> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<PostEntity> getPostByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public void createPost(PostEntity post) {
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long id, PostEntity post) {
        PostEntity postDB = getPostById(id).orElseThrow(() -> new InvalidParameterException("Invalid post id"));
        postDB.setTitle(post.getTitle());
        postDB.setContent(post.getContent());
        postRepository.save(postDB);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<PostEntity> searchPostByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }
}