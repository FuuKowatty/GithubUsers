package pl.bartoszmech.infrastructure.fetcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import pl.bartoszmech.infrastructure.exceptions.ExternalAPIException;
import pl.bartoszmech.infrastructure.exceptions.UserNotFoundException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ErrorHandlingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
            .flatMap(response -> {
                if (!response.statusCode().is2xxSuccessful()) {
                    return switch (response.statusCode().value()) {
                        case 403 -> Mono.error(new ExternalAPIException(403, "Limit from external API was exceed"));
                        case 404 -> Mono.error(new UserNotFoundException("User not found"));
                        case 500 -> Mono.error(new ExternalAPIException(500, "External server error"));
                        default ->
                            Mono.error(new ExternalAPIException(response.statusCode().value(), "Something went wrong while fetching data"));
                    };
                }

                return Mono.just(response);
            })
            .doOnError(response -> log.error("Error handled by filter with message {}", response.getMessage()));
    }

}
