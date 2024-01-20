package dev.tpcoder.pushnotification.rawhttp;

import com.google.auth.oauth2.GoogleCredentials;
import dev.tpcoder.pushnotification.common.SendNotificationService;
import dev.tpcoder.pushnotification.common.model.FcmPayload;
import dev.tpcoder.pushnotification.common.model.PushRequest;
import dev.tpcoder.pushnotification.common.model.SendNotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Service
public class FcmRawProtocolService implements SendNotificationService {

    private final Logger logger = LoggerFactory.getLogger(FcmRawProtocolService.class);

    private static final String PATH = "/messages:send";

    private final GoogleCredentials googleCredentials;

    private final WebClient fcmWebClient;

    public FcmRawProtocolService(
            //            @Qualifier("file") GoogleCredentials googleCredentials
            @Qualifier("base64") GoogleCredentials googleCredentials,
            @Qualifier("fcmWebClient") WebClient fcmWebClient
    ) {
        this.googleCredentials = googleCredentials;
        this.fcmWebClient = fcmWebClient;
    }

    // Reference: https://firebase.google.com/docs/cloud-messaging/send-message#send-messages-to-multiple-devices
    // Documentation has mentioned that we need to implement your own batch send by iterating through the list of recipients
    // and sending to each recipient's token
    @Override
    public Mono<SendNotificationResult> send(PushRequest pushRequest) {
        return Mono.just(pushRequest)
                .flatMapMany(req -> Flux.fromIterable(req.tokens()))
                .flatMap(token -> sendToSingleDevice(token, pushRequest))
                .then(Mono.just(new SendNotificationResult("Send notification success", true)));
    }

    private Mono<FcmApiResponse> sendToSingleDevice(String deviceToken, PushRequest pushRequest) {
        String accessToken;
        try {
            accessToken = getAccessToken();
        } catch (IOException e) {
            logger.error("Get access token failed", e);
            throw new RuntimeException(e);
        }
        return fcmWebClient.post()
                .uri(PATH)
                .header(HttpHeaders.AUTHORIZATION, STR."Bearer \{accessToken}")
                .bodyValue(FcmPayload.fromPushRequest(deviceToken, pushRequest))
                .retrieve()
                .bodyToMono(FcmApiResponse.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(2)));
    }


    private String getAccessToken() throws IOException {
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}
