package game;

import java.util.Arrays;

public class State {
    public Transition parentAction;
    public byte[] field;
    public int position;
    public int cost;
    public int distance;

    public State(
            final byte[] field,
            final Transition parentAction,
            final int prediction,
            final int distance
    ) {
        this.field = field;
        this.parentAction = parentAction;
        this.cost = prediction;
        this.distance = distance;
        for (int i = 0; i < field.length; i++) {
            if (field[i] == 0) {
                this.position = i;
            }
        }
    }

    public State move(final Transition transition, final int size) {
        State clone;
        try {
            clone = this.copy();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.distance++;
        clone.parentAction = transition;
        int x = clone.position % size;
        int y = (clone.position + 1) / size;
        switch (transition) {
            case UP:
                if (clone.position - size < 0 || clone.position - size > clone.field.length - 1) {
                    return null;
                }
                clone.field[clone.position] = clone.field[clone.position - size];
                clone.position = clone.position - size;
                clone.field[clone.position] = 0;
                return clone;
            case DOWN:
                if (clone.position + size < 0 || clone.position + size > clone.field.length - 1) {
                    return null;
                }
                clone.field[clone.position] = clone.field[clone.position + size];
                clone.position = clone.position + size;
                clone.field[clone.position] = 0;
                return clone;
            case LEFT:
                if (x == size - 1 || clone.position + 1 < 0 || clone.position + 1 > clone.field.length - 1) {
                    return null;
                }
                clone.field[clone.position] = clone.field[clone.position + 1];
                clone.position = clone.position + 1;
                clone.field[clone.position] = 0;
                return clone;
            case RIGHT:
                if (x == 0 || clone.position - 1 < 0 || clone.position - 1 > clone.field.length - 1) {
                    return null;
                }
                clone.field[clone.position] = clone.field[clone.position - 1];
                clone.position = clone.position - 1;
                clone.field[clone.position] = 0;
                return clone;
            default:
                throw new RuntimeException("WTF");
        }
    }

    public State copy() throws CloneNotSupportedException {
        return new State(
                field.clone(),
                parentAction,
                cost,
                distance
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return Arrays.equals(field, state.field);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(field);
    }
}
