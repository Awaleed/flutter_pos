package com.thea.pos

import android.graphics.BitmapFactory
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONArray


/** PosPlugin */
class PosPlugin : FlutterPlugin, MethodCallHandler {
//    private val TAG = "IPosPrinterTestDemo"
//    private var context: Context? = null

//    private var mIPosPrinterService: IPosPrinterService? = null

//    private val connectService: ServiceConnection = object : ServiceConnection {
//
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            Toast.makeText(context, "onServiceConnected", Toast.LENGTH_SHORT)
//                .show()
//
//            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service)
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            Toast.makeText(context, "onServiceDisconnected", Toast.LENGTH_SHORT)
//                .show()
//
//            mIPosPrinterService = null
//        }
//    }


//    private var callback: IPosPrinterCallback? = object : IPosPrinterCallback.Stub() {
//        @Throws(RemoteException::class)
//        override fun onRunResult(isSuccess: Boolean) {
//            android.util.Log.i(TAG, "result: $isSuccess\n")
//        }
//
//        @Throws(RemoteException::class)
//        override fun onReturnString(value: String) {
//            android.util.Log.i(TAG, "result: $value\n")
//        }
//    }


    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var printerMethod: PrinterMethod? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "pos")

        printerMethod =
            PrinterMethod(flutterPluginBinding.applicationContext)

        channel.setMethodCallHandler(this)
//        context = flutterPluginBinding.applicationContext
//
//        val intent = Intent()
//        intent.setPackage("com.iposprinter.iposprinterservice")
//        intent.action = "com.iposprinter.iposprinterservice.IPosPrintService"
//
//        context?.bindService(intent, connectService, Context.BIND_AUTO_CREATE)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "BIND_PRINTER_SERVICE" -> {
                printerMethod?.bindPrinterService()
                result.success(true)
            }
            "UNBIND_PRINTER_SERVICE" -> {
                printerMethod?.unbindPrinterService()
                result.success(true)
            }
            "INIT_PRINTER" -> {
                printerMethod?.initPrinter()
                result.success(true)
            }
            "GET_UPDATE_PRINTER" -> {
                val statusCode: Int? = printerMethod?.updatePrinter()
                var statusMsg = ""
                statusMsg = when (statusCode) {
                    0 -> "ERROR"
                    1 -> "NORMAL"
                    2 -> "ABNORMAL_COMMUNICATION"
                    3 -> "OUT_OF_PAPER"
                    4 -> "PREPARING"
                    5 -> "OVERHEATED"
                    6 -> "OPEN_THE_LID"
                    7 -> "PAPER_CUTTER_ABNORMAL"
                    8 -> "PAPER_CUTTER_RECOVERED"
                    9 -> "NO_BLACK_MARK"
                    505 -> "NO_PRINTER_DETECTED"
                    507 -> "FAILED_TO_UPGRADE_FIRMWARE"
                    else -> "EXCEPTION"
                }
                result.success(statusMsg)
            }
            "PRINT_TEXT" -> {
                val text = call.argument<String>("text")
                printerMethod?.printText(text!!)
                result.success(true)
            }
            "RAW_DATA" -> {
                printerMethod?.sendRaw(call.argument<Any>("data") as ByteArray?)
                result.success(true)
            }
            "PRINT_QRCODE" -> {
                val data = call.argument<String>("data")
                val modulesize = call.argument<Int>("modulesize")!!!!
                val errorlevel = call.argument<Int>("errorlevel")!!!!
                printerMethod?.printQRCode(data, modulesize, errorlevel)
                result.success(true)
            }
            "PRINT_BARCODE" -> {
                val barCodeData = call.argument<String>("data")
                val barcodeType = call.argument<Int>("barcodeType")!!!!
                val textPosition = call.argument<Int>("textPosition")!!!!
                val width = call.argument<Int>("width")!!!!
                val height = call.argument<Int>("height")!!!!
                printerMethod?.printBarCode(barCodeData, barcodeType, textPosition, width, height)
                printerMethod?.lineWrap(1)
                result.success(true)
            }
            "LINE_WRAP" -> {
                val lines = call.argument<Int>("lines")!!!!
                printerMethod?.lineWrap(lines)
                result.success(true)
            }
            "FONT_SIZE" -> {
                val fontSize = call.argument<Int>("size")!!
                printerMethod?.setFontSize(fontSize)
                result.success(true)
            }
            "SET_ALIGNMENT" -> {
                val alignment = call.argument<Int>("alignment")!!!!
                printerMethod?.setAlignment(alignment)
                result.success(true)
            }
            "PRINT_IMAGE" -> {
                val bytes = call.argument<ByteArray>("bitmap")
                val alignment = call.argument<Int>("alignment")!!
                val size = call.argument<Int>("size")!!
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                printerMethod?.printImage(alignment, size, bitmap)
                result.success(true)
            }
