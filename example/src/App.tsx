/**
 * Sample React Native App
 *
 * adapted from App.js generated by the following command:
 *
 * react-native init example
 *
 * https://github.com/facebook/react-native
 */

import React, { Component } from 'react';
import { Alert, StyleSheet, Text, View } from 'react-native';
import BadgeNumber from 'react-native-badge-number';

export default class App extends Component<{}> {
  state = {
    status: 'starting',
    message: '--',
  };
  componentDidMount() {
    BadgeNumber.setBadge(20).catch((e) => console.log(e));
    BadgeNumber.getBadge()
      .then((value: any) => {
        this.setState({
          status: 'native callback received',
          message: value,
        });
      })
      .catch((e) => console.log(e));
    Alert.alert(BadgeNumber.supported.toString());
    BadgeNumber.requestPermissions(null);
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>☆BadgeNumber example☆</Text>
        <Text style={styles.instructions}>STATUS: {this.state.status}</Text>
        <Text style={styles.welcome}>☆NATIVE CALLBACK MESSAGE☆</Text>
        <Text style={styles.instructions}>{this.state.message}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});