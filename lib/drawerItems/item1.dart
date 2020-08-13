import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';

class Item1 extends StatefulWidget {
  @override
  _Item1State createState() => _Item1State();
}

class _Item1State extends State<Item1> {
  double spacing = 20;

  Future<String> getStatusFromFirestore() async {
    String status = "[null]";
    await Firestore.instance
        .collection('app')
        .document('trplan')
        .collection("karamini")
        .document("text")
        .get()
        .then((DocumentSnapshot ds) {
//TODO Get array from Cloud Firestore
    });
    return (status);
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
              Text(snapshot.data),
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