//            "GET_PRINTER_MODE" -> {
//                val modeCode: Int? = printerMethod?.getPrinterMode()
//                var modeDesc = ""
//                modeDesc = when (modeCode) {
//                    0 -> "NORMAL_MODE"
//                    1 -> "BLACK_LABEL_MODE"
//                    2 -> "LABEL_MODE"
//                    3 -> "ERROR"
//                    else -> "EXCEPTION"
//                }
//                result.success(modeDesc)
//            }
//            "ENTER_PRINTER_BUFFER" -> {
//                val clearEnter = call.argument<Boolean>("clearEnter")
//                printerMethod?.enterPrinterBuffer(
//                    clearEnter
//                )
//                result.success(true)
//            }
//            "COMMIT_PRINTER_BUFFER" -> {
//                printerMethod?.commitPrinterBuffer()
//                result.success(true)
//            }
//            "CUT_PAPER" -> {
//                printerMethod?.cutPaper()
//                result.success(true)
//            }
//            "OPEN_DRAWER" -> {
//                printerMethod?.openDrawer()
//                result.success(true)
//            }
//            "DRAWER_OPENED" -> result.success(printerMethod?.timesOpened())
//            "DRAWER_STATUS" -> result.success(printerMethod?.drawerStatus())
            "PERFORM_PRINT" -> result.success(printerMethod?.performPrint())
            "PRINT_ROW" -> {
                val colsStr = call.argument<String>("cols")
                try {
                    val cols = JSONArray(colsStr)
                    val colsText = arrayOfNulls<String>(cols.length())
                    val colsWidth = IntArray(cols.length())
                    val colsAlign = IntArray(cols.length())
                    var i = 0
                    while (i < cols.length()) {
                        val col = cols.getJSONObject(i)
                        val textColumn = col.getString("text")
                        val widthColumn = col.getInt("width")
                        val alignColumn = col.getInt("align")
                        colsText[i] = textColumn
                        colsWidth[i] = widthColumn
                        colsAlign[i] = alignColumn
                        i++
                    }
                    printerMethod?.printColumn(colsText, colsWidth, colsAlign, 1)
                    result.success(true)
                } catch (err: Exception) {
                    android.util.Log.d("SunmiPrinter", err.message!!)
                }
            }
//            "EXIT_PRINTER_BUFFER" -> {
//                val clearExit = call.argument<Boolean>("clearExit")
//                printerMethod?.exitPrinterBuffer(
//                    clearExit
//                )
//                result.success(true)
//            }
//            "PRINTER_SERIAL_NUMBER" -> {
//                val serial: String? = printerMethod?.getPrinterSerialNo()
//                result.success(serial)
//            }
//            "PRINTER_VERSION" -> {
//                val printer_verison: String? = printerMethod?.getPrinterVersion()
//                result.success(printer_verison)
//            }
//            "PAPER_SIZE" -> {
//                val paper: Int? = printerMethod?.getPrinterPaper()
//                result.success(paper)
//            }
//            "LCD_COMMAND" -> {
//                val flag = call.argument<Int>("flag")!!!!
//                printerMethod?.sendLCDCommand(flag)
//                result.success(true)
//            }
//            "LCD_STRING" -> {
//                val lcdString = call.argument<String>("string")
//                printerMethod?.sendLCDString(lcdString)
//                result.success(true)
//            }
//            "LCD_BITMAP" -> {
//                val lcdBitmapData = call.argument<ByteArray>("bitmap")
//                val lcdBitmap = BitmapFactory.decodeByteArray(
//                    lcdBitmapData, 0, lcdBitmapData!!.size
//                )
//                printerMethod?.sendLCDBitmap(lcdBitmap)
//                result.success(true)
//            }
//            "LCD_DOUBLE_STRING" -> {
//                val topText = call.argument<String>("topText")
//                val bottomText = call.argument<String>("bottomText")
//                printerMethod?.sendLCDDoubleString(topText, bottomText)
//                result.success(true)
//            }
//            "LCD_FILL_STRING" -> {
//                val lcdFillString = call.argument<String>("string")
//                val lcdFillSize = call.argument<Int>("size")!!!!
//                val lcdFill = call.argument<Boolean>("fill")!!!!
//                printerMethod?.sendLCDFillString(lcdFillString, lcdFillSize, lcdFill)
//                result.success(true)
//            }
//            "LCD_MULTI_STRING" -> {
//                val lcdTextAL = call.argument<ArrayList<String>>("text")!!
//                val lcdText: Array<String?> = Utilities.arrayListToString(lcdTextAL)
//                val lcdAlignAL = call.argument<ArrayList<Int>>("align")!!
//                val lcdAlign: IntArray = Utilities.arrayListToIntList(lcdAlignAL)
//                printerMethod?.sendLCDMultiString(lcdText, lcdAlign)
//                result.success(true)
//            }
            else -> result.notImplemented()
        }

    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}