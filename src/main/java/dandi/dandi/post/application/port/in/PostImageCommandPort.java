package dandi.dandi.post.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageCommandPort {

    PostImageRegisterResponse uploadPostImage(Long memberId, MultipartFile multipartFile);
}
