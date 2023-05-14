package dandi.dandi.pushnotification.exception;

import dandi.dandi.advice.InternalServerException;

public class FCMInitializationFailedException extends InternalServerException {

    public FCMInitializationFailedException() {
        super("fcm 환경 구축 실패");
    }
}
