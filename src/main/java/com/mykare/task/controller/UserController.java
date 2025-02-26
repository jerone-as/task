package com.mykare.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mykare.task.bean.ResponseBean;
import com.mykare.task.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<ResponseBean> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<ResponseBean> deleteUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.deleteUser(email));
    }
}