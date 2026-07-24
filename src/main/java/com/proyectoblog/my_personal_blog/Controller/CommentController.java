package com.proyectoblog.my_personal_blog.controller;

import com.proyectoblog.my_personal_blog.entity.CommentEntity;
import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.service.CommentService;
import com.proyectoblog.my_personal_blog.service.NotificationService;
import com.proyectoblog.my_personal_blog.service.PostService;
import com.proyectoblog.my_personal_blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> payload, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        if (!payload.containsKey("postId") || !payload.containsKey("content")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "postId y content son requeridos"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        Long postId = Long.parseLong(payload.get("postId").toString());
        String content = (String) payload.get("content");

        UserEntity user = userService.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("¡Usuario no encontrado!"));
        PostEntity post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("¡Post no encontrado!"));
        
        CommentEntity comment = new CommentEntity();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);
        
        commentService.createComment(comment);
        
        // Procesar notificaciones para el dueño del post y usuarios mencionados con @
        notificationService.processCommentNotifications(user, post, content);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        CommentEntity comment = commentService.getCommentById(id)
            .orElseThrow(() -> new IllegalArgumentException("¡Comentario no encontrado!"));
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Map<String, String> payload, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        String content = payload.get("content");
        CommentEntity comment = new CommentEntity();
        comment.setContent(content);
        commentService.updateComment(id, comment);
        return ResponseEntity.ok(Map.of("message", "Comentario actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message", "Comentario eliminado correctamente"));
    }
}
