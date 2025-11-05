package tn.esprit.chiccercle.model

import com.google.gson.annotations.SerializedName
import java.util.Date


data class Assemble(
    @SerializedName("_id")  // This maps the backend "_id" to "id" in your model
    val id: String,
    val user : String,
    val name: String,
    val price : Double ,
    val image: String? ,
    val clothes: List<String>, // List of Clothes objects
    val date: Date
)