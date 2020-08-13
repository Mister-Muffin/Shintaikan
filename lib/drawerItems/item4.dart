import 'package:flutter/material.dart';
import 'package:shintaikan/drawerItems/text.dart';

class Item4 extends StatefulWidget {
  @override
  _Item4State createState() => _Item4State();
}

class _Item4State extends State<Item4> {
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
          "Neue Trainingspläne nach den Sommerferien!",
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.headline2,
        ),
        SizedBox(height: 30),
        Text(
          TextNachSoFe().text1,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 30),
        Image.asset('assets/images/bambus.png'),
        SizedBox(height: 30),
        Text(
          TextNachSoFe().text2,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyText1,
        ),
        SizedBox(height: 30),
        Image.asset('assets/images/bambus.png'),
      ],
    );
  }
}
