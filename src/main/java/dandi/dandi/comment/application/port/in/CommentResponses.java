package dandi.dandi.comment.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponses implements ImageResponse {

    private List<CommentResponse> comments;
    private boolean lastPage;

    public CommentResponses() {
    }

    public CommentResponses(List<CommentResponse> comments, boolean lastPage) {
        this.comments = comments;
        this.lastPage = lastPage;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<CommentResponse> comments = this.comments.stream()
                .map(commentResponse -> commentResponse.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new CommentResponses(comments, lastPage);
    }
}
