package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
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

        verify(clothesPersistencePort).save(any(Clothes.class));
    }

    @DisplayName("존재하지 않는 옷을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteClothes_NotFound() {
        Long notFountClothesId = 1L;
        when(clothesPersistencePort.findById(notFountClothesId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> clothesService.deleteClothes(MEMBER_ID, notFountClothesId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.clothes().getMessage());
    }

    @DisplayName("자신의 소유가 아닌 옷을 삭제하려 하면 예외를 발생시킨다.")
    @Test
    void deleteClothes_Forbidden() {
        Long clothesId = 1L;
        Long anotherMemberId = 2L;
        Clothes anotherMembersClothes = new Clothes(
                clothesId, anotherMemberId, TOP, List.of(SUMMER), CLOTHES_IMAGE_URL);
        when(clothesPersistencePort.findById(clothesId))
                .thenReturn(Optional.of(anotherMembersClothes));

        assertThatThrownBy(() -> clothesService.deleteClothes(MEMBER_ID, clothesId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.clothesDeletion().getMessage());
    }
}
