package algo;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class Main {
    private static final char SIZE_FLAG = 's';
    private static final char FILE_FLAG = 'f';

    private static void help() {
        System.out.println(
                SIZE_FLAG + " => size of game board\n" +
                FILE_FLAG + " => path to file (optional, if not present will be generated)"
        );
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static short[] generateMap(int size) {
        ArrayList<Integer> used = new ArrayList<>();
        short[] field = new short[size * size];
        for (int i = 0; i < field.length; i++) {
            int random = randInt(0, size * size - 1);
            while (used.contains(random)) {
                random = randInt(0, size * size - 1);
            }
            used.add(random);
            field[i] = (short) random;
        }
        return field;
    }

//    4 6 2
//    3 0 8
//    1 7 5
//    1 + 3 + 1 + 4 + 1 + 1 = 11

    // total number of opened unique states evere selected
    // maximum number of states ever in memory
    // number of movers to goal from initial state
    // sequence of states to goal
    public static void main(String[] args) {
        short[] map = generateMap(5);
//        short[] map = new short[] {
//                18, 19,  15, 1, 21,
//                23, 0,   2, 20, 12,
//                13, 5,   11, 16, 9,
//                10, 22,  4, 24, 17,
//                3,  8,   6, 14, 7
//        };
        double begin = System.currentTimeMillis();
        new SearchAlgorithm(
                map,
                HeuristicFunctions::manhattanDistance
        ).search();
        System.out.println(System.currentTimeMillis() - begin);
        System.out.println();
        begin = System.currentTimeMillis();
        new SearchAlgorithm(
                map,
                HeuristicFunctions::hammingDistance
        ).search();
        System.out.println(System.currentTimeMillis() - begin);
    }
}
