package dandi.dandi.image.application.out;

import dandi.dandi.image.exception.ImageDeletionFailedException;
import dandi.dandi.image.exception.ImageUploadFailedException;

import java.io.InputStream;
import java.util.List;

public interface ImageManager {

    void upload(String fileKey, InputStream inputStream) throws ImageUploadFailedException;

    void delete(String fileKey) throws ImageDeletionFailedException;

    void delete(List<String> filekeys) throws ImageDeletionFailedException;
}
