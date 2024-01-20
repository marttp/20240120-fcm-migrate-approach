package dev.tpcoder.pushnotification.rawhttp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static java.lang.StringTemplate.STR;

@Configuration
public class WebClientConfig {

    private final String projectId;

    public WebClientConfig(@Value("${firebase.project}") String projectId) {
        this.projectId = STR."https://fcm.googleapis.com/v1/projects/\{projectId}";
    }

    @Bean
    WebClient fcmWebClient() {
        return WebClient.builder()
                .baseUrl(projectId)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().compress(true)))
                .build();
    }
}
