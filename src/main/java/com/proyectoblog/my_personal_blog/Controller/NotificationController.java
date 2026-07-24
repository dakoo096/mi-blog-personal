package com.proyectoblog.my_personal_blog.controller;

import com.proyectoblog.my_personal_blog.entity.NotificationEntity;
import com.proyectoblog.my_personal_blog.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotifications(HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        List<NotificationEntity> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.ok(Map.of("unreadCount", 0));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        long unreadCount = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", unreadCount));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(Map.of("message", "Notificación marcada como leída"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(HttpSession session) {
        Object userIdObj = session.getAttribute("user_session_id");
        if (userIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Long userId = Long.parseLong(userIdObj.toString());
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("message", "Todas las notificaciones marcadas como leídas"));
    }
}
