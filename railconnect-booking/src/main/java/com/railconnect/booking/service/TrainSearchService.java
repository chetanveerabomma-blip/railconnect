package com.railconnect.booking.service;

import com.railconnect.booking.dto.FareCalculationDto;
import com.railconnect.booking.dto.TrainSearchResponse;
import com.railconnect.booking.model.Station;
import com.railconnect.booking.model.Train;
import com.railconnect.booking.model.TrainRoute;
import com.railconnect.booking.repository.BookingRepository;
import com.railconnect.booking.repository.StationRepository;
import com.railconnect.booking.repository.TrainRepository;
import com.railconnect.booking.repository.TrainRouteRepository;
import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.repository.CoachRepository;
import com.railconnect.seatengine.service.SeatAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TrainSearchService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainRouteRepository trainRouteRepository;
    private final CoachRepository coachRepository;
    private final BookingRepository bookingRepository;
    private final SeatAvailabilityService seatAvailabilityService;
    private final FareCalculationService fareCalculationService;

    public TrainSearchService(TrainRepository trainRepository,
                              StationRepository stationRepository,
                              TrainRouteRepository trainRouteRepository,
                              CoachRepository coachRepository,
                              BookingRepository bookingRepository,
                              SeatAvailabilityService seatAvailabilityService,
                              FareCalculationService fareCalculationService) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.trainRouteRepository = trainRouteRepository;
        this.coachRepository = coachRepository;
        this.bookingRepository = bookingRepository;
        this.seatAvailabilityService = seatAvailabilityService;
        this.fareCalculationService = fareCalculationService;
    }

    private Station resolveStation(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String clean = input.trim();
        Optional<Station> byCode = stationRepository.findByCodeIgnoreCase(clean);
        if (byCode.isPresent()) {
            return byCode.get();
        }
        List<Station> matches = stationRepository.searchStations(clean);
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        return null;
    }

    public List<TrainSearchResponse> searchTrains(String fromCode, String toCode, LocalDate journeyDate) {
        Station fromStation = resolveStation(fromCode);
        Station toStation = resolveStation(toCode);

        if (fromStation == null || toStation == null) {
            return Collections.emptyList();
        }

        List<Train> directTrains = trainRepository.findDirectTrainsBetweenStations(fromStation.getId(), toStation.getId());

        List<TrainSearchResponse> results = new ArrayList<>();

        for (Train train : directTrains) {
            // Find departure route stop and arrival route stop
            Optional<TrainRoute> fromRouteOpt = trainRouteRepository.findByTrainIdAndStationId(train.getId(), fromStation.getId());
            Optional<TrainRoute> toRouteOpt = trainRouteRepository.findByTrainIdAndStationId(train.getId(), toStation.getId());

            if (fromRouteOpt.isEmpty() || toRouteOpt.isEmpty()) {
                continue;
            }

            TrainRoute fromRoute = fromRouteOpt.get();
            TrainRoute toRoute = toRouteOpt.get();

            double distance = Math.max(50.0, toRoute.getDistanceFromSourceKm() - fromRoute.getDistanceFromSourceKm());
            String depTime = fromRoute.getDepartureTime() != null ? fromRoute.getDepartureTime().toString() : train.getDepartureTime().toString();
            String arrTime = toRoute.getArrivalTime() != null ? toRoute.getArrivalTime().toString() : train.getArrivalTime().toString();

            // Find booked seats for this train and journey date
            Set<Long> bookedSeatIds = bookingRepository.findBookedSeatIds(train.getId(), journeyDate != null ? journeyDate : LocalDate.now());

            // Get coaches for this train
            List<Coach> coaches = coachRepository.findByTrainId(train.getId());
            Set<String> distinctClasses = new LinkedHashSet<>();
            for (Coach c : coaches) {
                distinctClasses.add(c.getCoachType());
            }

            List<TrainSearchResponse.ClassAvailabilityDto> classDtos = new ArrayList<>();
            for (String coachType : distinctClasses) {
                int availableSeats = seatAvailabilityService.getAvailableSeatsCount(train.getId(), coachType, bookedSeatIds);
                FareCalculationDto fare = fareCalculationService.calculateFare(train.getTrainType(), coachType, distance, 1, 0.0);

                String status = availableSeats > 0 ? "AVAILABLE (" + availableSeats + ")" : "RAC / WL";
                classDtos.add(new TrainSearchResponse.ClassAvailabilityDto(coachType, availableSeats, fare.getFinalFare(), status));
            }

            TrainSearchResponse res = new TrainSearchResponse();
            res.setTrainId(train.getId());
            res.setTrainNumber(train.getTrainNumber());
            res.setTrainName(train.getTrainName());
            res.setTrainType(train.getTrainType());
            res.setFromStationCode(fromStation.getCode());
            res.setFromStationName(fromStation.getName());
            res.setToStationCode(toStation.getCode());
            res.setToStationName(toStation.getName());
            res.setDepartureTime(depTime);
            res.setArrivalTime(arrTime);
            res.setDurationHours(train.getDurationHours());
            res.setDistanceKm(distance);
            res.setRunningDays(train.getRunningDays());
            res.setAvailableClasses(classDtos);

            results.add(res);
        }

        // Sort trains chronologically by departure time
        results.sort(Comparator.comparing(TrainSearchResponse::getDepartureTime));

        return results;
    }
}
