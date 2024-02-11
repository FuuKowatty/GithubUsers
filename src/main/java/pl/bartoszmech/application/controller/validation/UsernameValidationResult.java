package pl.bartoszmech.application.controller.validation;

public enum UsernameValidationResult {
    IS_EMPTY("Username cannot be empty"),
    OUT_OF_RANGE("Username length must be between 1 and 39 characters"),
    INVALID_CHARACTERS("Username contains invalid characters"),
    STARTS_WITH_HYPHEN("Username cannot start with hyphen"),
    END_WITH_HYPHEN("Username cannot end with hyphen"),
    CONTAINS_DOUBLE_HYPHEN("Username cannot contain double hyphen");

    final String info;

    UsernameValidationResult(String info) {
        this.info = info;
    }

}
