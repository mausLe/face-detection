package api

data class data(
    val image_encoded: String,
    val class_id: String,
    val model: String,
    val classifier: String
)