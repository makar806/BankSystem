package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sinitsyn.app.DTO.AccountDTO;
import ru.sinitsyn.app.DTO.request.AmountRequest;
import ru.sinitsyn.app.MapperDTO;
import ru.sinitsyn.service.AccountService;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @Operation(summary = "Посмотреть список всех аккаунтов")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Аккаунты нашлись"),
            @ApiResponse(responseCode = "404", description = "Аккаунты не нашлись"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping
    public List<AccountDTO> getAllAccounts(Authentication authentication) {
        return accountService.getAllAccounts()
                .stream()
                .map(MapperDTO::toDTO)
                .toList();
    }

    @Operation(summary = "Создать аккаунт")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Аккаунт успешно создался!"),
            @ApiResponse(responseCode = "404", description = "Не удалось создать аккаунт"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping
    public AccountDTO createAccount(@RequestParam Long userId) {
        return MapperDTO.toDTO(accountService.createAccount(userId));
    }

    @Operation(summary = "Посмотреть баланс аккаунта по id")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Баланс успешно найден"),
            @ApiResponse(responseCode = "404", description = "Не удалось вернуть баланс"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable Long id, Authentication authentication){
        String currentLogin = authentication.getName();

        return accountService.getBalance(id, currentLogin);
    }

    @Operation(summary = "Получить список аккаунтов пользователя по id")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Аккаунт найден"),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping("/{id}/accounts")
    public List<AccountDTO> getAccountsByUserId(@PathVariable Long id){
        return accountService.getAccountsByUserId(id)
                .stream()
                .map(MapperDTO::toDTO)
                .toList();
    }

    @Operation(summary = "Удалить аккаунт")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Удалить ппользователя не получилось"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @DeleteMapping("/{id}")
    public void removeAccount(@PathVariable Long id) {
        accountService.deleteAccountById(id);
    }


}
