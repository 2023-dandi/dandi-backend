package dandi.dandi.auth.adapter.out.oauth.apple;

import dandi.dandi.auth.adapter.out.oauth.apple.dto.ApplePublicKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${feign.client.apple.name}", url = "${feign.client.apple.url}")
public interface AppleApiCaller {

    @GetMapping
    ApplePublicKeys getPublicKeys();
}
