package com.topcode.topenergyv2.storageservice.storage;


import com.topcode.topenergyv2.storageservice.utils.APIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StorageHandler {
    @Value("${volume-path}")
    private String volumePath;

    public Mono<ServerResponse> upload(ServerRequest req){
        return req.body(BodyExtractors.toMultipartData()).flatMap(b->{
            FilePart file = (FilePart) b.toSingleValueMap().get("file");
            FormFieldPart uploadPath = (FormFieldPart) b.toSingleValueMap().get("path");
            Path path = Paths.get(volumePath,uploadPath.value());
            System.out.println(path.toString());
            System.out.println(Files.exists(path));
            if(!Files.exists(path)){
                try {
                    System.out.println(path.toString());
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return file.transferTo(path.resolve(file.filename())).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(
                    new APIResponse<String>( Paths.get(uploadPath.value()).resolve(file.filename()).toString(),true,"").getAsMap())
            ));
        });

    }

}

/*

       return Mono.zip(file,uploadPath).flatMap(data->{
                FilePart f = data.getT1();
                String up = data.getT2();
                System.out.println(f.filename());
                System.out.println(up);
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<Void>(null,true,"").getAsMap()));
            });

 */


      /* return file.zipWith(uploadPath).flatMap(t->{
            FilePart f = t.getT1();
            String up = t.getT2();
            System.out.println(f.filename());
            Path path = Paths.get("./src/main/resources/files" + up);
            System.out.println(path.resolve(f.filename()));

            return f.transferTo(path.resolve(f.filename())).thenReturn(true);
        }).flatMap(e->{

            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<Void>(null,true,"").getAsMap()));

        });*/

        /*
                Mono<FilePart> file = body.map(b-> b.toSingleValueMap().get("file")).cast(FilePart.class);
        Mono<String> uploadPath = body.map(b-> b.toSingleValueMap().get("path")).cast(String.class);
        return file.flatMap(f->{
            return uploadPath.flatMap(up->{
                System.out.println(f.filename());
                Path path = Paths.get("./src/main/resources/files" + up);
                System.out.println(path.resolve(f.filename()));

                return f.transferTo(path.resolve(f.filename())).thenReturn(true);
            });

        }).flatMap(e->{

            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<Void>(null,true,"").getAsMap()));

        });*/