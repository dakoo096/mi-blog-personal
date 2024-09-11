
package com.proyectoblog.my_personal_blog.Controller;

import com.proyectoblog.my_personal_blog.entity.CommentEntity;
import com.proyectoblog.my_personal_blog.entity.PostEntity;
import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.service.CommentService;
import com.proyectoblog.my_personal_blog.service.PostService;
import com.proyectoblog.my_personal_blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comment")
public class CommentController {
    
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    
    @PostMapping("/addComment")
    public String addComment(@RequestParam("postId") Long postId,CommentEntity comment,HttpSession session){
        
        UserEntity user = userService.getUserById(Long.parseLong(session.getAttribute("user_session_id").toString())).get();
        PostEntity post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("¡Invalid post id!"));
        
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);
        
        commentService.createComment(comment);
        return "redirect:/post/postPage/"+ postId;
    
    
    }
 //mapear la vista
    @GetMapping("/edit/{id}")
    public String editComment(@PathVariable Long id,Model model){
        CommentEntity comment = commentService.getCommentById(id).orElseThrow(() -> new IllegalArgumentException("¡ Invalid comment id!"));
        model.addAttribute("comment",comment);
        return "/posts/update-comment";
    }
    
    //editar
    @PostMapping("/update")
    public String updateComment(@RequestParam("commentId") Long id,CommentEntity comment){
        CommentEntity commentDB = commentService.getCommentById(id).orElseThrow(() -> new IllegalArgumentException("¡Invalid comment id!"));
        commentService.updateComment(id, comment);
        return "redirect:/post/postPage/" + commentDB.getPost().getId();
    }
    //cancelar
        @PostMapping("/cancel/{id}")
    public String cancelEditComment(@PathVariable Long id){
        CommentEntity comment = commentService.getCommentById(id).orElseThrow(() -> new IllegalArgumentException("¡Invalid comment id!"));
        return "redirect:/post/postPage/" + comment.getPost().getId();
    }
    
    //borrar
    @GetMapping("delete/{id}")
    public String editComment(@PathVariable Long id){
        CommentEntity comment = commentService.getCommentById(id).orElseThrow(() -> new IllegalArgumentException("¡Invalid comment id!"));
        commentService.deleteComment(id);
        return "redirect:/post/postPage/" + comment.getPost().getId();
    }


}
