package com.festago.fcm.application;

import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import java.util.List;

public interface FcmClient {

    boolean sendAll(List<String> tokens, FCMChannel channel, FcmPayload fcmPayload);
}
