package com.firatkat.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateUserRequest {
    String mail;
    String firstName;
    String lastName;
    String middleName;
}
