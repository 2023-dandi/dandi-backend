package dandi.dandi.member.domain;

import java.io.IOException;
import java.io.InputStream;

public interface ProfileImageUploader {

    void upload(String fileKey, InputStream inputStream) throws IOException;
}
