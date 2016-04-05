package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WundergroundServiceInterface {
    @GET("{features}/q/{state}/{city}.json")
    public Call<WundergroundReponse> query(@Path("state") String state,
            @Path("city") String city, @Path(value ="features",
            encoded = true) String features);

    @GET("{features}/q/{latitude},{longitude}.json")
    public Call<WundergroundReponse> query(@Path("latitude") double latitude,
            @Path("longitude") double longitude, @Path(value ="features",
            encoded = true) String features);
}