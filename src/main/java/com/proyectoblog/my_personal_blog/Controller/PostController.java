package com.proyectoblog.my_personal_blog.controller;

import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.service.PostService;
import com.proyectoblog.my_personal_blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Optional<PostEntity> postOpt = postService.getPostById(id);
        if (postOpt.isPresent()) {
            return ResponseEntity.ok(postOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Post no encontrado"));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyPosts(HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        List<PostEntity> posts = postService.getPostByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostEntity>> searchPosts(@RequestParam("title") String title) {
        List<PostEntity> posts = postService.searchPostByTitle(title);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostEntity post, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        Optional<UserEntity> userOpt = userService.getUserById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Usuario no válido"));
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setUser(userOpt.get());
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostEntity post, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        postService.updatePost(id, post);
        Optional<PostEntity> updated = postService.getPostById(id);
        return ResponseEntity.ok(updated.orElse(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        postService.deletePostById(id);
        return ResponseEntity.ok(Map.of("message", "Post eliminado correctamente"));
    }
}
