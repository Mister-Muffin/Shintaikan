import 'package:flutter/material.dart';
import 'package:shintaikan/getFirestoreData.dart';

class Item8 extends StatefulWidget {
  @override
  _Item8State createState() => _Item8State();
}

class _Item8State extends State<Item8> {
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
        SizedBox(height: 40),
        Text(
          "Lehrgänge & Turniere!",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 60),
        FirestoreData(document: "turniere", color: 0xff000066),
        SizedBox(height: 60),
        Text(
          "Die Auschreibungen\nhängen auch im Dojo!",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText2,
        ),
        Image.asset('assets/images/bonsai.png'),
      ],
    );
  }
}
