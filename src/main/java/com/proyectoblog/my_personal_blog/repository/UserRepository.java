package com.proyectoblog.my_personal_blog.repository;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {//extendemos de jpa,y colocamos la entidad y tipo de dato de la pk
    //metodo con jpql
    //nos trae un userEntity y lo busca mediante una consulta jpql en base al usuario que le pasamos por parametro,el usuario que coincide lo devuelve
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(String username);
}
