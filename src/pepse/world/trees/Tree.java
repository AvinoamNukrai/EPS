package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import static pepse.util.BlockUtils.*;


/**
 * This class is represent the Trees creates in the game.
 */
public class Tree {

    // Constants
    public static final float TREE_TOP_DIMENSION = 5 * Block.SIZE;
    private static final float DIST_BETWEEN_TREES = 2 * Block.SIZE;
    private static final int TREE_POSITION_PROB = 4;
    private static final float HALF_FACTOR = 1 / 2f;
    private static final int MIN_DIS = 1;
    private static final int MAX_DIS = 6;

    // Data members
    private final Vector2 windowDimensions;
    private final TreeGenerator treeGenerator;
    private final int seed;


    /**
     * This is the Ctor of the class.
     *
     * @param windowDimensions - vector2, the dim of the screen
     * @param gameObjects      - the game objects collection
     * @param heightAtCallback - callback that will calc the ground height in given coordinate.
     * @param layer            - layer of the tree object
     * @param seed             - seed for the randomness
     */
    public Tree(Vector2 windowDimensions, GameObjectCollection gameObjects,
                Function<Float, Float> heightAtCallback, int layer, int seed) {
        this.windowDimensions = windowDimensions;
        this.treeGenerator = new TreeGenerator(windowDimensions, heightAtCallback, gameObjects, layer);
        this.seed = seed;
    }

    // ~~~~~~~~~~~~~~~~~~~~ Methods ~~~~~~~~~~~~~~~~~~~~~

    /**
     * This method check by randomness if to locate a tree in a given coordinate
     *
     * @param xCoord - int number - the coord to check for
     * @return bool value - true if we will locate the tree in the given index, false else.
     */
    public boolean isPlaceTree(int xCoord) {
        if (xCoord != windowDimensions.x() * HALF_FACTOR) {
            return new Random(Objects.hash(xCoord, seed)).nextInt((MAX_DIS - MIN_DIS)) + MIN_DIS == TREE_POSITION_PROB;
        }
        return false;
    }

    /**
     * This method create Trees of the game in the given range. creates trunks and tree top for each tree.
     *
     * @param minX - coord to start from.
     * @param maxX - coord to end.
     */
    public void createInRange(int minX, int maxX) {
        if (isNotBlockSizeMultiple(minX)) {
            // change minX values to match screen partition
            minX = roundUpCoordToBlockSize(minX);
        }
        if (isNotBlockSizeMultiple(maxX)) {
            maxX = roundDownCoordToBlockSize(maxX);
        }
        float distToNextTree = TREE_TOP_DIMENSION + DIST_BETWEEN_TREES;
        for (int xCoord = minX; xCoord <= maxX; xCoord += distToNextTree) {
            if (isPlaceTree(xCoord)) {
                // create tree organs
                Vector2 trunkTopLeft = treeGenerator.createTrunk(xCoord);
                treeGenerator.createTreeTop(trunkTopLeft);
            }
        }
    }

}
