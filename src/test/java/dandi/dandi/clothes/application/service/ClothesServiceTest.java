package dandi.dandi.clothes.application.service;

import static dandi.dandi.clothes.ClothesFixture.CLOTHES;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_CATEGORY;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_ID;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_FULL_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_IMAGE_URL;
import static dandi.dandi.clothes.ClothesFixture.CLOTHES_SEASONS;
import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.FALL;
import static dandi.dandi.clothes.domain.Season.SPRING;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.utils.image.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

import dandi.dandi.clothes.application.port.in.ClothesRegisterCommand;
import dandi.dandi.clothes.application.port.in.ClothesResponse;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import dandi.dandi.clothes.application.port.out.persistence.ClothesPersistencePort;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class ClothesServiceTest {

    private final ClothesPersistencePort clothesPersistencePort = Mockito.mock(ClothesPersistencePort.class);
    private final ClothesImageService clothesImageService = Mockito.mock(ClothesImageService.class);
    private final ClothesService clothesService =
            new ClothesService(clothesPersistencePort, clothesImageService, IMAGE_ACCESS_URL);

    @DisplayName("옷을 저장할 수 있다.")
    @Test
    void registerClothes() {
        ClothesRegisterCommand clothesRegisterCommand =
                new ClothesRegisterCommand(CLOTHES_CATEGORY, CLOTHES_SEASONS, CLOTHES_IMAGE_FULL_URL);

        clothesService.registerClothes(MEMBER_ID, clothesRegisterCommand);

        verify(clothesPersistencePort).save(any(Clothes.class));
    }

    @DisplayName("자신의 옷을 삭제할 수 있다.")
    @Test
    void deleteClothes() {
        when(clothesPersistencePort.findById(CLOTHES_ID))
                .thenReturn(Optional.of(CLOTHES));

        clothesService.deleteClothes(MEMBER_ID, CLOTHES_ID);

        verify(clothesPersistencePort).deleteById(CLOTHES_ID);
        verify(clothesImageService).deleteClothesImage(CLOTHES);
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

    @DisplayName("카테고리, 계절 조건에 따른 내 옷을 조회할 수 있다.")
    @Test
    void getClothes() {
        PageRequest pageable = PageRequest.of(0, 10, DESC, "createdAt");
        Clothes clothes1 = new Clothes(1L, MEMBER_ID, TOP, List.of(SUMMER, FALL), CLOTHES_IMAGE_URL);
        Clothes clothes2 = new Clothes(1L, MEMBER_ID, TOP, List.of(SPRING, FALL), CLOTHES_IMAGE_URL);
        when(clothesPersistencePort.findByMemberIdAndCategoryAndSeasons(MEMBER_ID, TOP, Set.of(SPRING, SUMMER),
                pageable))
                .thenReturn(new SliceImpl<>(List.of(clothes2, clothes1), pageable, false));

        ClothesResponses actual = clothesService.getClothes(MEMBER_ID, TOP.name(),
                Set.of(SPRING.name(), SUMMER.name()), pageable);

        ClothesResponse firstClothesResponse = actual.getClothes().get(0);
        assertAll(
                () -> assertThat(actual.isLastPage()).isTrue(),
                () -> assertThat(firstClothesResponse.getClothesImageUrl()).isEqualTo(CLOTHES_IMAGE_FULL_URL)
        );
    }
}
