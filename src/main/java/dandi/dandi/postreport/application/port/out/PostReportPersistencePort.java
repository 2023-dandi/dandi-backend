package dandi.dandi.postreport.application.port.out;

public interface PostReportPersistencePort {

    void savePostReportOf(Long memberId, Long postId);
}
