package game;

public enum Transition implements Cloneable {
    UP, DOWN, LEFT, RIGHT;

    public Transition reverse() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        throw new RuntimeException("WTF");
    }
}
