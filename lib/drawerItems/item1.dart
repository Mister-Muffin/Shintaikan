import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:flutter_html/style.dart';
import 'package:intl/intl.dart';

class Item1 extends StatefulWidget {
  @override
  _Item1State createState() => _Item1State();
}

class _Item1State extends State<Item1> {
  double spacing = 20;
  String lastChanged = "";

  Future<String> getStatusFromFirestore() async {
    String html = "[null]";
    final DateFormat formatter = DateFormat('dd-MM-yyyy, HH:mm');
    Timestamp date;
    await Firestore.instance
        .collection('app')
        .document('trplan')
        .get()
        .then((DocumentSnapshot ds) {
      Map<String, dynamic> documentFields = ds.data.cast();

      html = documentFields['html'];
      date = documentFields['last_changed'];
      setState(() {
        lastChanged = formatter.format(date.toDate());
      });
    });
    return (html);
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String>(
      future: getStatusFromFirestore(),
      builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          child = ListView(
            padding: const EdgeInsets.all(8),
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
              SizedBox(height: spacing * 2),
              Html(data: snapshot.data, style: {
                "html": Style(
                  color: Color(0xff000066),
//              color: Colors.white,
                ),
              }),
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
          );
        } else if (snapshot.hasError) {
          child = Column(children: <Widget>[
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
          ]);
        } else {
          child = ListView(padding: const EdgeInsets.all(8), children: <Widget>[
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
            Center(child: CircularProgressIndicator()),
          ]);
        }
        return child;
      },
    );
  }
}
