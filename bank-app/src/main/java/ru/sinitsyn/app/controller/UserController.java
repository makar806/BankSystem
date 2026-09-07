package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sinitsyn.app.DTO.AccountDTO;
import ru.sinitsyn.app.DTO.UserDTO;
import ru.sinitsyn.app.DTO.request.CreateUserRequest;
import ru.sinitsyn.app.MapperDTO;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Получить пользоваателя по id")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                             @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
                             @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping("/{id}")
    public UserDTO GetUserById(@PathVariable Long id){
        return MapperDTO.toDTO(userService.getUserById(id));
    }


    @Operation(summary = "Получить список всех пользователей")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(required = false) HairColor hairColor,
            @RequestParam(required = false) Gender gender
            ){
        return userService.getAllUsers(hairColor, gender)
                .stream()
                .map(MapperDTO::toDTO)
                .toList();
    }

    @Operation(summary = "Получить список друзей пользователя по id")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Друзья есть"),
            @ApiResponse(responseCode = "404", description = "Друзей нет("),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping("/{id}/friends")
    public Set<UserDTO> getFriendsByUserId(@PathVariable Long id) {
        return userService.getFriendByUserId(id)
                .stream()
                .map(MapperDTO::toDTO)
                .collect(Collectors.toSet());

    }

    @Operation(summary = "Создать пользователя")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Друзья есть"),
            @ApiResponse(responseCode = "404", description = "Друзей нет("),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping
    public UserDTO createUser(@RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam LocalDate birthDate, @RequestParam Gender gender, @RequestParam HairColor hairColor){
        return MapperDTO.toDTO(userService.creatUser(login, name, hairColor, gender, birthDate, password));
    }

    @Operation(summary = "Добавить в друзья")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Добавить удалось, все четко"),
            @ApiResponse(responseCode = "404", description = "Добавить не удалось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId, Authentication authentication){
        String currentUserLogin = authentication.getName();

        userService.addFriend(id, friendId, currentUserLogin);
    }

    @Operation(summary = "Удалить из друзей")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "От тебя ушёл последний друг"),
            @ApiResponse(responseCode = "404", description = "Удалить последнего друга не удалось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId, Authentication authentication) {
        String currentUserLogin = authentication.getName();

        userService.removeFriend(id, friendId, currentUserLogin);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Удалить ппользователя не получилось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        userService.removeUser(id);
    }

}
