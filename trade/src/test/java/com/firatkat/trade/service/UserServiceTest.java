package com.firatkat.trade.service;

import com.firatkat.trade.dto.CreateUserRequest;
import com.firatkat.trade.dto.UpdateUserRequest;
import com.firatkat.trade.dto.UserDto;
import com.firatkat.trade.dto.UserDtoConvertor;
import com.firatkat.trade.exception.UserIsNotActiveException;
import com.firatkat.trade.exception.UserNotFoundException;
import com.firatkat.trade.model.User;
import com.firatkat.trade.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.firatkat.trade.TestSupport.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDtoConvertor userDtoConvertor;
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDtoConvertor = mock(UserDtoConvertor.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository, userDtoConvertor);
    }

    @Test
    public void testGetAllUsers_itShouldReturnUserDtoList() {
        List<User> userList = generatedUsers();
        List<UserDto> userDtoList = generatedUserDtoLists(userList);

        when(userRepository.findAll()).thenReturn(userList);
        when(userDtoConvertor.convert(userList)).thenReturn(userDtoList);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(userDtoList, result);
        Mockito.verify(userRepository).findAll();
        Mockito.verify(userDtoConvertor).convert(userList);
    }

    @Test
    public void testGetUserById_whenUserIdExist_itShouldReturnUserDto() {
        Long testNumber = 100L;
        User user = generateUser(testNumber);
        UserDto userDto = generateUserDto(testNumber);

        when(userRepository.findById(testNumber)).thenReturn(Optional.of(user));
        when(userDtoConvertor.convert(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(testNumber);

        assertEquals(userDto, result);
        Mockito.verify(userRepository).findById(testNumber);
        Mockito.verify(userDtoConvertor).convert(user);
    }

    @Test
    public void testGetUserById_whenUserIdDoesNotExist_itShouldRThrowUserNotFoundException() {
        Long testNumber = 100L;

        when(userRepository.findById(testNumber)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(testNumber));

        Mockito.verify(userRepository).findById(testNumber);
        Mockito.verifyNoInteractions(userDtoConvertor);
    }

    @Test
    public void testCreateUser_itShouldReturnCreatedUserDto() {
        CreateUserRequest createUserRequest = new CreateUserRequest("@folksdev.net", "firstName", "lastName", "middleName");
        User user = new User("@folksdev.net", "firstName", "lastName", "middleName", false);
        User savedUser = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", false);
        UserDto userDto = new UserDto("@folksdev.net", "firstName", "lastName", "middleName");

        when(userRepository.save(user)).thenReturn(savedUser);
        when(userDtoConvertor.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(createUserRequest);

        assertEquals(userDto, result);

        Mockito.verify(userRepository).save(user);
        Mockito.verify(userDtoConvertor).convert(savedUser);

    }

    @Test
    public void testUpdateUser_whenUserIdExistAndUserIsActive_itShouldReturnUpdatedUserDto() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("firstName3", "lastName3", "middleName3");
        User user = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", true);
        User updatedUser = new User(1L, "@folksdev.net", "firstName3", "lastName3", "middleName3", true);
        User savedUser = new User(1L, "@folksdev.net", "firstName3", "lastName3", "middleName3", true);
        UserDto userDto = new UserDto("@folksdev.net", "firstName3", "lastName3", "middleName3");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(savedUser);
        when(userDtoConvertor.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(1L, updateUserRequest);

        assertEquals(userDto, result);

        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(userRepository).save(updatedUser);
        Mockito.verify(userDtoConvertor).convert(savedUser);

    }

    @Test
    public void testUpdateUser_whenUserIdDoesNotExist_itShouldThrowUserNotFoundException() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("firstName3", "lastName3", "middleName3");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updateUserRequest));

        Mockito.verify(userRepository).findById(1L);
        Mockito.verifyNoInteractions(userDtoConvertor);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testUpdateUser_whenUserIdExistButUserIsNotActive_itShouldThrowUserNotActiveException() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("firstName3", "lastName3", "middleName3");
        User user = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        assertThrows(UserIsNotActiveException.class, () -> userService.updateUser(1L, updateUserRequest));

        Mockito.verify(userRepository).findById(1L);
        Mockito.verifyNoInteractions(userDtoConvertor);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testDeactivateUser_whenUserIdExist_itShouldUpdateUserByActiveFalse() {
        User user = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", true);
        User savedUser = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(userRepository).save(savedUser);

    }

    @Test
    public void testDeactivateUser_whenUserIdDoesNotExist_itShouldThrowUserNotFoundException() {


        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(1L));

        Mockito.verify(userRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testActivateUser_whenUserIdExist_itShouldUpdateUserByActiveTrue() {
        User user = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", false);
        User updatedUser = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.activeUser(1L);

        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(userRepository).save(updatedUser);

    }

    @Test
    public void testActivateUser_whenUserIdIsNotExist_itShouldThrowUserNotFoundException() {


        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.activeUser(1L));

        Mockito.verify(userRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testDeleteUser_whenUserIdExist_itShouldDeleteUser() {
        User user = new User(1L, "@folksdev.net", "firstName", "lastName", "middleName", true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(userRepository).deleteById(1L);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testDeleteUser_whenUserIdIsNotExist_itShouldThrowUserNotFoundException() {


        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));

        Mockito.verify(userRepository).findById(1L);

        Mockito.verifyNoMoreInteractions(userRepository);

    }

}