package dev.tpcoder.pushnotification.common;

import dev.tpcoder.pushnotification.common.model.PushRequest;
import dev.tpcoder.pushnotification.common.model.SendNotificationResult;
import reactor.core.publisher.Mono;

public interface SendNotificationService {

    Mono<SendNotificationResult> send(PushRequest pushRequest);
}
