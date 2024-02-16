package pl.bartoszmech.application.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.GithubUsersService;
import reactor.core.publisher.Flux;


@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class GithubUsersController {

    private final GithubUsersService githubUsersService;

    @GetMapping("/{username}")
    public Flux<GithubUsersResponse> findAllRepositoriesByUsername(@PathVariable String username) {
        return githubUsersService.findAllRepositoriesByUsername(username);
    }

}
