package com.example.shoprunner_be.responses.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String name;
    String email;
    String phoneNumber;
    String address;
    String role;
}
