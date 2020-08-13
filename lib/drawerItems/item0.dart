import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:intl/intl.dart';

class Item0 extends StatefulWidget {
  @override
  _Item0State createState() => _Item0State();
}

class _Item0State extends State<Item0> {
  final double spacing = 20.0;
  final double blueFontSize = 18.0;

  String lastChanged = "";

  Future<String> getStatusFromFirestore() async {
    String status = "[null]";
    final DateFormat formatter = DateFormat('dd-MM-yyyy, HH:mm');
    Timestamp date;
    await Firestore.instance
        .collection('app')
        .document('status')
        .get()
        .then((DocumentSnapshot ds) {
      Map<String, dynamic> documentFields = ds.data.cast();

      status = documentFields['info'];
      date = documentFields['last_changed'];
      setState(() {
        lastChanged = formatter.format(date.toDate());
      });
    });
    return (status);
  }

  Widget status() {
    return FutureBuilder<String>(
      future: getStatusFromFirestore(),
      builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
        List<Widget> children;
        if (snapshot.hasData) {
          children = <Widget>[
            Html(
              data: snapshot.data,
            ),
          ];
        } else if (snapshot.hasError) {
          children = <Widget>[
            Icon(
              Icons.error_outline,
              color: Colors.red,
              size: 60,
            ),
            Padding(
              padding: const EdgeInsets.only(top: 16),
              child: Text(
                'Error: ${snapshot.error}',
                textAlign: TextAlign.center,
              ),
            )
          ];
        } else {
          children = <Widget>[
            CircularProgressIndicator(),
            const Padding(
              padding: EdgeInsets.only(top: 16),
              child: Text("Lädt..."),
            )
          ];
        }
        return Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: children,
          ),
        );
      },
    );
  }

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
        Card(
            child: Column(
              children: <Widget>[
                status(),
                Text(
                  lastChanged,
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w300,
                    color: Colors.grey,
                  ),
                  textAlign: TextAlign.end,
                ),
              ],
            )),
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
