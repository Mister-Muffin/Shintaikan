import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:intl/intl.dart';

class Item7 extends StatefulWidget {
  @override
  _Item7State createState() => _Item7State();
}

class _Item7State extends State<Item7> {
  String last_changed = "";

  Future<String> getStatusFromFirestore() async {
    String html = "[null]";
    final DateFormat formatter = DateFormat('dd-MM-yyyy, HH:mm');
    Timestamp date;
    await Firestore.instance
        .collection('app')
        .document('vorfuehrungen')
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
        html(),
        Text(
          last_changed,
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w300,
            color: Colors.grey,
          ),
          textAlign: TextAlign.end,
        ),
        SizedBox(height: 60),
        Image.asset('assets/images/bambus.png'),
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
