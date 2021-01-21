package com.example.mobilevo2.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class Person(
    @JvmField @PropertyName("full_name")
    var fullName: String = "",
    @JvmField @PropertyName("profile_picture")
    var profilePicture: String? = null,

    var description: String? = null,
    var following: List<DocumentReference> = emptyList(),
    var posts: List<DocumentReference> = emptyList(),
    var followers: List<DocumentReference> = emptyList()
)