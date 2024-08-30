package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import static pepse.util.BlockUtils.*;


/**
 * This class represent the Terrain (ground) of the game.
 */
public class Terrain {

    // Constants
    public static final String GROUND_BLOCK_TAG = "ground";
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_GRADIENT = 800;
    private static final float HALF_FACTOR = 0.5f;
    private static final float FACTOR = 2;
    private static final double NUM1 = -1.8;
    private static final double NUM2 = -1.5;
    private static final double NUM3 = 0.4;
    private static final double NUM4 = 2.6;
    private static final double NUM5 = 0.9;
    private static final double NUM6 = 0.4;
    private static final double NUM_ONE = 1;


    // Data members
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final int seed;


    /**
     * This is the Ctor of the class
     *
     * @param gameObjects      - game objects collection.
     * @param groundLayer      - the ground layer in the game.
     * @param windowDimensions - Vector2, the window dim of the screen
     * @param seed             - the seed for the randomness in the game.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = roundDownCoordToBlockSize((int) (HALF_FACTOR * windowDimensions.y()));
        this.seed = seed;
    }

    // ~~~~~~~~~~~~~~~~~~ Methods ~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This method is calc the ground height in the given x coordinate
     *
     * @param x - horizental coord in which we will calc the height of the ground.
     * @return float number - the height of the ground in the given coord.
     */
    public float groundHeightAt(float x) {
        if (x == windowDimensions.x() * HALF_FACTOR) {
            return groundHeightAtX0;
        }
        float groundHeightAddition =
                2 * Block.SIZE * Math.abs(perlinNoise((x / TERRAIN_GRADIENT)) + 1);
        if (groundHeightAddition < 0) {
            return groundHeightAtX0 + Block.SIZE;
        } else if (groundHeightAtX0 + groundHeightAddition > windowDimensions.y()) {
            return roundDownCoordToBlockSize((int) (windowDimensions.y() - Block.SIZE));
        }
        return roundDownCoordToBlockSize((int) (groundHeightAtX0 + groundHeightAddition));
    }

    /**
     * This method creates blocks (for the ground) in the given range.
     *
     * @param minX - coord to start from.
     * @param maxX - coord to end.
     */
    public void createInRange(int minX, int maxX) {
        if (isNotBlockSizeMultiple(minX)) minX = roundDownCoordToBlockSize(minX);
        if (isNotBlockSizeMultiple(maxX)) maxX = roundUpCoordToBlockSize(maxX);
        float windowHeight = windowDimensions.y();
        float curXCoord;
        for (int i = 0; i < (maxX - minX) / Block.SIZE; i++) {
            curXCoord = minX + i * Block.SIZE;
            for (int j = 0; j < (windowHeight - groundHeightAt(curXCoord)) / Block.SIZE * FACTOR; j++) {
                float curYCoord = groundHeightAt(curXCoord) + j * Block.SIZE;
                Block block = new Block(new Vector2(curXCoord, curYCoord),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(GROUND_BLOCK_TAG);
                gameObjects.addGameObject(block, groundLayer);
            }
        }
    }

    /**
     * This method is responsible for calc the perlin noise number for the tree random create.
     *
     * @param num - num to manipulate
     * @return float - the noise number.
     */
    private float perlinNoise(float num) {
        num += seed;
        double numToMult = (NUM1 * Math.sin(NUM2 * num) - NUM3 *
                Math.sin(NUM4 * Math.E * num) + NUM5 * Math.sin(NUM6 * Math.PI * num));
        return (float) (NUM_ONE + HALF_FACTOR * numToMult);
    }

}
