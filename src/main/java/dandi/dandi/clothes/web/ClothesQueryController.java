package dandi.dandi.clothes.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.clothes.application.port.in.CategorySeasonsResponses;
import dandi.dandi.clothes.application.port.in.ClothesDetailResponse;
import dandi.dandi.clothes.application.port.in.ClothesQueryServicePort;
import dandi.dandi.clothes.application.port.in.ClothesResponses;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clothes")
public class ClothesQueryController implements ClothesQueryControllerDocs {

    private final ClothesQueryServicePort queryServicePort;
    private final Clock clock;

    public ClothesQueryController(ClothesQueryServicePort queryServicePort, Clock clock) {
        this.queryServicePort = queryServicePort;
        this.clock = clock;
    }

    @GetMapping("/{clothesId}")
    public ResponseEntity<ClothesDetailResponse> getSingleClothesDetails(@Login Long memberId,
                                                                         @PathVariable Long clothesId) {
        return ResponseEntity.ok(queryServicePort.getSingleClothesDetails(memberId, clothesId));
    }

    @GetMapping
    public ResponseEntity<ClothesResponses> getClothes(@Login Long memberId, @RequestParam("category") String category,
                                                       @RequestParam("season") Set<String> seasons,
                                                       Pageable pageable) {
        return ResponseEntity.ok(queryServicePort.getClothes(memberId, category, seasons, pageable));
    }

    @GetMapping("/categories-seasons")
    public ResponseEntity<CategorySeasonsResponses> getCategoriesAndSeasons(@Login Long memberId) {
        return ResponseEntity.ok(queryServicePort.getCategoriesAndSeasons(memberId));
    }


    @GetMapping("/today")
    public ResponseEntity<ClothesResponses> getTodayClothes(@Login Long memberId, Pageable pageable) {
        LocalDate today = LocalDate.now(clock);
        return ResponseEntity.ok(queryServicePort.getTodayClothes(memberId, today, pageable));
    }
}
