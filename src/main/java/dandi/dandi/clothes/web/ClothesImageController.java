package dandi.dandi.clothes.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ClothesImageController implements ClothesImageControllerDocs {

    private final ClothesImageUseCase clothesImageUseCase;

    public ClothesImageController(ClothesImageUseCase clothesImageUseCase) {
        this.clothesImageUseCase = clothesImageUseCase;
    }

    @PostMapping("/clothes/image")
    public ResponseEntity<ClothesImageRegisterResponse> registerClothesImage(@Login Long memberId,
                                                                             @RequestPart(value = "clothesImage")
                                                                             MultipartFile clothesImage) {
        ClothesImageRegisterResponse clothesImageRegisterResponse =
                clothesImageUseCase.uploadClothesImage(memberId, clothesImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(clothesImageRegisterResponse);
    }
}
