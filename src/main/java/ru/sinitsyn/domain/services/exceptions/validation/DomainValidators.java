package ru.sinitsyn.domain.services.exceptions.validation;

import java.util.Objects;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

public final class DomainValidators {
    private DomainValidators() {}

    public static <T> T requireNonNull(T value, String field){
        return Objects.requireNonNull(value,field);
    }

    public static String requireNonBlank(String value, String field){
        if (value == null || value.trim().isEmpty()){
            throw new DomainValidationException(field + "must be not blank");
        }
        return value.trim();
    }

    public static long requirePositive(long value, String field){
        if (value <= 0){
            throw new DomainValidationException(field + "must be positive");
        }
        return value;
    }

    public static long requireNonNegative(long value, String field){
        if (value < 0){
            throw new DomainValidationException(field + "can't be negative");
        }
        return value;
    }
}