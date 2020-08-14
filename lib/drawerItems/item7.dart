import 'package:flutter/material.dart';
import 'package:shintaikan/getFirestoreData.dart';

class Item7 extends StatefulWidget {
  @override
  _Item7State createState() => _Item7State();
}

class _Item7State extends State<Item7> {
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
        Text(
          "Karate Club Shintaikan e.V.",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline1,
        ),
        SizedBox(height: 60),
        Text(
          "Vorführungen",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 60),
        FirestoreData(document: "vorfuehrungen"),
        SizedBox(height: 60),
        Image.asset('assets/images/bambus.png'),
      ],
    );
  }
}
