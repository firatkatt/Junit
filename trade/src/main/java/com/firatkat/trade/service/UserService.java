package com.firatkat.trade.service;

import com.firatkat.trade.dto.CreateUserRequest;
import com.firatkat.trade.dto.UpdateUserRequest;
import com.firatkat.trade.dto.UserDto;
import com.firatkat.trade.dto.UserDtoConvertor;
import com.firatkat.trade.exception.UserIsNotActiveException;
import com.firatkat.trade.exception.UserNotFoundException;
import com.firatkat.trade.model.User;
import com.firatkat.trade.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserDtoConvertor userDtoConvertor;


    public UserService(UserRepository userRepository, UserDtoConvertor userDtoConvertor) {
        this.userRepository = userRepository;
        this.userDtoConvertor = userDtoConvertor;
    }

    public List<UserDto> getAllUsers() {
        return userDtoConvertor.convert(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        User user = findUserById(id);
        return userDtoConvertor.convert(user);
    }

    public UserDto createUser(CreateUserRequest userRequest) {
        User user = new User(userRequest.getMail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getMiddleName(),false);
        return userDtoConvertor.convert(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = findUserById(id);
        if (!user.getIsActive()) {
            logger.warn(String.format("The user wanted update is not active!. user id: %d", id));
            throw new UserIsNotActiveException("The user wanted update is not active!");
        }
        User updateUser = new User(
                user.getId(),
                user.getMail(),
                updateUserRequest.getFirstName(),
                updateUserRequest.getLastName(),
                updateUserRequest.getMiddleName(),
                user.getIsActive()
        );
        return userDtoConvertor.convert(userRepository.save(updateUser));
    }

    public void deactivateUser(Long id) {
        changeUserActive(id, false);
    }

    public void activeUser(Long id) {
        changeUserActive(id, true);
    }

    public void deleteUser(Long id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User couldn't be found by following id: " + id));
    }

    private void changeUserActive(Long id, Boolean isActive) {
        User user = findUserById(id);
        User updateUser = new User(
                user.getId(),
                user.getMail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                isActive
        );
        userRepository.save(updateUser);
    }

}
