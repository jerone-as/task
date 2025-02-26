package com.mykare.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mykare.task.PROPS;
import com.mykare.task.bean.ResponseBean;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.serviceImp.UserServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail(PROPS.TestCaseConstant.email);
        testUser.setName(PROPS.TestCaseConstant.name);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllUsers_ShouldReturnUsersList() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        ResponseBean response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertEquals(testUser.getEmail(), ((List<User>) response.getData()).get(0).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenExists() {
        when(userRepository.findByEmail(PROPS.TestCaseConstant.email)).thenReturn(Optional.of(testUser));

        ResponseBean response = userService.deleteUser(PROPS.TestCaseConstant.email);

        assertNotNull(response);
        assertEquals(PROPS.Message.deleteSuccess, response.getMessage());

        verify(userRepository, times(1)).findByEmail(PROPS.TestCaseConstant.email);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_ShouldNotThrowError_WhenUserNotFound() {
        when(userRepository.findByEmail(PROPS.TestCaseConstant.wrongMail)).thenReturn(Optional.empty());

        ResponseBean response = userService.deleteUser(PROPS.TestCaseConstant.wrongMail);

        assertNotNull(response);
        assertEquals(PROPS.Message.deleteSuccess, response.getMessage());

        verify(userRepository, times(1)).findByEmail(PROPS.TestCaseConstant.wrongMail);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail(PROPS.TestCaseConstant.email)).thenReturn(Optional.of(testUser));

        User user = userService.findUserByEmail(PROPS.TestCaseConstant.email);

        assertNotNull(user);
        assertEquals(PROPS.TestCaseConstant.email, user.getEmail());

        verify(userRepository, times(1)).findByEmail(PROPS.TestCaseConstant.email);
    }

    @Test
    void findUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(PROPS.TestCaseConstant.wrongMail)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUserByEmail(PROPS.TestCaseConstant.wrongMail));

        verify(userRepository, times(1)).findByEmail(PROPS.TestCaseConstant.wrongMail);
    }
}