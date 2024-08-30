package pepse;

// Written by Avinoam Nukrai & Yair Ayalon, Winter 2021/2022 Hebrew U, OOP Course

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.trees.Tree;
import pepse.world.trees.TreeGenerator;

import java.awt.*;

import static pepse.util.BlockUtils.roundDownCoordToBlockSize;
import static pepse.util.RandomUtils.randomIntNumberInRange;


/**
 * This class is the Main class, the manager of the game.
 */
public class PepseGameManager extends GameManager {

    // Constants
    private static final int SEED_VALUE = randomIntNumberInRange(400, 2200);
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 10;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final float NIGHT_CYCLE_LENGTH = 60;
    private static final float SUN_CYCLE_LENGTH = 40;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float HALF_FACTOR = 1 / 2f;
    private static final String INTRO_GAME_MSG = "Welcome to Paradise! Do you want to dive in?";

    // Data members
    private Avatar avatar;
    private Terrain terrain;
    private Tree tree;
    private Vector2 windowDimensions;
    private int minXCoordGame;
    private int maxXCoordGame;


    /**
     * This is the main method of the game
     *
     * @param args - Strings array that represent the CLI arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        boolean startGame = windowController.openYesNoDialog(INTRO_GAME_MSG);
        if (!startGame) windowController.closeWindow(); // exit the game at first according the user wish
        windowDimensions = windowController.getWindowDimensions();
        minXCoordGame = roundDownCoordToBlockSize((int) -windowDimensions.x());
        maxXCoordGame = roundDownCoordToBlockSize((int) (2 * windowDimensions.x()));
        // Build the games objects
        createSky();
        createNight();
        createSunAndSunHalo();
        createTerrain();
        createTreesOnTerrain();
        createAvatar(inputListener, imageReader);
    }


    /**
     * This method is the override update method of the current class.
     *
     * @param deltaTime - The deltaTime for super() method.
     *                  responsible for keeps the world of the game in update mode (infinite world) and
     *                  updates the avatar location.
     */
    @Override
    public void update(float deltaTime) {
        updateWorld();
        updateAvatarLocation();
        super.update(deltaTime);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~ Private methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~


    /**
     * This method is responsible for create the Sky in the game
     */
    private void createSky() {
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
    }

    /**
     * This method is responsible for create the Night illusion in the game
     */
    private void createNight() {
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, NIGHT_CYCLE_LENGTH);
    }

