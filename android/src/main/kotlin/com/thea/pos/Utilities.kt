package com.thea.pos

import android.graphics.Bitmap

class Utilities {
    companion object {


        fun arrayListToIntList(list: ArrayList<Int>): IntArray {
            val ints = IntArray(list.size)
            for (i in list.indices) {
                ints[i] = list[i]
            }
            return ints
        }

        fun arrayListToString(list: ArrayList<String>): Array<String?> {
            val strings = arrayOfNulls<String>(list.size)
            for (i in list.indices) {
                strings[i] = list[i]
            }
            return strings
        }

        fun scaleDownBitmap(
            realImage: Bitmap, maxImageSize: Float,
            filter: Boolean
        ): Bitmap? {
            val ratio = Math.min(
                maxImageSize / realImage.width,
                maxImageSize / realImage.height
            )
            val width = Math.round(ratio * realImage.width)
            val height = Math.round(ratio * realImage.height)
            return Bitmap.createScaledBitmap(
                realImage, width,
                height, filter
            )
        }
    }
}