package com.firatkat.trade;

import com.firatkat.trade.dto.UserDto;
import com.firatkat.trade.model.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {
    public static List<User> generatedUsers(){
        return IntStream.range(0,5).mapToObj(i-> new User((long)i,
                i+"@folksdev.net",
                "firstName"+i,
                "lastName"+i,
                "middleName"+i
                ,new Random(2).nextBoolean())
        ).collect(Collectors.toList());
    }

    public static List<UserDto> generatedUserDtoLists(List<User> userList){
        return userList.stream().map(from->new UserDto(from.getMail()
                , from.getFirstName(),
                from.getLastName(),
                from.getMiddleName())).collect(Collectors.toList());
    }

    public static User generateUser(Long id){
        return new User(id,
                id+"@folksdev.net",
                "firstName"+id,
                "lastName"+id,
                "middleName"+id
                ,new Random(2).nextBoolean());
    }

    public static UserDto generateUserDto(Long id){
        return UserDto.builder().mail(id+"@folksdev.net")
                .firstName("firstName"+id)
                .lastName("lastName"+id)
                .middleName("middleName"+id)
                .build();
    }
}
