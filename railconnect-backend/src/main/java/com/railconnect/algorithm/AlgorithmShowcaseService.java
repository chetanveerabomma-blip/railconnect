package com.railconnect.algorithm;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Advanced Programming Practice (APP) Demonstration Service
 * Showcases the OOP & Data Structures and Algorithms principles utilized across RailConnect.
 */
@Service
public class AlgorithmShowcaseService {

    /**
     * DSA: Graph Route Search (Adjacency List)
     * Demonstrates BFS traversal across railway junction networks
     */
    public List<String> findShortestStationPath(Map<String, List<String>> graph, String source, String destination) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(List.of(source));
        visited.add(source);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastNode = path.get(path.size() - 1);

            if (lastNode.equalsIgnoreCase(destination)) {
                return path;
            }

            for (String neighbor : graph.getOrDefault(lastNode, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * DSA: PriorityQueue Berth Allocation
     * Scores candidate berths based on proximity & passenger requirements
     */
    public List<Integer> sortBerthsByProximity(List<Integer> candidateSeats, int targetCabin) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                Comparator.comparingInt(seat -> Math.abs(((seat - 1) / 8) + 1 - targetCabin))
        );
        pq.addAll(candidateSeats);

        List<Integer> sorted = new ArrayList<>();
        while (!pq.isEmpty()) {
            sorted.add(pq.poll());
        }
        return sorted;
    }
}
