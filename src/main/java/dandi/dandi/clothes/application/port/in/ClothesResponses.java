package dandi.dandi.clothes.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ClothesResponses implements ImageResponse {

    private List<ClothesResponse> clothes;
    private boolean lastPage;

    public ClothesResponses() {
    }

    public ClothesResponses(List<ClothesResponse> clothes, boolean lastPage) {
        this.clothes = clothes;
        this.lastPage = lastPage;
    }

    public List<ClothesResponse> getClothes() {
        return clothes;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<ClothesResponse> clothes = this.clothes.stream()
                .map(clothesResponse -> clothesResponse.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new ClothesResponses(clothes, lastPage);
    }
}
