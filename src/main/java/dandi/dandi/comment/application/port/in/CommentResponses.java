package dandi.dandi.comment.application.port.in;

import java.util.List;

public class CommentResponses {

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
}
