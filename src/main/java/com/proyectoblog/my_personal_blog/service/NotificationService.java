package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.NotificationEntity;
import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.entity.UserEntity;
import java.util.List;

public interface NotificationService {
    
    List<NotificationEntity> getUserNotifications(Long userId);
    
    long getUnreadCount(Long userId);
    
    void createNotification(UserEntity recipient, UserEntity actor, PostEntity post, String commentContent, String actionType);
    
    void processCommentNotifications(UserEntity commenter, PostEntity post, String commentContent);
    
    void markAsRead(Long notificationId, Long userId);
    
    void markAllAsRead(Long userId);
}
