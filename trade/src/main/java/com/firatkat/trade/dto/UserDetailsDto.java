package com.firatkat.trade.dto;

import lombok.Builder;

@Builder
public class UserDetailsDto {
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String postCode;
}
