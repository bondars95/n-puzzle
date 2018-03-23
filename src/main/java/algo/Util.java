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
                "choose one of heuristic (h - hammington, m - manhattan, e - eqluid)"
        );
        heuristicOpt.setRequired(true);

        Options options = new Options();
        options.addOption(pathOpt);
        options.addOption(showPathOpt);
        options.addOption(sizeOpt);
        options.addOption(heuristicOpt);
        options.addOption(statsOpt);
        options.addOption(infoOpt);

        return options;
    }

    public static void validateArguments(final String path, final String heuristic, final Integer size) {
        if (path != null && size != null) {
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
}
