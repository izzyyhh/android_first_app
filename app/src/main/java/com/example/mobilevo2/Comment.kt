package com.example.mobilevo2

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Comment (
    @JvmField @DocumentId
    var documentId: String ="",
    var author: Author = Author(),
    var text: String ="",
    var timestamp: Timestamp = Timestamp.now()
)