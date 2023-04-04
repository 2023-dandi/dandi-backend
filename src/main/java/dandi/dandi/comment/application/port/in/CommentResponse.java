package dandi.dandi.comment.application.port.in;

import java.time.LocalDate;

public class CommentResponse {

    private Long id;
    private CommentWriterResponse writer;
    private boolean postWriter;
    private LocalDate createdAt;
    private String content;

    public CommentResponse(Long id, CommentWriterResponse writerResponse, boolean wittenByPostWriter,
                           LocalDate createdAt, String content) {
        this.id = id;
        this.writer = writerResponse;
        this.postWriter = wittenByPostWriter;
        this.createdAt = createdAt;
        this.content = content;
    }


    public Long getId() {
        return id;
    }

    public CommentWriterResponse getWriter() {
        return writer;
    }

    public boolean isPostWriter() {
        return postWriter;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }
}
