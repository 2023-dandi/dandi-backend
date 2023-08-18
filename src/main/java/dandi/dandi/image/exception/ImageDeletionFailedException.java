package dandi.dandi.image.exception;

import dandi.dandi.advice.InternalServerException;

import java.util.List;
import java.util.stream.Collectors;

public class ImageDeletionFailedException extends InternalServerException {

    private static final String MESSAGE = "이미지 파일 삭제에 실패했습니다. \r\nfileKey ------%s------";

    public ImageDeletionFailedException(String fileName) {
        super(String.format(MESSAGE, fileName));
    }

    public ImageDeletionFailedException(List<String> fileNames) {
        this(fileNames.stream()
                .collect(Collectors.joining("\r\n", "\r\n", "\r\n")));
    }
}
