package dandi.dandi.clothes.adapter.out.persistence.jpa;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "clothes")
@EntityListeners(AuditingEntityListener.class)
public class ClothesJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_id")
    private Long id;

    private Long memberId;

    @OneToMany(mappedBy = "clothesJpaEntity", cascade = CascadeType.ALL)
    private List<ClothesSeasonJpaEntity> seasons = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private String clothesImageUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    protected ClothesJpaEntity() {
    }

    public ClothesJpaEntity(Long id, Long memberId, List<ClothesSeasonJpaEntity> seasons, Category category,
                            String clothesImageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.seasons = seasons;
        this.category = category;
        this.clothesImageUrl = clothesImageUrl;
        this.createdAt = createdAt;
        seasons.forEach(clothesSeasonJpaEntity -> clothesSeasonJpaEntity.setClothesJpaEntity(this));
    }

    public static ClothesJpaEntity fromClothes(Clothes clothes) {
        List<ClothesSeasonJpaEntity> clothesSeasonJpaEntities = clothes.getSeasons()
                .stream()
                .map(ClothesSeasonJpaEntity::fromSeason)
                .collect(Collectors.toUnmodifiableList());
        return new ClothesJpaEntity(
                null,
                clothes.getMemberId(),
                clothesSeasonJpaEntities,
                clothes.getCategory(),
                clothes.getClothesImageUrl(),
                null
        );
    }

    public Clothes toClothes() {
        List<Season> seasons = this.seasons.stream()
                .map(ClothesSeasonJpaEntity::getSeason)
                .collect(Collectors.toUnmodifiableList());
        return new Clothes(id, memberId, category, seasons, clothesImageUrl);
    }
}
