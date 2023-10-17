package dandi.dandi.post.adapter.out.persistence.jpa;

import dandi.dandi.member.domain.Member;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import dandi.dandi.postlike.adapter.out.persistence.jpa.PostLikeJpaEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities = new ArrayList<>();

    @OneToMany(mappedBy = "postLikeKey.postJpaEntity")
    private List<PostLikeJpaEntity> postLikeJpaEntities = new ArrayList<>();

    protected PostJpaEntity() {
    }

    public PostJpaEntity(Long id, Long memberId, Double minTemperature, Double maxTemperature, String postImageUrl,
                         Long feelingIndex, List<AdditionalFeelingIndexJpaEntity> additionalFeelingIndicesJpaEntities,
                         List<PostLikeJpaEntity> postLikeJpaEntities) {
        this.id = id;
        this.memberId = memberId;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.postImageUrl = postImageUrl;
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndicesJpaEntities = additionalFeelingIndicesJpaEntities;
        additionalFeelingIndicesJpaEntities.forEach(
                additionalFeelingIndexJpaEntity -> additionalFeelingIndexJpaEntity.setPostJpaEntity(this));
        this.postLikeJpaEntities = postLikeJpaEntities;
    }

    public static PostJpaEntity initial(Post post, Long memberId) {
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
                additionalFeelingIndices,
                null
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<PostLikeJpaEntity> getPostLikeJpaEntities() {
        return postLikeJpaEntities;
    }

    public Post toPost(Member postWriter) {
        return new Post(
                id,
                postWriter,
                new Temperatures(minTemperature, maxTemperature),
                postImageUrl,
                new WeatherFeeling(feelingIndex, getAdditionalFeelingIndices()),
                createdAt.toLocalDate(),
                getLikingMemberIds()
        );
    }

    private List<Long> getAdditionalFeelingIndices() {
        return additionalFeelingIndicesJpaEntities.stream()
                .map(AdditionalFeelingIndexJpaEntity::getValue)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Long> getLikingMemberIds() {
        return getPostLikeJpaEntities()
                .stream()
                .map(PostLikeJpaEntity::getMemberId)
                .collect(Collectors.toUnmodifiableList());
    }
}
