package pl.bartoszmech.infrastructure.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UrlBuilder {

    public String buildRepositoryUrl(String username) {
        UriComponentsBuilder builder = getURIBuilder();
        return builder
            .pathSegment("users", username, "repos")
            .toUriString();
    }

    public String buildBranchesUrl(String username, String repositoryName) {
        UriComponentsBuilder builder = getURIBuilder();
        return builder
            .pathSegment("repos", username, repositoryName, "branches")
            .toUriString();
    }

    private UriComponentsBuilder getURIBuilder() {
        final String BASE_URL = "https://api.github.com";
        return UriComponentsBuilder.fromUriString(BASE_URL);
    }

}
