package pl.bartoszmech.application.response;

public record RepositoriesResponseAPI(
    String name,
    Owner owner
) {

    public record Owner(
        String login
    ) {}

    public RepositoriesResponseAPI(String name, Owner owner) {
        this.name = name;
        this.owner = owner;
    }

}
