package com.thea.pos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.iposprinter.iposprinterservice.IPosPrinterCallback
import com.iposprinter.iposprinterservice.IPosPrinterService

class PrinterMethod(context: Context?) {

    private val TAG: String = PrinterMethod::class.java.simpleName

    private val _printingText = ArrayList<Boolean>()
    private var _woyouService: IPosPrinterService? = null
    private var _context: Context? = context

    private val connService: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                Log.i(TAG, "onServiceConnected");
                _woyouService = IPosPrinterService.Stub.asInterface(service)
                Toast.makeText(_context, "POS Printer Service Connected.", Toast.LENGTH_LONG)
                    .show()
            } catch (e: RemoteException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {

                Toast.makeText(_context, "POS Printer Service Not Found", Toast.LENGTH_LONG).show()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "onServiceDisconnected");

            Toast.makeText(_context, "POS Printer Service Disconnected", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun bindPrinterService() {
        val intent = Intent()
        intent.setPackage("com.iposprinter.iposprinterservice")
        intent.action = "com.iposprinter.iposprinterservice.IPosPrintService"
        _context!!.bindService(intent, connService, Context.BIND_AUTO_CREATE)
    }

    fun unbindPrinterService() {
        _context!!.unbindService(connService)
    }

    fun initPrinter() {
        try {
            _woyouService?.printerInit(_callback())
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

    fun updatePrinter(): Int? {
        return try {
            _woyouService?.printerStatus
        } catch (e: RemoteException) {
            0 // error
        } catch (e: NullPointerException) {
            0
        }
    }

    fun printText(text: String) {
        _printingText.add(_printText(text))
    }

    private fun _printText(text: String): Boolean {
        return try {
            _woyouService?.printText(text, _callback())
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }

//    fun setAlignment(alignment: Int): Boolean? {
//        return try {
//            _woyouService?.setPrinterPrintAlignment(alignment, _callback())
//            true
//        } catch (e: RemoteException) {
//            false
//        } catch (e: NullPointerException) {
//            false
//        }
//    }

    fun setFontSize(fontSize: Int): Boolean? {
        return try {
            _woyouService?.setPrinterPrintFontSize(fontSize, _callback())
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }

    fun setFontBold(bold: Boolean?): Boolean? {
        var bold = bold
        if (bold == null) {
            bold = false
        }
        var command = byteArrayOf(0x1B, 0x45, 0x1)
        if (bold == false) {
            command = byteArrayOf(0x1B, 0x45, 0x0)
        }
        return try {
            _woyouService?.printRawData(command, _callback())
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }


    fun printColumn(
        stringColumns: Array<String?>?,
        columnWidth: IntArray?,
        columnAlignment: IntArray?,
        isContinuousPrint: Int
    ): Boolean? {
        return try {
            _woyouService?.printColumnsText(
                stringColumns,
                columnWidth,
                columnAlignment,
                isContinuousPrint,
                _callback()
            )
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }


    fun printImage(
        alignment: Int,
        bitmapSize: Int,
        mBitmap: Bitmap?
    ): Boolean? {
        return try {
            _woyouService?.printBitmap(
                alignment,
                bitmapSize,
                mBitmap,
                _callback(),
            )
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }


//    fun cutPaper(): Boolean? {
//        return try {
//            _woyouService?.cutPaper(_callback())
//            true
//        } catch (e: RemoteException) {
//            false
//        } catch (e: NullPointerException) {
//            false
//        }
//    }

//    fun getPrinterSerialNo(): String? {
//        return try {
//            _woyouService?.getPrinterSerialNo()
//        } catch (e: RemoteException) {
//            "" // error;
//        } catch (e: NullPointerException) {
//            "NOT FOUND"
//        }
//    }

//    fun getPrinterVersion(): String? {
//        return try {
//            _woyouService?.getPrinterVersion()
//        } catch (e: RemoteException) {
//            "" // error;
//        } catch (e: NullPointerException) {
//            "NOT FOUND"
//        }
//    }

//    fun getPrinterPaper(): Int {
//        return try {
//            _woyouService?.papgetPrinterPaper()
//        } catch (e: RemoteException) {
//            1 // error;
//        } catch (e: NullPointerException) {
//            1
//        }
//    }

//    fun getPrinterMode(): Int {
//        return try {
//            _woyouService?.getPrinterMode()
//        } catch (e: RemoteException) {
//            3 // error;
//        } catch (e: NullPointerException) {
//            3
//        }
//    }

    //    fun openDrawer(): Boolean? {
//        return try {
//            _woyouService?.openDrawer(_callback())
//            true
//        } catch (e: RemoteException) {
//            false
//        } catch (e: NullPointerException) {
//            false
//        }
//    }
    fun performPrint(): Boolean? {
        return try {
            _woyouService?.printerPerformPrint(160, _callback())
            true
        } catch (e: RemoteException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }


//    fun drawerStatus(): Boolean? {
//        return try {
//            _woyouService?.getDrawerStatus()
//        } catch (e: RemoteException) {
//            false
//        } catch (e: NullPointerException) {
//            false
//        }
//    }

//    fun timesOpened(): Int {
//        return try {
//            _woyouService?.getOpenDrawerTimes()
//        } catch (e: RemoteException) {
//            0
//        } catch (e: NullPointerException) {
//            0
//        }
//    }

    fun lineWrap(lines: Int) {
        try {
            _woyouService?.printBlankLines(lines, 15, _callback())
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

    fun sendRaw(bytes: ByteArray?) {
        try {
            _woyouService?.printRawData(bytes, _callback())
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

//    fun enterPrinterBuffer(clear: Boolean?) {
//        try {
//            _woyouService?.buff(clear)
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun commitPrinterBuffer() {
//        try {
//            _woyouService?.commitPrinterBuffer()
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun exitPrinterBuffer(clear: Boolean?) {
//        try {
//            _woyouService?.exitPrinterBuffer(clear)
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

    fun setAlignment(alignment: Int) {
        try {
            _woyouService?.setPrinterPrintAlignment(alignment, _callback())
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

    fun printQRCode(data: String?, modulesize: Int, errorlevel: Int) {
        try {
            _woyouService?.printQRCode(data, modulesize, errorlevel, _callback())
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

    fun printBarCode(
        data: String?,
        barcodeType: Int,
        textPosition: Int,
        width: Int,
        height: Int
    ) {
        try {
            _woyouService?.printBarCode(
                data,
                barcodeType,
                height,
                width,
                textPosition,
                _callback()
            )
        } catch (e: RemoteException) {
        } catch (e: NullPointerException) {
        }
    }

    private fun _callback(): IPosPrinterCallback? {
        return object : IPosPrinterCallback.Stub() {
            @Throws(RemoteException::class)
            override fun onRunResult(isSuccess: Boolean) {
                android.util.Log.i(TAG, "result: $isSuccess\n")
            }

            @Throws(RemoteException::class)
            override fun onReturnString(value: String) {
                android.util.Log.i(TAG, "result: $value\n")
            }
        }
    }

    // LCD METHODS

    // LCD METHODS
//    fun sendLCDCommand(
//        flag: Int
//    ) {
//        try {
//            _woyouService?.sendLCDCommand(
//                flag
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun sendLCDString(
//        string: String?
//    ) {
//        try {
//            _woyouService?.sendLCDString(
//                string,
//                _lcdCallback()
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun sendLCDBitmap(
//        bitmap: Bitmap?
//    ) {
//        try {
//            _woyouService?.sendLCDBitmap(
//                bitmap,
//                _lcdCallback()
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun sendLCDDoubleString(
//        topText: String?,
//        bottomText: String?
//    ) {
//        try {
//            _woyouService?.sendLCDDoubleString(
//                topText, bottomText,
//                _lcdCallback()
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    fun sendLCDFillString(
//        string: String?,
//        size: Int,
//        fill: Boolean
//    ) {
//        try {
//            _woyouService?.sendLCDFillString(
//                string, size, fill,
//                _lcdCallback()
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

    /**
     * Show multi lines text on LCD.
     * @param text Text lines.
     * @param align The weight of the solid content of each line. Like flex.
     */
//    fun sendLCDMultiString(
//        text: Array<String?>?,
//        align: IntArray?
//    ) {
//        try {
//            _woyouService?.sendLCDMultiString(
//                text, align,
//                _lcdCallback()
//            )
//        } catch (e: RemoteException) {
//        } catch (e: NullPointerException) {
//        }
//    }

//    private fun _lcdCallback(): ILcdCallback? {
//        return object : ILcdCallback() {
//            fun asBinder(): IBinder? {
//                return null
//            }
//
//            @Throws(RemoteException::class)
//            fun onRunResult(show: Boolean) {
//            }
//        }
//    }
}