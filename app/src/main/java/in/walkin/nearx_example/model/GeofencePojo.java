package in.walkin.nearx_example.model;

import java.io.Serializable;

public class GeofencePojo implements Serializable {
    String id;
    Double latitude;
    Double longitude;
    int radius;
    String locationName;
    String canonicalName;

    public GeofencePojo(String id, Double latitude, Double longitude, int radius, String locationName, String canonicalName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.locationName = locationName;
        this.canonicalName = canonicalName;
    }

    public GeofencePojo() {
    }


    public String getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }
}
