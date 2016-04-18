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

package org.cyanogenmod.wundergroundcmweatherprovider;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Inspired by https://gist.github.com/swankjesse/8571a8207a5815cca1fb
 */
public class ApiKeyInterceptor implements Interceptor {

    /** http://api.wunderground.com/api/<API KEY>/conditions/forecast/q/98121.json **/
    private static final int API_KEY_PATH_SEGMENT = 1;

    private volatile String mApiKey;

    public void setApiKey(String apiKey) {
        this.mApiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String apiKey = mApiKey;
        if (apiKey != null) {
            HttpUrl newUrl = request.url().newBuilder()
                    .setPathSegment(1, apiKey)
                    .build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}
