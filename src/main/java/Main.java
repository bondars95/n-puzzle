import algo.HeuristicFunctions;
import algo.SearchAlgorithm;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import java.nio.file.Path;
import java.io.IOException;


public class Main {
    private static final char SIZE_FLAG = 's';
    private static final char FILE_FLAG = 'f';
    private static AtomicBoolean ready = new AtomicBoolean(false);

    // todo implement
    private static void help() {
        System.out.println(
                SIZE_FLAG + " => size of game board\n" +
                        FILE_FLAG + " => path to file (optional, if not present will be generated)"
        );
    }

    /**
     * Take random integer in given range.
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Generates map.
     *
     * @param size Size of map
     * @return
     */
    private static byte[] generateMap(final int size) {
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

    /**
     * Runs daemon thread to monitor memory usage.
     */
    private static void attachMemoryStats() {
        Thread runtimeMemoryStats = new Thread(() -> {
            while (!ready.get()) {
                Runtime runtime = Runtime.getRuntime();
                NumberFormat format = NumberFormat.getInstance();
                StringBuilder sb = new StringBuilder();
                long maxMemory = runtime.maxMemory();
                long allocatedMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();
                sb.append("max memory: ")
                        .append(format.format(maxMemory / 1024)).append("\n")
                        .append("free memory: ")
                        .append(format.format(freeMemory / 1024)).append("\n")
                        .append("total free memory: ")
                        .append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024))
                        .append("\n");
                System.out.println(sb.toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        runtimeMemoryStats.setDaemon(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(runtimeMemoryStats);
        executor.shutdown();
    }

    /**
     * Read file
     * @param fileName
     * @throws IOException
     */
    private static byte[] readUsingFiles(String fileName) throws IOException {
        fileName = "src/java/" + fileName;
        Path path = Paths.get(fileName);

        byte[] map = Files.readAllBytes(path);
        return map;
    }



    // total number of opened unique states evere selected
    // maximum number of states ever in memory
    // number of movers to goal from initial state
    // sequence of states to goal
    public static void main(String[] args) throws IOException {
        byte[] map = readUsingFiles(args[0]);

//          byte[] map = generateMap(3);
//        byte[] map = new byte[]{
//                1, 14, 2, 4, 6, 18,
//                9, 13, 3, 17, 11, 33,
//                19, 7, 16, 10, 5, 12,
//                8, 26, 20, 15, 22, 24,
//                21, 31, 27, 29, 23, 30,
//                25, 0, 32, 28, 34, 35,
//        };
//        attachMemoryStats();
        double begin = System.currentTimeMillis();
        new SearchAlgorithm(
                map,
                HeuristicFunctions::manhattanDistance
        ).search();
        System.out.println("Manhattan:");
        System.out.println("Millis " + (System.currentTimeMillis() - begin));
        begin = System.currentTimeMillis();
        System.out.println();
        new SearchAlgorithm(
                map,
                HeuristicFunctions::hammingDistance
        ).search();
        System.out.println("Hammington");
        System.out.println("Millis " + (System.currentTimeMillis() - begin));
    }
}
