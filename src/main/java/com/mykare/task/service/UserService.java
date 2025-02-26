package com.mykare.task.service;

import com.mykare.task.bean.ResponseBean;
import com.mykare.task.model.User;

public interface UserService {
    public ResponseBean getAllUsers();
    public ResponseBean deleteUser(String email);
    public User findUserByEmail(String email);
}
