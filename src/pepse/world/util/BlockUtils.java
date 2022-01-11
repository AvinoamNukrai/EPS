package pepse.util;

import pepse.world.Block;


/**
 * This util class is responsible for the blocks helpers methods in the program.
 */
public final class BlockUtils {

    /**
     * This method checks if given number (int) is a multiple of Block.SIZE
     * @param num - int number for check
     * @return boolean value - true if num%Block.SIZE = 0, false otherwise
     */
    public static boolean isNotBlockSizeMultiple(int num) {
        return num % Block.SIZE != 0;
    }

    /**
     * This method responsible for round down the given input coord (int)
     * such that (output coord)%SIZE = 0 and (output coord) < (input coord)
     * @param coord - int number to round down
     * @return - int number, the round one.
     */
    public static int roundDownCoordToBlockSize(int coord) {
        return (int) (Math.floor((float) coord / Block.SIZE) * Block.SIZE);
    }

    /**
     * This method responsible for round up the given input coord (int)
     * such that (output coord)%SIZE = 0 and (output coord) > (input coord)
     * @param coord - int number to round up.
     * @return - int number, the round one.
     */
    public static int roundUpCoordToBlockSize(int coord) {
        return (int) (Math.ceil((float)coord / Block.SIZE) * Block.SIZE);
    }

}
