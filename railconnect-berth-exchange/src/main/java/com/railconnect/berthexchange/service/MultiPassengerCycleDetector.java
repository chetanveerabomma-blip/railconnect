package com.railconnect.berthexchange.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Directed Graph Multi-Passenger Circular Berth Swap Cycle Detector.
 *
 * Employs depth-first cycle enumeration (Tarjan's strongly connected components derivative)
 * to uncover 3-way, 4-way, and N-way circular seat swap opportunities.
 *
 * Example:
 * Passenger A (holding UPPER) desires LOWER (held by B)
 * Passenger B (holding LOWER) desires SIDE_LOWER (held by C)
 * Passenger C (holding SIDE_LOWER) desires UPPER (held by A)
 * -> Atomic 3-way cycle resolves all 3 passenger preferences simultaneously!
 */
@Service
public class MultiPassengerCycleDetector {

    public static class SwapWish {
        private final Long passengerId;
        private final String passengerName;
        private final String currentBerth;
        private final String desiredBerth;

        public SwapWish(Long passengerId, String passengerName, String currentBerth, String desiredBerth) {
            this.passengerId = passengerId;
            this.passengerName = passengerName;
            this.currentBerth = currentBerth;
            this.desiredBerth = desiredBerth;
        }

        public Long getPassengerId() { return passengerId; }
        public String getPassengerName() { return passengerName; }
        public String getCurrentBerth() { return currentBerth; }
        public String getDesiredBerth() { return desiredBerth; }
    }

    public static class SwapCycle {
        private final List<SwapWish> participants;
        private final int cycleLength;

        public SwapCycle(List<SwapWish> participants) {
            this.participants = participants;
            this.cycleLength = participants.size();
        }

        public List<SwapWish> getParticipants() { return participants; }
        public int getCycleLength() { return cycleLength; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Circular Swap [length=").append(cycleLength).append("]: ");
            for (int i = 0; i < participants.size(); i++) {
                SwapWish w = participants.get(i);
                SwapWish next = participants.get((i + 1) % participants.size());
                sb.append(w.getPassengerName()).append(" (").append(w.getCurrentBerth()).append(") -> ")
                  .append(next.getPassengerName()).append(" (").append(next.getCurrentBerth()).append("); ");
            }
            return sb.toString();
        }
    }

    /**
     * Finds all circular swap opportunities from a list of submitted wishes.
     */
    public List<SwapCycle> detectSwapCycles(List<SwapWish> wishes) {
        List<SwapCycle> detectedCycles = new ArrayList<>();
        if (wishes == null || wishes.size() < 2) {
            return detectedCycles;
        }

        // Build adjacency graph: Wish index -> list of next wish indices whose current berth matches desired
        Map<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < wishes.size(); i++) {
            for (int j = 0; j < wishes.size(); j++) {
                if (i != j && wishes.get(i).getDesiredBerth().equalsIgnoreCase(wishes.get(j).getCurrentBerth())) {
                    adj.computeIfAbsent(i, k -> new ArrayList<>()).add(j);
                }
            }
        }

        // Search simple cycles up to length 4 using DFS
        Set<String> visitedCycleSignatures = new HashSet<>();
        for (int start = 0; start < wishes.size(); start++) {
            boolean[] visited = new boolean[wishes.size()];
            List<Integer> path = new ArrayList<>();
            path.add(start);
            dfsFindCycles(start, start, visited, path, adj, wishes, visitedCycleSignatures, detectedCycles);
        }

        return detectedCycles;
    }

    private void dfsFindCycles(int current, int start, boolean[] visited, List<Integer> path,
                               Map<Integer, List<Integer>> adj, List<SwapWish> wishes,
                               Set<String> visitedCycleSignatures, List<SwapCycle> results) {
        visited[current] = true;
        List<Integer> neighbors = adj.getOrDefault(current, Collections.emptyList());

        for (int next : neighbors) {
            if (next == start && path.size() >= 2) {
                // Cycle found
                String sig = computeCycleSignature(path);
                if (!visitedCycleSignatures.contains(sig)) {
                    visitedCycleSignatures.add(sig);
                    List<SwapWish> cycleWishes = new ArrayList<>();
                    for (int idx : path) {
                        cycleWishes.add(wishes.get(idx));
                    }
                    results.add(new SwapCycle(cycleWishes));
                }
            } else if (!visited[next] && path.size() < 4) { // Cap maximum multi-swap length to 4
                path.add(next);
                dfsFindCycles(next, start, visited, path, adj, wishes, visitedCycleSignatures, results);
                path.remove(path.size() - 1);
            }
        }
        visited[current] = false;
    }

    private String computeCycleSignature(List<Integer> path) {
        List<Integer> sorted = new ArrayList<>(path);
        Collections.sort(sorted);
        return sorted.toString();
    }
}
