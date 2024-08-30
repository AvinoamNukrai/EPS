package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

/**
 * This class represent the Sun game object in the game.
 */
public class Sun {

    // Constants
    private static final String SUN_TAG = "sun";
    private static final int SUN_REDUCTION_FACTOR = 10;
    private static final Float INIT_SUN_DEGREE = 0f;
    private static final Float FINAL_SUN_DEGREE = 360f;
    private static final float HALF_CIRCLE = 180f;


    /**
     * This method is responsible for creating the Sun object in the game.
     *
     * @param gameObjects      - game objects collection
     * @param layer            - the wanted layer
     * @param windowDimensions - the screen window dim
     * @param cycleLength      - the time which we want the night object will be presented in the game.
     * @return Game object - the Sun game object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        float sunXMovementCoord = windowDimensions.x() / 2f;
        float sunYMovementCoord = windowDimensions.y() / 3f;
        GameObject sun = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.y() / SUN_REDUCTION_FACTOR,
                        windowDimensions.y() / SUN_REDUCTION_FACTOR),
                new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW)));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(
                sun, // the game object being changed
                angle -> sun.setCenter(new Vector2((float) Math.sin((angle / HALF_CIRCLE) * Math.PI) * sunXMovementCoord,
                        (float) Math.cos(angle / HALF_CIRCLE * Math.PI) * sunYMovementCoord).add(
                        new Vector2(windowDimensions.mult(0.5f)))),// the method to call
                INIT_SUN_DEGREE,  // initial transition value - 0f
                FINAL_SUN_DEGREE,  // final transition value - 360f
                Transition.LINEAR_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                cycleLength / 2,  // transtion fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);  // nothing further to execute upon reaching final value
        return sun;
    }
}
