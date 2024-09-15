package com.sustainhive.ecoconnect.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(modifier: Modifier = Modifier, logOut: () -> Unit) {

    val notes = remember { mutableStateListOf<Note>() }
    var newNoteContent by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    // Fetch notes from Firestore
    LaunchedEffect(Unit) {
        try {
            if (user != null) {
                val snapshot = db.collection("notes")
                    .whereEqualTo("ownerId", user.uid) // Only fetch notes for the current user
                    .get().await()

                val fetchedNotes = snapshot.documents.map { doc ->
                    Note(
                        id = doc.id,
                        ownerId = doc.getString("ownerId") ?: "",
                        content = doc.getString("content") ?: ""
                    )
                }
                notes.addAll(fetchedNotes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(notes.size) { index ->
                    Text(text = notes[index].content, modifier = Modifier.padding(8.dp))
                }
            }
            OutlinedTextField(
                value = newNoteContent,
                onValueChange = { newNoteContent = it },
                label = { Text("Enter a new note") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            Button(
                onClick = {
                    if (newNoteContent.isNotEmpty() && user != null) {
                        // Add the note to Firestore
                        val newNote = hashMapOf(
                            "ownerId" to user.uid,
                            "content" to newNoteContent
                        )
                        db.collection("notes").add(newNote)
                            .addOnSuccessListener { docRef ->
                                // Add the new note to the list
                                notes.add(Note(id = docRef.id, ownerId = user.uid, content = newNoteContent))
                                newNoteContent = "" // Clear the input field
                            }
                            .addOnFailureListener {
                                // Handle failure
                                it.printStackTrace()
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Add Note")
            }
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedButton(
                onClick = logOut
            ) {
                Text("Log out")
            }
        }
    }
}

// Firestore data class
data class Note(
    val id: String,
    val ownerId: String,
    val content: String
)