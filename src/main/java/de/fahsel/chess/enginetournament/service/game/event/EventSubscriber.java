package de.fahsel.chess.enginetournament.service.game.event;

import de.fahsel.chess.enginetournament.model.event.GameEvent;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventSubscriber {
    private final List<GameEventHandler<GameEvent>> gameEventHandlers = new ArrayList<>();
    private final GameEventStream gameEventStream;

    public EventSubscriber(List<GameEventHandler<? extends GameEvent>> gameEventHandlers, GameEventStream gameEventStream) {
        for (GameEventHandler<? extends GameEvent> gameEventHandler : gameEventHandlers) {
            //noinspection unchecked
            this.gameEventHandlers.add((GameEventHandler<GameEvent>) gameEventHandler);
        }

        this.gameEventStream = gameEventStream;
    }

    @PostConstruct
    public void initialize() {
        for (GameEventHandler<GameEvent> gameEventHandler: gameEventHandlers) {
            Class<? extends GameEvent> eventClass = getEventByHandler(gameEventHandler);

            gameEventStream.subscribe(gameEventHandler::handle, eventClass);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends GameEvent> getEventByHandler(GameEventHandler<? extends GameEvent> gameEventHandler) {
        Class<? extends GameEventHandler<? extends GameEvent>> gameEvenetHandlerClass = (Class<? extends GameEventHandler<? extends GameEvent>>) gameEventHandler.getClass();

        Type[] interfaces = gameEvenetHandlerClass.getGenericInterfaces();

        ParameterizedType firstType = (ParameterizedType) interfaces[0];

        return (Class<? extends GameEvent>) firstType.getActualTypeArguments()[0];
    }
}
