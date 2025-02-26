package com.mykare.task.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.mykare.task.PROPS;
import com.mykare.task.exception.GlobalException;
import com.mykare.task.service.UserService;

@Component
public class AuthUserDetail implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            com.mykare.task.model.User user = userService.findUserByEmail(username);

            String[] roles = { (user.getRole() != null ? user.getRole() : PROPS.Roles.user) };

            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(roles)
                    .authorities(roles)
                    .build();
        } catch (Exception e) {
            throw new GlobalException.InternalServerError(e.getMessage());
        }
    }
}
