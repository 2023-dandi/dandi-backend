package dandi.dandi.clothes.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.clothes.application.port.in.ClothesImageRegisterResponse;
import dandi.dandi.clothes.application.port.in.ClothesImageUseCase;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.in.ClothesUseCase;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clothes")
public class ClothesController implements ClothesControllerDocs {

    private final ClothesImageUseCase clothesImageUseCase;
    private final ClothesUseCase clothesUseCase;

    public ClothesController(ClothesImageUseCase clothesImageUseCase, ClothesUseCase clothesUseCase) {
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

    @GetMapping
    public ResponseEntity<ClothesResponses> getClothes(@Login Long memberId, @RequestParam("category") String category,
                                                       @RequestParam("season") Set<String> seasons,
                                                       Pageable pageable) {
        return ResponseEntity.ok(clothesUseCase.getClothes(memberId, category, seasons, pageable));
    }

    @PostMapping
    public ResponseEntity<Void> registerClothes(@Login Long memberId,
                                                @RequestBody ClothesRegisterCommand clothesRegisterCommand) {
        clothesUseCase.registerClothes(memberId, clothesRegisterCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping({"/{clothesId}"})
    public ResponseEntity<Void> deleteClothes(@Login Long memberId, @PathVariable Long clothesId) {
        clothesUseCase.deleteClothes(memberId, clothesId);
        return ResponseEntity.noContent().build();
    }
}
