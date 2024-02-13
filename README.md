# Collect GITHUB user data by providing your username

## The project implements a WebClient and uses multithreading to efficiently collect GITHUB user data as quickly as possible.

By providing username you can get data about **login**, **repositories** and each repository **branches**.

## Running Application
If you want to run the application locally you can follow these steps:
1. clone repository 
```shell
git clone https://github.com/FuuKowatty/GithubUsers.git
```
2. Go to project directory
```shell
cd GithubUsers
```
3. You can simply run application by
```shell
mvn spring-boot:run
```

## API Endpoints

| Endpoint              | Method | Request                            | Response                           | Function                                    |
|-----------------------|--------|------------------------------------|------------------------------------|---------------------------------------------|
| /api/users/{username} | GET    | Path Parameter (String) - Required | OK, list of user repositories data | get all repositories of user                |


### /api/users/{github_username}
If you make **GET** request on **http://localhost:8080/api/users/{username}** operation will be success only if username is valid


#### Success Operation
If you provided valid username and API limit is not exceed you will get a List of **GithubUsersResponse.java** after JSON serialization
which may looks like:

```json
[
  {
    "repositoryName": "string",
    "ownerLogin": "string",
    "branches": [
      {
        "name": "string",
        "lastCommit": "string"
      }
    ]
  }
]
```

#### Username not found
If your username will be valid, but it does not exist application will return you **404** status and message.
```json
{
  "statusCode": 404,
  "message": "User not found"
}
```


## Technologies
- java - 21
- Lombok - 1.18.30
- Spring Boot - 3.2.1
- Spring Boot Starter Web
- Spring Boot Starter Webflux
- Spring Boot Starter Test
- WireMock - 3.3.1
- AssertJ Core - 3.8.0