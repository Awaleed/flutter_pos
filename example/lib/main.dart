import 'dart:typed_data';
import 'package:esc_pos_utils/esc_pos_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_pos/column_maker.dart';
import 'package:flutter_pos/enums.dart';
import 'dart:async';

import 'package:flutter_pos/flutter_pos.dart';
import 'package:flutter_pos/sunmi_style.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitDown, DeviceOrientation.portraitUp]);
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Sunmi Printer',
        theme: ThemeData(
          primaryColor: Colors.black,
        ),
        debugShowCheckedModeBanner: false,
        home: const Home());
  }
}

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  bool printBinded = false;

  @override
  void initState() {
    super.initState();

    _bindingPrinter().then((bool? isBind) async {
      // Pos.paperSize().then((int size) {
      //   setState(() {
      //     paperSize = size;
      //   });
      // });

      // Pos.printerVersion().then((String version) {
      //   setState(() {
      //     printerVersion = version;
      //   });
      // });

      // Pos.serialNumber().then((String serial) {
      //   setState(() {
      //     serialNumber = serial;
      //   });
      // });

      setState(() {
        printBinded = isBind!;
      });
    });
  }

  /// must binding ur printer at first init in app
  Future<bool?> _bindingPrinter() async {
    final bool? result = await Pos.bindingPrinter();
    return result;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Sunmi printer Example'),
        ),
        body: SingleChildScrollView(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(
                  top: 10,
                ),
                child: Text("Print binded: " + printBinded.toString()),
              ),
              // Padding(
              //   padding: const EdgeInsets.symmetric(vertical: 2.0),
              //   child: Text("Paper size: " + paperSize.toString()),
              // ),
              // Padding(
              //   padding: const EdgeInsets.symmetric(vertical: 2.0),
              //   child: Text("Serial number: " + serialNumber),
              // ),
              // Padding(
              //   padding: const EdgeInsets.symmetric(vertical: 2.0),
              //   child: Text("Printer version: " + printerVersion),
              // ),
              const Divider(),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                  // mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.setAlignment(SunmiPrintAlign.LEFT);
                          // await Pos.printText("Hello World");
                          // await Pos.setAlignment(SunmiPrintAlign.RIGHT);
                          for (var element in SunmiFontSize.values) {
                            await Pos.printText(
                              "?????????? ??????????????",
                              style: SunmiStyle(
                                align: SunmiPrintAlign.RIGHT,
                                fontSize: element,
                              ),
                            );
                          }

                          // await Pos.line();
                          // for (var element in SunmiFontSize.values) {
                          //   await Pos.printText(
                          //     "Hello World",
                          //     style: SunmiStyle(
                          //       // align: SunmiPrintAlign.LEFT,
                          //       fontSize: element,
                          //       // bold: true,
                          //     ),
                          //   );
                          // }
                          // await Pos.setAlignment(SunmiPrintAlign.CENTER);
                          // await Pos.printQRCode(
                          //   'https://github.com/brasizza/sunmi_printer',
                          //   size: 16,
                          // );
                          // await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Print qrCode')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printBarCode('1234567890',
                              barcodeType: SunmiBarcodeType.CODE128,
                              textPosition: SunmiBarcodeTextPos.TEXT_UNDER,
                              height: 20);
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Print barCode')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.line();
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Print line')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.lineWrap(2);
                        },
                        child: const Text('Wrap line')),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                  // mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Hello I\'m bold',
                              style: SunmiStyle(bold: true));
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Bold Text')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Very small!',
                              style: SunmiStyle(fontSize: SunmiFontSize.XS));
                          await Pos.lineWrap(2);

                          await Pos.performPrint();
                        },
                        child: const Text('Very small font')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Very small!',
                              style: SunmiStyle(fontSize: SunmiFontSize.SM));
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Small font')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Normal font',
                              style: SunmiStyle(fontSize: SunmiFontSize.MD));

                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Normal font')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          await Pos.printText('Large font',
                              style: SunmiStyle(fontSize: SunmiFontSize.LG));

                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Large font')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          // await Pos.setFontSize(SunmiFontSize.XL);
                          await Pos.printText('Very Large font!');
                          await Pos.resetFontSize();
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Very large font')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.setCustomFontSize(13);
                          await Pos.printText('Very Large font!');
                          await Pos.resetFontSize();
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Custom size font')),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                  // mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();
                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Align right',
                              style: SunmiStyle(align: SunmiPrintAlign.RIGHT));
                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Align right')),
                    ElevatedButton(
                        onPressed: () async {
                          await Pos.initPrinter();

                          // await Pos.startTransactionPrint(true);
                          await Pos.printText('Align left',
                              style: SunmiStyle(align: SunmiPrintAlign.LEFT));

                          await Pos.lineWrap(2);
                          await Pos.performPrint();
                        },
                        child: const Text('Align left')),
                    ElevatedButton(
                      onPressed: () async {
                        await Pos.initPrinter();

                        // await Pos.startTransactionPrint(true);
                        await Pos.printText(
                          'Align center/ LARGE TEXT AND BOLD',
                          style: SunmiStyle(
                              align: SunmiPrintAlign.CENTER,
                              bold: true,
                              fontSize: SunmiFontSize.LG),
                        );

                        await Pos.lineWrap(2);
                        await Pos.performPrint();
                      },
                      child: const Text('Align center'),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                  // mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    GestureDetector(
                      onTap: () async {
                        await Pos.initPrinter();

                        Uint8List byte =
                            await _getImageFromAsset('assets/images/dash.jpeg');
                        await Pos.setAlignment(SunmiPrintAlign.CENTER);

                        // await Pos.startTransactionPrint(true);
                        await Pos.printImage(byte);
                        await Pos.lineWrap(2);
                        await Pos.performPrint();
                      },
                      child: Column(
                        children: [
                          Image.asset(
                            'assets/images/dash.jpeg',
                            width: 100,
                          ),
                          const Text('Print this image from asset!')
                        ],
                      ),
                    ),
                    OutlinedButton(
                      onPressed: () async {
                        await Pos.initPrinter();

                        String url =
                            'https://scruss.com/wordpress/wp-content/uploads/2015/06/Thermal_Test_Image.png';
                        // convert image to Uint8List format
                        Uint8List byte =
                            (await NetworkAssetBundle(Uri.parse(url)).load(url))
                                .buffer
                                .asUint8List();
                        await Pos.setAlignment(SunmiPrintAlign.CENTER);
                        // await Pos.startTransactionPrint(true);
                        await Pos.printImage(byte, size: 16);
                        await Pos.lineWrap(2);
                        await Pos.performPrint();
                      },
                      child: Column(
                        children: [
                          Image.network(
                              'https://scruss.com/wordpress/wp-content/uploads/2015/06/Thermal_Test_Image.png'),
                          const Text('Print this image from WEB!')
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              const Divider(),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                    // mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                          onPressed: () async {
                            await Pos.initPrinter();
                            // ((384 / 24) << 1)-(number of columns + 1)
                            await Pos.setFontSize(SunmiFontSize.SM);
                            await Pos.printRow(cols: [
                              ColumnMaker(
                                text: 'Name',
                                width: 8,
                                align: SunmiPrintAlign.LEFT,
                              ),
                              ColumnMaker(
                                text: 'Name',
                                width: 5,
                                align: SunmiPrintAlign.LEFT,
                              ),
                              ColumnMaker(
                                text: 'Name',
                                width: 5,
                                align: SunmiPrintAlign.LEFT,
                              ),
                              ColumnMaker(
                                text: 'Name',
                                width: 5,
                                align: SunmiPrintAlign.LEFT,
                              ),
                            ]);
                            await Pos.performPrint();
                          },
                          child: const Text('CUT PAPER')),
                    ]),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                    // mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                          onPressed: () async {
                            await Pos.initPrinter();

                            // await Pos.startTransactionPrint(true);
                            await Pos.setAlignment(SunmiPrintAlign.CENTER);
                            await Pos.line();
                            await Pos.printText('Payment receipt');
                            await Pos.printText('Using the old way to bold!');
                            await Pos.line();

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'Name',
                                  width: 12,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: 'Qty',
                                  width: 6,
                                  align: SunmiPrintAlign.CENTER),
                              ColumnMaker(
                                  text: 'UN',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                              ColumnMaker(
                                  text: 'TOT',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'Fries',
                                  width: 12,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '4x',
                                  width: 6,
                                  align: SunmiPrintAlign.CENTER),
                              ColumnMaker(
                                  text: '3.00',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                              ColumnMaker(
                                  text: '12.00',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'Strawberry',
                                  width: 12,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '1x',
                                  width: 6,
                                  align: SunmiPrintAlign.CENTER),
                              ColumnMaker(
                                  text: '24.44',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                              ColumnMaker(
                                  text: '24.44',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'Soda',
                                  width: 12,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '1x',
                                  width: 6,
                                  align: SunmiPrintAlign.CENTER),
                              ColumnMaker(
                                  text: '1.99',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                              ColumnMaker(
                                  text: '1.99',
                                  width: 6,
                                  align: SunmiPrintAlign.RIGHT),
                            ]);

                            await Pos.line();
                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'TOTAL',
                                  width: 25,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '38.43',
                                  width: 5,
                                  align: SunmiPrintAlign.RIGHT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'ARABIC TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '?????? ??????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: '?????? ??????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '?????? ??????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'RUSSIAN TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '??????????-????????????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);
                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: '??????????-????????????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '??????????-????????????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);

                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: 'CHINESE TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);
                            await Pos.printRow(cols: [
                              ColumnMaker(
                                  text: '????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                              ColumnMaker(
                                  text: '????????????',
                                  width: 15,
                                  align: SunmiPrintAlign.LEFT),
                            ]);

                            await Pos.setAlignment(SunmiPrintAlign.CENTER);
                            await Pos.line();
                            await Pos.bold();
                            await Pos.printText('Transaction\'s Qrcode');
                            await Pos.resetBold();
                            await Pos.printQRCode(
                                'https://github.com/brasizza/sunmi_printer');
                            await Pos.lineWrap(2);
                            await Pos.performPrint();
                          },
                          child: const Text('TICKET EXAMPLE')),
                    ]),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Wrap(
                    // mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                          onPressed: () async {
                            final List<int> _escPos = await _customEscPos();
                            await Pos.initPrinter();
                            // await Pos.startTransactionPrint(true);
                            await Pos.printRawData(Uint8List.fromList(_escPos));
                            await Pos.performPrint();
                          },
                          child: const Text('Custom ESC/POS to print')),
                    ]),
              ),
            ],
          ),
        ));
  }
}

