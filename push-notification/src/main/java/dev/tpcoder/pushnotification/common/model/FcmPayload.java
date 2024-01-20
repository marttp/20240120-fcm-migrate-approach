package dev.tpcoder.pushnotification.common.model;

public record FcmPayload(FcmMessage message) {

    public static FcmPayload fromPushRequest(String token, PushRequest pushRequest) {
        return new FcmPayload(
                new FcmPayload.FcmMessage(
                        token,
                        pushRequest.notification(),
                        pushRequest.data()
                )
        );
    }

    record FcmMessage(String token, NotificationDetail notification, DataDetail data) {

    }
}
