import algo.SearchAlgorithm;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static algo.Util.*;

public class Main {
    private static AtomicBoolean ready = new AtomicBoolean(false);
    private static final char comment = '#';


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
                    Thread.sleep(1000);
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

    private static byte[] readMap(String path) {
        List<Integer> res = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            AtomicInteger size = new AtomicInteger(-1);
            List<Integer> present = new ArrayList<>();
            stream.forEach(line -> {
                line = line.indexOf(comment) != -1 ? line.substring(0, line.indexOf(comment)) : line;
<<<<<<< HEAD
                if (size.get() == -1) {
                    size.set(Integer.parseInt(line.trim()));
                }
                res.addAll(
                        Arrays.stream(line.trim().split("\\s+"))
                                .map(val -> {
                                    int parsed = Integer.parseInt(val);
                                    if (parsed > size.get() - 1 || parsed < 0 || present.contains(parsed)) {
                                        throw new RuntimeException("Map is not valid");
                                    }
                                    present.add(parsed);
                                    return parsed;
                                })
                                .collect(Collectors.toList())
                );
=======
                if (size.get() == -1 && line.trim().length() > 0) {
                    size.set(Integer.parseInt(line.trim()));
                    System.out.println("size = "+size.get());
                }
                else if (size.get() != -1){
                    if ((line.trim().split("\\s+").length) != size.get())
                        throw new RuntimeException("Map is not valid");
                    res.addAll(
                            Arrays.stream(line.trim().split("\\s+"))
                                    .map(val -> {
                                        int parsed = Integer.parseInt(val);
                                        if (parsed > size.get() * size.get() - 1 || parsed < 0 || present.contains(parsed)) {
                                            throw new RuntimeException("Map is not valid");
                                        }
                                        present.add(parsed);
                                        return parsed;
                                    })
                                    .collect(Collectors.toList())
                    );
                }
>>>>>>> dtelega
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        byte[] map = new byte[res.size()];
        for (int i = 0; i < res.size(); i++) {
            map[i] = (byte) (int) res.get(i);
        }
        return map;
    }

    private static void doAlgo(final byte[] map, final String heuristic, final boolean info, final boolean path) {
        double begin = System.currentTimeMillis();
        new SearchAlgorithm(map, heuristic).search(info, path);
        if (info) {
            System.out.println("Execution time in seconds: " + (System.currentTimeMillis() - begin) / 1000. + " sec.");
        }
    }

    // total number of opened unique states evere selected
    // maximum number of states ever in memory
    // number of movers to goal from initial state
    // sequence of states to goal
    public static void main(String[] args) {

        Options options = parseArguments(args);
        String path;
        String heuristic;
        Integer size;
        boolean stats;
        boolean info;
        boolean printPath;
        try {
            CommandLine cmd = new BasicParser().parse(options, args);
            path = cmd.getOptionValue("map");
            heuristic = cmd.getOptionValue("heuristic");
            size = cmd.hasOption("size") ? new Integer(cmd.getOptionValue("size")) : null;
            stats = cmd.hasOption("stats");
            info = cmd.hasOption("info");
            printPath = cmd.hasOption("path");
            validateArguments(path, heuristic, size);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("npuzzle", options);
            System.exit(1);
            return;
        }
        byte[] map = size != null ? generateMap(size) : readMap(path);
        if (stats) attachMemoryStats();
        try {
            doAlgo(map, heuristic, info, printPath);
        } catch (OutOfMemoryError e) {
            ready.set(true);
        }
    }
}
