package org.traccar.processing.peripheralsensorprocessors.fuelsensorprocessors;

import org.traccar.Context;
import org.traccar.helper.DistanceCalculator;
import org.traccar.helper.Log;
import org.traccar.model.PeripheralSensor;
import org.traccar.model.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class FuelConsumptionChecker {

    private static final long DEFAULT_MAX_CAPACITY = 100L;
    private static final long MILLIS_IN_HOUR = 36_00_000L;

    public static boolean isFuelConsumptionAsExpected(Position startPosition,
                                                      Position endPosition,
                                                      double changeVolume,
                                                      Optional<Long> maxCapacity,
                                                      PeripheralSensor fuelSensor) {

        long deviceId = startPosition.getDeviceId();
        DeviceConsumptionInfo consumptionInfo = Context.getDeviceManager().getDeviceConsumptionInfo(deviceId);
        boolean requiredFieldsPresent = checkRequiredFieldsPresent(startPosition, endPosition, consumptionInfo, fuelSensor);

        if (!requiredFieldsPresent) {
            // Not enough info to process data loss.
            return false;
        }

        ExpectedFuelConsumption expectedFuelConsumption =
                getExpectedFuelConsumptionValues(startPosition, endPosition, maxCapacity, consumptionInfo);

        boolean consumptionAsExpected = expectedFuelConsumption != null
                && isFuelConsumptionAsExpected(changeVolume, expectedFuelConsumption);

        if (consumptionAsExpected) {
            Log.debug(String.format("Data Loss: Distance covered %f, Exp fuel consumed: %f, actual fuel consumed: %f",
                                    expectedFuelConsumption.getMaximumDistanceTravelled(),
                                    expectedFuelConsumption.expectedCurrentFuelConsumed,
                                    changeVolume));
        }

        return consumptionAsExpected;
    }

    public static boolean checkRequiredFieldsPresent(Position startPosition,
                                                     Position endPosition,
                                                     DeviceConsumptionInfo consumptionInfo,
                                                     PeripheralSensor fuelSensor) {

        String consumptionType = consumptionInfo.getDeviceConsumptionType().toLowerCase();
        String calibFuelField = fuelSensor.getCalibFuelFieldName();
        switch (consumptionType) {
            case "hourly":
                return startPosition.getAttributes().containsKey(calibFuelField)
                        && endPosition.getAttributes().containsKey(calibFuelField);

            case "odometer":
                return startPosition.getAttributes().containsKey(Position.KEY_TOTAL_DISTANCE)
                        && startPosition.getAttributes().containsKey(Position.KEY_ODOMETER)
                        && endPosition.getAttributes().containsKey(Position.KEY_TOTAL_DISTANCE)
                        && endPosition.getAttributes().containsKey(Position.KEY_ODOMETER);

            case "empty":
            case "enginelesshourly":
            case "noconsumption":
                return true;

            default:
                Log.debug("Found strange fuel consumption category vehicle");
                return false;
        }
    }

    public static ExpectedFuelConsumption getExpectedFuelConsumptionValues(Position startPosition,
                                                                           Position endPosition,
                                                                           Optional<Long> maxCapacity,
                                                                           DeviceConsumptionInfo consumptionInfo) {

        double allowedDeviation = maxCapacity.orElse(DEFAULT_MAX_CAPACITY) * 0.01;

        switch (consumptionInfo.getDeviceConsumptionType().toLowerCase()) {

            case "hourly":
                return getExpectedHourlyFuelConsumptionValues(startPosition, endPosition, allowedDeviation, consumptionInfo);

            case "odometer":
                return getExpectedDistanceFuelConsumptionValues(startPosition, endPosition, allowedDeviation, consumptionInfo);

            case "enginelesshourly":
                return getExpectedEnginelessConsumptionValues(startPosition, endPosition, allowedDeviation, consumptionInfo);

            // All other categories, including "noconsumption" and default "empty" means that there is no expected fuel
            // consumption, and any activity should be treated as is, with or without data loss.
            // The default case here should never be hit, even if we've not set the string in the db. It is included
            // for sanity sake. The null that it returns will be treated as "unexpected consumption", and the event will
            // be treated as is.

            case "empty":
            case "noconsumption":
                return new ExpectedFuelConsumption(0, 0, 0, 0);
            default:
                Log.debug("Found strange fuel consumption category vehicle");
                return null;

        }
    }

    private static ExpectedFuelConsumption getExpectedDistanceFuelConsumptionValues(Position startPosition,
                                                                                    Position endPosition,
                                                                                    double allowedDeviation,
                                                                                    DeviceConsumptionInfo consumptionInfo) {

        double startTotalGPSDistanceInMeters = (double) startPosition.getAttributes().get(Position.KEY_TOTAL_DISTANCE);
        double endTotalGPSDistanceInMeters = (double) endPosition.getAttributes().get(Position.KEY_TOTAL_DISTANCE);

        Number startOdometerInMeters = (Number) startPosition.getAttributes().get(Position.KEY_ODOMETER);
        Number endOdometerInMeters = (Number) endPosition.getAttributes().get(Position.KEY_ODOMETER);

        double differenceTotalDistanceInMeters;
        if (endTotalGPSDistanceInMeters > 0 && startTotalGPSDistanceInMeters > 0) {
            differenceTotalDistanceInMeters = endTotalGPSDistanceInMeters - startTotalGPSDistanceInMeters;
        } else {
            double distance = DistanceCalculator.distance(
                    endPosition.getLatitude(), endPosition.getLongitude(),
                    startPosition.getLatitude(), startPosition.getLongitude());

            differenceTotalDistanceInMeters = BigDecimal.valueOf(distance)
                                                        .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        }

        double differenceOdometerInMeters = endOdometerInMeters.longValue() - startOdometerInMeters.longValue();

        // max distance in KM
        double maximumDistanceTravelled = Math.max(differenceTotalDistanceInMeters, differenceOdometerInMeters) / 1000;
        double expectedMinFuelConsumed = maximumDistanceTravelled / consumptionInfo.getMaxDeviceConsumptionRate();
        double expectedMaxFuelConsumed = maximumDistanceTravelled / consumptionInfo.getMinDeviceConsumptionRate();
        double expectedCurrentFuelConsumed = maximumDistanceTravelled / consumptionInfo.getAssumedDeviceConsumptionRate();


        ExpectedFuelConsumption expectedFuelConsumption = new ExpectedFuelConsumption(expectedMinFuelConsumed,
                                                                                      expectedMaxFuelConsumed,
                                                                                      expectedCurrentFuelConsumed,
                                                                                      allowedDeviation);

        expectedFuelConsumption.setMaximumDistanceTravelled(maximumDistanceTravelled);
        return expectedFuelConsumption;
    }


    private static ExpectedFuelConsumption getExpectedHourlyFuelConsumptionValues(Position startPosition,
                                                                                  Position endPosition,
                                                                                  double allowedDeviation,
                                                                                  DeviceConsumptionInfo consumptionInfo) {

        long maxRunningTime = endPosition.getLong(Position.KEY_TOTAL_IGN_ON_MILLIS) - startPosition.getLong(Position.KEY_TOTAL_IGN_ON_MILLIS);

        double maxRunTimeHours = maxRunningTime / MILLIS_IN_HOUR;

        double expectedMinFuelConsumed = consumptionInfo.getMinDeviceConsumptionRate() * maxRunTimeHours;
        double expectedMaxFuelConsumed = consumptionInfo.getMaxDeviceConsumptionRate() * maxRunTimeHours;
        double expectedCurrentFuelConsumed = consumptionInfo.getAssumedDeviceConsumptionRate() * maxRunTimeHours;

        ExpectedFuelConsumption expectedFuelConsumption = new ExpectedFuelConsumption(expectedMinFuelConsumed,
                                                                                      expectedMaxFuelConsumed,
                                                                                      expectedCurrentFuelConsumed,
                                                                                      allowedDeviation);

        expectedFuelConsumption.setMaxRunningTimeMillis(maxRunningTime);
        return expectedFuelConsumption;
    }

    private static ExpectedFuelConsumption getExpectedEnginelessConsumptionValues(Position startPosition,
                                                                                  Position endPosition,
                                                                                  double allowedDeviation,
                                                                                  DeviceConsumptionInfo consumptionInfo) {

        long timeDeltaMillis = endPosition.getDeviceTime().getTime() - startPosition.getDeviceTime().getTime();

        if (timeDeltaMillis <= 0) { // Just making sure that we don't run into a weird case of start and end being the same.
            return new ExpectedFuelConsumption(0, 0, 0, 0);
        }

        double timeDeltaHours = timeDeltaMillis / MILLIS_IN_HOUR;

        double expectedMinFuelConsumed = consumptionInfo.getMinDeviceConsumptionRate() * timeDeltaHours;
        double expectedMaxFuelConsumed = consumptionInfo.getMaxDeviceConsumptionRate() * timeDeltaHours;
        double expectedCurrentFuelConsumed = consumptionInfo.getAssumedDeviceConsumptionRate() * timeDeltaHours;

        ExpectedFuelConsumption expectedFuelConsumption = new ExpectedFuelConsumption(expectedMinFuelConsumed,
                                                                                      expectedMaxFuelConsumed,
                                                                                      expectedCurrentFuelConsumed,
                                                                                      allowedDeviation);
        return expectedFuelConsumption;
    }

    public static boolean isFuelConsumptionAsExpected(final double calculatedFuelChangeVolume,
                                                      final ExpectedFuelConsumption expectedFuelConsumption) {

        return Math.abs(calculatedFuelChangeVolume) > expectedFuelConsumption.allowedDeviation
               && Math.abs(calculatedFuelChangeVolume) >= expectedFuelConsumption.expectedMinFuelConsumed
               && Math.abs(calculatedFuelChangeVolume) <= expectedFuelConsumption.expectedMaxFuelConsumed;
    }
}