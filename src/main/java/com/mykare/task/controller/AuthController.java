package com.mykare.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mykare.task.bean.LoginBean;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.model.User;
import com.mykare.task.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    AuthService authService;

    @PostMapping("/user/register")
    public ResponseEntity<ResponseBean> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResponseBean> loginUser(@RequestBody LoginBean loginBean) {
        return ResponseEntity.ok(authService.validateUser(loginBean));
    }

}
