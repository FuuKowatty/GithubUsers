package pl.bartoszmech.domain;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
public class GithubUsersIntegrationTest {
//
//    @RegisterExtension
//    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
//        .options(wireMockConfig().dynamicPort())
//        .build();


//    public should_return_client_response() {
//        // given
//        stubFor(WireMock.get(WireMock.urlPathEqualTo("/"))
//            .willReturn(WireMock.aResponse()
//                .withStatus(SC_OK)
//                .withHeader("Content-Type", APPLICATION_JSON)
//                .withBody("""
//                            {
//                            "city_name": "Jastarnia",
//                                "data": [
//                                    {
//                                        "temp": 20.5,
//                                        "wind_spd": 10.2,
//                                        "datetime": "2024-01-05"
//                                    },
//                                    {
//                                        "temp": 18.9,
//                                        "wind_spd": 8.6,
//                                        "datetime": "2024-01-06"
//                                    }
//                                ]
//                            }""".trim())));
//
//        // when
//        // then
//    }

}
