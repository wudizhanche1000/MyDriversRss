package org.weijian.mydriversrss

import java.util.*

/**
 * Created by weijian on 5/4/16.
 */
fun get(){
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
