package dandi.dandi.image.adapter.out.persistence.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UnusedImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_usage_id")
    private Long id;

    private String imageUrl;

    protected UnusedImageJpaEntity() {
    }

    public UnusedImageJpaEntity(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
