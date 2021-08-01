import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_wordpress/flutter_wordpress.dart' as wp;
import 'package:shintaikan/getFirestoreData.dart';

class Item0 extends StatefulWidget {
  final wp.WordPress wordPress;

  const Item0({required Key key, required this.wordPress}) : super(key: key);

  @override
  _Item0State createState() => _Item0State();
}

class _Item0State extends State<Item0> {
  final double spacing = 20.0;
  final double blueFontSize = 18.0;

  late Future<List<wp.Post>> posts;

  @override
  void initState() {
    super.initState();

    fetchPosts();
  }

  Future<void> fetchPosts() {
    setState(() {
      posts = widget.wordPress.fetchPosts(
        postParams: wp.ParamsPostList(perPage: 1),
        fetchAuthor: true,
        fetchFeaturedMedia: true,
      );
    });
    return posts;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<wp.Post>>(
        future: posts,
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
                FirestoreData(document: "status"),
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
