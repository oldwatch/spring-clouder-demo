package net.demo.springcloud.backend.action;

import com.fasterxml.jackson.databind.util.RawValue;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class EchoController {



    public EchoController() {
    }


    @GetMapping(path="/mock/hello/{value}")
    public Mono<EchoInfoEntry> echo(@PathVariable String value){

        return Mono.just(value).map(str->{
            EchoInfoEntry entry=new EchoInfoEntry();
            entry.setName(System.getenv("DOCKER_HOST"));
            entry.setStr("Hello "+str);
            return entry;
        });

    }

    @Data
    public static class EchoInfoEntry{

        private String str;

        private String name;
    }


    @RequestMapping(path = "/mock/request/**", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> mock(@RequestBody(required = false) Mono<String> bodyMono, ServerHttpRequest request) {



        return    bodyMono.map((body) -> {

                            MockResponse response = new MockResponse(body, request);

                            response.setSuccess(true);

                            return response;

                        })
                        .map((response) -> ResponseEntity.status(response.success ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response))
                ;

    }


    @Data
    public static class MockResponse {

        private URI url;

        private Map<String, String> params = Maps.newHashMap();

        private String method;

        private String rawBody;

        private RawValue jsonBody;

        private boolean success;

        private Map<String, String> headers = Maps.newHashMap();

        public MockResponse(String body, ServerHttpRequest request) {

            url = request.getURI();

            rawBody = body;

            method = request.getMethodValue();

            MultiValueMap<String, String> map = request.getQueryParams();
            params = getValueMap(map);

            HttpHeaders theHeaders = request.getHeaders();

            headers = getValueMap(theHeaders);

            if (StringUtils.isNotBlank(rawBody)) {
                MediaType contentType = theHeaders.getContentType();
                if (contentType.equals(MediaType.APPLICATION_JSON)) {

                    jsonBody = new RawValue(body);

                }
            }
        }

        private static Map<String, String> getValueMap(MultiValueMap<String, String> map) {

            Map<String, String> result = Maps.newHashMap();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                List<String> values = entry.getValue();

                if (values.size() > 1) {
                    result.put(entry.getKey(), entry.getValue().toString());
                } else {
                    result.put(entry.getKey(), values.get(0));
                }
            }
            return result;
        }

    }

}
