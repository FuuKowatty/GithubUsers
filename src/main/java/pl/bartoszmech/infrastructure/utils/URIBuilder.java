package pl.bartoszmech.infrastructure.utils;

import org.springframework.web.util.UriComponentsBuilder;

public class URIBuilder {

    public static String buildRepositoryUrl(String username) {
        return getURIBuilder()
            .pathSegment("users", username, "repos")
            .toUriString();
    }

    public static String buildBranchesUrl(String username, String repositoryName) {
        return getURIBuilder()
            .pathSegment("repos", username, repositoryName, "branches")
            .toUriString();
    }

    private static UriComponentsBuilder getURIBuilder() {
        return UriComponentsBuilder.newInstance();
    }

}
