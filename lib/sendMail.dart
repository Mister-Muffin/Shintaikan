import 'package:cloud_functions/cloud_functions.dart';
import 'package:flutter/material.dart';
import 'package:outline_material_icons/outline_material_icons.dart';

//TODO: PLS HELP! 😥
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
  bool loading = false;
  String cloudFuncReturnStatus = "";
//TODO: Futurebuilder?
  final HttpsCallable callable = FirebaseFunctions
      .instanceFor(region: 'europe-west1')
      .httpsCallable('sendEmail');

  @override
  Widget build(BuildContext context) {
    final formKey = GlobalKey<FormState>();
    return Scaffold(
      appBar: AppBar(
        title: Text("Kontakt & Feedback"),
        actions: [
          IconButton(
              icon: Icon(OMIcons.send),
              onPressed: () {
                sendMessage(formKey);
              })
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: SingleChildScrollView(
          child: Form(
            key: formKey,
            child: Column(mainAxisSize: MainAxisSize.max, children: [
              Text("Hier kannst du uns kontaktieren:",
                  style: TextStyle(
                    color: Colors.black87,
                    fontSize: 14,
                    fontWeight: FontWeight.normal,
                  )),
              TextFormField(
                decoration: InputDecoration(labelText: 'Deine Email-Adresse *'),
                /* onChanged: (String value) {
                  setState(() {
                    email = value;
                  });
                },*/
                validator: (String? value) {
                  if (value.toString().isEmpty) return "ALLA!";

                  bool emailValid = RegExp(
                          r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                      .hasMatch(value!);
                  return emailValid ? null : 'NE! So nich';
                },
              ),
              TextFormField(
                decoration: InputDecoration(labelText: 'Betreff'),
                onChanged: (String value) {
                  subject = value;
                },
              ),
              TextFormField(
                decoration: InputDecoration(labelText: 'Nachicht *'),
                minLines: 1,
                maxLines: 20,
                maxLength: 500,
                maxLengthEnforced: false,
                onChanged: (String value) {
                  message = value;
                },
                validator: (String? value) {
                  return value.toString().isNotEmpty ? null : "is leer :(";
                },
              ),
              Container(
                height: 20,
              ),
              messageStatusWidget(),
            ]),
          ),
        ),
      ),
    );
  }

  void sendMessage(GlobalKey<FormState> formKey) async {
    if (!formKey.currentState!.validate()) return;
    setState(() {
      showStatusWidget = true;
      loading = true;
    });
    try {
      if (email.isEmpty) email = "[Keine Email angegeben]";
      await callable.call(
        <String, dynamic>{
          'email': email,
          'subject': subject,
          'message': message
        },
      );
      setState(() {
        success = true;
        showStatusWidget = true;
      });
    } on Exception catch (e) {
      print('caught firebase functions exception');
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
    setState(() {
      loading = false;
    });
  }

  Widget messageStatusWidget() {
    if (!showStatusWidget) {
      return Container();
    } else {
      if (loading) {
        return Row(
          children: [
            CircularProgressIndicator(),
            Text('Email wird gesendet...',
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 14,
                  fontWeight: FontWeight.normal,
                ),
                textAlign: TextAlign.center)
          ],
        );
      } else {
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
              Text(
                  'Ein Fehler ist aufgetreten!\nSollte das Problem wiederholt auftreten,\nschreib bitte eine Mail an shintaikan@web.de.',
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: 14,
                    fontWeight: FontWeight.normal,
                  ),
                  textAlign: TextAlign.justify)
            ],
          );
        }
      }
    }
  }
}