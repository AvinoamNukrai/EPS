package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class is represent single Block object in the game.
 */
public class Block extends GameObject {

    // Constants
    public static final int SIZE = 30;

    /**
     * This is the Ctor of the class
     *
     * @param topLeftCorner - Vector2 for locating the Block
     * @param renderable    - renderable of the block (in our game it will be Rectangle renderable)
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

}