    /**
     * This method is responsible for create the Sun and her Halo of the game
     */
    private void createSunAndSunHalo() {
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, SUN_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, SUN_HALO_COLOR);
    }

    /**
     * This method is responsible for create the Terrain of the game
     */
    private void createTerrain() {
        terrain = new Terrain(gameObjects(), TERRAIN_LAYER, windowDimensions, SEED_VALUE);
        terrain.createInRange(minXCoordGame, maxXCoordGame);
    }

    /**
     * This method is responsible for create the Trees of the game
     */
    private void createTreesOnTerrain() {
        float dist_from_window_borders = (int) (windowDimensions.x() / 10);
        tree = new Tree(windowDimensions, gameObjects(), terrain::groundHeightAt, TRUNK_LAYER, SEED_VALUE);
        int minX = (int) (minXCoordGame + dist_from_window_borders);
        int maxX = (int) (maxXCoordGame - dist_from_window_borders - Tree.TREE_TOP_DIMENSION);
        tree.createInRange(minX, maxX);
        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS + 5, TERRAIN_LAYER, true);
    }

    /**
     * This method is responsible for creates the Avatar of the Game
     *
     * @param inputListener - for detecting the user keyboard input
     * @param imageReader   - for read positions images of the Avatar
     *                      Calc the avatar init position and calls Avatar.create() for add the object to
     *                      the game.
     *                      In addition we define, the objects in which the avatar should collide with.
     */
    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
        float xCoordAvatarTopLeftCorner =
                windowDimensions.x() * HALF_FACTOR - Avatar.AVATAR_SIZE * HALF_FACTOR;
        float yCoordAvatarTopLeftCorner =
                terrain.groundHeightAt(xCoordAvatarTopLeftCorner) - Avatar.AVATAR_SIZE;
        Vector2 avatarTopLeftCorner =
                new Vector2(xCoordAvatarTopLeftCorner, yCoordAvatarTopLeftCorner).add(Vector2.ONES);
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarTopLeftCorner, inputListener, imageReader);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TERRAIN_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TRUNK_LAYER, true);
        setCamera(new Camera(avatar, windowDimensions.mult(HALF_FACTOR).subtract(avatar.getCenter()),
                windowDimensions, windowDimensions));
    }

    /**
     * This method is responsible for updating the avatar location in the game.
     */
    private void updateAvatarLocation() {
        float xCoordAvatarTopLeftCorner = avatar.getTopLeftCorner().x();
        if (avatar.getTopLeftCorner().y() > terrain.groundHeightAt(xCoordAvatarTopLeftCorner) + HALF_FACTOR * Block.SIZE) {
            avatar.setTopLeftCorner(new Vector2(xCoordAvatarTopLeftCorner, windowDimensions.y() -
                    terrain.groundHeightAt(xCoordAvatarTopLeftCorner - avatar.getDimensions().y())));
        }
    }

    /**
     * This method is responsible for updating our game in terms of "infinite world".
     * Calc the dis between the avatar to the sides of the screen and checks if it > 0, if so we expend the
     * ground, update the bounds of the game and remove the object that belong to the old other hand of the
     * screen.
     */
    private void updateWorld() {
        int windowWidth = (int) windowDimensions.x();
        float centerMinCoordDelta = avatar.getCenter().x() - minXCoordGame;
        float centerMaxCoordDelta = maxXCoordGame - avatar.getCenter().x();
        if (centerMinCoordDelta < windowWidth) {
            expandGround(minXCoordGame - windowWidth, minXCoordGame);
            updateWorldBoundaries(-windowWidth);
        }
        if (centerMaxCoordDelta < windowWidth) {
            expandGround(maxXCoordGame, maxXCoordGame + windowWidth);
            updateWorldBoundaries(windowWidth);
        }
        removeObjectsOutOfBounds();
    }

    /**
     * This method is responsible for "expend" our Terrain in the world of our game in some given range.
     *
     * @param minX - the start coord
     * @param maxX - the end coord of the game
     */
    private void expandGround(int minX, int maxX) {
        terrain.createInRange(minX, maxX);
        tree.createInRange(minX, maxX);
    }

    /**
     * This method is responsible for update the world bounds.
     *
     * @param windowWidth - The factor to add to the bounds.
     */
    private void updateWorldBoundaries(int windowWidth) {
        minXCoordGame += windowWidth;
        maxXCoordGame += windowWidth;
    }

    /**
     * This method is responsible for removing the "out of bounds" objects of the game.
     * using the bounds of the game and the method removeGameObject.
     */
    private void removeObjectsOutOfBounds() {
        for (GameObject gameObject : gameObjects()) {
            float xCoordLeftGameObject = gameObject.getTopLeftCorner().x();
            if (xCoordLeftGameObject < minXCoordGame || xCoordLeftGameObject > maxXCoordGame) {
                String gameObjectTag = gameObject.getTag();
                switch (gameObjectTag) {
                    case Terrain.GROUND_BLOCK_TAG:
                        gameObjects().removeGameObject(gameObject, TERRAIN_LAYER);
                        break;
                    case TreeGenerator.TRUNK_TAG:
                        gameObjects().removeGameObject(gameObject, TRUNK_LAYER);
                        break;
                    case TreeGenerator.LEAF_TAG:
                        gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS + 5);
                        break;
                }
            }
        }
    }

}