package org.grupo11.Utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class Fetcher {
    private static OkHttpClient client = new OkHttpClient();

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute();
    }

    public static Response getWithAuthorization(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();
        return client.newCall(request).execute();
    }

    public static Response post(String url, String body) throws IOException {
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public static Response put(String url, String body) throws IOException {
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public static Response delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        return client.newCall(request).execute();
    }
}
