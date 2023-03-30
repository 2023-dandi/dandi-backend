package dandi.dandi.clothes.adapter.persistence;

import dandi.dandi.clothes.domain.Season;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ClothesSeasonJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    private ClothesJpaEntity clothesJpaEntity;

    private Season season;

    public ClothesSeasonJpaEntity() {
    }

    public ClothesSeasonJpaEntity(Long id, ClothesJpaEntity clothesJpaEntity, Season season) {
        this.id = id;
        this.clothesJpaEntity = clothesJpaEntity;
        this.season = season;
    }

    public ClothesSeasonJpaEntity(ClothesJpaEntity clothesJpaEntity, Season season) {
        this(null, clothesJpaEntity, season);
    }

    public static ClothesSeasonJpaEntity fromSeason(Season season) {
        return new ClothesSeasonJpaEntity(null, null, season);
    }

    public void setClothesJpaEntity(ClothesJpaEntity clothesJpaEntity) {
        this.clothesJpaEntity = clothesJpaEntity;
    }
}
