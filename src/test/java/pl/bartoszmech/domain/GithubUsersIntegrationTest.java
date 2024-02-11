package pl.bartoszmech.domain;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import pl.bartoszmech.BaseIntegrationTest;
import pl.bartoszmech.SampleAPIBody;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.ErrorResponseExternalAPI;

import java.util.Arrays;
import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;;


public class GithubUsersIntegrationTest extends BaseIntegrationTest implements SampleAPIBody {

    @Test
    public void should_return_client_response_list() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(bodyWithThreeRepositoriesJson())));

        stubBranchesForRepository("Owner1", "Repository1", new String[]{"branch1", "branch2"});
        stubBranchesForRepository("Owner2", "Repository2", new String[]{"branch3", "branch4"});
        stubBranchesForRepository("Owner3", "Repository3", new String[]{"branch5", "branch6"});

        // when
        List<GithubUsersResponse> githubUsersRespons = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

        // then
            .expectStatus().isOk()
            .expectBodyList(GithubUsersResponse.class).hasSize(3)
            .returnResult()
            .getResponseBody();

        assertThat(githubUsersRespons).extracting("ownerLogin", "repositoryName", "branches")
            .containsExactlyInAnyOrder(
                tuple("Owner1", "Repository1", Arrays.asList(
                    new GithubUsersResponse.Branch("branch1", "sha0"),
                    new GithubUsersResponse.Branch("branch2", "sha1")
                )),
                tuple("Owner2", "Repository2", Arrays.asList(
                        new GithubUsersResponse.Branch("branch3", "sha0"),
                    new GithubUsersResponse.Branch("branch4", "sha1")
                )),
                tuple("Owner3", "Repository3", Arrays.asList(
                        new GithubUsersResponse.Branch("branch5", "sha0"),
                    new GithubUsersResponse.Branch("branch6", "sha1")
                ))
            );
    }

    @Test
    public void should_return_403_if_limit_is_exceeded_since_start() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_FORBIDDEN)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(bodyWithThreeRepositoriesJson())));

        // when
        ErrorResponseExternalAPI errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

        // then
            .expectStatus().isForbidden()
            .expectBody(ErrorResponseExternalAPI.class)
            .returnResult()
            .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(SC_FORBIDDEN);
        assertThat(errorResponse.message()).isEqualTo("Limit from external API was exceed");
    }

    @Test
    public void should_return_403_if_limit_is_exceeded_during_downloading_branches() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(bodyWithThreeRepositoriesJson())));

        stubBranchesForRepository("Owner1", "Repository1", new String[]{"branch1", "branch2"});
        wireMockServer.stubFor(WireMock.get("/repos/Owner2/Repository2/branches")
            .willReturn(WireMock.aResponse()
                .withStatus(SC_FORBIDDEN)
                .withHeader("Content-Type", APPLICATION_JSON)));
        stubBranchesForRepository("Owner3", "Repository3", new String[]{"branch5", "branch6"});

        // when
        ErrorResponseExternalAPI errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

            // then
            .expectStatus().isForbidden()
            .expectBody(ErrorResponseExternalAPI.class)
            .returnResult()
            .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(SC_FORBIDDEN);
        assertThat(errorResponse.message()).isEqualTo("Limit from external API was exceed");
    }

    @Test
    public void should_return_404_if_user_not_found() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_FOUND)
                .withHeader("Content-Type", APPLICATION_JSON)));

        // when
        ErrorResponseExternalAPI errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

            // then
            .expectStatus().isNotFound()
            .expectBody(ErrorResponseExternalAPI.class)
            .returnResult()
            .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(errorResponse.message()).isEqualTo("User not found");
    }

    @Test
    public void should_return_500_if_internal_server_error() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .withHeader("Content-Type", APPLICATION_JSON)));

        // when
        ErrorResponseExternalAPI errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

            // then
            .expectStatus().is5xxServerError()
            .expectBody(ErrorResponseExternalAPI.class)
            .returnResult()
            .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.message()).isEqualTo("External server error");

    }

    @Test
    public void should_return_default_error_on_unexpected_error() {
        // given
        wireMockServer.stubFor(WireMock.get("/users/username/repos")
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .withHeader("Content-Type", APPLICATION_JSON)));

        // when
        ErrorResponseExternalAPI errorResponse = webTestClient
            .get()
            .uri("/api/users/username")
            .accept()
            .exchange()

            // then
            .expectStatus().is5xxServerError()
            .expectBody(ErrorResponseExternalAPI.class)
            .returnResult()
            .getResponseBody();

        assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.SC_SERVICE_UNAVAILABLE);
        assertThat(errorResponse.message()).isEqualTo("Something went wrong while fetching data");
    }

    private void stubBranchesForRepository(String owner, String repoName, String[] branches) {
        String url = String.format("/repos/%s/%s/branches", owner, repoName);
        wireMockServer.stubFor(WireMock.get(url)
            .willReturn(WireMock.aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(bodyWithBranchesJson(branches))));
    }

}
