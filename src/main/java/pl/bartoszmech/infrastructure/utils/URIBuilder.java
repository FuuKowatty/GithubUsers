package pl.bartoszmech.infrastructure.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class URIBuilder {

    public String buildRepositoryUrl(String username) {
        return getURIBuilder()
            .pathSegment("users", username, "repos")
            .toUriString();
    }

    public String buildBranchesUrl(String username, String repositoryName) {
        return getURIBuilder()
            .pathSegment("repos", username, repositoryName, "branches")
            .toUriString();
    }

    private UriComponentsBuilder getURIBuilder() {
        return UriComponentsBuilder.newInstance();
    }

}
