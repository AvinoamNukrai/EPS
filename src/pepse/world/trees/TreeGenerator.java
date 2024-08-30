package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BlockUtils;
import pepse.util.ColorSupplier;
import pepse.util.RandomUtils;
import pepse.world.Block;

import java.awt.*;
import java.util.HashMap;
import java.util.function.Function;


/**
 * This class is responsible for generate a Tree in the game.
 */
public class TreeGenerator {

    // Constants
    public static final String TRUNK_TAG = "trunk";
    public static final String LEAF_TAG = "leaf";
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final float TREE_TOP_MULT_FACTOR = 1 / 3f;
    private static final float TRUNK_SIZE_MULT_FACTOR = 1 / 5f;
    private static final float TRUNK_WIDTH_DIMENSION = Block.SIZE;
    private static final float WINDOW_HEIGHT_MULT_FACTOR = 1 / 9f;
    private static final float LEAF_CREATION_THRESHOLD = 0.9f;
    private static final float ZERO_MASS = 0f;

    // Data members
    private final Vector2 windowDimensions;
    private final Function<Float, Float> heightAtCallback;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final HashMap<Integer, Float> trunksXCoordsHeightsMap;

    // Ctor
    public TreeGenerator(Vector2 windowDimensions, Function<Float, Float> heightAtCallback,
                         GameObjectCollection gameObjects, int layer) {
        this.windowDimensions = windowDimensions;
        this.heightAtCallback = heightAtCallback;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.trunksXCoordsHeightsMap = new HashMap<>();
    }


    // ~~~~~~~~~~~~~~~~~~ Method ~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This method is responsible for creates the trunk of a single Tree.
     *
     * @param xTrunkLeft - int, coordinate in which we will start to build the trunk
     * @return Vector2 - vector2 that represent the end of the trunk (will be use for build the tree top)
     * The method calc the exact starting coordinates of each tree by the given callback and starts build
     * upon.
     */
    public Vector2 createTrunk(int xTrunkLeft) {
        Float trunkHeight = trunksXCoordsHeightsMap.get(xTrunkLeft);
        float groundHeightAtTrunkXCoord = heightAtCallback.apply((float) xTrunkLeft);
        if (trunkHeight == null) {
            // for round the blocks of the trunk
            float distFromWindowTopBorder =
                    BlockUtils.roundUpCoordToBlockSize(
                            (int) (windowDimensions.y() * WINDOW_HEIGHT_MULT_FACTOR));
            float yCoordTrunkTop = distFromWindowTopBorder + Tree.TREE_TOP_DIMENSION;
            float maxTrunkSize = groundHeightAtTrunkXCoord - yCoordTrunkTop;
            float minTrunkSize = maxTrunkSize * TRUNK_SIZE_MULT_FACTOR;
            trunkHeight =
                    (float) BlockUtils.roundDownCoordToBlockSize((int)
                            RandomUtils.randomFloatNumberInRange(minTrunkSize, maxTrunkSize));
            trunksXCoordsHeightsMap.put(xTrunkLeft, trunkHeight);
        }
        for (int i = 1; i < trunkHeight / Block.SIZE - 1; i++) {
            for (int j = 0; j < TRUNK_WIDTH_DIMENSION / Block.SIZE; j++) {
                Vector2 blocksCoords = new
                        Vector2(xTrunkLeft + j * Block.SIZE, groundHeightAtTrunkXCoord - i * Block.SIZE);
                Renderable blockRectangle =
                        new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
                GameObject block = new Block(blocksCoords, blockRectangle);
                block.setTag(TRUNK_TAG);
                gameObjects.addGameObject(block, layer);
            }
        }
        Vector2 trunkTopLeft = new Vector2(xTrunkLeft, groundHeightAtTrunkXCoord - trunkHeight + Block.SIZE);
        GameObject block = new Block(
                trunkTopLeft, new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
        block.setTag(TRUNK_TAG);
        gameObjects.addGameObject(block, layer);
        return trunkTopLeft;
    }

    /**
     * This method is responsible for builds the tree top of each tree.
     *
     * @param trunkTopLeft - vector2 that contains the top left coordinates of the trunk of the specific tree.
     *                     The method calc the range and coordinates of the top tree by the given input and
     *                     builds blocks (Leaf) upon.
     */
    public void createTreeTop(Vector2 trunkTopLeft) {
        for (int i = 1; i <= Tree.TREE_TOP_DIMENSION / Block.SIZE; i++) {
            for (int j = 0; j < Tree.TREE_TOP_DIMENSION / Block.SIZE; j++) {
                if (RandomUtils.randBoolAccordThreshold(LEAF_CREATION_THRESHOLD)) {
                    float xLeafTopLeftCorner =
                            trunkTopLeft.x() - TREE_TOP_MULT_FACTOR * Tree.TREE_TOP_DIMENSION + j * Block.SIZE;
                    float yLeafTopLeftCorner = trunkTopLeft.y() - i * Block.SIZE;
                    Vector2 leafOriginalTopLeftCorner = new Vector2(xLeafTopLeftCorner, yLeafTopLeftCorner);
                    Leaf leaf = new Leaf(leafOriginalTopLeftCorner,
                            new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
                    leaf.physics().setMass(ZERO_MASS);
                    leaf.setTag(LEAF_TAG);
                    gameObjects.addGameObject(leaf, Layer.STATIC_OBJECTS + 5);
                    leaf.scheduleLifeCycle();
                }
            }
        }
    }

}
