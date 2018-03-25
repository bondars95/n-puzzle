package algo;

class HeuristicFunctions {

    private static int countDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    static int manhattanDistance(final byte[] field) {
        int res = 0;
        int size = (int) Math.sqrt(field.length + 1);
        for (int i = 0; i < field.length; i++) {
            if (i == field.length - 1 && field[i] == 0) {
                continue;
            }
            if (field[i] != i + 1) {
                if (field[i] == 0) {
                    res += countDistance(i % size, (i + 1) / size, size - 1, size);
                } else {
                    res += countDistance(i % size, (i + 1) / size, (field[i] - 1) % size, field[i] / size);
                }
            }
        }
        return res;
    }

    static Integer hammingDistance(final byte[] field) {
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

    static Integer linearConflicts(final byte[] field) {
        int conflicts = 0;
        int size = (int) Math.sqrt(field.length + 1);
        for (int i = 0; i < field.length - 1; i++) {
            if (field[i] == 0 && i == field.length - 2 && field[i + 1] == i + 1) // for zero horisontal conflict
                conflicts++;
            else if (field[i] == 0 && i + size < field.length && field[i + size] == i + 1) // for zero vertical conflict
                conflicts++;
            else if ((i + 1)%size < size && field[i] == i + 1 + 1 && field[i + 1] == i + 1) // horisontal confilct
                conflicts++;
            else if (i + size < field.length && field[i] == i + size + 1 && field[i + size] == i + 1) // vertical conflict
                conflicts++;
        }
        return 2*conflicts + manhattanDistance(field);
    }
}
