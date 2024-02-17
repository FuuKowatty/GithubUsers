package pl.bartoszmech.application.response;

import java.util.List;


public record GithubUsersResponse(
    String repositoryName,
    String ownerLogin,
    List<Branch> branches
) {

    public record Branch(
        String name,
        String lastCommit
    ) {}

}
