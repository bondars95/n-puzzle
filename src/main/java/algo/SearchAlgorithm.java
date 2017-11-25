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
        }
        printPath();
        System.out.println(path.size());
        System.out.println(openNodes.size());
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
}
