package dandi.dandi.batchcommons.application.port.in;

public class ChunkSizeUpdateCommand {

    private String updateKey;
    private String name;
    private Integer chunkSize;

    public ChunkSizeUpdateCommand() {
    }

    public ChunkSizeUpdateCommand(String updateKey, String name, Integer chunkSize) {
        this.updateKey = updateKey;
        this.name = name;
        this.chunkSize = chunkSize;
    }

    public String getUpdateKey() {
        return updateKey;
    }

    public String getName() {
        return name;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }
}
