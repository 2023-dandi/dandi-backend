package dandi.dandi.image.adapter.out.persistence.jpa;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "unused_image")
@EntityListeners(AuditingEntityListener.class)
public class UnusedImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unused_image_id")
    private Long id;

    private String imageUrl;

    @CreatedDate
    private LocalDate createdAt;

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
