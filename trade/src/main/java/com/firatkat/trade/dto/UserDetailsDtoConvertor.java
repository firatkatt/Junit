package com.firatkat.trade.dto;

import com.firatkat.trade.model.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsDtoConvertor {
    public UserDetailsDto convert(UserDetails from){
        return UserDetailsDto.builder().
                address(from.getAddress()).
                city(from.getCity()).
                country(from.getCountry()).
                phoneNumber(from.getPhoneNumber())
                .build();
    }

    public List<UserDetailsDto> convert(List<UserDetails> from){
        return from.stream().map(x-> convert(x)).collect((Collectors.toList()));
    }
}
