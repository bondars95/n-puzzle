package algo;

import graph.Node;

public class HeuristicFunctions {
    private static int countDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
    }

    public static int manhattanDistance(final short[] field) {
        int res = 0;
        int size = (int) Math.sqrt(field.length + 1);
        for (int i = 0; i < field.length; i++) {
            if (i == field.length - 1 && field[i] == 0) {
                continue;
            }
            if (field[i] != i + 1) {
                if (field[i] ==  0) {
                    res += countDistance(i % size, (i + 1) / size, size - 1, size);
                } else {
                    res += countDistance(i % size, (i + 1) / size, (field[i] - 1) % size, field[i] / size);
                }
            }
        }
        return res;
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
