package com.proyectoblog.my_personal_blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tbl_notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que recibe la notificación (dueño de la publicación)
    @JsonIgnoreProperties({"password", "posts", "comments"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Usuario que realizó la acción (el que comentó)
    @JsonIgnoreProperties({"password", "posts", "comments"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id")
    private UserEntity actor;

    // Publicación sobre la cual se comentó
    @JsonIgnoreProperties({"user", "comments"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(columnDefinition = "TEXT")
    private String commentContent;

    private boolean isRead = false;

    private String actionType = "COMMENT"; // "COMMENT" o "MENTION"

    private LocalDateTime createdAt;
}
