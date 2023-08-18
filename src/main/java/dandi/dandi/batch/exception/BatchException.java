package dandi.dandi.batch.exception;

public class BatchException extends RuntimeException {

    private static final String QUERY_PROVIDER_OBJECT_CREATION_FAILED_MESSAGE = "[jobName : %s] Query Provider 생성 실패";

    public BatchException(String message) {
        super(message);
    }

    public static BatchException queryProviderObjectCreationFailed(String jobName) {
        return new BatchException(String.format(QUERY_PROVIDER_OBJECT_CREATION_FAILED_MESSAGE, jobName));
    }
}
