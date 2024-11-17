package org.knowm.xchange.mexc.v3;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import com.google.gson.Gson;
import org.knowm.xchange.mexc.v3.util.SignatureUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserDataClient {
    private static final String REQUEST_HOST = "https://api.mexc.com";

    private static OkHttpClient OK_HTTP_CLIENT = null;

    private static String accessKey;
    private static String secretKey;

    private UserDataClient() {
        //hides the public one.
    }

    public static void init(String accessKey, String secretKey) {
        if(OK_HTTP_CLIENT == null) {
            UserDataClient.accessKey = accessKey;
            UserDataClient.secretKey = secretKey;
            OK_HTTP_CLIENT = createOkHttpClient();
        }
    }

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new SignatureInterceptor(secretKey, accessKey))
                .build();
    }

    public static <T> T post(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .post(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain"))).build()).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T handleResponse(Response response, TypeReference<T> ref) throws IOException {
        Gson gson = new Gson();
        assert response.body() != null;
        String content = response.body().string();
        if (200 != response.code()) {
            throw new RuntimeException(content);
        }
        return gson.fromJson(content, ref.getType());
    }


}
