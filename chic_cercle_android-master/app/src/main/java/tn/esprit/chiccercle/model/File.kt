package tn.esprit.chiccercle.model

import java.io.Serializable

data class File(
    val filename: String,
    val path: String,
    val size: Int,
    val mimetype: String
) : Serializable