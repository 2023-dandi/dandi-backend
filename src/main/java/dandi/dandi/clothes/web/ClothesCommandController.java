package dandi.dandi.clothes.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesUseCasePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clothes")
public class ClothesCommandController implements ClothesCommandControllerDocs {

    private final ClothesUseCasePort clothesUseCasePort;

    public ClothesCommandController(ClothesUseCasePort clothesUseCasePort) {
        this.clothesUseCasePort = clothesUseCasePort;
    }

    @PostMapping
    public ResponseEntity<Void> registerClothes(@Login Long memberId,
                                                @RequestBody ClothesRegisterCommand clothesRegisterCommand) {
        clothesUseCasePort.registerClothes(memberId, clothesRegisterCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping({"/{clothesId}"})
    public ResponseEntity<Void> deleteClothes(@Login Long memberId, @PathVariable Long clothesId) {
        clothesUseCasePort.deleteClothes(memberId, clothesId);
        return ResponseEntity.noContent().build();
    }
}
