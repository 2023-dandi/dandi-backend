package dandi.dandi.event.application.port.out;

public interface EventPort {

    void publishEvent(Object event);
}
