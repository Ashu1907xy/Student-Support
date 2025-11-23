package com.example.collagemajorproject.Repo

import com.example.collagemajorproject.Common.ResultState
import com.example.collagemajorproject.DataModel.HandWrittenNotes
import com.example.collagemajorproject.DataModel.LostItem
import com.example.collagemajorproject.DataModel.MidPaper
import com.example.collagemajorproject.DataModel.Midsem
import com.example.collagemajorproject.DataModel.RgpvPaper
import com.example.collagemajorproject.DataModel.Shivani
import com.example.collagemajorproject.DataModel.Timetable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.jvm.java


class TimetableRepo @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun getTimetable(): Flow<ResultState<List<Timetable>>> = callbackFlow {
        trySend(ResultState.Loading)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.mapNotNull {
                    it.getValue(Timetable::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(ResultState.Error(databaseError.message))
            }
        }

        firebaseDatabase.reference
            .child("Timetable")
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("Timetable").removeEventListener(postListener)
        }
    }

    fun getMidSem(): Flow<ResultState<List<Midsem>>> = callbackFlow {
        trySend(ResultState.Loading)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.mapNotNull {
                    it.getValue(Midsem::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(ResultState.Error(databaseError.message))
            }
        }

        firebaseDatabase.reference
            .child("Midsem")
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("Midsem").removeEventListener(postListener)
        }
    }

    fun getStudyNotes(): Flow<ResultState<List<HandWrittenNotes>>> = callbackFlow {
        trySend(ResultState.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(HandWrittenNotes::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
            }
        }
        firebaseDatabase.reference.child("HandWrittenNotes").addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("HandWrittenNotes").removeEventListener(postListener)
        }

    }


    fun getMidSemPaper(): Flow<ResultState<List<MidPaper>>> = callbackFlow {
        trySend(ResultState.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(MidPaper::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
            }
        }
        firebaseDatabase.reference.child("MidPaper").addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("MidPaper").removeEventListener(postListener)
        }
    }

    fun getRgpvPaper(): Flow<ResultState<List<RgpvPaper>>> = callbackFlow {
        trySend(ResultState.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(RgpvPaper::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
            }
        }
        firebaseDatabase.reference.child("RgpvPaper").addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("RgpvPaper").removeEventListener(postListener)
        }
    }

    fun getShivani(): Flow<ResultState<List<Shivani>>> = callbackFlow {
        trySend(ResultState.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(Shivani::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
            }
        }
        firebaseDatabase.reference.child("Shivani").addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.reference.child("Shivani").removeEventListener(postListener)
        }

    }


    fun getLostItems(): Flow<ResultState<List<LostItem>>> = callbackFlow {
        trySend(ResultState.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(LostItem::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
            }
        }
        firebaseDatabase.reference.child("LostItem").addValueEventListener(postListener)
        awaitClose {
            firebaseDatabase.reference.child("LostItem").removeEventListener(postListener)
        }
    }


    fun addLostItem(item: LostItem, onResult: (Boolean) -> Unit) {

        val ref = firebaseDatabase.reference.child("LostItem")
        val key = ref.push().key ?: return onResult(false)

        val newItem = item.copy(id = key)

        ref.child(key).setValue(newItem)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


}

