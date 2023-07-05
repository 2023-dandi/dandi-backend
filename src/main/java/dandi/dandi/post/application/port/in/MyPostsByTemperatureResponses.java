package dandi.dandi.post.application.port.in;

import java.util.List;

public class MyPostsByTemperatureResponses {

    private List<MyPostByTemperatureResponse> posts;
    private PostWriterResponse writer;
    private boolean lastPage;

    public MyPostsByTemperatureResponses() {
    }

    public MyPostsByTemperatureResponses(List<MyPostByTemperatureResponse> posts, PostWriterResponse writer,
                                         boolean lastPage) {
        this.posts = posts;
        this.writer = writer;
        this.lastPage = lastPage;
    }

    public List<MyPostByTemperatureResponse> getPosts() {
        return posts;
    }

    public PostWriterResponse getWriter() {
        return writer;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
