/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
import AndroidVideo from 'react-native-android-video';

export default class App extends Component<Props> {
  render() {
    return (
      <View style={{ flex: 1, backgroundColor: 'black' }}>
        {Platform.OS === 'android' ? (
          <AndroidVideo
            ref={(ref) => {
              this.player = ref
            }}
            style={{ width: '100%', height: 200, marginTop: '20%' }}
            source={"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"}
            paused={false}
          />
        ) : (
          <Text style={{ alignSelf: 'center', marginTop: '30%' }}>
            This module is only supported in Android
          </Text>
        )}
      </View>
    );
  }
}

