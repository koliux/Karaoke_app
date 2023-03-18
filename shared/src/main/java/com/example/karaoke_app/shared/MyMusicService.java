package com.example.karaoke_app.shared;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.media.MediaBrowserServiceCompat;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

public class MyMusicService extends MediaBrowserServiceCompat {
    private SimpleExoPlayer exoPlayer;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        // Create a SimpleExoPlayer instance
        exoPlayer = new SimpleExoPlayer.Builder(context).build();

        // Set a listener to handle ExoPlayer events
        exoPlayer.addListener(new ExoPlayerEventListener());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private class ExoPlayerEventListener implements Player.Listener {
        public void onPlayerError(ExoPlaybackException error) {
            // Handle ExoPlayer error here
        }

        public void onPositionDiscontinuity(int reason) {
            // Handle position discontinuity here
        }

        public void onPlaybackStateChanged(int playbackState) {
            // Handle playback state changes here
        }
    }

    private void playAudio(Uri audioUri) {
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_PLAY.equals(intent.getAction())) {
            Uri audioUri = Uri.parse(intent.getStringExtra(EXTRA_AUDIO_URI));
            MediaItem mediaItem = MediaItem.fromUri(audioUri);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
        }
        return START_STICKY;
    }

    public static final String ACTION_PLAY = "com.example.karaoke_app.shared.ACTION_PLAY";
    public static final String EXTRA_AUDIO_URI = "com.example.karaoke_app.shared.EXTRA_AUDIO_URI";

    public static final String ACTION_PROGRESS_UPDATE = "com.example.karaoke_app.shared.ACTION_PROGRESS_UPDATE";
    public static final String EXTRA_CURRENT_POSITION = "com.example.karaoke_app.shared.EXTRA_CURRENT_POSITION";
    public static final String EXTRA_DURATION = "com.example.karaoke_app.shared.EXTRA_DURATION";




}
