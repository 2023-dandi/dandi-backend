package dandi.dandi.clothes.application.port.in;

public interface ClothesUseCase {

    void registerClothes(Long memberId, ClothesRegisterCommand clothesRegisterCommand);

    void deleteClothes(Long memberId, Long clothesId);
}
