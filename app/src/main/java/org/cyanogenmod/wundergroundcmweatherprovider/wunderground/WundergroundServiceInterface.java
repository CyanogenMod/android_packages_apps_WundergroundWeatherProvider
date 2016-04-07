/**
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @GET("{features}/q/{postalCode}.json")
    public Call<WundergroundReponse> query(@Path("postalCode") String postalCode,
            @Path(value ="features", encoded = true) String features);
}