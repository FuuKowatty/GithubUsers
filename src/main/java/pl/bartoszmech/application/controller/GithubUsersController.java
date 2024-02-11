package pl.bartoszmech.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.domain.GithubUsersService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class GithubUsersController {

    private final GithubUsersService githubUsersService;

    @Operation(summary = "Retrieve data about user's repositories by specified username")
    @ApiResponses(
        @ApiResponse(
            responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
            array =  @ArraySchema(schema = @Schema(implementation = GithubUsersResponse.class)))
        )
    )
    @GetMapping("/{username}")
    public List<GithubUsersResponse> findAllRepositoriesByUsername(@PathVariable String username) {
        return githubUsersService.findAllRepositoriesByUsername(username);
    }

}
