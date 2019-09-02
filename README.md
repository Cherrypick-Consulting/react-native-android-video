# React Native Android Video
>**react-native-android-video**
>
>Android ExoPlayer `<Video />` component for react-native.

## Installation

Using **npm**:

```$ npm install --save react-native-android-video```

Or using ***yarn***:

```$ yarn add react-native-android-video```

### Android

1. Append the following lines to `android/settings.gradle`:

```gradle
include ':react-native-android-video'
project(':react-native-android-video').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-video/src/android')
```
2. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
```gradle
compile project(':react-native-android-video')
```
3. Open `MainApplication.java`:
- Import the package on top of the file:
```java
import com.cherrypick.androidvideo.RNAndroidVideoPackage;
```

- Add the `RNAndroidVideoPackage` class to your list of exported packages:
```java
@Override
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
    new MainReactPackage(),
    new RNAndroidVideoPackage(),
    ...
  );
}
```

## Usage

```javascript
// Import the module
import AndroidVideo from 'react-native-android-video'

...

// In your render function
<AndroidVideo
  ref={(ref) => {
    this.player = ref
  }}
  style={styles.video}
  source={this.props.videoUrl}
  paused={!this.props.isPlaying}
  rate={this.props.playerRate}
  onPlayerReady={this._onVideoReady}
  onPlayerLoading={this._onVideoPlayerLoading}
  onProgress={this._onVideoPlayerProgress}
  onPlayerError={this._onVideoError}
/>
```
---
###  Configurable props

Property | Type | Description
--- | --- | ---
source | string | Sets the media source
paused | boolean | Controls whether the media is paused
rate | number | Speed at which the media should play
---
### Event props

#### onPlayerReady
>Callback function that is called when the media is loaded and the player is ready to play.

Payload:

Property | Type | Description
--- | --- | ---
duration | number | The duration of the media in seconds

#### onPlayerLoading
>Callback function that is called when the player is start or stop buffering.

Payload: `true / false`.


#### onProgress
>Callback function that is called every 250 mili seconds with info about which position the media is currently playing.

Payload:

Property | Type | Description
--- | --- | ---
currentTime | number | Current player position in seconds


#### onPlayerError
>Callback function that is called when error is occurs.

Payload:

Property | Type | Description
--- | --- | ---
error | string | Error message
---
### Methods

#### seek

>Seek to the specified position represented by seconds.

Example:
```javascript
this.player.seek(450)
```
