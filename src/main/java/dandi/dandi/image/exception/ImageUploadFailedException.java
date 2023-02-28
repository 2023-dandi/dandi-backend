package dandi.dandi.image.exception;

import dandi.dandi.advice.InternalServerException;

public class ImageUploadFailedException extends InternalServerException {

    private static final String MESSAGE = "이미지 파일 업로드에 실패했습니다.";

    public ImageUploadFailedException() {
        super(MESSAGE);
    }
}
