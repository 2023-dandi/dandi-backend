package dandi.dandi.event.adapter.out;

import dandi.dandi.event.application.port.out.EventPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventAdapter implements EventPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
