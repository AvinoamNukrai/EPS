package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;


/**
 * This class is represent the Sky in the game.
 */
public class Sky {

    // Constants
    private static final String SKY_TAG = "sky";
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * This method is creates the sky in the game.
     *
     * @param gameObjects      - game objects collection.
     * @param windowDimensions - vector2, the wanted coordinates.
     * @param skyLayer         - the layer of the sky.
     * @return gameobject, the sky it self.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(BASIC_SKY_COLOR)));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SKY_TAG);
        return sky;
    }

}
