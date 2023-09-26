package dandi.dandi.batchcommons.application.port.in;

public class ChunkSizeUpdateCommand {

    private String batchAdminKey;
    private String name;
    private Integer chunkSize;

    public ChunkSizeUpdateCommand() {
    }

    public ChunkSizeUpdateCommand(String batchAdminKey, String name, Integer chunkSize) {
        this.batchAdminKey = batchAdminKey;
        this.name = name;
        this.chunkSize = chunkSize;
    }

    public String getBatchAdminKey() {
        return batchAdminKey;
    }

    public String getName() {
        return name;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }
}
