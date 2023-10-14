package dandi.dandi.batchcommons.application.port.in;

public class BatchThreadSizeUpdateCommand {

    private String batchAdminKey;
    private String name;
    private Integer batchThreadSize;

    public BatchThreadSizeUpdateCommand() {
    }

    public BatchThreadSizeUpdateCommand(String batchAdminKey, String name, Integer batchThreadSize) {
        this.batchAdminKey = batchAdminKey;
        this.name = name;
        this.batchThreadSize = batchThreadSize;
    }

    public String getBatchAdminKey() {
        return batchAdminKey;
    }

    public String getName() {
        return name;
    }

    public Integer getBatchThreadSize() {
        return batchThreadSize;
    }
}
