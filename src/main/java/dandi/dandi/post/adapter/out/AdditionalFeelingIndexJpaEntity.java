package dandi.dandi.post.adapter.out;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "additional_feeling_index")
public class AdditionalFeelingIndexJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_feeling_index_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostJpaEntity postJpaEntity;

    private Long value;

    protected AdditionalFeelingIndexJpaEntity() {
    }

    public AdditionalFeelingIndexJpaEntity(Long value) {
        this.value = value;
    }

    public void setPostJpaEntity(PostJpaEntity postJpaEntity) {
        this.postJpaEntity = postJpaEntity;
    }

    public Long getValue() {
        return value;
    }
}
