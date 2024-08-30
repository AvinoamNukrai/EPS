package pepse.util;

import java.util.Random;


/**
 * This util class contains util random functions for our game randomization.
 */
public class RandomUtils {

    /**
     * This method is responsible for check whether we got a random num under the given threshold or not.
     *
     * @param threshold - float number that indicates the wanted threshold
     * @return boolean - if we got a random number under the given threshold or not.
     */
    public static boolean randBoolAccordThreshold(float threshold) {
        return new Random().nextFloat() <= threshold;
    }

    public static int randomIntNumberInRange(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * This method is responsible for return a random float number in the range of min to max
     *
     * @param min - float start index
     * @param max - float end index
     * @return random float number
     */
    public static float randomFloatNumberInRange(float min, float max) {
        return new Random().nextFloat() * (max - min) + min;
    }
    
}
