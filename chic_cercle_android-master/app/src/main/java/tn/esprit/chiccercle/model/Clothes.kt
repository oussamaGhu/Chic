package tn.esprit.chiccercle.model

import com.google.gson.annotations.SerializedName

data class Clothes(
    @SerializedName("_id")  // This maps the backend "_id" to "id" in your model
    val id: String,
    val user: String,
    val images: String?, // Correspond à l'ID MongoDB sous forme de chaîne
    val occasions: List<String>,
    val moods: List<String>,
    val weather: List<String>,
    val types: List<String >?= null,
    val colors: List<String>,
    val price: Double? = null, // Prix nullable
    val name: String? = null, // Nom nullable
    val description: String? = null, // Description nullable
    val size: String? = null
)

