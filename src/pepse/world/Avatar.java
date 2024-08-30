package pepse.world;

import danogl.*;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * This class represent the Avatar character in the game.
 */
public class Avatar extends GameObject {

    // Constants
    private static final String AVATAR_TAG = "avatar";
    private static final String STAND_IMG_PATH = "assets/MarioPositions/stand.png";
    private static final String RUN_IMG_PATH = "assets/MarioPositions/run.png";
    private static final String FLY_IMG_PATH = "assets/MarioPositions/flying.png";
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 200;
    public static final float AVATAR_SIZE = 50;
    private static final float MAX_ENERGY_LVL = 100;
    private static final float ENERGY_REDUCTION_FLYING = 0.5f;
    private static final float ENERGY_ADDITION_FLYING = 0.5f;


    // Data members
    private static ImageRenderable standAvatarImage;
    private static ImageRenderable runAvatarImage;
    private static ImageRenderable flyAvatarImage;
    private final UserInputListener inputListener;
    private float energyLevel;
    private AvatarEnergyMeter avatarEnergyMeter;



    /**
     * The Ctor of the class.
     *
     * @param pos           - vector2, the coordinates in which the avatar will be.
     * @param inputListener - UserInputListener, for detect user keyboard input.
     * @param imageReader   - ImageReader, for read the avatar image positions.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(AVATAR_SIZE), new OvalRenderable(AVATAR_COLOR));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        // Creates avatar images positions
        standAvatarImage = imageReader.readImage(STAND_IMG_PATH, true);
        runAvatarImage = imageReader.readImage(RUN_IMG_PATH, true);
        flyAvatarImage = imageReader.readImage(FLY_IMG_PATH, true);
        this.inputListener = inputListener;
        this.energyLevel = MAX_ENERGY_LVL;
        this.avatarEnergyMeter = new AvatarEnergyMeter(new Vector2(0,0), new Vector2(10,10), imageReader,
                new RectangleRenderable(Color.GREEN), (int)MAX_ENERGY_LVL);


    }

    // ~~~~~~~~~~~~~~~~~~~~ Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This method creates the Avatar character of the game.
     *
     * @param gameObjects   - game object collection of the game
     * @param layer         - the layer in which the avatar will be hold
     * @param topLeftCorner - top left corner coordinates of the avatar
     * @param inputListener - for getting the keyboard user input
     * @param imageReader   - for set the right image of the avatar accord to his position (run/fly etc)
     * @return Avatar object.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar avatar = new Avatar(topLeftCorner, inputListener, imageReader);
        avatar.setTag(AVATAR_TAG);
        AvatarEnergyMeter.create();
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }


    /**
     * This method represent the collision of the avatar in the game, specific with the Ground.
     *
     * @param other     - other object to be collide with.
     * @param collision - collision object for collision logic.
     *                  If the avatar is collide with the ground we will set his speed to zero.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        String otherTag = other.getTag();
        if (otherTag.equals(Terrain.GROUND_BLOCK_TAG)) {
            setVelocity(Vector2.ZERO);
        }
    }


    /**
     * This method is responsible for update the avatar movement in the game.
     *
     * @param deltaTime - for super func.
     *                  The method change the avatar velocity and Image position according to the keyboard
     *                  input from the user.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Renderable avatarImgToDisplay = standAvatarImage; // Init position
        float xVel = 0;
        // move left
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            avatarImgToDisplay = runAvatarImage;
            renderer().setIsFlippedHorizontally(true);
            xVel -= VELOCITY_X;
        }
        // move right
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            avatarImgToDisplay = runAvatarImage;
            renderer().setIsFlippedHorizontally(false);
        }

        transform().setVelocityX(xVel);

        // jump
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
        }

        // fly
        else if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (energyLevel > 0) {
                transform().setVelocityY(VELOCITY_Y);
                energyLevel -= ENERGY_REDUCTION_FLYING;
                avatarImgToDisplay = flyAvatarImage;
            }
            // TODO: NEW CHANGE

        }

        // charge energy on rest
        if (transform().getVelocity().equals(Vector2.ZERO) && energyLevel < MAX_ENERGY_LVL) {
            energyLevel += ENERGY_ADDITION_FLYING;
        }
        renderer().setRenderable(avatarImgToDisplay);
    }


}

