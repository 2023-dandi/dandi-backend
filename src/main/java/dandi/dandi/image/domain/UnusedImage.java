package dandi.dandi.image.domain;

public class UnusedImage {

    private Long id;
    private String imageUrl;

    public UnusedImage(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
