package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

import android.util.Log;

import com.google.common.base.Joiner;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adnan on 4/4/16.
 */
public class WundergroundServiceManager {
    private final static String TAG = WundergroundServiceManager.class.getSimpleName();
    private final WundergroundServiceInterface mWundergroundServiceInterface;

    public WundergroundServiceManager(String apiKey) {
        Retrofit baseAdapter = buildRestAdapter(apiKey);
        mWundergroundServiceInterface = baseAdapter.create(WundergroundServiceInterface.class);
    }

    public Call<WundergroundReponse> query(String state, String city,
            FeatureParam... features) {
        return mWundergroundServiceInterface.query(state, city,
                coerceVarArgFeaturesToDelimitedString(features));
    }

    public Call<WundergroundReponse> query(double latitude, double longitude,
            FeatureParam... features) {
        return mWundergroundServiceInterface.query(latitude, longitude,
                coerceVarArgFeaturesToDelimitedString(features));
    }

    private String coerceVarArgFeaturesToDelimitedString(FeatureParam... featureParams) {
        return Joiner.on('/').join(featureParams);
    }

    private Retrofit buildRestAdapter(String apiKey) {
        //TODO: Wrap this in debug flag
        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/api/" + apiKey + "/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

