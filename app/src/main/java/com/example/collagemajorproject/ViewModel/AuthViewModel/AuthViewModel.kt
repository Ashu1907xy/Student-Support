
//}
package com.example.collagemajorproject.ViewModel.AuthViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.collagemajorproject.DataModel.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    private val database = FirebaseDatabase.getInstance().getReference("users")


    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null || auth.currentUser?.isAnonymous == true) {
            _authState.value = AuthState.Unauthenticated

        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: "Something went wrong"
                    )
                }
            }
    }

    fun signup(
        navController: NavController,
        name: String,
        collegeName: String,
        rollNumber: String,
        department: String,
        age: String,
        bloodGroup: String,
        mobile: String,
        email: String,
        password: String,
        imageUri: Uri?,
        context: Context
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        if (name.isEmpty()) {
            _authState.value = AuthState.Error("Name is required")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // Step 1: Create Firebase Auth user
                val authTask = withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, password)
                }

                authTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = task.result.user?.uid ?: ""

                        // Step 2: Process image in background
                        viewModelScope.launch(Dispatchers.IO) {
                            val base64Image = imageUri?.let { uri ->
                                compressAndEncodeImage(context, uri)
                            } ?: ""

                            // Step 3: Create ProfileData
                            val profileData = ProfileData(
                                name = name,
                                collegeName = collegeName,
                                rollNumber = rollNumber,
                                department = department,
                                age = age,
                                bloodGroup = bloodGroup,
                                mobile = mobile,
                                email = email,
                                imageUrl = base64Image
                            )

                            // Step 4: Save to Firebase
                            withContext(Dispatchers.Main) {
                                database.child(uid).child("ProfileData").setValue(profileData)
                                    .addOnSuccessListener {
                                        _authState.value = AuthState.Authenticated
                                    }
                                    .addOnFailureListener { e ->
                                        // Cleanup: Delete auth user if database save fails
                                        auth.currentUser?.delete()
                                        _authState.value = AuthState.Error(
                                            "Failed to save profile: ${e.message}"
                                        )
                                    }
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error(
                            task.exception?.message ?: "Something went wrong"
                        )
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    /**
     * Compress and encode image to Base64
     * - Fixes rotation issues (camera/gallery)
     * - Resizes to max 512x512 pixels
     * - Compresses to JPEG at 70% quality
     * - Keeps size under 200KB
     */
    private fun compressAndEncodeImage(context: Context, imageUri: Uri): String? {
        return try {
            // Read the image
            val inputStream = context.contentResolver.openInputStream(imageUri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) return null

            // Fix rotation issue (especially for camera photos)
            bitmap = fixImageRotation(context, imageUri, bitmap)

            // Calculate scaling to max 512x512 (keeps aspect ratio)
            val maxDimension = 512
            val scale = minOf(
                maxDimension.toFloat() / bitmap.width,
                maxDimension.toFloat() / bitmap.height
            )

            if (scale < 1.0f) {
                val newWidth = (bitmap.width * scale).toInt()
                val newHeight = (bitmap.height * scale).toInt()
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            }

            // Compress to JPEG
            val outputStream = ByteArrayOutputStream()
            var quality = 70
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // If still too large, reduce quality further
            while (outputStream.size() > 200_000 && quality > 30) {
                outputStream.reset()
                quality -= 10
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }

            val byteArray = outputStream.toByteArray()

            // Check if still too large
            if (byteArray.size > 300_000) {
                return null // Return null if image too large
            }

            // Encode to Base64 without line breaks
            Base64.encodeToString(byteArray, Base64.NO_WRAP)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Fix image rotation based on EXIF data
     * Camera photos often have rotation metadata that needs to be applied
     */
    private fun fixImageRotation(context: Context, imageUri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()

            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }

            if (!matrix.isIdentity) {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap // Return original if rotation fails
        }
    }

    /**
     * Decode Base64 string back to Bitmap for display
     */
    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            if (base64String.isEmpty()) return null
            val decodedBytes = Base64.decode(base64String, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }










/*
    fun deleteAccount(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val user = auth.currentUser

        if (user == null) {
            onError("No user logged in")
            return
        }

        _authState.value = AuthState.Loading
        val uid = user.uid

        viewModelScope.launch {
            try {
                // Step 1: Delete user data from database
                database.child(uid).removeValue()
                    .addOnSuccessListener {
                        // Step 2: Delete user from Firebase Auth
                        user.delete()
                            .addOnSuccessListener {
                                _authState.value = AuthState.Unauthenticated
                                onSuccess()
                            }
                            .addOnFailureListener { exception ->
                                // If auth deletion fails, handle re-authentication needed
                                val errorMessage = when {
                                    exception.message?.contains("recent login", ignoreCase = true) == true -> {
                                        "Please log in again to delete your account"
                                    }
                                    else -> exception.message ?: "Failed to delete account"
                                }
                                _authState.value = AuthState.Error(errorMessage)
                                onError(errorMessage)
                            }
                    }
                    .addOnFailureListener { exception ->
                        _authState.value = AuthState.Error(
                            "Failed to delete user data: ${exception.message}"
                        )
                        onError(exception.message ?: "Failed to delete user data")
                    }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
                onError(e.message ?: "An error occurred")
            }
        }
    }

    /**
     * Re-authenticate user before sensitive operations like account deletion
     * Required when the user's login is not recent
     */
    fun reauthenticateAndDelete(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onError("Email or password can't be empty")
            return
        }

        val user = auth.currentUser
        if (user == null) {
            onError("No user logged in")
            return
        }

        _authState.value = AuthState.Loading

        // Re-authenticate user
        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                // After successful re-authentication, delete account
                deleteAccount(onSuccess, onError)
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(
                    exception.message ?: "Re-authentication failed"
                )
                onError(exception.message ?: "Re-authentication failed")
            }
    }




 */










    // timetable


}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}