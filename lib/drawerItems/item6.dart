import 'package:flutter/material.dart';
import 'package:shintaikan/drawerItems/text.dart';

class Item6 extends StatefulWidget {
  @override
  _Item6State createState() => _Item6State();
}

class _Item6State extends State<Item6> {
  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(8),
      children: <Widget>[
        Text("Karate Club Shintaikan e.V.",
            textAlign: TextAlign.center,
            style: Theme.of(context).textTheme.headline1),
        SizedBox(height: 20),
        Text(
          "Anfänger und Interessenten",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 20),
        Text(
          "Generell sind 2 Schnupperstunden gratis!",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 20),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 20),
        Text(
          "Karaminis",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text1,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text3,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 40),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 40),
        Text(
          "Kinder Karate Anfänger:",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text4,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 20),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 20),
        Text(
          "Jugend Karate:",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text5,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 20),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 20),
        Text(
          "Karate Erwachsene:",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text6,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 20),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 20),
        Text(
          "Beiträge und Zahlweise:",
          textAlign: TextAlign.center,
          style: TextStyle(
              fontWeight: FontWeight.bold,
              fontStyle: FontStyle.italic,
              fontSize: 30.0,
              color: Colors.blue[900]),
        ),
        SizedBox(height: 20),
        Text(
          TextAbout().text7,
          style: Theme.of(context).textTheme.bodyText1,
        ),
      ],
    );
  }
}
