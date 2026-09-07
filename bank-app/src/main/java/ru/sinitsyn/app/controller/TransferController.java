package ru.sinitsyn.app.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsyn.app.DTO.request.TransferRequest;
import ru.sinitsyn.service.TransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService, RequestBodyService requestBodyService){
        this.transferService = transferService;
    }

    @Operation(summary = "Совершить перевод между пользователями")
    @ApiResponses( value = { @ApiResponse(responseCode = "200", description = "Перевод средств прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Перевод не удался"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    @PostMapping
    public void transfer(@RequestBody TransferRequest request, Authentication authentication) {
        String currentUserLogin = authentication.getName();

        transferService.transfer(request.fromAccountId(), request.toAccountId(), request.amount(), currentUserLogin);
    }
}
