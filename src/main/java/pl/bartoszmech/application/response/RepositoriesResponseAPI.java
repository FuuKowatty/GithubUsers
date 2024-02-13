package pl.bartoszmech.application.response;

public record RepositoriesResponseAPI(
    String name,
    Owner owner,
    Boolean fork
) {

    public record Owner(
        String login
    ) {}

    public RepositoriesResponseAPI(String name, Owner owner, Boolean fork) {
        this.name = name;
        this.owner = owner;
        this.fork = fork;
    }

}
