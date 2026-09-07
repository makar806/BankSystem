package ru.sinitsyn.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsyn.app.DTO.OperationDTO;
import ru.sinitsyn.app.MapperDTO;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.OperationService;

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
}
