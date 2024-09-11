package com.proyectoblog.my_personal_blog.entity;

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
@AllArgsConstructor //Constructor con argumentos
@NoArgsConstructor  //  Constructor vacio
@Data //getters y setters
@Table(
        name = "tbl_comments"
)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT") //sirve para cambiar el tipo de dato de esta columna cuando se mapee en la bd
    private String content;
    private LocalDateTime createdAt;

    //relaciones entre entidades
    @ManyToOne( //muchos comentarios pueden pertenecer a un usuario
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne( //muchos comentarios pueden pertenecer a un unico post
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "post_id")
    private PostEntity post;

}
