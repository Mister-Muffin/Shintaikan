import 'package:cloud_functions/cloud_functions.dart';
import 'package:flutter/material.dart';
import 'package:outline_material_icons/outline_material_icons.dart';

class SendMail extends StatefulWidget {
  static const routeName = '/mail';
  @override
  _SendMailState createState() => _SendMailState();
}

class _SendMailState extends State<SendMail> {
  String subject = "Kein Betreff angegeben";
  String email = "";
  String message = "";

  bool showStatusWidget = false;
  bool success = false;
  String cloudFuncReturnStatus = "";

  final HttpsCallable callable = CloudFunctions(region: 'europe-west1').getHttpsCallable(
    functionName: 'sendEmail',
  );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Kontakt & Feedback"),
        actions: [IconButton(icon: Icon(OMIcons.send), onPressed: sendMessage)],
      ),
      body: Padding(
        padding: const EdgeInsets.all(10),
        child: Column(mainAxisSize: MainAxisSize.max, children: [
          Text("Hier kannst du die Geschäftsstelle, Silvia kontaktieren:",
              style: TextStyle(
                color: Colors.black87,
                fontSize: 14,
                fontWeight: FontWeight.normal,
              )),
          TextFormField(
            decoration: InputDecoration(labelText: 'Betreff'),
            onChanged: (String value) {
              subject = value;
            },
          ),
          TextFormField(
            decoration: InputDecoration(labelText: 'Deine Email'),
            onChanged: (String value) {
              setState(() {
                email = value;
              });
            },
            validator: (String value) {
              return value.contains('@') ? null : 'Ungültige E-Mail Adresse';
            },
          ),
          TextFormField(
            decoration: InputDecoration(labelText: 'Nachicht - Pflichtfeld'),
            onChanged: (String value) {
              message = value;
            },
          ),
          Container(
            height: 20,
          ),
          noEmailWarning(),
          messageStatusWidget(),
        ]),
      ),
    );
  }

  void sendMessage() async {
    try {
      if (email.isEmpty) email = "[Keine Email angegeben]";
      final HttpsCallableResult result = await callable.call(
        <String, dynamic>{
          'email': email,
          'subject': subject,
          'message': message
        },
      );
      print(result.data);
      setState(() {
        cloudFuncReturnStatus = result.data.toString();
        success = true;
        showStatusWidget = true;
      });
    } on CloudFunctionsException catch (e) {
      print('caught firebase functions exception');
      print(e.code);
      print(e.message);
      print(e.details);
      setState(() {
        success = false;
        showStatusWidget = true;
      });
    } catch (e) {
      print('caught generic exception');
      print(e);
      setState(() {
        success = false;
        showStatusWidget = true;
      });
    }
  }

  Widget noEmailWarning() {
    if (email.isEmpty) {
      return Row(
        children: [
          Icon(
            OMIcons.warning,
            color: Colors.orange,
          ),
          Text('Ohne Email können wir dir nicht Antworten.',
              style: TextStyle(
                color: Colors.black,
                fontSize: 14,
                fontWeight: FontWeight.normal,
              ),
              textAlign: TextAlign.center)
        ],
      );
    } else {
      return Container();
    }
  }

  Widget messageStatusWidget() {
    if (showStatusWidget) {
      if (success) {
        return Row(
          children: [
            Icon(
              OMIcons.done,
              color: Colors.green,
            ),
            Text('Die Nachicht wurde versandt!',
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 14,
                  fontWeight: FontWeight.normal,
                ),
                textAlign: TextAlign.center)
          ],
        );
      } else {
        return Row(
          children: [
            Icon(
              OMIcons.error,
              color: Colors.red,
            ),
            Text('Ein Fehler ist aufgetreten!',
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 14,
                  fontWeight: FontWeight.normal,
                ),
                textAlign: TextAlign.center)
          ],
        );
      }
    } else {
      return Container();
    }
  }
}
