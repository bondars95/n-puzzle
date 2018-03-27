package algo;

import java.util.ArrayList;
import java.util.List;

class HeuristicFunctions {

    private static int countDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static int getZeroPosition(final byte[] field) {
        int size = (int) Math.sqrt(field.length + 1);
        return size % 2 == 0 ? (field.length / 2) + size / 2  - 1 : (field.length) / 2;
    }

    private static int getValuePosition(final byte[] field, byte val) {
        for (int i = 0; i < field.length; i++) {
            if (field[i] == val) {
                return i;
            }
        }
        throw new RuntimeException("Not found");
    }

    static int manhattanDistance(final byte[] field, final byte[] terminalMap) {
        int res = 0;
        int size = (int) Math.sqrt(field.length + 1);
        
        for (int i = 0; i < field.length; i++) {
            if (i == field.length - 1 && field[i] == 0) {
                continue;
            }
            if (field[i] != terminalMap[i]) {
                int pos = getValuePosition(terminalMap, field[i]);
                res += countDistance(i % size, (i + 1) / size, pos % size, pos / size);
            }
        }
        return res;
    }

    static Integer hammingDistance(final byte[] field, final byte[] terminalMap) {
        int res = 0;
        for (int i = 0; i < field.length; i++) {
            if (i == getZeroPosition(terminalMap) && field[i] == 0) {
                continue;
            }
            if (field[i] != terminalMap[i]) {
                res++;
            }
        }
        return res;
    }

    static Integer linearConflicts(final byte[] field, final byte[] terminalMap) {
        int conflicts = 0;
        int size = (int) Math.sqrt(field.length + 1);
        for (int i = 0; i < field.length - 1; i++) {
            if ((i + 1)%size < size && field[i] == terminalMap[i + 1] && field[i + 1] == terminalMap[i])
                conflicts++;
            else if ((i + size) < field.length && field[i] == terminalMap[i + size] && field[i + size] == terminalMap[i])
                conflicts++;

       /*     if (field[i] == 0 && i == field.length - 2 && field[i + 1] == i + 1) // for zero horisontal conflict
                conflicts++;
            else if (field[i] == 0 && i + size < field.length && field[i + size] == i + 1) // for zero vertical conflict
                conflicts++;
            else if ((i + 1)%size < size && field[i] == getValuePosition(terminalMap[i+1]) && field[i + 1] == getValuePosition(terminalMap[i])) // horisontal confilct
                conflicts++;
            else if (i + size < field.length && field[i] == i + size + 1 && field[i + size] == i + 1) // vertical conflict
                conflicts++;*/
        }
        return 2*conflicts + manhattanDistance(field, terminalMap);
    }
}
