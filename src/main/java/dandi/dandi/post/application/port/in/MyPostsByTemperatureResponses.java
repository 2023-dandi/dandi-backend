package dandi.dandi.post.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class MyPostsByTemperatureResponses implements ImageResponse {

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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        List<MyPostByTemperatureResponse> posts = this.posts
                .stream()
                .map(post -> post.addImageAccessUrl(imageAccessUrl))
                .collect(Collectors.toUnmodifiableList());
        return new MyPostsByTemperatureResponses(posts, writer.addImageAccessUrl(imageAccessUrl), lastPage);
    }
}
