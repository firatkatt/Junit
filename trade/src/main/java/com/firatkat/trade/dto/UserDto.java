package com.firatkat.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserDto {
    String mail;
    String firstName;
    String lastName;
    String middleName;
}
