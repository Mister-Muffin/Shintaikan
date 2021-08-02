import 'dart:async';

import 'package:connectivity/connectivity.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:package_info/package_info.dart';
import 'package:shintaikan/drawerItems/item0.dart';
import 'package:shintaikan/drawerItems/item1.dart';
import 'package:shintaikan/drawerItems/item2.dart';
import 'package:shintaikan/drawerItems/item3.dart';
import 'package:shintaikan/drawerItems/item4.dart';
import 'package:shintaikan/drawerItems/item5.dart';
import 'package:shintaikan/drawerItems/item6.dart';
import 'package:shintaikan/drawerItems/item7.dart';
import 'package:shintaikan/drawerItems/item8.dart';
import 'package:shintaikan/sendMail.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:webview_flutter/webview_flutter.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      debugShowCheckedModeBanner: false,
      routes: {
        Video.routeName: (context) => Video(),
      },
      theme: ThemeData(
          brightness: Brightness.light,
          primaryColor: Colors.lightBlue[800],
          accentColor: Colors.lightBlue[600],
          textTheme: TextTheme(
            headline1: TextStyle(
                fontSize: 40.0,
                fontWeight: FontWeight.bold,
                color: Colors.lightBlue[800]),
            headline2: TextStyle(
                fontSize: 30.0,
                fontWeight: FontWeight.bold,
                color: Colors.lightBlue[800]),
            bodyText1: TextStyle(
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
                color: Colors.lightBlue[900]),
            bodyText2: TextStyle(
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
                color: Color.fromRGBO(180, 0, 0, 1)),
          )),
      home: App(),
    );
  }
}

class App extends StatefulWidget {
  // Create the initialization Future outside of `build`:
  @override
  _AppState createState() => _AppState();
}

String appBarTitle = "Shintaikan";

