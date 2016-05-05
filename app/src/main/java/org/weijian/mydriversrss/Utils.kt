@file:JvmName("Utils")

package org.weijian.mydriversrss

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.*

/**
 * Created by weijian on 5/4/16.
 */
fun get() {
    val random = Random()
    val signId = random.nextInt().toString()
    val xaId = {
        // Generate random XAID
        var stringBuilder = StringBuilder()
        kotlin.repeat(8) {
            stringBuilder.append(Integer.toHexString(random.nextInt(16)))
        }
        stringBuilder.toString()
    }()
    val udId = random.nextLong().toString()
    val minId = "0"
}

fun getAppVersion(context: Context): Int {
    val info = context.packageManager.getPackageInfo(context.packageName, 0);
    return info.versionCode;
}

fun getDiskCacheDir(context: Context): File {
    var cachePath: File
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()) {
        cachePath = context.externalCacheDir;
    } else {
        cachePath = context.cacheDir;
    }
    return cachePath;

}
