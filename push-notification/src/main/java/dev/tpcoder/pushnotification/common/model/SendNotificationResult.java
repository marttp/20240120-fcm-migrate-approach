package dev.tpcoder.pushnotification.common.model;

public record SendNotificationResult(
        String message,
        boolean success
) {
}
