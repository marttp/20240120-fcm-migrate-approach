package dev.tpcoder.pushnotification.common.model;

import java.util.List;

public record PushRequest(
        List<String> tokens,
        NotificationDetail notification,
        DataDetail data
) {

}
