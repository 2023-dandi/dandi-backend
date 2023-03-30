package dandi.dandi.clothes.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageUseCase;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clothes")
public class ClosetController implements ClosetControllerDocs {

    private final ClothesImageUseCase clothesImageUseCase;
    private final ClothesUseCase clothesUseCase;

    public ClosetController(ClothesImageUseCase clothesImageUseCase, ClothesUseCase clothesUseCase) {
        this.clothesImageUseCase = clothesImageUseCase;
        this.clothesUseCase = clothesUseCase;
    }

    @PostMapping("/image")
    public ResponseEntity<ClothesImageRegisterResponse> registerClothesImage(@Login Long memberId,
                                                                             @RequestPart(value = "clothesImage")
                                                                             MultipartFile clothesImage) {
        ClothesImageRegisterResponse clothesImageRegisterResponse =
                clothesImageUseCase.uploadClothesImage(memberId, clothesImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(clothesImageRegisterResponse);
    }

    @PostMapping
    public ResponseEntity<Void> registerClothes(@Login Long memberId,
                                                @RequestBody ClothesRegisterCommand clothesRegisterCommand) {
        clothesUseCase.registerClothes(memberId, clothesRegisterCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
