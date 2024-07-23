package com.example.shoprunner_be.services.User;

import com.example.shoprunner_be.dtos.UserDTO;
import com.example.shoprunner_be.dtos.UserLoginDTO;
import com.example.shoprunner_be.entitys.User;
import com.example.shoprunner_be.responses.User.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User getUserById(Long userId);

    User createUser(UserDTO userDTO) throws Exception;

    String login(UserLoginDTO userLoginDTO) throws Exception;

    Page<User> getAllUsers(String keyword, Pageable pageable);

    User getUser(UserLoginDTO userLoginDTO);
    void blockUser(Long userId);
    void unblockUser(Long userId);

}
