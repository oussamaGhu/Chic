package tn.esprit.chiccercle.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("_id")  // This maps the backend "_id" to "id" in your model
    val id: String?,
    val sellerId: String?,
    val clientId: String?,
    val nameClient: String?,
    val clientPhone: Int?,
    val clientMail: String?,
    val itemId: String?,
    val isClothes: Boolean?,
    val isSold: Boolean?,
    val nameSeller: String?,
    val nameClothes: String?,
    var PriceClothes : Double?
)