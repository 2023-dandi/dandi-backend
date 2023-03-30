package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClothesServiceTest {

    @Mock
    private ClothesPersistencePort clothesPersistencePort;

    @InjectMocks
    private ClothesService clothesService;

    @DisplayName("옷을 저장할 수 있다.")
    @Test
    void registerClothes() {
        ClothesRegisterCommand clothesRegisterCommand =
                new ClothesRegisterCommand(CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_URL);

        clothesService.registerClothes(MEMBER_ID, clothesRegisterCommand);

        verify(clothesPersistencePort).save(any(), any());
    }
}
