package com.firatkat.trade.service;

import com.firatkat.trade.repository.UserRepository;

public class UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
