package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import ru.sinitsyn.app.DTO.AccountDTO;
import ru.sinitsyn.app.DTO.ConvertedBalanceDTO;
import ru.sinitsyn.app.DTO.request.AmountRequest;
import ru.sinitsyn.app.MapperDTO;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.model.ConvertedBalanceModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


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
    public List<AccountDTO> getAllAccounts() {
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
    public ConvertedBalanceDTO getBalanceCurrency(@PathVariable Long id, @RequestParam(defaultValue = "RUB") String currency){

        return MapperDTO.toDTO(accountService.getBalanceInCurrency(id, currency));
    }

    @Operation(summary = "Положить деньги на аккаунт")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Деньги успешно зачислены на карту"),
            @ApiResponse(responseCode = "404", description = "Не удалось пополниить баланс"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestBody AmountRequest request) {
        accountService.deposit(id, request.amount());
    }

    @Operation(summary = "Снять деньги с аккаунта")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Деньги успешно сняты со счета"),
            @ApiResponse(responseCode = "404", description = "Не удалось снть деньги со счета"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestBody AmountRequest request) {
        accountService.withdraw(id, request.amount());
    }

}
