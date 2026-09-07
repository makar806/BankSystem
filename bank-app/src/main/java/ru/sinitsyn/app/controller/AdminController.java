package ru.sinitsyn.app.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sinitsyn.service.AuthService;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService){
        this.authService = authService;
    }

    @Operation(summary = "Создать админимстратора")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Админ успешно зарегистрирован"),
            @ApiResponse(responseCode = "404", description = "Зарегистрировать админа не вышло"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping
    public void createAdmin(@RequestParam String login, @RequestParam String password) {
        authService.createAdmin(login, password);
    }

    @Operation(summary = "Удалить админимстратора")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Админ успешно удвлён"),
            @ApiResponse(responseCode = "404", description = "Удалить админа не вышло"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @DeleteMapping
    public void deleteAdmin(@RequestParam String login, Authentication authentication) {
        String currentAdminLogin = authentication.getName();

        authService.deleteAdmin(login, currentAdminLogin);
    }

}
