package com.firatkat.trade.dto;

import com.firatkat.trade.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConvertor {
    public UserDto convert(User from){
        return new UserDto(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName());
    }
    public List<UserDto> convert(List<User> fromList){
        return fromList.stream().map(from -> new UserDto(from.getMail(),from.getFirstName(),from.getLastName(),from.getMiddleName())).collect(Collectors.toList());
    }
}
