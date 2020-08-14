import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:flutter_html/style.dart';
import 'package:intl/intl.dart';

class FirestoreData extends StatefulWidget {
  final String document;
  final int color;
  FirestoreData({@required this.document, this.color});

  @override
  _FirestoreDataState createState() =>
      _FirestoreDataState(document: this.document, color: this.color);
}

class _FirestoreDataState extends State<FirestoreData> {
  final String document;
  final int color;

  String lastChanged = "";

  _FirestoreDataState({@required this.document, this.color});

  Future<String> getStatusFromFirestore(String document) async {
    String status = "[null]";
    final DateFormat formatter = DateFormat('dd-MM-yyyy, HH:mm');
    Timestamp date;
    await Firestore.instance
        .collection('app')
        .document(document)
        .get()
        .then((DocumentSnapshot ds) {
      Map<String, dynamic> documentFields = ds.data.cast();

      status = documentFields['html'];
      date = documentFields['last_changed'];
      setState(() {
        lastChanged = formatter.format(date.toDate());
      });
    });
    return (status);
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        html(document, color),
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
  }

  Widget html(String document, int color) {
    return FutureBuilder<String>(
      future: getStatusFromFirestore(document),
      builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
        List<Widget> children;
        if (snapshot.hasData) {
          children = <Widget>[
            Container(
              child: color == null
                  ? new Container(
                      child: Html(
                      data: snapshot.data,
                    ))
                  : new Container(
                      child: Html(
                          data: snapshot.data,
                          style: {"html": Style(color: Color(color))}),
                    ),
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
