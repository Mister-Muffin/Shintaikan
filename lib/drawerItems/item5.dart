import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:shintaikan/drawerItems/text.dart';

class Item5 extends StatefulWidget {
  @override
  _Item5State createState() => _Item5State();
}

class _Item5State extends State<Item5> {
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
        SizedBox(height: 20),
        Text(
          "GPS: [GPS LOC]",
          textAlign: TextAlign.center,
          style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 18.0,
              color: Colors.blue[800]),
        ),
        SizedBox(height: 40),
        Text(
          TextClub().text1,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 40),
        Text(
          "Wegbeschreibung",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 40),
        Text(
          TextClub().text2,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        Padding(
          padding: EdgeInsets.all(8),
          child: Image.asset('assets/images/map02.jpg'),
        ),
      ],
    );
  }
}
