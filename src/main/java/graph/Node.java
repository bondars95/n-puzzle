package graph;

import game.State;

/**
 * Represents node in graph.
 */
public class Node {
    public Node parent;
    /**
     * Game state.
     */
    public State state;

    public Node(Node parent, State state) {
        this.parent = parent;
        this.state = state;
    }
}
