package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.repository.query.Param;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    public UserController(UserService userService, AccountService accountService){
        this.userService = userService;
        this.accountService = accountService;
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

    @Operation(summary = "Получить список аккаунтов пользователя по id")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Аккаунт найден"),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping("/{id}/accounts")
    public List<AccountDTO> getAccountsByUserId(@PathVariable Long id){
        return accountService.getAllAccounts()
                .stream()
                .map(MapperDTO::toDTO)
                .toList();
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Друзья есть"),
            @ApiResponse(responseCode = "404", description = "Друзей нет("),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping
    public UserDTO createUser(@RequestBody CreateUserRequest request){
        return MapperDTO.toDTO(userService.creatUser(request.login(), request.name(), request.hairColor(), request.gender(), request.age()));
    }

    @Operation(summary = "Добавить в друзья")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Добавить удалось, все четко"),
            @ApiResponse(responseCode = "404", description = "Добавить не удалось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId){
        userService.addFriend(id, friendId);
    }

    @Operation(summary = "Удалить из друзей")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "От тебя ушёл последний друг"),
            @ApiResponse(responseCode = "404", description = "Удалить последнего друга не удалось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

}
