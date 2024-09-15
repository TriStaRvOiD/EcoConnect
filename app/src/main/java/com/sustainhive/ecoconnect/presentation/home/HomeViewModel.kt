package com.sustainhive.ecoconnect.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    init {
        db.collection("")
    }
}