package tn.esprit.chiccercle.model;

import java.io.Serializable
import java.util.Date

data class User(

    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: Int? = null,
    val role: String,
    val pictureProfile: String? = null,  // Store the file path or URL
    val birthday: Date? = null,
    val clothes: List<Clothes?>,
    val assemble: List<Assemble?>,
    val height: Int? = null,
    val weight: Int? = null,
    val shape: String? = null,
    val location: String? = null,

    ) : Serializable
