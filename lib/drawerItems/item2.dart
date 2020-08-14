import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:shintaikan/getFirestoreData.dart';

class Item2 extends StatefulWidget {
  @override
  _Item2State createState() => _Item2State();
}

class _Item2State extends State<Item2> {
  final double spacing = 40;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(8),
      children: <Widget>[
        Align(
          alignment: Alignment.centerRight,
          child: Image.asset('assets/images/kaempfer-app.png',
              width: 120, height: 120),
        ),
        SizedBox(height: spacing),
        Text(
          "Gürtelprüfungen",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: spacing),
        FirestoreData(document: "pruefungen"),
        SizedBox(height: spacing),
        Image.asset('assets/images/seerose1.png'),
      ],
    );
  }
}
