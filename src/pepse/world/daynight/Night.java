package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;


/**
 * This class represent the night illusion in the game.
 */
public class Night {

    // Constants
    private static final String NIGHT_TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * This method is responsible for creating the Night object in the game.
     *
     * @param gameObjects      - game objects collection
     * @param layer            - the wanted layer
     * @param windowDimensions - the screen window dim
     * @param cycleLength      - the time which we want the night object will be presented in the game.
     * @return Game object - the Night game object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects, int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), windowDimensions.y()),
                new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK)));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        gameObjects.addGameObject(night, layer);
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness,  // the method to call
                0f,  // initial transition value
                MIDNIGHT_OPACITY,  // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                cycleLength / 2,  // transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  // nothing further to execute upon reaching final value
        return night;
    }
}
