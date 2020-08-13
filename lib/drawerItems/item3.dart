import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class Item3 extends StatefulWidget {
  @override
  _Item3State createState() => _Item3State();
}

class _Item3State extends State<Item3> {
  @override
  Widget build(BuildContext context) {
    return WebView(
      initialUrl: "http://shintaikan.de/ferien-app.html",
      javascriptMode: JavascriptMode.unrestricted,
    );
  }
}
