package dev.tpcoder.pushnotification.firebaseadmin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import dev.tpcoder.pushnotification.common.SendNotificationService;
import dev.tpcoder.pushnotification.common.model.PushRequest;
import dev.tpcoder.pushnotification.common.model.SendNotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class FcmService implements SendNotificationService {
    private final Logger logger = LoggerFactory.getLogger(FcmService.class);
    private final FirebaseMessaging firebaseMessaging;
    private final ObjectMapper objectMapper;

    public FcmService(FirebaseMessaging firebaseMessaging, ObjectMapper objectMapper) {
        this.firebaseMessaging = firebaseMessaging;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<SendNotificationResult> send(PushRequest pushRequest) {
        var notificationDetail = pushRequest.notification();
        Notification notification = Notification.builder()
                .setTitle(notificationDetail.title())
                .setBody(notificationDetail.body())
                .setImage("https://portfolio.tpcoder.dev/assets/images/profile.webp")
                .build();
        var dataPayload = pushRequest.data();
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(notification)
                .putAllData(convertToMap(dataPayload))
                .addAllTokens(pushRequest.tokens())
                .build();
        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(multicastMessage);
            logger.debug(STR."Successfully sent message: \{response}");
            return Mono.just(new SendNotificationResult("Send notification success", true));
        } catch (FirebaseMessagingException e) {
            logger.error("Send notification failed", e);
            return Mono.just(new SendNotificationResult("Send notification failed", false));
        }
    }

    private Map<String, String> convertToMap(Object object) {
        return objectMapper.convertValue(object, new TypeReference<>() {});
    }
}
