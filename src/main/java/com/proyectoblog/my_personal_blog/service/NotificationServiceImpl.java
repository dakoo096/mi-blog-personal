package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.NotificationEntity;
import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<NotificationEntity> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void createNotification(UserEntity recipient, UserEntity actor, PostEntity post, String commentContent, String actionType) {
        if (recipient == null || actor == null || recipient.getId().equals(actor.getId())) {
            return;
        }

        NotificationEntity notification = new NotificationEntity();
        notification.setUser(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setCommentContent(commentContent);
        notification.setActionType(actionType != null ? actionType : "COMMENT");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void processCommentNotifications(UserEntity commenter, PostEntity post, String commentContent) {
        Set<Long> notifiedUserIds = new HashSet<>();

        // 1. Detectar menciones con @ en el texto del comentario
        Pattern pattern = Pattern.compile("@([^\\s@]+)");
        Matcher matcher = pattern.matcher(commentContent);

        while (matcher.find()) {
            String targetUser = matcher.group(1).trim();
            Optional<UserEntity> mentionedOpt = userService.getUserByUsername(targetUser);

            if (mentionedOpt.isPresent()) {
                UserEntity mentionedUser = mentionedOpt.get();
                if (!mentionedUser.getId().equals(commenter.getId()) && !notifiedUserIds.contains(mentionedUser.getId())) {
                    createNotification(mentionedUser, commenter, post, commentContent, "MENTION");
                    notifiedUserIds.add(mentionedUser.getId());
                }
            }
        }

        // 2. Notificar al dueño de la publicación (si no es el mismo que comentó y si no fue notificado ya por mención)
        if (post != null && post.getUser() != null) {
            UserEntity owner = post.getUser();
            if (!owner.getId().equals(commenter.getId()) && !notifiedUserIds.contains(owner.getId())) {
                createNotification(owner, commenter, post, commentContent, "COMMENT");
            }
        }
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Optional<NotificationEntity> optNotif = notificationRepository.findById(notificationId);
        if (optNotif.isPresent()) {
            NotificationEntity notif = optNotif.get();
            if (notif.getUser().getId().equals(userId)) {
                notif.setRead(true);
                notificationRepository.save(notif);
            }
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<NotificationEntity> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for (NotificationEntity notif : list) {
            if (!notif.isRead()) {
                notif.setRead(true);
            }
        }
        notificationRepository.saveAll(list);
    }
}
