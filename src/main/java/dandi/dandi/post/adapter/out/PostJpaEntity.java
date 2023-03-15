package dandi.dandi.post.adapter.out;

import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "post")
public class PostJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;

    private Double minTemperature;

    private Double maxTemperature;

    private String postImageUrl;

    private Long feelingIndex;

    @OneToMany(mappedBy = "postJpaEntity")
    @Cascade(value = CascadeType.ALL)
    private List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities;

    protected PostJpaEntity() {
    }

    public PostJpaEntity(Long id, MemberJpaEntity memberJpaEntity, Double minTemperature, Double maxTemperature,
                         String postImageUrl, Long feelingIndex,
                         List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities) {
        this.id = id;
        this.memberJpaEntity = memberJpaEntity;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.postImageUrl = postImageUrl;
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndicesJpaEntities = additionalFeelingIndicesJpaEntities;
        additionalFeelingIndicesJpaEntities.forEach(
                additionalFeelingIndexJpaEntity -> additionalFeelingIndexJpaEntity.setPostJpaEntity(this));
    }

    public static PostJpaEntity fromPostAndMemberJpaEntity(Post post, MemberJpaEntity memberJpaEntity) {
        List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndices = post.getAdditionalWeatherFeelingIndices()
                .stream()
                .map(AdditionalFeelingIndexJpaEntity::new)
                .collect(Collectors.toUnmodifiableList());
        return new PostJpaEntity(
                null,
                memberJpaEntity,
                post.getMinTemperature(),
                post.getMaxTemperature(),
                post.getPostImageUrl(),
                post.getWeatherFeelingIndex(),
                additionalFeelingIndices
        );
    }

    public Post toPost() {
        List<Long> additionalFeelingIndices = additionalFeelingIndicesJpaEntities.stream()
                .map(AdditionalFeelingIndexJpaEntity::getValue)
                .collect(Collectors.toUnmodifiableList());
        return new Post(
                id,
                memberJpaEntity.getNickname(),
                new Temperatures(minTemperature, maxTemperature),
                postImageUrl,
                new WeatherFeeling(feelingIndex, additionalFeelingIndices)
        );
    }
}
