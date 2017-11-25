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
            if (o1.state.prediction == o2.state.prediction) {
                return o1.state.distance - o2.state.distance;
            } else {
                return o1.state.prediction - o2.state.prediction;
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
        while (!openNodes.isEmpty()) {
            Node n = openNodes.poll();
            if (closedNodes.containsKey(n.state)) {
                continue;
            }
            if (n.state.prediction == 0 && n.parent != null) {
                for (Node p = n; p != null; p = p.parent)
                    path.add(p.state);
                break;
            }
            closedNodes.put(n.state, n);
            for (int i = 0; i < Transition.values().length; i++) {
                Node node = new Node(n, n.state.move(Transition.values()[i], size));
                node.state.prediction = heuristicFunction.apply(node.state.field);
                openNodes.add(node);
            }
        }
        return path;
    }
}
