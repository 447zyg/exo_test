package com.zyg.exotest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;

@SuppressLint("SourceLockedOrientationActivity")
public class MainActivity extends Activity {
    private PlayerView video_view;

    private ViewGroup video_container;

    private SimpleExoPlayer player;

    private Activity self() {
        return MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video_container = findViewById(R.id.video_container);

        video_view = findViewById(R.id.video_view);
        Button start = findViewById(R.id.start);
        Button full = findViewById(R.id.full);


        player = new SimpleExoPlayer.Builder(self()).build();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(self(), Util.getUserAgent(self(), ""));
        String videoUrl = "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv";
        final MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).
                createMediaSource(Uri.parse(videoUrl));

        video_view.setPlayer(player);

        start.setOnClickListener(view -> player.prepare(videoSource));

        full.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            if (video_view.getParent() instanceof ViewGroup) {
                ((ViewGroup) video_view.getParent()).removeView(video_view);
                ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ViewGroup group = findViewById(android.R.id.content);
                group.addView(video_view, layout);
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (isLand(self())) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().getDecorView().setSystemUiVisibility(0);
            if (video_view.getParent() instanceof ViewGroup) {
                ((ViewGroup) video_view.getParent()).removeView(video_view);
                ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                video_container.addView(video_view, layout);
            }
        }
    }

    public static boolean isLand(@NonNull Context activity) {
        Resources resources = activity.getResources();
        assert resources != null;
        Configuration configuration = resources.getConfiguration();
        Assertions.checkState(configuration != null);
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
