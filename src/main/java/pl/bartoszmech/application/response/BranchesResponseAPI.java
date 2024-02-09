package pl.bartoszmech.application.response;


public record BranchesResponseAPI(
    String name,
    Commit commit
) {

    public record Commit(
        String sha
    ) {}

}
