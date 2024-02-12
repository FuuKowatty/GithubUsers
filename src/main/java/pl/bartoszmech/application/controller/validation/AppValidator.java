package pl.bartoszmech.application.controller.validation;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.bartoszmech.infrastructure.exceptions.UsernameValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bartoszmech.application.controller.validation.UsernameValidationResult.CONTAINS_DOUBLE_HYPHEN;
import static pl.bartoszmech.application.controller.validation.UsernameValidationResult.END_WITH_HYPHEN;
import static pl.bartoszmech.application.controller.validation.UsernameValidationResult.INVALID_CHARACTERS;
import static pl.bartoszmech.application.controller.validation.UsernameValidationResult.OUT_OF_RANGE;
import static pl.bartoszmech.application.controller.validation.UsernameValidationResult.STARTS_WITH_HYPHEN;

@Service
@Log4j2
public class AppValidator {

    private List<UsernameValidationResult> errors;

    public void validateGithubUsername(String username) {
        if(!isValidUsername(username)) {
            String errors = getAllErrorMessages();
            log.error("Github username validation failed. Errors: " + errors);
            throw new UsernameValidationException(errors);
        }
    }

    private boolean isValidUsername(String username) {
        errors = new LinkedList<>();

        if (isEmpty(username)) {
            errors.addAll(Arrays.stream(UsernameValidationResult.values()).toList());
            return false;
        }

        if (isOutOfRange(username)) {
            errors.add(OUT_OF_RANGE);
        }

        if (!hasInvalidCharacters(username)) {
            errors.add(INVALID_CHARACTERS);
        }

        if (isStartWithHyphen(username)) {
            errors.add(STARTS_WITH_HYPHEN);
        }

        if (isEndWithHyphen(username)) {
            errors.add(END_WITH_HYPHEN);
        }

        if (isContainDoubleHyphen(username)) {
            errors.add(CONTAINS_DOUBLE_HYPHEN);
        }

        return errors.isEmpty();
    }

    private static boolean isContainDoubleHyphen(String username) {
        return username.contains("--");
    }

    private static boolean isEndWithHyphen(String username) {
        return username.endsWith("-");
    }

    private static boolean isStartWithHyphen(String username) {
        return username.startsWith("-");
    }

    private static boolean hasInvalidCharacters(String username) {
        String regex = "^[a-zA-Z0-9-]+$";
        return username.matches(regex);
    }

    private static boolean isOutOfRange(String username) {
        return username.length() > 39;
    }

    private static boolean isEmpty(String username) {
        return username == null || username.isEmpty();
    }

    private String getAllErrorMessages() {
        return errors
            .stream()
            .map(validationResult -> validationResult.info)
            .collect(Collectors.joining(", "));
    }

}
