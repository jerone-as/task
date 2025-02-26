package com.mykare.task.service;

import com.mykare.task.bean.LoginBean;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.model.User;

public interface AuthService {
    public ResponseBean registerUser(User user);
    public ResponseBean validateUser(LoginBean loginBean);
}
