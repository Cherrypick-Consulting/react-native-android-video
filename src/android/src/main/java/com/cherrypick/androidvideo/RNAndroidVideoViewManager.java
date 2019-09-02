package com.cherrypick.androidvideo;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;


public class RNAndroidVideoViewManager extends SimpleViewManager<RNAndroidVideoView> implements LifecycleEventListener {

    public static final String REACT_CLASS = "RNAndroidVideoViewManager";

    private RCTEventEmitter mEventEmitter;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public RNAndroidVideoView createViewInstance(ThemedReactContext themedReactContext) {
        this.mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
        themedReactContext.addLifecycleEventListener(this);

        return new RNAndroidVideoView(themedReactContext); //If your customview has more constructor parameters pass it from here.
    }

    @ReactProp(name = "source")
    public void setSource(RNAndroidVideoView view, String source) {
        RNAndroidVideoManager.getInstance().initializePlayer(view.getContext(), view, source, mEventEmitter);
    }

    @ReactProp(name = "paused")
    public void setPaused(RNAndroidVideoView view, boolean paused) {
        if (paused) {
            RNAndroidVideoManager.getInstance().pause();
        } else {
            RNAndroidVideoManager.getInstance().play();
        }
    }

    @ReactProp(name = "rate")
    public void setRate(RNAndroidVideoView view, float rate) {
        RNAndroidVideoManager.getInstance().setRate(rate);
    }

    @ReactProp(name = "seek")
    public void setSeek(RNAndroidVideoView view, float seek) {
        RNAndroidVideoManager.getInstance().seekTo(Math.round(seek));
    }

    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder().
                put("onVideoProgress", MapBuilder.of("phasedRegistrationNames",
                        MapBuilder.of("bubbled", "onVideoProgress"))).

                put("onPlayerLoading", MapBuilder.of("phasedRegistrationNames",
                        MapBuilder.of("bubbled", "onPlayerLoading"))).

                put("onPlayerReady", MapBuilder.of("phasedRegistrationNames",
                        MapBuilder.of("bubbled", "onPlayerReady")))

                .put("onPlayerError", MapBuilder.of("phasedRegistrationNames",
                        MapBuilder.of("bubbled", "onPlayerError")))

                .build();
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }
}
