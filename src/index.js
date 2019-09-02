import React, {Component} from 'react';
import {requireNativeComponent, View} from 'react-native';
import PropTypes from 'prop-types';

export default class AndroidVideo extends Component {

  constructor(props) {
    super(props);
  }

  setNativeProps(nativeProps) {
    this._root.setNativeProps(nativeProps);
  }

  _assignRoot = (component) => {
    this._root = component;
  };

  _onProgress = (event) => {
    if (this.props.onProgress) {
      this.props.onProgress(event.nativeEvent);
    }
  }

  _onReady = (event) => {
    if (this.props.onPlayerReady) {
      this.props.onPlayerReady(event.nativeEvent);
    }
  }

  _onLoading = (event) => {
    if (this.props.onPlayerLoading) {
      this.props.onPlayerLoading(event.nativeEvent.isLoading);
    }
  }

  _onError = (event) => {
    if (this.props.onPlayerError) {
      this.props.onPlayerError(event.nativeEvent);
    }
  }

  seek = (time) => {
      this.setNativeProps({ seek: time });
  }

  render() {
    const nativeProps = Object.assign({}, this.props);
    Object.assign(nativeProps, {
      style: nativeProps.style,
      onVideoProgress: this._onProgress,
      onPlayerReady: this._onReady,
      onPlayerLoading: this._onLoading,
      onPlayerError: this._onError
    });

    return (
      <View style={nativeProps.style}>
        <Video
          ref={this._assignRoot}
          {...nativeProps}
        />
      </View>
    );
  }
}

AndroidVideo.propTypes = {
    ...View.propTypes,
    source: PropTypes.string,
    paused: PropTypes.bool,
    rate: PropTypes.number,
    onVideoProgress: PropTypes.func,
    onPlayerReady: PropTypes.func,
    onPlayerLoading: PropTypes.func,
    onPlayerError: PropTypes.func
}

const Video = requireNativeComponent('RNAndroidVideoViewManager', AndroidVideo);
