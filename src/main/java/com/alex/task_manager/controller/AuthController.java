package com.alex.task_manager.controller;

import com.alex.task_manager.entity.UserEntity;
import com.alex.task_manager.exception.ErrorResponse;
import com.alex.task_manager.io.user.UserRequest;
import com.alex.task_manager.io.user.UserResponse;
import com.alex.task_manager.mapper.user.UserRequestMapper;
import com.alex.task_manager.mapper.user.UserResponseMapper;
import com.alex.task_manager.security.AuthRequest;
import com.alex.task_manager.security.CustomUserDetailsService;
import com.alex.task_manager.security.JwtResponse;
import com.alex.task_manager.service.user.UserService;
import com.alex.task_manager.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер авторизации", description = "Контроллер для регистрации и авторизации")
public class AuthController {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Аутентификация пользователя", description = "Позволяет произвести аутентификацию пользователя")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public ResponseEntity<JwtResponse> login(@RequestBody @Valid
                                             @Parameter(description = "Данные пользователя при аутентификации")
                                             AuthRequest authRequest) throws Exception {
        authenticate(authRequest.getEmail(), authRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authManager.authenticate
                    (new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать пользователя")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Новый пользователь зарегистрирован",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "400", description = "Введены неверные данные при регистрации",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public UserResponse createUser(@Valid @RequestBody @Parameter(description = "Данные нового пользователя")
                                   UserRequest user) {
        UserEntity createdUser = userService.createUser(userRequestMapper.mapToEntity(user));
        return userResponseMapper.mapToResponse(createdUser);
    }
}
