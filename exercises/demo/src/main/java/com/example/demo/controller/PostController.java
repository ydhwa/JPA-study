package com.example.demo.controller;

import com.example.demo.repository.PostDao;
import com.example.demo.vo.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostDao postDao;

    @GetMapping("/hello")
    public String hello() {
        return "hello!";
    }

    @PostMapping("/write")
    public Post write(Post post) {
        post.setRegDate(new Date());
        return postDao.save(post);
    }

    @GetMapping("/list")
    public List<Post> list() {
        return postDao.findAll();
    }

    @GetMapping("/{id}")
    public Post view(@PathVariable Long id) {
        return postDao.getOne(id);
    }

}
