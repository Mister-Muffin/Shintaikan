import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:intl/intl.dart';

class Item2 extends StatefulWidget {
  @override
  _Item2State createState() => _Item2State();
}

class _Item2State extends State<Item2> {
  final double spacing = 40;
  Timestamp date;
  String last_changed = "";
  final DateFormat formatter = DateFormat('dd-MM-yyyy, H:m');
  Future<String> getStatusFromFirestore() async {
    String html = "[null]";
    await Firestore.instance
        .collection('app')
        .document('pruefungen')
        .get()
        .then((DocumentSnapshot ds) {
      Map<String, dynamic> documentFields = ds.data.cast();

      html = documentFields['html'];
      date = documentFields['last_changed'];
      setState(() {
        last_changed = formatter.format(date.toDate());
      });
    });
    return (html);
  }

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
        html(),
        Align(
          alignment: Alignment.centerRight,
          child: Text(last_changed,
            style: TextStyle(
                fontSize: 15,
                fontWeight: FontWeight.normal,
                color: Colors.grey),),),
        SizedBox(height: spacing),
        Image.asset('assets/images/seerose1.png'),
      ],
    );
  }

  Widget html() {
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
}
