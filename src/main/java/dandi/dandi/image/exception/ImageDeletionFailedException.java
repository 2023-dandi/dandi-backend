package dandi.dandi.image.exception;

import dandi.dandi.advice.InternalServerException;

public class ImageDeletionFailedException extends InternalServerException {

    private static final String MESSAGE = "이미지 파일 삭제에 실패했습니다.";

    public ImageDeletionFailedException() {
        super(MESSAGE);
    }
}
