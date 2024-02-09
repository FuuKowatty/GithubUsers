package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.ClientResponse;

import java.util.List;

@Service
@AllArgsConstructor
public class GithubUsersService {

    public IFetcher fetcher;

    public List<ClientResponse> fetchUserRepositories(String username) {
        System.out.println(fetcher.fetchRepositories(username));

        return null;
    }

}
