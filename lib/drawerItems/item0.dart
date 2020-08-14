import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:intl/intl.dart';
import 'package:shintaikan/getFirestoreData.dart';

class Item0 extends StatefulWidget {
  @override
  _Item0State createState() => _Item0State();
}

class _Item0State extends State<Item0> {
  final double spacing = 20.0;
  final double blueFontSize = 18.0;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(8),
      children: <Widget>[
        Text(
          "Karate Club Shintaikan e.V.",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline1,
        ),
        SizedBox(height: spacing),
        Text(
          "Linnéstr. 14, Freiburg West",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        Image.asset('assets/images/pelli.png'),
        FirestoreData(document: "status"),
        SizedBox(height: spacing),
        Container(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Align(
                alignment: Alignment.centerLeft,
                child: Image.asset('assets/images/icon_karate.png',
                    width: 100, height: 100),
              ),
              Align(
                alignment: Alignment.centerRight,
                child: Image.asset('assets/images/icon_karate_2.png',
                    width: 100, height: 100),
              ),
            ],
          ),
        ),
        SizedBox(height: spacing),
        Text(
          "Lieber Karateka\nMit der Installation unserer App erhälst du direkt alle Infos",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: spacing),
        Image.asset('assets/images/zeichen_kara.png', width: 120, height: 120),
        Image.asset('assets/images/zeichen_te.png', width: 120, height: 120),
        SizedBox(height: spacing),
        Text(
          "Kontakt:\nshintaikan@web.de",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText2,
        ),
      ],
    );
  }
}
