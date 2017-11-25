package game;

public class State {
    public final Transition parentAction;
    public final Transition nextAction;
    public short[] field;
    public int position;
    public int prediction;
    public int distance;

    public State(
            final short[] field,
            final Transition parentAction,
            final Transition nextAction,
            final int prediction,
            final int distance
    ) {
        this.field = field;
        this.parentAction = parentAction;
        this.nextAction = nextAction;
        this.prediction = prediction;
        this.distance = distance;
        for (int i = 0; i < field.length; i++) {
            if (field[i] == 0) {
                this.position = i;
            }
        }
    }

    public State move(Transition transition, int size) {
        State clone;
        try {
            clone = this.copy();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.distance++;
        switch (transition) {
            case UP:
                clone.field[clone.position - 1] = clone.field[clone.position - size - 1];
                clone.position = clone.position - size;
                clone.field[clone.position - 1] = 0;
                return  clone;
            case DOWN:
                clone.field[clone.position - 1] = clone.field[clone.position + size - 1];
                clone.position = clone.position + size;
                clone.field[clone.position - 1] = 0;
                return  clone;
            case LEFT:
                clone.field[clone.position - 1] = clone.field[clone.position + 1 - 1];
                clone.position = clone.position + 1;
                clone.field[clone.position - 1] = 0;
                return  clone;
            case RIGHT:
                clone.field[clone.position - 1] = clone.field[clone.position - 1 - 1];
                clone.position = clone.position - 1;
                clone.field[clone.position - 1] = 0;
                return  clone;
            default:
                throw new RuntimeException("WTF");
        }
    }

    public State copy() throws CloneNotSupportedException {
        return (State) super.clone();
    }
}
