package com.proyectoblog.my_personal_blog.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor //Constructor con argumentos
@NoArgsConstructor  //  Constructor vacio
@Data //getters y setters
@Table(
        name = "tbl_users"
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;

    //relaciones entre entidades
    @OneToMany(//un usuario puede realizar muchos post
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(//un usuario puede realizar muchos comentarios
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<CommentEntity> comments = new ArrayList<>();
}
