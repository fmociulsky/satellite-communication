package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.TrilateralizationExceptionError.SATELLITE_QTY_NOT_ENOUGH;
import static mlchallenge.fuegoquasar.util.MathUtils.powerTwo;

public class PositionCalculator {

    protected static Position calculatePosition(HashMap<String, SatellitePositionData> satellitePositionDatas) throws PositionCalculatorException {
        if (satellitePositionDatas.size() < 3) throw new PositionCalculatorException(SATELLITE_QTY_NOT_ENOUGH);
        final List<SatellitePositionData> satellitePositionDataDistanceList = new ArrayList<>(satellitePositionDatas.values());
        final List<Position> twoCirclesIntersection = calculateTwoCirclesIntersection(satellitePositionDataDistanceList.get(0), satellitePositionDataDistanceList.get(1));
        return calculatePositionWithThreeCircles(twoCirclesIntersection, satellitePositionDataDistanceList.get(2));
    }

    @SuppressWarnings("TooBroadScope")
    protected static List<Position> calculateTwoCirclesIntersection(SatellitePositionData a, SatellitePositionData b) throws PositionCalculatorException {
        final float xA = a.getX();
        final float yA = a.getY();
        final float rA = a.getDistance();

        final float xB = b.getX();
        final float yB = b.getY();
        final float rB = b.getDistance();

        final float k = (powerTwo(rA) - powerTwo(rB) + powerTwo(xB) - powerTwo(xA) + powerTwo(yB) - powerTwo(yA)) / (2 * (yB - yA));
        final float c = (xB - xA) / (yB - yA);

        final float minusB = (-2 * yA) * c + (2 * xA) + (2 * k * c);

        final float toRoot = powerTwo(2 * yA * c - 2 * xA - 2 * k * c) - 4 * (1 + powerTwo(c)) * (powerTwo(k) + powerTwo(xA) + powerTwo(yA) - powerTwo(rA) - 2 * yA * k);
        if (toRoot <= 0 || Float.isNaN(toRoot)) throw new PositionCalculatorException(PositionCalculatorException.TrilateralizationExceptionError.SATELLITES_INFO_MISMATCH);
        final float root = (float) Math.sqrt(toRoot);

        final float divisor = 2 * (1 + powerTwo(c));

        final float x1 = (minusB + root) / divisor;
        final float x2 = (minusB - root) / divisor;

        final Function<Float, Float> lineal =  x -> -c * x + k;
        final float y1 = lineal.apply(x1);
        final float y2 = lineal.apply(x2);

        final Position intersection1 = new Position(x1, y1);
        final Position intersection2 = new Position(x2, y2);

        return Arrays.asList(intersection1,intersection2);
    }

    protected static Position calculatePositionWithThreeCircles(List<Position> twoCirclesIntersections, SatellitePositionData c) {
        final float xC = c.getX();
        final float yC = c.getY();
        final float rC = c.getDistance();

        final Position intersection1 = twoCirclesIntersections.get(0);
        final Position intersection2 = twoCirclesIntersections.get(1);

        final float circleCEquation = powerTwo(rC) - powerTwo(xC) - powerTwo(yC);

        final Function<Position, Float> findIntersection = location -> {
            final float x = location.getX();
            final float y = location.getY();
            return powerTwo(x) + powerTwo(y) - 2 * xC * x - 2 * yC * y;
        };

        float comparisonTolerance = MIN_TOLERANCE;

        Position threeCirclesIntersection = null;
        final float possibleIntersectionWithC1 = findIntersection.apply(intersection1);
        final float possibleIntersectionWithC2 = findIntersection.apply(intersection2);

        while (threeCirclesIntersection == null && comparisonTolerance >= MIN_TOLERANCE && comparisonTolerance <= MAX_TOLERANCE) {
            final boolean closeEnough1 = isCloseEnough(possibleIntersectionWithC1, circleCEquation, comparisonTolerance);
            final boolean closeEnough2 = isCloseEnough(possibleIntersectionWithC2, circleCEquation, comparisonTolerance);

            if (closeEnough1 || closeEnough2) {
                if (closeEnough1 && closeEnough2) comparisonTolerance = MAX_TOLERANCE;

                else threeCirclesIntersection = closeEnough1 ? intersection1 : intersection2;
            }
            comparisonTolerance += TOLERANCE_STEP;
        }

        return threeCirclesIntersection;
    }

    private static boolean isCloseEnough(float test, float result, float tolerance) {
        final float difference = Math.abs(result - test);
        return difference <= Math.abs(result * tolerance);
    }

    public static final float MIN_TOLERANCE = 0.001f;
    public static final float TOLERANCE_STEP = 0.001f;
    public static final float MAX_TOLERANCE = 0.05f;
}
