package dandi.dandi.clothes.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface ClothesImageUseCase {

    ClothesImageRegisterResponse uploadClothesImage(Long memberId, MultipartFile multipartFile);
}
