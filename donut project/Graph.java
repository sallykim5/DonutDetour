import java.util.*;
import java.util.stream.Stream;

public class Graph {
    public List<Edge>[] incoming_edges;

    @SuppressWarnings("unchecked")
    public Graph(int size) {
        incoming_edges = (List<Edge>[]) Stream.generate(ArrayList<Edge>::new).limit(size).toArray(List<?>[]::new);
    }

    public List<Edge> getIncomingEdges(int target) {
        return incoming_edges[target];
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < size(); i++)
            edges.addAll(incoming_edges[i]);

        return edges;
    }

    public int size() {
        return incoming_edges.length;
    }

    @SuppressWarnings("unchecked")
    public Deque<Edge>[] getOutgoingEdges() {
        Deque<Edge>[] edges = (Deque<Edge>[]) Stream.generate(ArrayDeque<Edge>::new).limit(size()).toArray(Deque<?>[]::new);

        for (int target = 0; target < size(); target++) {
            for (Edge edge : getIncomingEdges(target))
                edges[edge.getSource()].add(edge);
        }

        return edges;
    }

    public Deque<Integer> getVerticesWithNoIncomingEdges() {
        Deque<Integer> deque = new ArrayDeque<>();
        int i, size = size();

        for (i = 0; i < size; i++) {
            if (getIncomingEdges(i).isEmpty())
                deque.add(i);
        }

        return deque;
    }

    public Edge setDirectedEdge(int source, int target, double weight) {
        List<Edge> edges = getIncomingEdges(target);
        Edge edge = new Edge(source, target, weight);
        edges.add(edge);
        return edge;
    }

    public void setUndirectedEdge(int source, int target, double weight) {
        setDirectedEdge(source, target, weight);
        setDirectedEdge(target, source, weight);
    }

    public boolean containsCycle() {
        Deque<Integer> notVisited = new ArrayDeque<>();
        for (int i = 0; i < size(); i++) notVisited.add(i);

        while (!notVisited.isEmpty()) {
            if (containsCycleAux(notVisited.poll(), notVisited, new HashSet<>()))
                return true;
        }

        return false;
    }

    private boolean containsCycleAux(int target, Deque<Integer> notVisited, Set<Integer> visited) {
        notVisited.remove(target);
        visited.add(target);

        for (Edge edge : getIncomingEdges(target)) {
            if (visited.contains(edge.getSource()))
                return true;

            if (containsCycleAux(edge.getSource(), notVisited, new HashSet<>(visited)))
                return true;
        }

        return false;
    }

    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!getIncomingEdges(i).isEmpty())
                return false;
        }

        return true;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < incoming_edges.length; i++) {
            build.append(i);
            build.append(" <- ");
            build.append(incoming_edges[i].toString());
            build.append("\n");
        }

        return build.toString();
    }
}