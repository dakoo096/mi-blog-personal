
package com.proyectoblog.my_personal_blog.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor //Constructor con argumentos
@NoArgsConstructor  //  Constructor vacio
@Data //getters y setters
@Table (
    name = "tbl_posts"
)
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdAt;
    
    
    //relaciones entre entidades
    
    @ManyToOne( //muchos post pueden pertenecer a un usuario
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
            private UserEntity user;
    
    @OneToMany( //un post pueden tener muchos comentarios
            mappedBy = "post",
            cascade = CascadeType.ALL
    )
    private List<CommentEntity> comments = new ArrayList<>();
}
