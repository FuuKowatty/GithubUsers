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
If you make **GET** request on **http://localhost:8080/api/users/{username}** you will receive 200 (OK) status only if
username is valid

#### Username Validation
Criteria of github_username are based on https://github.com/shinnn/github-username-regex and contains:
1. Username cannot be empty or null.
2. String requires length between 1 and 39.
3. String requires characters which matches "^[a-zA-Z0-9-]+$" regex.
4. Username cannot start with and end with hyphen ("-"), and cannot contain double hyphen ("--").

If any of this rules will be broke application will return **ErrorResponse.java** after JSON serialization which may looks like:
```json
{
  "statusCode": 0,
  "message": "string"
}
```

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

### Errors&Limitations
#### Api restrictions
Unlikely github api have limitations, especially if you are not authorized you will GET max 30results about user's repositories.
Github API can proceed limited requested on IP during one hour, so I prefer to use **dev profile** for developing which
will response you with raw data.

If you exceed limit API during fetching data, application will throw Exception and return valid message with **403** status.

#### Username not found
If your username will be valid, but it does not exist application will return you **404** status and valid message.

<br/>

**You can check fully described endpoint with all responses on http://localhost:8080/swagger-ui/index.html#/github-users-controller/findAllRepositoriesByUsername**

## Technologies
- java - 21
- Lombok - 1.18.30
- Spring Boot - 3.1.6
- Spring Boot Starter Web
- Spring Boot Starter Webflux
- Spring Boot Starter Validation - 3.2.0
- Spring Boot Starter Test
- WireMock - 3.3.1
- AssertJ Core - 3.8.0
- Spring Boot Configuration Processor
- Springdoc OpenAPI Starter Webmvc UI - 2.3.0
- Spring Boot Starter Cache