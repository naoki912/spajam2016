package wcdi.spajam2016;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class utils {

    public static String readString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        return builder.toString();
    }

    public static String getURL(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setDoInput(true);
        connection.connect();

        return readString(connection.getInputStream());
    }

    public static String postURL(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(true);
        connection.setDoInput(true);
        connection.connect();

        return readString(connection.getInputStream());
    }

    public static String okGetURL(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String okPostURL(String url, String post) throws IOException {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"), post
        );
        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String okJsonPostURL(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json
        );
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
