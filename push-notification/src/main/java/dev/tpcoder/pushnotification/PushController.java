package dev.tpcoder.pushnotification;

import dev.tpcoder.pushnotification.common.SendNotificationService;
import dev.tpcoder.pushnotification.common.model.PushRequest;
import dev.tpcoder.pushnotification.common.model.SendNotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PushController {
    private final Logger logger = LoggerFactory.getLogger(PushController.class);
    private final SendNotificationService fcmAdminService;
    private final SendNotificationService fcmRawProtocolService;

    public PushController(
            @Qualifier("fcmService") SendNotificationService fcmAdminService,
            @Qualifier("fcmRawProtocolService") SendNotificationService fcmRawProtocolService
    ) {
        this.fcmAdminService = fcmAdminService;
        this.fcmRawProtocolService = fcmRawProtocolService;
    }

    @PostMapping("/push/firebase-admin")
    public Mono<SendNotificationResult> sendNotificationWithFirebase(@RequestBody PushRequest body) {
        logger.debug("Send notification with firebase admin");
        return fcmAdminService.send(body)
                .doOnSuccess(_ -> logger.debug("Send notification with firebase admin success"));
    }

    @PostMapping("/push/google")
    public Mono<SendNotificationResult> sendNotificationWithGoogleAuth(@RequestBody PushRequest body) {
        logger.debug("Send notification with google auth");
        return fcmRawProtocolService.send(body)
                .doOnSuccess(_ -> logger.debug("Send notification with google auth success"));
    }

}
