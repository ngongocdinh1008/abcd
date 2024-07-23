package com.example.shoprunner_be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    String name;

    @JsonProperty("phone_number")
    String phoneNumber;

    String email;

    @NotBlank(message = "asked to enter a password !!")
    String password;

    String confirmPassword;

    String address;

}
