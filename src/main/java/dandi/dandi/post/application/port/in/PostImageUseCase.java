package dandi.dandi.post.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageUseCase {

    String uploadPostImage(Long memberId, MultipartFile multipartFile);
}
