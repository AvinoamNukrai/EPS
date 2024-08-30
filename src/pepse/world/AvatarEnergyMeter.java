package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represent the Energy Meter object of the Avatar in the game.
 */
public class AvatarEnergyMeter extends GameObject {

    // Constants
    private static final String ENERGY_METER = "EnergyMeter";
    private static final String ENERGY_IMG_PATH = "assets/MarioPositions/EnergyMeter.jpg";

    // Data members
    private Vector2 topLeftCorner;
    private Vector2 dimensions;
    private ImageReader imageReader;
    private Renderable renderable;
    private int energyLevel;
    private static ImageRenderable energyLevelImg;

    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public AvatarEnergyMeter(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader, Renderable renderable,
                             int energyLevel){
        super(topLeftCorner, dimensions, renderable);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.imageReader = imageReader;
        this.renderable = renderable;
        this.energyLevel = energyLevel;
        energyLevelImg = imageReader.readImage(ENERGY_IMG_PATH, true);
    }


    public static AvatarEnergyMeter create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                           Renderable renderable, Vector2 dimensions,
                                           ImageReader imageReader, int energyLevel){
        AvatarEnergyMeter energyMeter = new AvatarEnergyMeter(topLeftCorner, dimensions, imageReader,
                renderable, energyLevel);
        gameObjects.addGameObject(energyMeter, layer);
        energyMeter.setTag(ENERGY_METER);
//        new Transition<Float>(
//                energyMeter,
//
//        )
        return energyMeter;
    }
}
