package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "clothes")
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

    protected ClothesJpaEntity() {
    }

    public ClothesJpaEntity(Long id, Long memberId, List<ClothesSeasonJpaEntity> seasons, Category category,
                            String clothesImageUrl) {
        this.id = id;
        this.memberId = memberId;
        this.seasons = seasons;
        this.category = category;
        this.clothesImageUrl = clothesImageUrl;
        seasons.forEach(clothesSeasonJpaEntity -> clothesSeasonJpaEntity.setClothesJpaEntity(this));
    }

    public static ClothesJpaEntity fromClothesAndMemberId(Clothes clothes, Long memberId) {
        List<ClothesSeasonJpaEntity> clothesSeasonJpaEntities = clothes.getSeasons()
                .stream()
                .map(ClothesSeasonJpaEntity::fromSeason)
                .collect(Collectors.toUnmodifiableList());
        return new ClothesJpaEntity(
                null,
                memberId,
                clothesSeasonJpaEntities,
                clothes.getCategory(),
                clothes.getClothesImageUrl()
        );
    }

    public List<ClothesSeasonJpaEntity> getSeasons() {
        return seasons;
    }


    public Clothes toClothes() {
        List<Season> seasons = this.seasons.stream()
                .map(ClothesSeasonJpaEntity::getSeason)
                .collect(Collectors.toUnmodifiableList());
        return new Clothes(id, category, seasons, clothesImageUrl);
    }
}
