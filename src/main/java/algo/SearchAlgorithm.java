package algo;

import game.State;
import game.Transition;
import graph.Node;

import java.util.*;
import java.util.function.Function;

public final class SearchAlgorithm {
    private final short size;
    private final Node root;
    private final PriorityQueue<Node> openNodes;
    private final List<State> path;
    private final HashMap<State, Node> closedNodes;
    private final Function<short[], Integer> heuristicFunction;

    public SearchAlgorithm(
            final short[] field,
            final Function<short[], Integer> heuristicFunction
    ) {
        this.size = (short) Math.sqrt(field.length + 1);
        this.openNodes = new PriorityQueue<>((o1, o2) -> {
            if (o1.state.cost == o2.state.cost) {
                return o1.state.distance - o2.state.distance;
            } else {
                return o1.state.cost - o2.state.cost;
            }
        });
        this.closedNodes = new HashMap<>();
        this.path = new ArrayList<>();
        this.heuristicFunction = heuristicFunction;
        this.root = new Node(
                null,
                new State(
                        field,
                        null,
                        null,
                        0,
                        0
                )
        );
    }

    public List<State> search() {
        openNodes.add(root);
        printField(root.state.field);
        int openNodesMax = 0;
        if (!isUnresolvable(root.state.field, root.state.position)) {
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
//            System.out.println(closedNodes.size());
            openNodesMax = openNodes.size() > openNodesMax ? openNodes.size() : openNodesMax;
        }
//        printPath();
        System.out.println(path.size());
        System.out.println(closedNodes.size());
        System.out.println(openNodesMax);
        System.out.println("Initial state:");
        printField(root.state.field);
        return path;
    }

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

    private void printField(short[] field) {
        for (int i = 0; i < field.length; i++) {
            System.out.print(field[i] + " ");
            if ((i + 1) % size == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private int countInversion(short[] field, int position) {
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

    private boolean isUnresolvable(short[] field, int emptyPosition) {
        int inversion = 0;
        for (int i = 0; i < field.length; i++) {
            inversion += countInversion(field, i);
        }
        // todo pair
        return (size % 2 != 0  && inversion % 2 == 0);
//                || (size % 2 == 0 && (((++emptyPosition / size) + 1) % 2 != 0) && (inversion % 2 == 0));
    }
}
