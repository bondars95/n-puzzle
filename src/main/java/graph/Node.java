package graph;

import game.State;

public class Node {
    public Node parent;
    public State state;

    public Node(Node parent, State state) {
        this.parent = parent;
        this.state = state;
    }
}
