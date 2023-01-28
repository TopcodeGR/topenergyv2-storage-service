package com.topcode.topenergyv2.storageservice.webclients;



import com.topcode.topenergyv2.storageservice.utils.APIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceWebClient {

    private final WebClient client;

    @Value("${auth-service-uri}")
    private String authServiceUri;

    public AuthServiceWebClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(authServiceUri).build();
    }


    public Mono<Boolean> authorizeRequest(String accessToken, String clientId, String clientSecret) {
        return this.client.get().uri("/auth/authorizeRequest").accept(MediaType.APPLICATION_JSON)

                .retrieve()
                .bodyToMono(APIResponse.class)
                .map(r-> r.getSuccess());
    }
}
