package ru.sinitsyn.app;

import ru.sinitsyn.app.DTO.AccountDTO;
import ru.sinitsyn.app.DTO.ConvertedBalanceDTO;
import ru.sinitsyn.app.DTO.OperationDTO;
import ru.sinitsyn.app.DTO.UserDTO;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.model.AccountModel;
import ru.sinitsyn.service.model.ConvertedBalanceModel;
import ru.sinitsyn.service.model.OperationModel;
import ru.sinitsyn.service.model.UserModel;

public class MapperDTO {

    public static UserDTO toDTO(UserModel user){
        return new UserDTO(user.id(), user.login(), user.name(), user.age(), user.gender(), user.hairColor());
    }

    public static AccountDTO toDTO(AccountModel account) {
        return new AccountDTO(account.id(), account.balance(), account.ownerId());
    }

    public static OperationDTO toDTO(OperationModel operation) {
        return new OperationDTO(operation.id(), operation.amount(), operation.type(), operation.accountId(), operation.createdAt());
    }

    public static ConvertedBalanceDTO toDTO(ConvertedBalanceModel model) {
        return new ConvertedBalanceDTO(model.accountId(), model.balanceRub(), model.currency(), model.rateToRub(), model.convertedBalance(), model.rateTimestamp());
    }
}
