package ru.sinitsyn.service;

import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.model.AccountModel;
import ru.sinitsyn.service.model.OperationModel;
import ru.sinitsyn.service.model.UserModel;

import java.util.Set;
import java.util.stream.Collectors;

public class DomainMapper {
    public static AccountModel toModel(Account entity) {
        if (entity == null) return null;
        return new AccountModel(entity.getId(),
                entity.getBalance(),
                entity.getOwner() != null ? entity.getOwner().getId() : null);

    }

    public static UserModel toModel(User entity) {
        if (entity == null) return null;

        Set<Long> friendIds = entity.getFriends() == null ? Set.of() : entity.getFriends().stream().map(User::getId).collect(Collectors.toSet());

        return new UserModel(
                entity.getId(),
                entity.getLogin(),
                entity.getName(),
                entity.getAge(),
                entity.getGender(),
                entity.getHairColor(),
                friendIds
        );
    }

    public static OperationModel toModel(Operation entity){
        if (entity == null) return null;
        return new OperationModel(entity.getId(), entity.getAmount(), entity.getType(), entity.getCreatedAt(), entity.getAccount() != null ? entity.getAccount().getId() : null);
    }
}
