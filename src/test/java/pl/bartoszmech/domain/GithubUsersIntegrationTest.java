package pl.bartoszmech.domain;

import com.github.tomakehurst.wiremock.client.WireMock;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.ErrorResponse;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static wiremock.org.apache.hc.core5.http.HttpStatus.SC_NOT_FOUND;
import static wiremock.org.apache.hc.core5.http.HttpStatus.SC_OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 8181)
public class GithubUsersIntegrationTest {

    @Autowired
    public WebTestClient webTestClient;

    @Test
    public void should_return_client_response_list() {
        // given
        stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody("""
                    [
                    {
                        "name": "Repository1",
                        "owner": {
                            "login": "username"
                        },
                        "fork": false
                    },
                    {
                        "name": "Repository2",
                        "owner": {
                            "login": "username"
                        },
                        "fork": true
                    },
                    {
                        "name": "Repository3",
                        "owner": {
                            "login": "username"
                        },
                        "fork": true
                    }
                    ]
            """.trim())));

        stubFor(WireMock.get("/repos/username/Repository1/branches")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody("""
                    [
                      {
                        "name": "branch1",
                        "commit": {
                          "sha": "commit1_sha"
                        }
                      },
                      {
                        "name": "branch2",
                        "commit": {
                          "sha": "commit2_sha"
                        }
                      },
                      {
                        "name": "branch3",
                        "commit": {
                          "sha": "commit3_sha"
                        }
                      }
                    ]
                    """)));

        // when
        Flux<GithubUsersResponse> githubUsersResponseFlux = webTestClient
            .get()
            .uri("/api/users/username")
            .header(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(GithubUsersResponse.class)
            .getResponseBody();

        // then
        StepVerifier.create(githubUsersResponseFlux)
            .expectNextMatches(response ->
                    response.repositoryName().equals("Repository1") &&
                    response.ownerLogin().equals("username") &&
                    response.branches().size() == 3
            )
            .expectComplete()
            .verify();

    }

    @Test
    public void should_return_404_if_user_not_found() {
        // given
        stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_NOT_FOUND)
                .withHeader("Content-Type", APPLICATION_JSON)));

        // when
        ErrorResponse errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .header("Content-Type", "application/json")
            .exchange()

        // then
        .expectBody(ErrorResponse.class)
        .returnResult()
        .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(SC_NOT_FOUND);
        assertThat(errorResponse.message()).isEqualTo("User username not found");
    }

}
