package com.mykare.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.mykare.task.PROPS;
import com.mykare.task.bean.LoginBean;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.exception.GlobalException;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.serviceImp.AuthServiceImp;
import com.mykare.task.utils.JwtUtil;
import com.mykare.task.utils.Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthServiceImp authService;

    private User testUser;

    private MockedStatic<Utils> utilsMock; // For static mocking

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail(PROPS.TestCaseConstant.email);
        testUser.setPassword(PROPS.TestCaseConstant.password);
        testUser.setRole(PROPS.Roles.admin);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn(PROPS.TestCaseConstant.hashedPassword);
        lenient().when(restTemplate.getForObject(PROPS.ExternalApis.ipUrl, String.class))
                .thenReturn(PROPS.TestCaseConstant.ipAddress);
        lenient()
                .when(restTemplate.getForObject(PROPS.ExternalApis.locationUrl + PROPS.TestCaseConstant.ipAddress,
                        String.class))
                .thenReturn(PROPS.TestCaseConstant.location);
        utilsMock = mockStatic(Utils.class); 
    }

    @AfterEach
    void tearDown() {
        utilsMock.close(); 
    }

    @Test
    void testRegisterUser_InvalidEmail_ThrowsException() {
        User user = new User();
        user.setEmail(PROPS.TestCaseConstant.invalidMail);
        user.setPassword(PROPS.TestCaseConstant.password);

        utilsMock.when(() -> Utils.validateEmail(PROPS.TestCaseConstant.invalidMail)).thenReturn(false);

        assertThrows(GlobalException.Forbidden.class, () -> authService.registerUser(testUser));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldRegisterUser_WhenEmailNotExists() {
        utilsMock.when(() -> Utils.validateEmail(testUser.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn(PROPS.TestCaseConstant.hashedPassword);

        ResponseBean response = authService.registerUser(testUser);

        assertNotNull(response);
        assertEquals(PROPS.Message.saveSuccess, response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        utilsMock.when(() -> Utils.validateEmail(testUser.getEmail())).thenReturn(true); 
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(GlobalException.Forbidden.class, () -> authService.registerUser(testUser));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void validateUser_ShouldReturnToken_WhenCredentialsAreCorrect() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(PROPS.TestCaseConstant.email);
        loginBean.setPassword(PROPS.TestCaseConstant.password);

        when(userRepository.findByEmail(loginBean.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginBean.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(loginBean.getEmail())).thenReturn(PROPS.TestCaseConstant.token);

        ResponseBean response = authService.validateUser(loginBean);

        assertNotNull(response);
        assertEquals(PROPS.Message.loginSuccess, response.getMessage());
        assertEquals(PROPS.TestCaseConstant.token, response.getToken());
    }

    @Test
    void validateUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(PROPS.TestCaseConstant.email);
        loginBean.setPassword(PROPS.TestCaseConstant.wrongPassword);

        when(userRepository.findByEmail(loginBean.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginBean.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(GlobalException.UnAuthorized.class, () -> authService.validateUser(loginBean));
    }

    @Test
    void validateUser_ShouldThrowException_WhenUserNotFound() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(PROPS.TestCaseConstant.wrongMail);
        loginBean.setPassword(PROPS.TestCaseConstant.password);

        when(userRepository.findByEmail(loginBean.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(GlobalException.UnAuthorized.class,
                () -> authService.validateUser(loginBean));

        assertEquals(PROPS.Message.emailNotFound, exception.getMessage()); // ✅ Ensures correct message
    }
}