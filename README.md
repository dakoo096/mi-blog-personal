# Proyecto Blog Personal

Este es un proyecto de blog personal desarrollado con **Java**, **Spring Boot**, **JPA**, **Spring Security**, **MySQL**, **Thymeleaf**, y **Bootstrap**. 
El objetivo es ofrecer una plataforma donde los usuarios puedan crear, editar y comentar publicaciones, manteniendo la seguridad y la privacidad de las acciones de cada usuario.

## Funcionalidades

### 1. **Registro y Login de Usuario**
- El usuario puede registrarse a través de un enlace en la página de login.
- Una vez registrado, puede iniciar sesión con sus credenciales.
  
### 2. **Página de Inicio (Home)**
- El usuario tiene acceso a la página principal donde se pueden visualizar todos los posts creados por cualquier usuario.
- Los posts pueden ser abiertos para leer su contenido completo y ver los comentarios asociados.

### 3. **Comentarios**
- Los usuarios pueden comentar en los posts.
- Cada comentario muestra el nombre del usuario que lo hizo, junto con la fecha y hora en que se realizó.
- Los usuarios pueden **editar sus propios comentarios**, pero no los de otros usuarios.

### 4. **Gestión de Posts Propios**
- En el **navbar**, hay un apartado que redirige a una página donde se listan los posts propios del usuario.
- Desde esta página, el usuario puede:
  - Editar sus posts.
  - Ver los comentarios asociados a cada uno de sus posts.

### 5. **Creación de Nuevos Posts**
- El usuario puede crear un nuevo post, proporcionando un título y contenido.

### 6. **Buscador de Posts**
- Se incluye un buscador que permite encontrar posts por palabras clave en el título.

### 7. **Perfil de Usuario**
- El **nombre del usuario logueado** se muestra en el navbar.
- Al hacer clic en el nombre de usuario, se despliega un menú que permite **cerrar sesión** y loguearse con otro usuario.

## Tecnologías Utilizadas

- **Java**
- **Spring Boot**
- **JPA (Java Persistence API)**
- **Spring Security**
- **MySQL**
- **Thymeleaf**
- **Bootstrap**

## Requisitos

- **Java 17** o superior
- **Maven**
- **MySQL**

## Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tuusuario/blog-personal.git](https://github.com/dakoo096/mi-blog-personal.git
   ```
2. Configurar la base de datos en `application.properties`:
   ```properties
     spring.datasource.url= jdbc:mysql://localhost:3306/db_personal_blog
     spring.datasource.username=admin
     spring.datasource.password=admin
   ```
3. Ejecutar el proyecto con Maven:
   ```bash
   mvn spring-boot:run
   ```

   
![blog-1](https://github.com/user-attachments/assets/5999f7c3-f384-4367-84ca-8d692f06e01e)
![blog-2](https://github.com/user-attachments/assets/c03d3abb-c8f2-46d0-ae21-c1bc984db085)
![blog-3](https://github.com/user-attachments/assets/2ed51d40-b4e4-4baf-882d-f7eb27b294a8)
![blog-4](https://github.com/user-attachments/assets/a1a1d345-88e7-40ad-a918-e8baae16110e)
![blog-5](https://github.com/user-attachments/assets/83a9d454-6f3d-4c82-8f55-0b089f11ab53)
![blog-6](https://github.com/user-attachments/assets/d74f50b0-ab50-4e8d-a9a2-c58cbc58664f)
![blog-7](https://github.com/user-attachments/assets/492b2887-87c1-4c2f-819a-88b8e96a6897)
