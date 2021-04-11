package api

data class Data(
    val class_id: String,
    val classifier: String,
    val image_encoded: String,
    val model: String
)