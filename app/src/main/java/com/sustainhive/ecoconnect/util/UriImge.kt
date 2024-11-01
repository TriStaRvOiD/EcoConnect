package com.sustainhive.ecoconnect.util

import android.net.Uri

data class UriImage(
    val uri: Uri,
    var caption: String,
    val remoteImagePath: String
)