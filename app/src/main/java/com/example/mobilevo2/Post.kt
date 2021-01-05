package com.example.mobilevo2

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class PostFirebase(
    @JvmField @DocumentId
    var documentId: String = "",

    @JvmField @PropertyName("image_url")
    var imageUrl: String = "",

    var author : Author = Author(),
    var text: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var likes : List<DocumentReference> = emptyList()
)