package com.example.collagemajorproject.DataModel


data class ProfileData(
    val name: String = "",
    val collegeName: String = "",
    val rollNumber: String = "",
    val department: String = "",
    val age: String = "",
    val bloodGroup: String = "",
    val mobile: String = "",
    val email: String = "",
    val imageUrl: String = ""

)


data class Notes(
    val id: String = "",
    val quote: String = "",
    val book: String = "",
    val author: String = "",
    val page: String = ""

    )

data class Timetable(
    val branch: String = "",
    val year: String = "",
    val timetableImage: String = "",
    val image: String = ""
)

data class Midsem(
    val branch: String = "",
    val image: String = "",
    val semC: String = "",
    val semF: String = "",
    val timetableImage: String = ""
)

data class HandWrittenNotes(
    val branch: String = "",
    val image: String = "",
    val pdf: String = "",
    val subjectCode: String = "",
    val subjectName: String = ""
)


data class MidPaper(
    val subjectCode: String = "",
    val subjectName: String = "",
    val image: String = "",
    val pdf : String = "",
    val branch: String = "",
    val year : String = ""
)


data class RgpvPaper(
    val subjectCode: String = "",
    val subjectName: String = "",
    val year: String = "",
    val branch: String = "",
    val image: String = "",
    val pdf: String = ""
)

data class Shivani(
    val subjectCode: String = "",
    val subjectName: String = "",
    val year: String = "",
    val branch: String = "",
    val image: String = "",
    val pdf: String = ""
)

data class LostItem(


    val userName: String = "",
    val title: String = "",
    val contactInfo: String = "",
    val id: String = "",
    val imageUrl: String = "",

    // val userId: String = "",
    // val userImage: String = "",
    //val image: String = "", // Base64 encoded
    //val comments: Int = 0,

)







