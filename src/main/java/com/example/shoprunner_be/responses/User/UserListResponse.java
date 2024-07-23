package com.example.shoprunner_be.responses.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponse {
    List<UserResponse> userResponses;
    int totalPages;
}
