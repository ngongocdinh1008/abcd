package com.example.shoprunner_be.services.User;

import com.example.shoprunner_be.components.JwtTokenUtil;
import com.example.shoprunner_be.dtos.UserDTO;
import com.example.shoprunner_be.dtos.UserLoginDTO;
import com.example.shoprunner_be.entitys.Role;
import com.example.shoprunner_be.entitys.User;
import com.example.shoprunner_be.exceptions.Dataintegrityviolationexception;
import com.example.shoprunner_be.exceptions.EntityNotFoundException;
import com.example.shoprunner_be.repositories.RoleRepo;
import com.example.shoprunner_be.repositories.UserRepo;
import com.example.shoprunner_be.responses.User.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserImp implements UserService{
    final UserRepo userRepo;
    final RoleRepo roleRepo;
    final PasswordEncoder passwordEncoder;
    final JwtTokenUtil jwtTokenUtil;
    final AuthenticationManager authenticationManager;

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }
    @Override
    public User getUser(UserLoginDTO userLoginDTO){
        User user;
        if (userLoginDTO.getPhoneNumber() != null && !userLoginDTO.getPhoneNumber().isBlank()) {
            user = userRepo.findByPhoneNumber(userLoginDTO.getPhoneNumber())
                    .orElseThrow(()-> new EntityNotFoundException("Phone number not found"));
        }else {
            user = userRepo.findByEmail(userLoginDTO.getEmail())
                    .orElseThrow(()-> new EntityNotFoundException("Email not found"));
        }
        return user;
    }
    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepo.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new Dataintegrityviolationexception("Phone number already in use");
        }
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            throw new Dataintegrityviolationexception("Email already in use");
        }
        User user = User.builder()
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .address(userDTO.getAddress())
                .build();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            Role defaultRole = roleRepo.findById(2L)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            user.setRole(defaultRole);
        return userRepo.save(user);
    }

    @Transactional
    public String login(UserLoginDTO userLoginDTO)
    throws Exception{
        Optional<User> optionalUser = Optional.empty();
        String subject = null;
        if (userLoginDTO.getPhoneNumber() != null && !userLoginDTO.getPhoneNumber().isBlank()) {
            optionalUser = userRepo.findByPhoneNumber(userLoginDTO.getPhoneNumber());
            subject = userLoginDTO.getPhoneNumber();
        }
        if (optionalUser.isEmpty() && userLoginDTO.getEmail() != null) {
            optionalUser = userRepo.findByEmail(userLoginDTO.getEmail());
            subject = userLoginDTO.getEmail();
        }
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Account and password are incorrect");
        }
        User existingUser = optionalUser.get();
        //check password
        if (existingUser.getGoogleAccountID() == 0){
            if (!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())){
                throw new BadCredentialsException("Wrong password");
            }
        }
        if (!existingUser.isActive()){
            throw new EntityNotFoundException("Account has been locked");
        }
        //authenticate
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        subject,userLoginDTO.getPassword(),
                        existingUser.getAuthorities()

                );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public Page<User> getAllUsers(String keyword, Pageable pageable) {
        return userRepo.findAll(keyword, pageable);
    }

    @Override
    public void blockUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepo.save(user);
    }
    @Override
    public void unblockUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(true);
        userRepo.save(user);
    }
}
