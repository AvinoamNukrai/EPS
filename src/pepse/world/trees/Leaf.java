package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.RandomUtils;
import pepse.world.Block;
import pepse.world.Terrain;


/**
 * This class represent Leaf (of the tree) in the game.
 */
public class Leaf extends Block {

    // Constants
    private static final float LEAF_FADE_OUT_TIME = 9;
    private static final float LEAF_FALL_DOWN_VELOCITY = 50;
    private static final int TIME_TILL_LEAF_FLOW_START = 14;
    private static final float MAX_OPAQUENESS_ARG = 1f;
    private static final float INIT_LEAF_ANGLE = 2f;
    private static final float FINAL_LEAF_ANGLE = -2f;
    private static final float LEAF_ANGLE_TRANSITION_TIME = 1f;
    private static final float LEAF_SHRINKAGE_TRANSITION_TIME = 1f;
    private static final float LEAF_WIDTH_SUBTRACTION = 1 / 5f * Block.SIZE;
    private static final float PROB_FALL_AND_FADE_OUT = 1 / 8f;
    private static final float INIT_LEAF_HORIZONTAL_VELOCITY = -100f;
    public static final float FINAL_LEAF_HORIZONTAL_VELOCITY = 100f;
    private static final float LEAF_HORIZONTAL_VELOCITY_TRANSITION_TIME = 1f;

    // Data members
    private Object positionStatus;
    private final Vector2 originalTopLeftCorner;
    private Transition<Float> horizontalVelocityTransition;

    /**
     * This is the ctor of the class
     *
     * @param topLeftCorner - vector2, the top left corner of the single leaf
     * @param renderable    - renderable for the leaf.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        positionStatus = LeafPositionStatus.ON_TREE;
        originalTopLeftCorner = topLeftCorner;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~ Methods ~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This method is responsible for the life cycle of the leaf and for his movement on the screen.
     */
    void scheduleLifeCycle() {
        new ScheduledTask(
                this,
                RandomUtils.randomFloatNumberInRange(LEAF_FADE_OUT_TIME, TIME_TILL_LEAF_FLOW_START) + 1,
                true,
                () -> {
                    if (positionStatus == LeafPositionStatus.ON_GROUND) initToTree();
                    else if (positionStatus == LeafPositionStatus.ON_TREE &&
                            RandomUtils.randBoolAccordThreshold(PROB_FALL_AND_FADE_OUT)) {
                        fallAndFadeOut();
                    }
                }
        );
        setAngleTransition();
        setShrinkageTransition();
    }

    /**
     * This method sets the leaf first values - replacing the leaf in his original place on the screen.
     */
    private void initToTree() {
        setTopLeftCorner(originalTopLeftCorner);
        renderer().setOpaqueness(MAX_OPAQUENESS_ARG);
    }

    /**
     * This method is responsible for creates a Transition for the Horizontal velocity change.
     *
     * @return The made Transition
     */
    private Transition<Float> setHorizontalVelocityTransition() {
        // set velocity in axis x
        return new Transition<>(
                this, // the game object being changed
                horizontalVelocity -> {
                    transform().setVelocityX(horizontalVelocity);
                },
                INIT_LEAF_HORIZONTAL_VELOCITY,  // initial transition value
                FINAL_LEAF_HORIZONTAL_VELOCITY,  // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                LEAF_HORIZONTAL_VELOCITY_TRANSITION_TIME,  // transtion fully over five seconds
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  // nothing further to execute upon reaching final value
    }

    /**
     * This method creates the Angle Transition - cause the leaf angle to change.
     */
    void setAngleTransition() {
        new Transition<>(
                this, // the game object being changed
                angle -> {
                    renderer().setRenderableAngle(angle);
                },
                INIT_LEAF_ANGLE,  // initial transition value
                FINAL_LEAF_ANGLE,  // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                LEAF_ANGLE_TRANSITION_TIME,  // transtion fully over one second
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  // nothing further to execute upon reaching final value
    }

    /**
     * This method creates the Shrinkage Transition of the leaf.
     */
    void setShrinkageTransition() {
        new Transition<>(
                this, // the game object being changed
                this::setDimensions,  // the method to call
                new Vector2(Block.SIZE, Block.SIZE),  // initial transition value
                new Vector2(Block.SIZE - LEAF_WIDTH_SUBTRACTION, Block.SIZE),  // final transition value
                Transition.CUBIC_INTERPOLATOR_VECTOR,  // use a cubic interpolator
                LEAF_SHRINKAGE_TRANSITION_TIME,  // transtion fully over one second
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  // nothing further to execute upon reaching final value
    }

    /**
     * This method is responsible for the fall down of the leaf and for his fade out,
     * and set 'isLeafFallBefore' flag to true after fade out done.
     */
    public void fallAndFadeOut() {
        transform().setVelocityY(LEAF_FALL_DOWN_VELOCITY);
        positionStatus = LeafPositionStatus.FALLING;
        horizontalVelocityTransition = setHorizontalVelocityTransition();
        renderer().fadeOut(LEAF_FADE_OUT_TIME);
    }

    /**
     * This method represent the collision case of the leaf with the ground
     *
     * @param other     - the object to collide with.
     * @param collision - collision object for the collision logic (super method)
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Terrain.GROUND_BLOCK_TAG)) {
            positionStatus = LeafPositionStatus.ON_GROUND;
            zeroVelocity();
        }
    }

    /**
     * This method set the leaf velocity to zero by Scheduled Task
     */
    private void zeroVelocity() {
        removeComponent(horizontalVelocityTransition);
        new ScheduledTask(this,
                0,
                false,
                () -> setVelocity(Vector2.ZERO));
    }
}
