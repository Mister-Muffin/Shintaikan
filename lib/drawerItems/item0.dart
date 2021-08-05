import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:http/http.dart';
import 'dart:convert' as convert;

class Item0 extends StatefulWidget {
  final bool connected;

  Item0({Key? key, required this.connected}) : super(key: key);

  @override
  _Item0State createState() => _Item0State();
}

class _Item0State extends State<Item0> with TickerProviderStateMixin {
  final double spacing = 20.0;
  final double blueFontSize = 18.0;

  // Duration _cacheValidDuration = Duration(minutes: 30);

  // DateTime _lastFetchTime = DateTime.fromMillisecondsSinceEpoch(0);

  // List<dynamic> _allRecords = [];

  @override
  void initState() {
    // getPosts();
    super.initState();

    _controllerReset = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 200),
    );
  }

  @override
  void dispose() {
    _controllerReset.dispose();
    super.dispose();
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

  //
  final TransformationController _transformationController =
      TransformationController();
  Animation<Matrix4>? _animationReset;
  late final AnimationController _controllerReset;

  void _onAnimateReset() {
    _transformationController.value = _animationReset!.value;
    if (!_controllerReset.isAnimating) {
      _animationReset!.removeListener(_onAnimateReset);
      _animationReset = null;
      _controllerReset.reset();
    }
  }

  void _animateResetInitialize() {
    _controllerReset.reset();
    _animationReset = Matrix4Tween(
      begin: _transformationController.value,
      end: Matrix4.identity(),
    ).animate(_controllerReset);
    _animationReset!.addListener(_onAnimateReset);
    _controllerReset.forward();
  }

  // Stop a running reset to home transform animation.
  void _animateResetStop() {
    _controllerReset.stop();
    _animationReset?.removeListener(_onAnimateReset);
    _animationReset = null;
    _controllerReset.reset();
  }

  // ignore: unused_element
  void _onInteractionStart(ScaleStartDetails details) {
    // If the user tries to cause a transformation while the reset animation is
    // running, cancel the reset animation.
    if (_controllerReset.status == AnimationStatus.forward) {
      _animateResetStop();
    }
  }

  // ignore: unused_element
  void _onInteractionEnd(ScaleEndDetails details) {
    _animateResetInitialize();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<dynamic>>(
        future: getPosts(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return RefreshIndicator(
                onRefresh: () {
                  setState(() {});
                  return getPosts();
                },
                child: ListView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  padding: const EdgeInsets.all(8),
                  children: <Widget>[
                    Part1(spacing: spacing),
                    ListView.separated(
                      //Wordpress content
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
                                    customImageRenders: {
                                      classAndIdMatcher(
                                        classToMatch:
                                            "flutter_html_custom_renderer",
                                      ): classAndIdRender(buildContext: context)
                                    },
                                    data: snapshot.data![index]["content"]
                                        ["rendered"])),
                          ],
                        );
                      },
                      separatorBuilder: (BuildContext context, int index) =>
                          const Divider(),
                    ), //--

                    Part2(spacing: spacing),
                    //FirestoreData(document: "status"),
                  ],
                ));
          } else if (snapshot.hasError) {
            if (!widget.connected) {
              return ListView(
                  padding: const EdgeInsets.all(8),
                  children: <Widget>[
                    Part1(spacing: spacing),
                    Icon(
                      Icons.cloud_off,
                      color: Colors.red,
                      size: 60,
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top: 16),
                      child: Text(
                        'Du bist Offline!\nDie Daten werden geladen, sobald du wieder Online bist...',
                        textAlign: TextAlign.center,
                        style: TextStyle(color: Colors.red),
                      ),
                    ),
                    Part2(spacing: spacing),
                  ]);
            } else {
              return Text(
                snapshot.error.toString(),
                style: TextStyle(color: Colors.red),
              );
            }
          } else {
            return Center(
                child: CircularProgressIndicator(
              valueColor: AlwaysStoppedAnimation(Colors.blue),
            ));
          }
        });
  }

  //Always returns true so every image is interactive
  ImageSourceMatcher classAndIdMatcher(
          {required String classToMatch, String? idToMatch}) =>
      (attributes, element) => true;
  // attributes["class"] != null &&
  // (attributes["class"]!.contains(classToMatch) ||
  //     attributes["id"]!.contains(idToMatch!));

  var snackbar = false;
  ImageRender classAndIdRender({required buildContext}) =>
      (context, attributes, element) {
        return InteractiveViewer(
          boundaryMargin: EdgeInsets.all(double.infinity),
          transformationController: _transformationController,
          // onInteractionStart: _onInteractionStart,
          // onInteractionEnd: _onInteractionEnd,
          child: GestureDetector(
            onTap: () {
              if (!snackbar) {
                snackbar = true;
                ScaffoldMessenger.of(buildContext)
                    .showSnackBar(SnackBar(
                      content: Text("Ziehe mit zwei Fingern zum Zoomen!"),
                    ))
                    .closed
                    .then((value) => snackbar = false);
              }
            },
            child: Image.network(
              attributes["src"] ?? "about:blank",
              loadingBuilder: (context, child, loadingProgress) {
                if (loadingProgress == null) return child;
                return Center(child: CircularProgressIndicator());
              },
              semanticLabel: attributes["longdesc"] ?? "",
              frameBuilder: (ctx, child, frame, _) {
                if (frame == null) {
                  return Text(attributes["alt"] ?? "",
                      style: context.style.generateTextStyle());
                }
                return child;
              },
            ),
          ),
        );
      };
}

class Part1 extends StatelessWidget {
  final double spacing;

  Part1({Key? key, required this.spacing}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(children: [
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
      SizedBox(height: spacing),
    ]);
  }
}

class Part2 extends StatelessWidget {
  final double spacing;

  Part2({Key? key, required this.spacing}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(children: [
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
      Image.asset('assets/images/zeichen_kara.png', width: 120, height: 120),
      Image.asset('assets/images/zeichen_te.png', width: 120, height: 120),
      SizedBox(height: spacing),
      Text(
        "Kontakt:\nshintaikan@web.de",
        textAlign: TextAlign.center,
        style: Theme.of(context).textTheme.bodyText2,
      ),
    ]);
  }
}
