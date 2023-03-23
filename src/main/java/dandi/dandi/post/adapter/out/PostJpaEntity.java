package dandi.dandi.post.adapter.out;

import dandi.dandi.member.domain.Member;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class PostJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private Long memberId;

    private Double minTemperature;

    private Double maxTemperature;

    private String postImageUrl;

    private Long feelingIndex;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "postJpaEntity")
    @Cascade(value = CascadeType.ALL)
    @BatchSize(size = 10)
    private List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities;

    protected PostJpaEntity() {
    }

    public PostJpaEntity(Long id, Long memberId, Double minTemperature, Double maxTemperature, String postImageUrl,
                         Long feelingIndex, List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities) {
        this.id = id;
        this.memberId = memberId;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.postImageUrl = postImageUrl;
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndicesJpaEntities = additionalFeelingIndicesJpaEntities;
        additionalFeelingIndicesJpaEntities.forEach(
                additionalFeelingIndexJpaEntity -> additionalFeelingIndexJpaEntity.setPostJpaEntity(this));
    }

    public static PostJpaEntity fromPostAndMemberId(Post post, Long memberId) {
        List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndices = post.getAdditionalWeatherFeelingIndices()
                .stream()
                .map(AdditionalFeelingIndexJpaEntity::new)
                .collect(Collectors.toUnmodifiableList());
        return new PostJpaEntity(
                null,
                memberId,
                post.getMinTemperature(),
                post.getMaxTemperature(),
                post.getPostImageUrl(),
                post.getWeatherFeelingIndex(),
                additionalFeelingIndices
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Post toPost(Member member) {
        List<Long> additionalFeelingIndices = additionalFeelingIndicesJpaEntities.stream()
                .map(AdditionalFeelingIndexJpaEntity::getValue)
                .collect(Collectors.toUnmodifiableList());
        return new Post(
                id,
                member,
                new Temperatures(minTemperature, maxTemperature),
                postImageUrl,
                new WeatherFeeling(feelingIndex, additionalFeelingIndices),
                createdAt.toLocalDate()
        );
    }
}
