package com.topcode.topenergyv2.storageservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class StorageRouter {
    @Value("${volume-path}")
    private String volumePath;

    @Bean
    public RouterFunction<ServerResponse> storageRoute(StorageHandler storageHandler){
        return RouterFunctions.route()
                .path("/storage", b -> b
                        .POST("/upload", accept(MediaType.MULTIPART_MIXED),storageHandler::upload)
                        .resources("/view/**",new FileSystemResource(volumePath)))



                .build();

    }
}
