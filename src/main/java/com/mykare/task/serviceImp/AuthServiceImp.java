package com.mykare.task.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mykare.task.PROPS;
import com.mykare.task.bean.IpDetails;
import com.mykare.task.bean.IpResponse;
import com.mykare.task.bean.LoginBean;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.exception.GlobalException;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.service.AuthService;
import com.mykare.task.utils.JwtUtil;
import com.mykare.task.utils.Utils;

import java.util.Optional;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseBean registerUser(User user) {
        try {
            if (user.getEmail() != null) {
                Boolean isValidEmail = Utils.validateEmail(user.getEmail());
                if (!isValidEmail) {
                    throw new GlobalException.Forbidden(PROPS.Message.validEmail);
                }
                Boolean isUserPresent = userRepository.findByEmail(user.getEmail()).isPresent();
                if (isUserPresent) {
                    throw new GlobalException.Forbidden(PROPS.Message.emailAlreadyExists);
                }
                RestTemplate restTemplate = new RestTemplate();
                String ip = restTemplate.getForObject(PROPS.ExternalApis.ipUrl, IpResponse.class).ip;
                String country = restTemplate.getForObject(PROPS.ExternalApis.locationUrl + ip,
                        IpDetails.class).country;

                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setIpAddress(ip);
                user.setCountry(country);
                userRepository.save(user);

                return ResponseBean.builder()
                        .message(PROPS.Message.saveSuccess)
                        .data(user)
                        .build();
            } else {
                throw new GlobalException.Forbidden(PROPS.Message.emailNotFound);
            }
        } catch (Exception e) {
            throw new GlobalException.Forbidden(e.getMessage());
        }
    }

    public ResponseBean validateUser(LoginBean loginBean) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginBean.getEmail());
            if (userOptional.isPresent()) {
                Boolean passwordMatch = passwordEncoder.matches(loginBean.getPassword(),
                        userOptional.get().getPassword());
                if (passwordMatch) {
                    String token = null;
                    if (userOptional.get().getRole().equalsIgnoreCase(PROPS.Roles.admin)) {
                        token = jwtUtil.generateToken(loginBean.getEmail());
                    }
                    return ResponseBean.builder()
                            .message(PROPS.Message.loginSuccess)
                            .data(userOptional.get())
                            .token(token)
                            .build();
                } else {
                    throw new GlobalException.UnAuthorized(PROPS.Message.loginFailed);
                }
            } else {
                throw new GlobalException.UnAuthorized(PROPS.Message.emailNotFound);
            }
        } catch (Exception e) {
            throw new GlobalException.UnAuthorized(e.getMessage());
        }

    }

}