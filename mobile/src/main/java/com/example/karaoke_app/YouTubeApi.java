package com.example.karaoke_app;

import com.example.karaoke_app.model.VideoItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class YouTubeApi {
    private static final String API_KEY = "AIzaSyCLC9GsSwJq2hDTliOrpqAhzQJTiMI9-Sw";
    private static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=10&key=" + API_KEY;

    public List<VideoItem> searchVideos(String query) throws IOException {
        String searchUrl = SEARCH_URL + "&q=" + query;
        URL url = new URL(searchUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        List<VideoItem> videoItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray items = jsonObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject id = item.getJSONObject("id");
                JSONObject snippet = item.getJSONObject("snippet");
                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                JSONObject defaultThumbnail = thumbnails.getJSONObject("default");

                String videoId = id.getString("videoId");
                String title = snippet.getString("title");
                String description = snippet.getString("description");
                String thumbnailUrl = defaultThumbnail.getString("url");

                videoItems.add(new VideoItem(videoId, title, description, thumbnailUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoItems;
    }
}
