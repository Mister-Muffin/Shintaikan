import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:shintaikan/getFirestoreData.dart';
import 'package:http/http.dart';
import 'dart:convert' as convert;

class Item0 extends StatefulWidget {
  @override
  _Item0State createState() => _Item0State();
}

class _Item0State extends State<Item0> {
  final double spacing = 20.0;
  final double blueFontSize = 18.0;

  @override
  void initState() {
    super.initState();

    getPosts();
  }

  Future<List<dynamic>> getPosts() async {
    Response res =
        await get(Uri.parse("https://shintaikan.de/?rest_route=/wp/v2/posts"));

    if (res.statusCode == 200) {
      final List<dynamic> jsonResponse = convert.json.decode(res.body);

      return jsonResponse;
    } else {
      throw "Unable to retrieve posts.";
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<dynamic>>(
        future: getPosts(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
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
                ListView.separated(
                  physics: NeverScrollableScrollPhysics(),
                  scrollDirection: Axis.vertical,
                  shrinkWrap: true,
                  padding: const EdgeInsets.all(8),
                  itemCount: snapshot.data!.length,
                  itemBuilder: (BuildContext context, int index) {
                    return Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        Align(
                            alignment: Alignment.center,
                            child: Text(snapshot.data![index]["title"]
                                    ["rendered"]
                                .toString())),
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Html(
                                data: snapshot.data![index]["content"]
                                    ["rendered"])),
                      ],
                    );
                  },
                  separatorBuilder: (BuildContext context, int index) =>
                      const Divider(),
                ),
                //Text(snapshot.data![0]["id"].toString()),
                //FirestoreData(document: "status"),
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
                Image.asset('assets/images/zeichen_kara.png',
                    width: 120, height: 120),
                Image.asset('assets/images/zeichen_te.png',
                    width: 120, height: 120),
                SizedBox(height: spacing),
                Text(
                  "Kontakt:\nshintaikan@web.de",
                  textAlign: TextAlign.center,
                  style: Theme.of(context).textTheme.bodyText2,
                ),
              ],
            );
          } else if (snapshot.hasError) {
            return Text(
              snapshot.error.toString(),
              style: TextStyle(color: Colors.red),
            );
          }

          return CircularProgressIndicator(
            valueColor: AlwaysStoppedAnimation(Colors.blue),
          );
        });
  }
}