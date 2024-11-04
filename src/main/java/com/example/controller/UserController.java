package com.example.controller;

import com.example.model.JwtResponse;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import com.example.service.impl.JwtService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @GetMapping("/users/paging")
//    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
//                               @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return userService.getAllUsers(pageable);
//    }


    @GetMapping("/admin/users")
    public ResponseEntity<Iterable<User>> showAllUserByAdmin() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("user/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        userService.updateEnabled(authentication.getName(), false);
        Long remainingTime;
        if (authentication != null) {
            String username = authentication.getName();
            remainingTime = jwtService.getRemainingTime(token.substring(7)); // Bỏ "Bearer " khỏi token
            userService.updateTokenRemainingTime(userService.findByUsername(username).getId(), remainingTime);
            SecurityContextHolder.clearContext(); // Xóa SecurityContext
        } else {
            remainingTime = 0L;
        }
        return new ResponseEntity<>(remainingTime, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        List<String> errors = ExceptionController.getMessageError(bindingResult);
        if (user.getUsername().isEmpty()) {
            errors.add("username: Vui lòng không để trống");
        }
        if (user.getPassword().isEmpty()) {
            errors.add("password: Vui lòng không để trống");
        }
        if (user.getIdentityCode() == 0L) {
            errors.add("identityCode: Vui lòng nhập mã số căn cước");
        }
        if (userService.isRegister(user)) {
            errors.add("username: Tên đăng nhập đã tồn tại");
        }
        if (userService.findByIdentityCode(user.getIdentityCode()).isPresent()) {
            errors.add("identityCode: Mã số căn cước đã tồn tại");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        if (user.getRoles() == null) {
            Role role1 = roleService.findByName("ROLE_USER");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            user.setRoles(roles1);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // kiểm tra tài khoản mật khẩu
        User currentUser = userService.updateEnabled(user.getUsername(), true);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        // đúng thì tạo ra SecurityContextHolder để lưu trữ đối tượng đang đăng nhập
        if (currentUser.getTime() > 0){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // tạo ra token
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
        }
         userService.updateEnabled(user.getUsername(), false);
       return new ResponseEntity<>("Tài khoản hiện đã hết tiền",HttpStatus.UNAUTHORIZED);
    }



    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setId(userOptional.get().getId());
        user.setUsername(userOptional.get().getUsername());
        user.setEnabled(userOptional.get().isEnabled());
        user.setPassword(userOptional.get().getPassword());
        user.setRoles(userOptional.get().getRoles());

        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<User> updateUserByAdmin(@RequestBody User user) {
        User userExists = userService.findByUsername(user.getUsername());
        userExists.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(userExists);
        return new ResponseEntity<>(userExists, HttpStatus.OK);
    }

    @PostMapping("/admin/users/money")
    public ResponseEntity<User> addTime(@RequestBody User user) {

        User userExists = userService.findByUsername(user.getUsername());
        Long newTime = userExists.getTime() + user.getTime();
        userExists.setTime(newTime);
        userService.save(userExists);
        return new ResponseEntity<>(userExists, HttpStatus.OK);
    }

    @PostMapping("/users/time")
    public ResponseEntity<?> getTimeRemaining(@RequestHeader("Authorization") String token) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null) {
            Long remainingTime = jwtService.getRemainingTime(token.substring(7));
            return new ResponseEntity<>(remainingTime, HttpStatus.OK);
        }
        return new ResponseEntity<>(0L, HttpStatus.OK);
    }
}