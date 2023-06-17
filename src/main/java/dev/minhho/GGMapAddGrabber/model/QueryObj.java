package dev.minhho.GGMapAddGrabber.model;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryObj {
    private String apiKey;
    private String city;
    private String query;

    public QueryObj() {
    }
    public QueryObj(String apiKey, String city, String query) {
        this.apiKey = apiKey;
        this.city = city;
        this.query = query;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<LocationObj> fetchLocations(QueryObj queryObj) throws IOException, InterruptedException, ApiException {
        // Implement the logic to call the Google Maps API and fetch the desired information
        // You can use libraries like Retrofit, OkHttp, or Spring RestTemplate for making API requests

        // Dummy implementation for demonstration purposes
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        // Create a Geocoding API request to get the coordinates of the city
        GeocodingResult[] geocodingResults = GeocodingApi.geocode(context, queryObj.city).await();

        // Get the coordinates of the city
        double cityLat = geocodingResults[0].geometry.location.lat;
        double cityLng = geocodingResults[0].geometry.location.lng;

        // Create a Places API request to search for elementary schools near the city coordinates
        PlacesSearchResponse response = PlacesApi.textSearchQuery(context, queryObj.query)
                .location(new LatLng(cityLat, cityLng))
                .radius(10000) // Set the search radius (in meters)
                .type(PlaceType.SCHOOL) // Specify the place type
                .await();

        List<PlacesSearchResult> results = List.of(response.results);
        List<LocationObj> locations = new ArrayList<>();
        for(PlacesSearchResult result : results) {
            LocationObj locationObj = new LocationObj();
            locationObj.setName(result.name);
            locationObj.setAddress(result.formattedAddress);
            locationObj.setLat(String.valueOf(result.geometry.location.lat));
            locationObj.setLng(String.valueOf(result.geometry.location.lng));

            locations.add(locationObj);
        }
        return locations;
    }

}