Future<Uint8List> readFileBytes(String path) async {
  ByteData fileData = await rootBundle.load(path);
  Uint8List fileUnit8List = fileData.buffer
      .asUint8List(fileData.offsetInBytes, fileData.lengthInBytes);
  return fileUnit8List;
}

Future<Uint8List> _getImageFromAsset(String iconPath) async {
  return await readFileBytes(iconPath);
}

Future<List<int>> _customEscPos() async {
  final profile = await CapabilityProfile.load();
  final generator = Generator(PaperSize.mm58, profile);
  List<int> bytes = [];

  bytes += generator.text(
      'Regular: aA bB cC dD eE fF gG hH iI jJ kK lL mM nN oO pP qQ rR sS tT uU vV wW xX yY zZ');
  bytes += generator.text('Special 1: ???? ???? ???? ???? ???? ???? ????',
      styles: const PosStyles(codeTable: 'CP1252'));
  bytes += generator.text('Special 2: bl??b??rgr??d',
      styles: const PosStyles(codeTable: 'CP1252'));

  bytes += generator.text('Bold text', styles: const PosStyles(bold: true));
  bytes +=
      generator.text('Reverse text', styles: const PosStyles(reverse: true));
  bytes += generator.text('Underlined text',
      styles: const PosStyles(underline: true), linesAfter: 1);
  bytes += generator.text('Align left',
      styles: const PosStyles(align: PosAlign.left));
  bytes += generator.text('Align center',
      styles: const PosStyles(align: PosAlign.center));
  bytes += generator.text('Align right',
      styles: const PosStyles(align: PosAlign.right), linesAfter: 1);
  bytes += generator.qrcode('Barcode by escpos',
      size: QRSize.Size4, cor: QRCorrection.H);
  bytes += generator.feed(2);

  bytes += generator.row([
    PosColumn(
      text: 'col3',
      width: 3,
      styles: const PosStyles(align: PosAlign.center, underline: true),
    ),
    PosColumn(
      text: 'col6',
      width: 6,
      styles: const PosStyles(align: PosAlign.center, underline: true),
    ),
    PosColumn(
      text: 'col3',
      width: 3,
      styles: const PosStyles(align: PosAlign.center, underline: true),
    ),
  ]);

  bytes += generator.text('Text size 200%',
      styles: const PosStyles(
        height: PosTextSize.size2,
        width: PosTextSize.size2,
      ));

  bytes += generator.reset();
  bytes += generator.cut();

  return bytes;
}
