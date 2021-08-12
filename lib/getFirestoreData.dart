import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:flutter_html/style.dart';
import 'package:intl/intl.dart';
import 'package:shintaikan/checkConnection.dart';

class FirestoreData extends StatefulWidget {
  final String document;
  final int color;
  FirestoreData({required this.document, this.color = 0x000000});

  @override
  _FirestoreDataState createState() =>
      _FirestoreDataState(document: this.document, color: this.color);
}

class _FirestoreDataState extends State<FirestoreData> {
  final String document;
  final int color;

  String lastChanged = "";

  bool isOnline = false;
  final DateFormat formatter = DateFormat('dd-MM-yyyy, HH:mm');

  _FirestoreDataState({required this.document, this.color = 0x000000});

  Future<Map<String, dynamic>> getStatusFromFirestore(String document) async {
    Map<String, dynamic> documentFields = new Map<String, dynamic>();
    documentFields['html'] = "<h1>ERROR!</h1>";
    documentFields['last_changed'] = "<h1>ERROR!</h1>";

    await FirebaseFirestore.instance
        .collection('app')
        .doc(document)
        .get()
        .then((DocumentSnapshot ds) {
      documentFields = ds.data() as Map<String, dynamic>;
    });
    isOnline = await CheckConnection().isConnected();

    return documentFields;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        FutureBuilder<Map<String, dynamic>>(
          future: getStatusFromFirestore(document),
          builder: (BuildContext context,
              AsyncSnapshot<Map<String, dynamic>> snapshot) {
            List<Widget> children;
            if (snapshot.hasData) {
              children = <Widget>[
                Column(children: <Widget>[
                  Html(
                      data: snapshot.data!['html'],
                      style: {"html": Style(color: Color(color))}),
                  Text(
                    formatter.format(snapshot.data!['last_changed'].toDate()),
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w300,
                      color: Colors.grey,
                    ),
                    textAlign: TextAlign.end,
                  ),
                ]),
              ];
            } else if (snapshot.hasError && !isOnline) {
              children = <Widget>[
                Icon(
                  Icons.cloud_off,
                  color: Colors.red,
                  size: 60,
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 16),
                  child: Text(
                    'Du bist Offline!\nBitte starte die App neu, sobald du wieder Online bist..',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.red),
                  ),
                )
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
        )
      ],
    );
  }
}
