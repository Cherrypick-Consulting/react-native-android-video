import React, {Component} from 'react'
import {
  StyleSheet,
  Platform,
  View,
  Text,
  TouchableHighlight,
  Image
} from 'react-native'
import AndroidVideo from 'react-native-android-video'

export default class App extends Component {
  constructor (props) {
    super(props)
    this.state = {
      isPlaying: false,
      duration: 0,
      currentTime: 0
    }
    this._onPlayerReady = this._onPlayerReady.bind(this)
    this._onVideoPlayerProgress = this._onVideoPlayerProgress.bind(this)
    this._playPausePressed = this._playPausePressed.bind(this)
    this._seekForward = this._seekForward.bind(this)
    this._seekBackward = this._seekBackward.bind(this)
  }

  _onPlayerReady (data) {
    this.setState({
      duration: data.duration / 1000 // convert to seconds
    })
  }

  _onVideoPlayerProgress (data) {
    this.setState({
      currentTime: data.currentTime
    })
  }

  _playPausePressed () {
    if (this.player) {
      this.setState({ isPlaying: !this.state.isPlaying })
    }
  }

  _seekForward () {
    if (this.player) {
      this.player.seek(
        Math.min(this.state.currentTime + 15, this.state.duration)
      )
    }
  }

  _seekBackward () {
    if (this.player) {
      this.player.seek(
        Math.max(0, this.state.currentTime - 15)
      )
    }
  }

  getFormatedTime(currentTime) {
    let sec = Math.floor(currentTime % 60)
    if (sec < 10) {
      sec = "0" + sec
    }
    let min = Math.floor(currentTime / 60)
    if (min < 10) {
      min = "0" + min;
    }
    return min + ":" + sec
  }

  render() {
    return (
      <View style={styles.container}>
        {Platform.OS === 'android' ? (
          <View>
            <AndroidVideo
              ref={(ref) => {
                this.player = ref
              }}
              style={styles.videoPlayer}
              source={"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"}
              paused={!this.state.isPlaying}
              onProgress={this._onVideoPlayerProgress}
              onPlayerReady={this._onPlayerReady}
            />
            <Text style={styles.timersText}>
              {this.getFormatedTime(this.state.currentTime)} / {this.getFormatedTime(this.state.duration)}
            </Text>
            <View style={styles.playControlsView}>
              <TouchableHighlight onPress={this._seekBackward}>
                <Image
                  resizeMode={'contain'}
                  source={require('./App/Images/player-back15-white.png')}
                />
              </TouchableHighlight>
              <TouchableHighlight onPress={this._playPausePressed}>
                <Image
                  resizeMode={'contain'}
                  source={this.state.isPlaying ?
                      require('./App/Images/player-pause-white.png') :
                      require('./App/Images/player-play-white.png')}
                />
              </TouchableHighlight>
              <TouchableHighlight onPress={this._seekForward}>
                <Image
                  resizeMode={'contain'}
                  source={require('./App/Images/player-fwd15-white.png')}
                />
              </TouchableHighlight>
            </View>
          </View>
        ) : ( // iOS
          <Text style={styles.iosText}>
            This module is designed for Android only
          </Text>
        )}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black'
  },
  videoPlayer: {
    width: '100%',
    height: 200,
    marginTop: '10%'
  },
  timersText: {
    marginTop: 50,
    textAlign: 'center',
    color: 'white',
    fontSize: 24
  },
  playControlsView: {
    marginTop: 50,
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center'
  },
  iosText: {
    alignSelf: 'center',
    marginTop: '30%',
    color: 'white'
  }
})