class _AppState extends State<App> {
  /// The future is part of the state of our widget. We should not call `initializeApp`
  /// directly inside [build].
  final Future<FirebaseApp> _initialization = Firebase.initializeApp();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      // Initialize FlutterFire:
      future: _initialization,
      builder: (context, snapshot) {
        // Check for errors
        if (snapshot.hasError) {
          return Scaffold(
            body: Builder(
              builder: (context) => SafeArea(
                top: false,
                child: Scaffold(
                  appBar: AppBar(
                    title: Text(appBarTitle),
                    brightness: Brightness.dark,
                  ),
                  body: Center(
                    child: Column(
                      children: [
                        Icon(
                          Icons.error_outline,
                          color: Colors.red,
                          size: 60,
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: 16),
                          child: Text(
                            'Ein Fehler ist aufgetreten!',
                            textAlign: TextAlign.center,
                          ),
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ),
          );
        }

        // Once complete, show your application
        if (snapshot.connectionState == ConnectionState.done) {
          return Main();
        }

        // Otherwise, show something whilst waiting for initialization to complete
        return Scaffold(
          body: Builder(
            builder: (context) => SafeArea(
              top: false,
              child: Scaffold(
                appBar: AppBar(
                  title: Text(appBarTitle),
                  brightness: Brightness.dark,
                ),
                body: Center(
                  child: CircularProgressIndicator(),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}

class Main extends StatefulWidget {
  @override
  MyAppState createState() => MyAppState();
}

enum DrawerSelection { home, favorites, settings }
enum Qualities { low, medium }

Qualities _qualities = Qualities.low;

// ignore: non_constant_identifier_names
String GCMID = "";

class MyAppState extends State<Main> with TickerProviderStateMixin {
  Future<String> asyncFuture() async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    await messaging.getToken().then((value) => strToken = value.toString());
    buildNumber = packageInfo.buildNumber;
    appName = packageInfo.appName;
    return Future.value("Data successfully");
  }

  late AnimationController rotationController;
  late StreamSubscription subscription;

  bool initialized = false;
  bool initError = false;

  final listController = ScrollController();
  double develInfoContainerHeight =
      0; //height of the container at the bottom of the App Drawer
  String buildNumber = "";
  String appName = "name";
  String strToken = "...";

  final String messagingTopic = "push";

  int clip = 0;
  int clickedItem = 0;
  double webViewOpacity = 0;
  String url = "https://www.shintaikan.de/index-app.html";

  bool connectionIcon = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Builder(
        builder: (context) => SafeArea(
          top: false,
          child: Scaffold(
            drawer: myAppDrawer(context),
            appBar: AppBar(
              title: Text(appBarTitle),
              brightness: Brightness.dark,
              actions: <Widget>[
                connectionWidget(),
              ],
            ),
            body: Center(
              child: mainBody(),
            ),
          ),
        ),
      ),
    );
  }

  Widget connectionWidget() {
    if (!connectionIcon) {
      return Container();
    } else {
      return IconButton(
        icon: Icon(Icons.cloud_off_outlined),
        onPressed: () async => {
          showDialog<void>(
            context: context,
            barrierDismissible: true,
            builder: (BuildContext context) {
              return AlertDialog(
                title: Text('Keine Verbindung'),
                content: SingleChildScrollView(
                  child: ListBody(
                    children: <Widget>[
                      Icon(Icons.cloud_off_outlined, size: 48),
                      Text(
                        '\nDu hast keine Verbindung zum Internet!',
                        textAlign: TextAlign.center,
                      ),
                      Text(
                          '\nDie Daten in der App sind möglicherweise veraltet und aktualisieren sich, sobald du wieder Online gehst'),
                    ],
                  ),
                ),
                actions: <Widget>[
                  TextButton(
                    child: Text('Okay'),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  ),
                ],
              );
            },
          ),
        },
      );
    }
  }

  Widget mainBody() {
    if (clickedItem == 0)
      return (Item0(
        connected: !connectionIcon,
      ));
    else if (clickedItem == 1)
      return (Item1());
    else if (clickedItem == 2)
      return (Item2());
    else if (clickedItem == 3)
      return (Item3());
    else if (clickedItem == 4)
      return (Item4());
    else if (clickedItem == 5)
      return (Item5());
    else if (clickedItem == 6)
      return (Item6());
    else if (clickedItem == 7)
      return (Item7());
    else if (clickedItem == 8)
      return (Item8());
    else
      return (Item0(connected: !connectionIcon));
  }

  void showAlertDialog(BuildContext context) {
    Future<void> _showMyDialog() async {
      return showDialog<void>(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text('Clip starten'),
            content: SingleChildScrollView(
              child: ListBody(
                children: <Widget>[
                  ListTile(
                    title: const Text('Niedrige Qualität'),
                    leading: Radio(
                      value: Qualities.low,
                      groupValue: _qualities,
                      onChanged: (Qualities? value) {
                        setState(() {
                          _qualities = value!;
                        });
                      },
                    ),
                  ),
                  ListTile(
                    title: const Text('Mittlere Qualität'),
                    leading: Radio(
                      value: Qualities.medium,
                      groupValue: _qualities,
                      onChanged: (Qualities? value) {
                        setState(() {
                          _qualities = value!;
                        });
                      },
                    ),
                  ),
                ],
              ),
            ),
            actions: <Widget>[
              OutlinedButton(
                child: Text('Abbrechen'),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
              ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pop();
                  String url = "https://www.shintaikan.de";
                  if (_qualities == Qualities.low) {
                    if (clip == 0) {
                      url =
                          "https://shintaikan.de/content/filme/seefestfilm2019_25.mp4";
                    } else if (clip == 1) {
                      url =
                          "https://shintaikan.de/content/filme/mixfilm2019_25.mp4";
                    }
                  } else {
                    if (clip == 0) {
                      url =
                          "https://shintaikan.de/content/filme/seefestfilm2019_50.mp4";
                    } else if (clip == 1) {
                      url =
                          "https://shintaikan.de/content/filme/mixfilm2019_50.mp4";
                    }
                  }
                  Navigator.pushNamed(
                    context,
                    Video.routeName,
                    arguments: ScreenArguments(url),
                  );
                },
                // textColor: Colors.white,
                // padding: const EdgeInsets.all(0.0),
                child: Container(
                  padding: const EdgeInsets.all(10.0),
                  child: const Text('Film ab!'),
                ),
              ),
            ],
          );
        },
      );
    }

    _showMyDialog();
  }

  void showInfoDialog(BuildContext context, String text) {
    Future<void> _showMyDialog() async {
      return showDialog<void>(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext context) {
          return AlertDialog(
            content: Text(text),
            actions: <Widget>[
              TextButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: const Text('Ok'),
              ),
            ],
          );
        },
      );
    }

    _showMyDialog();
  }

  FirebaseMessaging messaging = FirebaseMessaging.instance;

  void initializeFlutterFire() async {
    try {
      // Wait for Firebase to initialize and set `_initialized` state to true
      await Firebase.initializeApp();
      setState(() {
        initialized = true;
      });
    } catch (e) {
      // Set `_error` state to true if Firebase initialization fails
      setState(() {
        initError = true;
      });
    }
    firebaseCloudMessagingListeners();
  }

  @override
  void initState() {
    initializeFlutterFire();
    rotationController = AnimationController(
        duration: const Duration(milliseconds: 1000), vsync: this);
    // Network listener
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      setState(() {
        connectionIcon = result == ConnectivityResult.none;
      });
    });

    super.initState();
  }

  @override
  void dispose() {
    super.dispose();

    subscription.cancel();
  }

  void firebaseCloudMessagingListeners() {
    messaging.setAutoInitEnabled(true);

    messaging.subscribeToTopic(messagingTopic);
  }

  Widget myAppDrawer(BuildContext context) {
    DrawerSelection _drawerSelection = DrawerSelection.home;
    return Drawer(
        child: ListView(
            controller: listController,
            padding: EdgeInsets.zero,
            children: <Widget>[
          DrawerHeader(
            child: Image.asset('assets/images/pelli.png'),
          ),
          ListTile(
            leading: Icon(Icons.info_outlined),
            title: Text('Start',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("https://shintaikan.de/index-app.html");
              setState(() {
                clickedItem = 0;
                appBarTitle = "Shintaikan";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.date_range_outlined),
            title: Text('Trainingsplan',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/trplan-app.html");
              setState(() {
                clickedItem = 1;
                appBarTitle = "Trainingsplan";
              });
              Navigator.pop(context);
            },
            selected: _drawerSelection == DrawerSelection.favorites,
          ),
          ListTile(
            leading: Icon(Icons.call_made_outlined),
            title: Text('Gürtelprüfungen',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/pruefung-app.html");
              setState(() {
                clickedItem = 2;
                appBarTitle = "Gürtelprüfungen";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.beach_access_outlined),
            title: Text('Ferientraining',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/ferien-app.html");
              setState(() {
                clickedItem = 3;
                appBarTitle = "Ferientraining";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.wb_sunny_outlined),
            title: Text('Nach den Sommerferien',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/nach_sofe-app.html");
              setState(() {
                clickedItem = 4;
                appBarTitle = "Nach den Sommerferien";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.home_outlined),
            title: Text('Der Club/Wegbeschreibung',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/club-app.html");
              setState(() {
                clickedItem = 5;
                appBarTitle = "Der Club/Wegbeschreibung";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.directions_walk_outlined),
            title: Text('Anfänger/Interessenten',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/interessenten-app.html");
              setState(() {
                clickedItem = 6;
                appBarTitle = "Anfänger/Interessenten";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.remove_red_eye_outlined),
            title: Text('Vorführungen',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/show-app.html");

              setState(() {
                clickedItem = 7;
                appBarTitle = "Vorführungen";
              });
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.people_outline),
            title: Text('Lehrgänge + Turniere',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              //controller.loadUrl("http://shintaikan.de/lehrgang-app.html");
              setState(() {
                clickedItem = 8;
                appBarTitle = "Lehrgänge + Turniere";
              });
              Navigator.pop(context);
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.movie_outlined),
            title: Text('Infofilmchen',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              Navigator.pushNamed(
                context,
                Video.routeName,
                arguments:
                    ScreenArguments("https://shintaikan.de/Shintaikanfilm.mp4"),
              );
            },
          ),
          ListTile(
            leading: Icon(Icons.movie_outlined),
            title: Text('Seefest 2019',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              Navigator.pop(context);
              clip = 0;
              showAlertDialog(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.movie_outlined),
            title: Text('Mixfilm 2019',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              Navigator.pop(context);
              clip = 1;
              showAlertDialog(context);
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.mail_outlined),
            title: Text('Kontakt & Feedback',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => SendMail()),
              );
            },
          ),
          ListTile(
            leading: Icon(Icons.attach_file_outlined),
            trailing: Icon(Icons.open_in_new_outlined),
            title: Text('Impressum',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              loadBrowser("https://shintaikan.de/?page_id=207");
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.lock_outlined),
            trailing: Icon(Icons.open_in_new_outlined),
            title: Text('Datenschutz',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              loadBrowser("https://shintaikan.de/?page_id=378");
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.tag_faces_outlined),
            title: Text('Weiteres',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              Navigator.pop(context);
              showInfoDialog(context,
                  "Was Rüdiger noch sagen wollte:\nTiefer stehen, schneller schlagen! :)");
            },
          ),
          ListTile(
            leading: Icon(Icons.info_outlined),
            title: Text('Über',
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w500,
                    color: Colors.black)),
            onTap: () {
              showAboutDialog(
                  context: context,
                  applicationVersion: "Version: " + buildNumber,
                  applicationIcon: Container(
                    height: 50,
                    width: 50,
                    child: Image.asset('assets/images/pelli.png'),
                  ),
                  children: [
                    OutlinedButton(
                      onPressed: () {
                        loadBrowser(
                            "https://github.com/Mister-Muffin/Shintaikan");
                      },
                      child: Text("GitHub"),
                      style: OutlinedButton.styleFrom(
                        side: BorderSide(color: Colors.black),
                        elevation: 0,
                        primary: Colors.black,
                      ),
                    )
                  ],
                  applicationName: appName);
            },
          ),
          Divider(),
          Center(
              child: IconButton(
            icon: FlutterLogo(),
            onPressed: () {
              setState(() {
                develInfoContainerHeight = 80.0;
              });
              Future.delayed(const Duration(milliseconds: 500), () {
                listController.animateTo(
                  listController.position.maxScrollExtent,
                  duration: Duration(seconds: 1),
                  curve: Curves.fastOutSlowIn,
                );
              });
            },
          )),
          Container(
            alignment: Alignment.center,
            height: develInfoContainerHeight,
            child: FutureBuilder(
                future: asyncFuture(),
                builder:
                    (BuildContext context, AsyncSnapshot<String> snapshot) {
                  return (Column(children: <Widget>[
                    SizedBox(height: 5),
                    Text(
                      buildNumber,
                      style: TextStyle(
                        color: Colors.black.withOpacity(0.5),
                        fontSize: 10,
                      ),
                    ),
                    TextButton(
                        onPressed: () {
                          Navigator.pop(context);
                          Clipboard.setData(new ClipboardData(text: strToken))
                              .then((value) {
                            final snackBar = SnackBar(
                              content: Text('In die Zwischenablage kopiert'),
                            );
                            ScaffoldMessenger.of(context)
                                .showSnackBar(snackBar);
                          });
                        },
                        child: Text(
                          strToken,
                          style: TextStyle(
                            color: Colors.black.withOpacity(0.5),
                            fontSize: 8,
                          ),
                        ))
                  ]));
                }),
          ),
        ]));
  }

  void loadBrowser(String url) async {
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }
}

class ScreenArguments {
  final String url;

  ScreenArguments(this.url);
}

class Video extends StatelessWidget {
  static const routeName = '/video';

  @override
  Widget build(BuildContext context) {
    final ScreenArguments args =
        ModalRoute.of(context)!.settings.arguments as ScreenArguments;
    return Scaffold(
        appBar: AppBar(
          title: null,
          backgroundColor: Colors.black,
        ),
        body: Center(
          child: WebView(
            javascriptMode: JavascriptMode.unrestricted,
            initialUrl: args.url,
          ),
        ));
  }
}
