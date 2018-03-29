package algo;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import game.State;
import game.Transition;
import graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A star algorithm implementation.
 */
public final class SearchAlgorithm {
    /**
     * Game field size.
     */
    private final short size;
    /**
     * Root node.
     */
    private final Node root;
    /**
     * List of visited nodes to visit.
     */
    private final PriorityQueue<Node> openNodes;
    /**
     * List of visited nodes.
     */
    private final ObjectObjectOpenHashMap<State, Node> closedNodes;
    /**
     * Final path.
     */
    private final List<State> path;
    /**
     * Heuristic function to calculate node cost.
     */
    private final BiFunction<byte[], byte[], Integer> heuristicFunction;
    /**
     * Terminal map for heuristic
     */
    private final byte[] terminalMap;

    public SearchAlgorithm(
            final byte[] field,
            final String heuristic
    ) {
        this.size = (short) Math.sqrt(field.length + 1);
        this.terminalMap = Util.generateTerminalMap(size);
        // comparator for sorting nodes based on its cost
        this.openNodes = new PriorityQueue<>((o1, o2) -> {
            if (o1.state.cost == o2.state.cost) {
                return o1.state.distance - o2.state.distance;
            } else {
                return o1.state.cost - o2.state.cost;
            }
        });
        // high performance map
        this.closedNodes = new ObjectObjectOpenHashMap<>();
        this.path = new ArrayList<>();
        switch (heuristic) {
            case "h":
                this.heuristicFunction = HeuristicFunctions::hammingDistance;
                break;
            case "m":
                this.heuristicFunction = HeuristicFunctions::manhattanDistance;
                break;
            case "e":
                this.heuristicFunction = HeuristicFunctions::linearConflicts;
                break;
            default:
                throw new RuntimeException("WTF");
        }
        // init root
        this.root = new Node(
                null,
                new State(
                        field,
                        null,
                        0,
                        0
                )
        );
    }

    /**
     * Performs search of final path.
     */
    public void search(final boolean info, final boolean printPath) {
        openNodes.add(root);
        int openNodesMax = 0;
        if (!Util.isSolvable(root.state.field, root.state.position)) {
            System.out.println("Sorry, not solvable");
            return;
        }
        root.state.cost = heuristicFunction.apply(root.state.field, terminalMap);
        printField(root.state.field);
        while (!openNodes.isEmpty() && path.isEmpty()) {
            Node n = openNodes.poll();
            if (closedNodes.containsKey(n.state)) {
                continue;
            }
            if (n.state.cost == 0) {
                for (Node p = n; p != null; p = p.parent)
                    path.add(p.state);
                break;
            }
            closedNodes.put(n.state, n);
            for (int i = 0; i < Transition.values().length; i++) {
                State state = n.state.move(Transition.values()[i], size);
                if (state != null) {
                    Node node = new Node(n, state);
                    node.state.cost = heuristicFunction.apply(node.state.field, terminalMap);
                    openNodes.add(node);
                }
            }
            openNodesMax = openNodes.size() > openNodesMax ? openNodes.size() : openNodesMax;
        }
        if (printPath) {
            printPath();
            System.out.println("Initial state:");
            printField(root.state.field);
        }
        if (info) {
            System.out.println("Path size: " + path.size());
            System.out.println("Open nodes: " + openNodesMax);
            System.out.println("Closed nodes: " + closedNodes.size());
        }
    }

    /**
     * Prints final path.
     */
    private void printPath() {
        Collections.reverse(path);
        path.forEach(node -> {
            if (node.parentAction != null) {
                System.out.println(node.parentAction);
                System.out.println();
            }
            printField(node.field);
        });
    }

    /**
     * Prints game state.
     */
    private void printField(byte[] field) {
        for (int i = 0; i < field.length; i++) {
            System.out.print(String.format("%4d", field[i]));
            if ((i + 1) % size == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
