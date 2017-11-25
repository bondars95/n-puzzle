package algo;

public class Main {
    private static final char SIZE_FLAG = 's';
    private static final char FILE_FLAG = 'f';

    public static void help() {
        System.out.println(
                SIZE_FLAG + " => size of game board\n" +
                FILE_FLAG + " => path to file (optional, if not present will be generated)"
        );
    }

    // total number of opened unique states evere selected
    // maximum number of states ever in memory
    // number of movers to goal from initial state
    // sequence of states to goal
    public static void main(String[] args) {
        new SearchAlgorithm(
                new short[]{
                    6,  5,  2,  13,
                    8,  14,  3,  7,
                    9,  10,  15,  4,
                    1,   0,    12,  11
                },
                HeuristicFunctions::hammingDistance
        ).search();
    }
}
