package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sinitsyn.app.DTO.OperationDTO;
import ru.sinitsyn.app.MapperDTO;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.OperationService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/operations")
public class OperationController {
    private final OperationService operationService;

    public OperationController(OperationService operationService){
        this.operationService = operationService;
    }

    @Operation(summary = "Просмотреть все опперации")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Операции удачно нашлись"),
            @ApiResponse(responseCode = "404", description = "Не удалось найти операции"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @GetMapping
    public List<OperationDTO> getAllOperations(@RequestParam(required = true) OperationType type, @RequestParam(required = false) Long id){
        return operationService.getAllOperations(type, id)
                .stream()
                .map(MapperDTO::toDTO)
                .toList();
    }

    @Operation(summary = "Положить деньги на аккаунт")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Деньги успешно зачислены на карту"),
            @ApiResponse(responseCode = "404", description = "Не удалось пополниить баланс"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam BigDecimal amount, Authentication authentication) {
        String currentLogin = authentication.getName();

        operationService.deposit(id, amount, currentLogin);
    }

    @Operation(summary = "Снять деньги с аккаунта")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Деньги успешно сняты со счета"),
            @ApiResponse(responseCode = "404", description = "Не удалось снть деньги со счета"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam BigDecimal amount, Authentication authentication) {
        String currentLogin = authentication.getName();

        operationService.withdraw(id, amount, currentLogin);
    }
}
