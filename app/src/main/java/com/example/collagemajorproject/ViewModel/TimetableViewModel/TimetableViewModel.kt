package com.example.collagemajorproject.ViewModel.TimetableViewModel


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collagemajorproject.Common.ResultState
import com.example.collagemajorproject.DataModel.HandWrittenNotes
import com.example.collagemajorproject.DataModel.LostItem
import com.example.collagemajorproject.DataModel.MidPaper
import com.example.collagemajorproject.DataModel.Midsem
import com.example.collagemajorproject.DataModel.RgpvPaper
import com.example.collagemajorproject.DataModel.Shivani
import com.example.collagemajorproject.DataModel.Timetable
import com.example.collagemajorproject.Repo.TimetableRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.Result.Companion.success


@HiltViewModel

class TimeTableViewModel @Inject constructor(private val timetableRepo: TimetableRepo) :
    ViewModel() {


    private val _getAllTimetableState = MutableStateFlow(TimetableState())
    val getAllTimetableState = _getAllTimetableState.asStateFlow()


    private val _getAllMidSemState = MutableStateFlow(MidSemState())
    val getAllMidSemState = _getAllMidSemState.asStateFlow()


    private val _getAllHandWrittenNotesState = MutableStateFlow(HandWrittenNotesSemState())
    val getAllHandWrittenNotes = _getAllHandWrittenNotesState.asStateFlow()



    private val _getAllMidSemPaperState = MutableStateFlow(MidSemPaperState())
    val getAllMidSemPaperState  = _getAllMidSemPaperState.asStateFlow()


    private val _getAllRgpvPaperState = MutableStateFlow(RgpvPaperState())
    val getAllRgpvPaperState = _getAllRgpvPaperState.asStateFlow()


    private val _getAllShivaniState = MutableStateFlow(ShivaniState())
    val getAllShivaniState = _getAllShivaniState.asStateFlow()


    private val _getAllLostItemsState = MutableStateFlow(LostItemState())
    val getAllLostItemState = _getAllLostItemsState.asStateFlow()


    private val _addPost = MutableStateFlow(AddLostItemState())
    val addPost = _addPost.asStateFlow()



    init {

        getAllTimetable()
        getAllMidSem()
        getAllHandWrittenNotes()
        getAllMidSemPaper()
        getAllRgpvPaper()
        getAllShivani()
        getAllLostItems()

    }


    fun getAllTimetable() {
        viewModelScope.launch(Dispatchers.IO) {
            timetableRepo.getTimetable().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllTimetableState.value = TimetableState(
                            isLoading = true
                        )
                    }


                    is ResultState.Error -> {

                        _getAllTimetableState.value = TimetableState(
                            isLoading = false,
                            error = it.message

                        )
                    }

                    is ResultState.Success -> {
                        _getAllTimetableState.value = TimetableState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }


    fun getAllMidSem() {
        viewModelScope.launch(Dispatchers.IO) {
            timetableRepo.getMidSem().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllMidSemState.value = MidSemState(
                            isLoading = true
                        )
                    }


                    is ResultState.Error -> {
                        _getAllMidSemState.value = MidSemState(
                            isLoading = false,
                            error = it.message

                        )
                    }

                    is ResultState.Success -> {
                        _getAllMidSemState.value = MidSemState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllHandWrittenNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            timetableRepo.getStudyNotes().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllHandWrittenNotesState.value = HandWrittenNotesSemState(
                            isLoading = true
                        )
                    }

                    is ResultState.Error<*> -> {
                        _getAllHandWrittenNotesState.value = HandWrittenNotesSemState(
                            isLoading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Success -> {
                        _getAllHandWrittenNotesState.value = HandWrittenNotesSemState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllMidSemPaper(){
        viewModelScope.launch(Dispatchers.IO) {
            timetableRepo.getMidSemPaper().collect {
                when(it){
                    is ResultState.Loading -> {
                        _getAllMidSemPaperState.value = MidSemPaperState(
                            isLoading = true
                        )
                    }

                    is ResultState.Error -> {
                        _getAllMidSemPaperState.value = MidSemPaperState(
                            isLoading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Success -> {
                        _getAllMidSemPaperState.value = MidSemPaperState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }


    fun getAllRgpvPaper(){
        viewModelScope.launch (Dispatchers.IO){
            timetableRepo.getRgpvPaper().collect {
                when(it){
                    is ResultState.Loading -> {
                        _getAllRgpvPaperState.value = RgpvPaperState(
                            isLoading = true
                        )
                    }
                    is ResultState.Error ->{
                        _getAllRgpvPaperState.value = RgpvPaperState(
                            isLoading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Success ->{
                        _getAllRgpvPaperState.value = RgpvPaperState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllShivani(){
        viewModelScope.launch(Dispatchers.IO) {
            timetableRepo.getShivani().collect {
                when(it){
                    is ResultState.Loading -> {
                        _getAllShivaniState.value = ShivaniState(
                            isLoading = true
                        )
                    }
                    is ResultState.Error -> {
                        _getAllShivaniState.value = ShivaniState(
                            isLoading = false,
                            error = it.message
                        )
                    }
                    is ResultState.Success ->{
                        _getAllShivaniState.value = ShivaniState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }

    }


    fun getAllLostItems(){
        viewModelScope.launch (Dispatchers.IO){
            timetableRepo.getLostItems().collect {
                when(it){
                    is ResultState.Loading -> {
                        _getAllLostItemsState.value = LostItemState(
                            isLoading = true
                        )
                    }
                    is ResultState.Error ->{
                        _getAllLostItemsState.value = LostItemState(
                            isLoading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Success ->{
                        _getAllLostItemsState.value = LostItemState(
                            isLoading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }


    fun addLostItem(userName: String, title: String, contactInfo: String , imageUri: Uri?,context: Context ) {
        if (imageUri == null) {
            _addPost.value = AddLostItemState(
                isLoading = false,
                error = "Please select an image"
            )
            return // Function se bahar nikal jao
        }

        viewModelScope.launch(Dispatchers.IO) {

            val base64Image = imageUri?.let { uri ->
                compressAndEncodeImages(context, uri)
            } ?: ""


            // Set loading state
            _addPost.value = AddLostItemState(
                isLoading = true
            )

            val item = LostItem(
                userName = userName,
                title = title,
                contactInfo = contactInfo,
                imageUrl = base64Image,
            )

            timetableRepo.addLostItem(item) { success ->

                if (success) {
                    _addPost.value = AddLostItemState(
                        isLoading = false,
                        success = true
                    )
                } else {
                    _addPost.value = AddLostItemState(
                        isLoading = false,
                        error = "Failed to upload"
                    )
                }
            }
        }
    }



}


data class TimetableState(
    val isLoading: Boolean = false,
    val success: List<Timetable> = emptyList(),
    val error: String? = null
)

data class MidSemState(
    val isLoading: Boolean = false,
    val success: List<Midsem> = emptyList(),
    val error: String? = null
)

data class HandWrittenNotesSemState(
    val isLoading: Boolean = false,
    val success: List<HandWrittenNotes> = emptyList(),
    val error: String? = null
)

data class MidSemPaperState(
    val isLoading: Boolean = false,
    val success: List<MidPaper> = emptyList(),
    val error: String? = null
)

data class RgpvPaperState(
    val isLoading: Boolean = false,
    val success: List<RgpvPaper> = emptyList(),
    val error: String? = null
)

data class ShivaniState(
    val isLoading: Boolean = false,
    val success: List<Shivani> = emptyList(),
    val error: String? = null
)



data class LostItemState(
    val isLoading: Boolean = false,
    val success: List<LostItem> = emptyList(),
    val error: String? = null
)

data class AddLostItemState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val error: String? = null
)


private fun compressAndEncodeImages(context: Context, imageUri: Uri): String? {
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