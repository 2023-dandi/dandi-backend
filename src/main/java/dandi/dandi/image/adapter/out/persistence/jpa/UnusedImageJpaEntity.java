package dandi.dandi.image.adapter.out.persistence.jpa;

import javax.persistence.*;

@Entity
@Table(name = "unused_image")
public class UnusedImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unused_image_id")
    private Long id;

    private String imageUrl;

    protected UnusedImageJpaEntity() {
    }

    public Long getId() {
        return id;
    }

    public UnusedImageJpaEntity(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
