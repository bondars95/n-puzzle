package algo;

import graph.Node;

public class HeuristicFunctions {
    public static int manhattanDistanse(final short[] field) {
        return 0;
    }

    public static Integer hammingDistance(final short[] field) {
        int res = 0;
        for (int i = 0; i < field.length; i++) {
            if (i == field.length - 1 && field[i] == 0) {
                continue;
            }
            if (field[i] != i + 1) {
                res++;
            }
        }
        return res;
    }
}
