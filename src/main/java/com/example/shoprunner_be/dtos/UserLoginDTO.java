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
public class UserLoginDTO {
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @NotBlank(message = "asked to enter a password !!")
    String password;
}
