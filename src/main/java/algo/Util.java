package algo;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Random;

public class Util {
    /**
     * Take random integer in given range.
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Generates map.
     *
     * @param size Size of map
     * @return generated map
     */
    public static byte[] generateMap(final int size) {
        ArrayList<Integer> used = new ArrayList<>();
        byte[] field = new byte[size * size];
        for (int i = 0; i < field.length; i++) {
            int random = randInt(0, size * size - 1);
            while (used.contains(random)) {
                random = randInt(0, size * size - 1);
            }
            used.add(random);
            field[i] = (byte) random;
        }
        return !isSolvable(field, size) ? generateMap(size) : field;
    }

    /**
     * Generates terminal map.
     *
     * @param size Size of map
     * @return generated map
     */

    public static byte[] generateTerminalMap(final int size) {
        ArrayList<Integer> used = new ArrayList<>();
        byte[] field = new byte[size * size];
        int[][] matrix = new int[size][size];

        int row = 0;
        int col = 0;
        int dx = 1;
        int dy = 0;
        int dirChanges = 0;
        int visits = size;
 
        for (int i = 0; i < size * size; i++) {
            matrix[row][col] = i + 1;
            if (i + 1 == size * size)
                matrix[row][col] = 0;
            if (--visits == 0) {
                visits = size * (dirChanges % 2) + 
                    size * ((dirChanges + 1) % 2) -
                    (dirChanges / 2 - 1) - 2;
                int temp = dx;
                dx = -dy;
                dy = temp;
                dirChanges++;
            }
            col += dx;
            row += dy;
        }
        for (int i = 0, m = 0; i < size; i++)
            for (int j = 0; j < size; j++) 
                field[m++] = (byte) matrix[i][j];
        return field;
    }


    public static Options parseArguments(final String[] args) {
        // Map path
        Option pathOpt = new Option("m", "map", true, "input map path");
        pathOpt.setRequired(false);
        // Size
        Option sizeOpt = new Option("s", "size", true, "size of generated map");
        sizeOpt.setRequired(false);
        // Stats
        Option statsOpt = new Option("st", "stats", false, "show memory usage in runtime");
        statsOpt.setRequired(false);
        // Info
        Option infoOpt = new Option("i", "info", false, "show addition information on algorithm");
        infoOpt.setRequired(false);
        // Path
        Option showPathOpt = new Option("p", "path", false, "show final path");
        infoOpt.setRequired(false);
        // Heuristic
        Option heuristicOpt = new Option(
                "h",
                "heuristic",
                true,
                "choose one of heuristic (h - hammington, m - manhattan, e - linear conflicts)"
        );
        heuristicOpt.setRequired(true);

        Option finalMapOpt = new Option(
                "f",
                "final",
                true,
                "choose final terminal map (l - linear solution, s - snaike solution)"
        );
        finalMapOpt.setRequired(false);

        Options options = new Options();
        options.addOption(pathOpt);
        options.addOption(showPathOpt);
        options.addOption(sizeOpt);
        options.addOption(heuristicOpt);
        options.addOption(statsOpt);
        options.addOption(infoOpt);
        options.addOption(finalMapOpt);

        return options;
    }

    public static void validateArguments(final String path, final String heuristic, final Integer size) {
        if (path != null && size != null || path == null && size == null) {
            System.out.println("Either path to file or size should be choose");
            System.exit(1);
        }
        if (size != null && size > 10) {
            System.out.println("Max size allowed 10");
            System.exit(1);
        }
        if (!heuristic.equals("h") && !heuristic.equals("m") && !heuristic.equals("e")) {
            System.out.println("Unknown heuristic " + heuristic);
            System.exit(1);
        }

    }

    /**
     * Utility function to count inversion.
     */
    private static int countInversion(byte[] field, int position) {
        int current = field[position];
        int count = 0;
        for (int i = position + 1; i < field.length; i++) {
            if (field[i] == 0) {
                continue;
            }
            if (current > field[i]) {
                count++;
            }
        }
        return count;
    }

//        If the grid width is odd, then the number of inversions
//      in a solvable situation is even.
//      If the grid width is even, and the blank is on an even
//      row counting from the bottom (second-last, fourth-last etc),
//      then the number of inversions in a solvable situation is odd.
//      If the grid width is even, and the blank is on an odd row counting
//      from the bottom (last, third-last, fifth-last etc)
//      then the number of inversions in a solvable situation is even.
    public static boolean isSolvable(byte[] field, int size) {
        int emptyPosition = 0;
        int inversion = 0;
        size = (int) Math.sqrt(field.length + 1);
        for (int i = 0; i < field.length; i++){
            if (field[i] == 0)
                emptyPosition = i;
        }
        if (size == 1)
            return true;
        for (int i = 0; i < field.length; i++) {
            inversion += countInversion(field, i);
        }
        boolean res = (size % 2 != 0 && inversion % 2 == 0)
                || (size % 2 == 0 && (((emptyPosition / size) + 1) % 2 == inversion % 2)); 
        return size % 2 == 0 ? res : !res;
    }
}
