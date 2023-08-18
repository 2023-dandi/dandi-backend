package dandi.dandi.image.application.out;

public interface UnusedImagePersistencePort {

    void save(String imageUrl);

    void delete(String imageUrl);

    void deleteAllBatch(Iterable<Long> ids);
}
