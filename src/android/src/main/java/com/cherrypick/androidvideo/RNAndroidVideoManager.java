package com.cherrypick.androidvideo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

class RNAndroidVideoManager {
  private static RNAndroidVideoManager instance;

  private  RNAndroidVideoManager(){}

  static synchronized RNAndroidVideoManager getInstance() {
    if (instance == null) {
      instance = new RNAndroidVideoManager();
    }
    return instance;
  }

  private SimpleExoPlayer player;
  private Handler mProgressUpdateHandler = new Handler();
  private Runnable mProgressUpdateRunnable = null;

  private static final long PROGRESS_UPDATE_INTERVAL_MS = 250;

  private boolean playerLoaded = false;
  private boolean playing = false;


  void initializePlayer(Context context, RNAndroidVideoView reactVideoView, String url, RCTEventEmitter eventEmitter) {
    if (player != null) {
      player.release();
      player = null;
    }

    player = ExoPlayerFactory.newSimpleInstance(context);

    PlayerView playerView = reactVideoView.getPlayerView();

    // Bind the player to the view.
    playerView.setPlayer(player);

    initializePlayer(reactVideoView, url, eventEmitter);
  }

  void releasePlayer() {
    if (player != null) {
      player.release();
      player = null;
      playerLoaded = false;
    }
  }

  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource.Factory(
      new DefaultHttpDataSourceFactory("exoplayer-codelab")).
      createMediaSource(uri);
  }

  private void initializePlayer(RNAndroidVideoView reactVideoView, String url, RCTEventEmitter eventEmitter) {

    // Prepare the player
    Uri uri = Uri.parse(url);
    MediaSource mediaSource = buildMediaSource(uri);
    player.prepare(mediaSource);

    // Add event listener
    addPlayerEventListener(reactVideoView, eventEmitter);

    // Initialize progress update task
    initProgressUpdateReporting(reactVideoView, eventEmitter);

    if (playing) {
      player.setPlayWhenReady(true);
    }
  }

  private void addPlayerEventListener(final RNAndroidVideoView reactVideoView, final RCTEventEmitter eventEmitter) {
    Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onLoadingChanged(boolean isLoading) {
          WritableMap event = Arguments.createMap();
          event.putBoolean("isLoading", isLoading);

          eventEmitter.receiveEvent(reactVideoView.getId(), "onPlayerLoading", event);
        }

      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
          // Report isLoading == false
          WritableMap loadingEvent = Arguments.createMap();
          loadingEvent.putBoolean("isLoading", false);

          eventEmitter.receiveEvent(reactVideoView.getId(), "onPlayerLoading", loadingEvent);

          // Report onPlayerReady when the current player is loaded for the first time
          if (!playerLoaded) {
            playerLoaded = true;
            WritableMap event = Arguments.createMap();
            event.putDouble("duration", player.getDuration());

            eventEmitter.receiveEvent(reactVideoView.getId(), "onPlayerReady", event);
          }
        }
      }

      @Override
      public void onSeekProcessed() {
          // Report current position (necessary when the player isn't playing)
          WritableMap event = Arguments.createMap();
          event.putDouble("currentTime", player.getCurrentPosition() / 1000.0);

          eventEmitter.receiveEvent(reactVideoView.getId(), "onVideoProgress", event);
      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {
        WritableMap event = Arguments.createMap();
        event.putString("error", error.getMessage());

        eventEmitter.receiveEvent(reactVideoView.getId(), "onPlayerError", event);
      }
    };

    player.addListener(eventListener);
  }

  private void initProgressUpdateReporting(final RNAndroidVideoView reactVideoView, final RCTEventEmitter eventEmitter) {
    mProgressUpdateRunnable = new Runnable() {
      @Override
      public void run() {
        {
          if (player != null && player.getPlayWhenReady()) {
            WritableMap event = Arguments.createMap();
            event.putDouble("currentTime", player.getCurrentPosition() / 1000.0);

            eventEmitter.receiveEvent(reactVideoView.getId(), "onVideoProgress", event);
          }

          // Check for update after an interval
          mProgressUpdateHandler.postDelayed(this, PROGRESS_UPDATE_INTERVAL_MS);
        }
      }
    };
    mProgressUpdateHandler.postDelayed(mProgressUpdateRunnable, PROGRESS_UPDATE_INTERVAL_MS);
  }

  void play() {
    playing = true;
    if (player != null) {
      player.setPlayWhenReady(true);

    }
  }

  void pause() {
    playing = false;
    if (player != null) {
      player.setPlayWhenReady(false);
    }
  }

  void seekTo(float posMs) {
    if (player != null) {
      player.seekTo(Math.round(posMs * 1000.0f));
    }
  }

  void setRate(float rate) {
    if (player != null) {
      player.setPlaybackParameters(new PlaybackParameters(rate));
    }
  }
}
