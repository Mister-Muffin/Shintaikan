import 'package:flutter/material.dart';
import 'package:shintaikan/getFirestoreData.dart';

class Item1 extends StatefulWidget {
  @override
  _Item1State createState() => _Item1State();
}

class _Item1State extends State<Item1> {
  final double spacing = 20;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: EdgeInsets.all(8),
      children: <Widget>[
        Align(
          alignment: Alignment.centerRight,
          child: Image.asset('assets/images/kaempfer-app.png',
              width: 120, height: 120),
        ),
        SizedBox(height: spacing),
        Text(
          "Trainingsplan",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: spacing),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: spacing),
        FirestoreData(document: "trplan"),
        SizedBox(height: spacing),
        Image.asset('assets/images/bambus.png'),
      ],
    );
  }
}
