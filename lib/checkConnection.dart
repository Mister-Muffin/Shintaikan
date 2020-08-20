import 'package:connectivity/connectivity.dart';

class CheckConnection {
  Future<bool> isConnected() async {
    var connectivityResult = await (Connectivity().checkConnectivity());

    if (connectivityResult == ConnectivityResult.mobile ||
        connectivityResult == ConnectivityResult.wifi) {
      // I am connected to a network.
      return true;
    } else {
      return false;
    }
  }
}
