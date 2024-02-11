package pl.bartoszmech.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.controller.validation.AppValidator;
import pl.bartoszmech.application.response.ErrorResponse;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.domain.GithubUsersService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class GithubUsersController {

    private final GithubUsersService githubUsersService;
    private final AppValidator appValidator;

    @Operation(summary = "Retrieve data about user's repositories by specified username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GithubUsersResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "It occurs probably because your API limit has exceeded",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Username not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "External API server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{username}")
    @Cacheable(value = "githubUsers", key = "#username",unless="#result.getStatusCodeValue() != 200")
    public ResponseEntity<List<GithubUsersResponse>> findAllRepositoriesByUsername(@PathVariable String username) {
        appValidator.validateGithubUsername(username);
        return ResponseEntity.status(OK).body(githubUsersService.findAllRepositoriesByUsername(username));
    }

}
