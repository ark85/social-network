package io.github.ark85.study.socialnetwork.service.queue;

import io.github.ark85.study.socialnetwork.configuration.RabbitProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RabbitQueueRouterService {

    private final RabbitProperties rabbitProperties;

    public String getRoutingKey(UUID userId) {
        int shard = Math.floorMod(userId.hashCode(), rabbitProperties.getPostQueueNumber());
        return rabbitProperties.getPostRoutingKeyPrefix() + "." + shard;
    }
}
