package dandi.dandi.batchcommons.adapter.in;

import dandi.dandi.batchcommons.application.port.in.ChunkSizeUpdateCommand;
import dandi.dandi.batchcommons.application.port.in.ChunkSizeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChunkSizeController {

    private final ChunkSizeUseCase chunkSizeUseCase;

    public ChunkSizeController(ChunkSizeUseCase chunkSizeUseCase) {
        this.chunkSizeUseCase = chunkSizeUseCase;
    }

    @PatchMapping("/batch/chunkSize")
    public ResponseEntity<Void> updateChunkSizeByName(ChunkSizeUpdateCommand command) {
        chunkSizeUseCase.updateChunkSizeByName(command);
        return ResponseEntity.noContent().build();
    }
}
