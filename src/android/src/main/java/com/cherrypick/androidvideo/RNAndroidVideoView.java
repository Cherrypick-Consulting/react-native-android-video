package com.cherrypick.androidvideo;

import android.content.Context;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.exoplayer2.ui.PlayerView;

import com.cherrypick.androidvideo.R;

public class RNAndroidVideoView extends FrameLayout {

  private Context context;
  private PlayerView playerView;

  public RNAndroidVideoView(ThemedReactContext themedReactContext) {
    super(themedReactContext);
    this.context = themedReactContext;
    init();
  }

  public void init() {
    inflate(this.context, R.layout.layout_player, this);

    playerView = findViewById(R.id.player_view);

    playerView.setUseController(false);
  }

  public PlayerView getPlayerView() {
    return playerView;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    RNAndroidVideoManager.getInstance().releasePlayer();
  }
}
