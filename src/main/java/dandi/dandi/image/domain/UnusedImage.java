package dandi.dandi.image.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UnusedImage {

    private Long id;
    private String imageUrl;
    private LocalDate createdAt;

    public UnusedImage(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public UnusedImage(Long id, String imageUrl, LocalDate createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
