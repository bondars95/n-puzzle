package algo;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import game.State;
import game.Transition;
import graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
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
    private final Function<byte[], Integer> heuristicFunction;

    public SearchAlgorithm(
            final byte[] field,
            final Function<byte[], Integer> heuristicFunction
    ) {
        this.size = (short) Math.sqrt(field.length + 1);
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
        this.heuristicFunction = heuristicFunction;
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
    public List<State> search() {
        openNodes.add(root);
        printField(root.state.field);
        int openNodesMax = 0;
        if (!isSolvable(root.state.field, root.state.position)) {
            System.out.println("Sorry, not solvable");
            return null;
        }
        root.state.cost = heuristicFunction.apply(root.state.field);
        while (!openNodes.isEmpty() && path.isEmpty()) {
            Node n = openNodes.poll();
//            printField(n.state.field);
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
                    node.state.cost = heuristicFunction.apply(node.state.field);
                    openNodes.add(node);
                }
            }
            openNodesMax = openNodes.size() > openNodesMax ? openNodes.size() : openNodesMax;
        }
//        printPath();
        System.out.println("Path size: " + path.size());
        System.out.println("Open nodes: " + openNodesMax);
        System.out.println("Closed nodes: " + closedNodes.size());
        System.out.println("Initial state:");
        printField(root.state.field);
        return path;
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
            System.out.print(field[i] + ", ");
            if ((i + 1) % size == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * Utility function to count inversion.
     */
    private int countInversion(byte[] field, int position) {
        int current = field[position];
        int count = 0;
        for (int i = position + 1; i < field.length; i++) {
            if (field[i] == 0) {
                continue;
            }
            if (current > field[i]) {
                count++;
            }
        }
        return count;
    }

    //    If the grid width is odd, then the number of inversions
//      in a solvable situation is even.
//    If the grid width is even, and the blank is on an even
//      row counting from the bottom (second-last, fourth-last etc),
//      then the number of inversions in a solvable situation is odd.
//    If the grid width is even, and the blank is on an odd row counting
//      from the bottom (last, third-last, fifth-last etc)
//      then the number of inversions in a solvable situation is even.
    private boolean isSolvable(byte[] field, int emptyPosition) {
        int inversion = 0;
        for (int i = 0; i < field.length; i++) {
            inversion += countInversion(field, i);
        }
        return (size % 2 != 0 && inversion % 2 == 0)
                || (size % 2 == 0 && (((emptyPosition / size) + 1) % 2 == inversion % 2)
        );
    }
}
