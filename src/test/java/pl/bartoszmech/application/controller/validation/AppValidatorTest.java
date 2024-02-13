package pl.bartoszmech.application.controller.validation;

import org.junit.jupiter.api.Test;
import pl.bartoszmech.infrastructure.exceptions.UsernameValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppValidatorTest {

    AppValidator appValidator = new AppValidator();

    @Test
    public void should_throw_nothing_if_username_is_min_characters() {
        //given
        String username = "a";

        //when
        //given
        assertDoesNotThrow(() -> appValidator.validateGithubUsername(username));
    }

    @Test
    public void should_throw_nothing_if_username_is_max_characters() {
        //given
        String username = "a".repeat(39);

        //when
        //given
        assertDoesNotThrow(() -> appValidator.validateGithubUsername(username));
    }

    @Test
    public void should_throw_nothing_if_username_is_contains_one_hyphen() {
        //given
        String username = "user-name";

        //when
        //given
        assertDoesNotThrow(() -> appValidator.validateGithubUsername(username));
    }

    @Test
    public void should_throw_username_too_long_after_reach_the_limit() {
        //given
        String username = "a".repeat(39+1);

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo("Username length must be between 1 and 39 characters");
    }

    @Test
    public void should_throw_all_errors_if_pass_empty_string() {
        //given
        String username = "";

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo(
            "Username cannot be empty, " +
                "Username length must be between 1 and 39 characters, " +
                "Username contains invalid characters, " +
                "Username cannot start with hyphen, " +
                "Username cannot end with hyphen, " +
                "Username cannot contain double hyphen");
    }

    @Test
    public void should_throw_all_errors_if_pass_null() {
        //given
        String username = null;

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo(
            "Username cannot be empty, " +
                "Username length must be between 1 and 39 characters, " +
                "Username contains invalid characters, " +
                "Username cannot start with hyphen, " +
                "Username cannot end with hyphen, " +
                "Username cannot contain double hyphen");
    }

    @Test
    public void should_throw_invalid_characters_error_if_pass_invalid_characters() {
        //given
        String username = "usern@me";

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo("Username contains invalid characters");
    }

    @Test
    public void should_throw_starts_with_hyphen_error_if_pass_start_with_hyphen() {
        //given
        String username = "-username";

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo("Username cannot start with hyphen");
    }

    @Test
    public void should_throw_ends_with_hyphen_error_if_pass_end_with_hyphen() {
        //given
        String username = "username-";

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo("Username cannot end with hyphen");
    }

    @Test
    public void should_throw_double_hyphen_error_if_pass_double_hyphen() {
        //given
        String username = "user--name";

        //when
        //given
        Throwable tooLongException = assertThrows(UsernameValidationException.class, () -> appValidator.validateGithubUsername(username));
        assertThat(tooLongException.getMessage()).isEqualTo("Username cannot contain double hyphen");
    }

}
