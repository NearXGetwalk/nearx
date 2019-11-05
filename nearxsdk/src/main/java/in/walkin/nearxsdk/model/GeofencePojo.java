package in.walkin.nearxsdk.model;

public class GeofencePojo {
    String id;
    double latitude;
    double longitude;
    int radius;
    String locationName;
    String canonicalName;

    public GeofencePojo(String id, double latitude, double longitude, int radius, String locationName, String canonicalName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.locationName = locationName;
        this.canonicalName = canonicalName;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
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
