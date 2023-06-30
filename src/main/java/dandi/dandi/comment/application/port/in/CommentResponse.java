package dandi.dandi.comment.application.port.in;

import java.time.LocalDate;

public class CommentResponse {

    private Long id;
    private CommentWriterResponse writer;
    private boolean postWriter;
    private boolean mine;
    private LocalDate createdAt;
    private String content;

    public CommentResponse() {
    }

    public CommentResponse(Long id, CommentWriterResponse writerResponse, boolean wittenByPostWriter,
                           boolean mine, LocalDate createdAt, String content) {
        this.id = id;
        this.writer = writerResponse;
        this.postWriter = wittenByPostWriter;
        this.mine = mine;
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

    public boolean isMine() {
        return mine;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public CommentResponse addImageAccessUrl(String imageAccessUrl) {
        CommentWriterResponse writer = new CommentWriterResponse(this.writer.getId(), this.writer.getNickname(),
                imageAccessUrl + this.writer.getProfileImageUrl());
        return new CommentResponse(id, writer, postWriter, mine, createdAt, content);
    }
}
