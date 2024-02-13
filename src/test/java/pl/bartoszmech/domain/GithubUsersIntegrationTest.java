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

import java.util.Arrays;
import java.util.List;

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
                            "login": "Owner1"
                        },
                        "fork": false
                    },
                    {
                        "name": "Repository2",
                        "owner": {
                            "login": "Owner2"
                        },
                        "fork": true
                    },
                    {
                        "name": "Repository3",
                        "owner": {
                            "login": "Owner3"
                        },
                        "fork": true
                    }
                    ]
            """.trim())));

        stubFor(WireMock.get("/repos/Owner1/Repository1/branches")
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
        List<GithubUsersResponse> githubUsersResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .header("Content-Type", "application/json")
            .accept()
            .exchange()

        // then
        .expectBodyList(GithubUsersResponse.class).hasSize(1)
        .returnResult()
        .getResponseBody();

        assertThat(githubUsersResponse.getFirst())
            .isEqualTo(new GithubUsersResponse("Repository1", "Owner1",
                Arrays.asList(
                    new GithubUsersResponse.Branch("branch1", "commit1_sha"),
                    new GithubUsersResponse.Branch("branch2", "commit2_sha"),
                    new GithubUsersResponse.Branch("branch3", "commit3_sha")
                ))
            );

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
            .accept()
            .exchange()

        // then
        .expectBody(ErrorResponse.class)
        .returnResult()
        .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(SC_NOT_FOUND);
        assertThat(errorResponse.message()).isEqualTo("User username not found");
    }

}
