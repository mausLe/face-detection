package api

data class Status(
    val sentCount: Int,
    val verified: Boolean
)

data class ThuaNguyenJSON(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val deleted: Boolean,
    val source: String,
    val status: Status,
    val text: String,
    val type: String,
    val updatedAt: String,
    val used: Boolean,
    val user: String

)
/*
val code: String,
    val status: String,
    val token: String
 */