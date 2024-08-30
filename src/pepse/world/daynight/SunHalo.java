package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class represent the Sun Halo game object in the game.
 */
public class SunHalo {

    // Constants
    private static final String SUN_HALO_TAG = "sunHalo";

    /**
     * This method is responsible for creating the Sun Halo object in the game.
     *
     * @param gameObjects - game objects collection
     * @param layer       - the wanted layer
     * @param sun         - Sun game object
     * @param color       - the color of the sun halo object.
     * @return Game object - the Sun halo game object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        GameObject sunHalo = new GameObject(Vector2.ZERO,
                new Vector2(sun.getDimensions().add(new Vector2(100, 100))), new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }


}
