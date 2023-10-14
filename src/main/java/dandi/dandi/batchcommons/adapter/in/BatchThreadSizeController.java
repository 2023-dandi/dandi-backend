package dandi.dandi.batchcommons.adapter.in;

import dandi.dandi.batchcommons.application.port.in.BatchThreadSizeUpdateCommand;
import dandi.dandi.batchcommons.application.port.in.BatchThreadSizeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchThreadSizeController {

    private final BatchThreadSizeUseCase batchThreadSizeUseCase;

    public BatchThreadSizeController(BatchThreadSizeUseCase batchThreadSizeUseCase) {
        this.batchThreadSizeUseCase = batchThreadSizeUseCase;
    }

    @PatchMapping("/batch/chunkSize")
    public ResponseEntity<Void> updateChunkSizeByName(BatchThreadSizeUpdateCommand command) {
        batchThreadSizeUseCase.updateBatchThreadSizeByName(command);
        return ResponseEntity.noContent().build();
    }
}
