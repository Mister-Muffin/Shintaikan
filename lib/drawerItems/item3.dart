import 'package:flutter/material.dart';
import 'package:shintaikan/getFirestoreData.dart';
import 'package:webview_flutter/webview_flutter.dart';

class Item3 extends StatefulWidget {
  @override
  _Item3State createState() => _Item3State();
}

class _Item3State extends State<Item3> {
  final double spacing = 40;
  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: EdgeInsets.all(8),
      children: <Widget>[
        Text(
          "Karate Club Shintaikan e.V.",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline1,
        ),
        SizedBox(height: spacing),
        Text(
          "Ferientraining",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: spacing),
        FirestoreData(document: "ferientraining", color: 0xff000066),
        SizedBox(height: spacing),
        Image.asset('assets/images/sakura.png'),
      ],
    );
  }
}
