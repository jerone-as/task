package com.mykare.task.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mykare.task.PROPS;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.exception.GlobalException;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.service.UserService;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseBean getAllUsers() {
        try {
            return ResponseBean.builder()
                    .data(userRepository.findAll())
                    .build();
        } catch (Exception e) {
            throw new GlobalException.InternalServerError(e.getMessage());
        }
    }

    @Transactional
    public ResponseBean deleteUser(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                userRepository.delete(optionalUser.get());
                return ResponseBean.builder()
                        .message(PROPS.Message.deleteSuccess)
                        .build();
            } else {
                throw new GlobalException.InternalServerError(PROPS.Message.emailNotFound);
            }
        } catch (Exception e) {
            throw new GlobalException.InternalServerError(e.getMessage());
        }
    }

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.get();
    }

}