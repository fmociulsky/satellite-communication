package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.util.MathUtils;

import java.io.Serializable;

public class SatellitePositionData implements Serializable {
    private static final long serialVersionUID = 4063011792560708498L;
    private float x;
    private float y;
    private Float distance;

    public SatellitePositionData(float x, float y, float distance) {
        this.x = MathUtils.roundTwoDecimals(x);
        this.y = MathUtils.roundTwoDecimals(y);
        this.distance = MathUtils.roundTwoDecimals(distance);
    }

    public SatellitePositionData(float x, float y) {
        this.x = MathUtils.roundTwoDecimals(x);
        this.y = MathUtils.roundTwoDecimals(y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SatellitePositionData)) return false;
        final SatellitePositionData other = (SatellitePositionData) obj;
        return other.x == x && other.y == y;
    }


}
