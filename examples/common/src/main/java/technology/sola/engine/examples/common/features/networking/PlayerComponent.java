package technology.sola.engine.examples.common.features.networking;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;

/**
 * {@link Component} to mark an {@link technology.sola.ecs.Entity} as a player.
 */
@NullMarked
public record PlayerComponent() implements Component {
}
