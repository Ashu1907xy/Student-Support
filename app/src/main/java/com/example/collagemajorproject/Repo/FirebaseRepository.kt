package com.example.collagemajorproject.Repo

import android.util.Log
import com.example.collagemajorproject.DataModel.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseRepository {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    fun addNote(note: Notes) {
        val uid = auth.currentUser?.uid ?: return
        val noteRef = db.child(uid).child("Notes")
        val id = noteRef.push().key ?: return
        noteRef.child(id).setValue(note.copy(id = id))

    }

    fun getNote(onDataChange: (List<Notes>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val noteRef = db.child(uid).child("Notes")
        noteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noteList = mutableListOf<Notes>()
                snapshot.children.forEach {
                    it.getValue(Notes::class.java)?.let { note -> noteList.add(note) }
                }
                onDataChange(noteList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseRepository", "Error: ${error.message}")
            }
        })
    }

    fun deleteNote(id: String) {
        val uid = auth.currentUser?.uid ?: return
        db.child(uid).child("Notes").child(id).removeValue()

    }

    fun updateNote(note: Notes) {
        val uid = auth.currentUser?.uid ?: return
        if (note.id.isEmpty()) {return}
        db.child(uid).child("Notes").child(note.id)
            .setValue(note)

    }


}