package com.firatkat.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class UpdateUserRequest {
    String firstName;
    String lastName;
    String middleName;
}
