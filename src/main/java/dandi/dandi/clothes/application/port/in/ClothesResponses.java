package dandi.dandi.clothes.application.port.in;

import java.util.List;

public class ClothesResponses {

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
}
