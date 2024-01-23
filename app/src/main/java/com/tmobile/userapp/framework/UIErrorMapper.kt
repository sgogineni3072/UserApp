package com.tmobile.userapp.framework

import android.content.res.Resources
import com.tmobile.userapp.R

class UIErrorMapper(private val resources: Resources) {
    fun mapErrorCodeToMessage(errorCode: Int): String {
        return if (errorCode == -1) {
            resources.getString(R.string.network_error)
        } else {
            resources.getString(R.string.unknown_error)
        }
    }
}