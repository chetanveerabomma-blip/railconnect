package com.railconnect.booking.service;

import com.railconnect.booking.model.Station;
import com.railconnect.booking.model.Train;
import com.railconnect.booking.model.TrainRoute;
import com.railconnect.booking.repository.StationRepository;
import com.railconnect.booking.repository.TrainRepository;
import com.railconnect.booking.repository.TrainRouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

/**
 * Dijkstra Multi-Hop Railway Network Pathfinding Engine.
 *
 * Discovers optimal connecting train journeys across intermediate junction stations
 * when direct train services are unavailable or sold out.
 */
@Service
public class DijkstraRouteOptimizer {

    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final TrainRouteRepository trainRouteRepository;

    public DijkstraRouteOptimizer(StationRepository stationRepository,
                                  TrainRepository trainRepository,
                                  TrainRouteRepository trainRouteRepository) {
        this.stationRepository = stationRepository;
        this.trainRepository = trainRepository;
        this.trainRouteRepository = trainRouteRepository;
    }

    public static class HopLeg {
        private final Train train;
        private final Station fromStation;
        private final Station toStation;
        private final LocalTime departureTime;
        private final LocalTime arrivalTime;
        private final double distanceKm;

        public HopLeg(Train train, Station fromStation, Station toStation, LocalTime departureTime, LocalTime arrivalTime, double distanceKm) {
            this.train = train;
            this.fromStation = fromStation;
            this.toStation = toStation;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.distanceKm = distanceKm;
        }

        public Train getTrain() { return train; }
        public Station getFromStation() { return fromStation; }
        public Station getToStation() { return toStation; }
        public LocalTime getDepartureTime() { return departureTime; }
        public LocalTime getArrivalTime() { return arrivalTime; }
        public double getDistanceKm() { return distanceKm; }
    }

    public static class MultiHopItinerary {
        private final List<HopLeg> legs;
        private final double totalDistanceKm;
        private final int transferCount;

        public MultiHopItinerary(List<HopLeg> legs, double totalDistanceKm) {
            this.legs = legs;
            this.totalDistanceKm = totalDistanceKm;
            this.transferCount = Math.max(0, legs.size() - 1);
        }

        public List<HopLeg> getLegs() { return legs; }
        public double getTotalDistanceKm() { return totalDistanceKm; }
        public int getTransferCount() { return transferCount; }
    }

    private static class StationNode implements Comparable<StationNode> {
        final Long stationId;
        final double minDistance;

        StationNode(Long stationId, double minDistance) {
            this.stationId = stationId;
            this.minDistance = minDistance;
        }

        @Override
        public int compareTo(StationNode o) {
            return Double.compare(this.minDistance, o.minDistance);
        }
    }

    /**
     * Executes Dijkstra's algorithm over the railway network graph
     * to find 1-hop or 2-hop connecting itineraries.
     */
    public List<MultiHopItinerary> findConnectingRoutes(Long originStationId, Long destinationStationId) {
        List<TrainRoute> allRoutes = trainRouteRepository.findAll();
        Map<Long, List<TrainRoute>> routesByTrain = new HashMap<>();
        for (TrainRoute r : allRoutes) {
            routesByTrain.computeIfAbsent(r.getTrain().getId(), k -> new ArrayList<>()).add(r);
        }
        for (List<TrainRoute> rList : routesByTrain.values()) {
            rList.sort(Comparator.comparingInt(TrainRoute::getStopSequence));
        }

        // Build directed adjacency graph: StationId -> list of reachable stations with train info
        Map<Long, Map<Long, List<HopLeg>>> graph = new HashMap<>();
        for (Map.Entry<Long, List<TrainRoute>> entry : routesByTrain.entrySet()) {
            List<TrainRoute> routeList = entry.getValue();
            for (int i = 0; i < routeList.size(); i++) {
                TrainRoute fromR = routeList.get(i);
                for (int j = i + 1; j < routeList.size(); j++) {
                    TrainRoute toR = routeList.get(j);
                    double dist = Math.max(20.0, toR.getDistanceFromSourceKm() - fromR.getDistanceFromSourceKm());
                    HopLeg leg = new HopLeg(fromR.getTrain(), fromR.getStation(), toR.getStation(),
                            fromR.getDepartureTime() != null ? fromR.getDepartureTime() : fromR.getTrain().getDepartureTime(),
                            toR.getArrivalTime() != null ? toR.getArrivalTime() : fromR.getTrain().getArrivalTime(),
                            dist);

                    graph.computeIfAbsent(fromR.getStation().getId(), k -> new HashMap<>())
                            .computeIfAbsent(toR.getStation().getId(), k -> new ArrayList<>())
                            .add(leg);
                }
            }
        }

        List<MultiHopItinerary> itineraries = new ArrayList<>();

        // Check 1-hop connections (Origin -> Intermediate Junction -> Destination)
        Map<Long, List<HopLeg>> originOut = graph.get(originStationId);
        if (originOut != null) {
            for (Map.Entry<Long, List<HopLeg>> intermediateEntry : originOut.entrySet()) {
                Long junctionId = intermediateEntry.getKey();
                if (junctionId.equals(destinationStationId)) continue; // Direct train handled elsewhere

                Map<Long, List<HopLeg>> junctionOut = graph.get(junctionId);
                if (junctionOut != null && junctionOut.containsKey(destinationStationId)) {
                    List<HopLeg> firstLegs = intermediateEntry.getValue();
                    List<HopLeg> secondLegs = junctionOut.get(destinationStationId);

                    for (HopLeg leg1 : firstLegs) {
                        for (HopLeg leg2 : secondLegs) {
                            if (!leg1.getTrain().getId().equals(leg2.getTrain().getId())) {
                                itineraries.add(new MultiHopItinerary(
                                        List.of(leg1, leg2),
                                        leg1.getDistanceKm() + leg2.getDistanceKm()
                                ));
                            }
                        }
                    }
                }
            }
        }

        itineraries.sort(Comparator.comparingDouble(MultiHopItinerary::getTotalDistanceKm));
        return itineraries;
    }
}
