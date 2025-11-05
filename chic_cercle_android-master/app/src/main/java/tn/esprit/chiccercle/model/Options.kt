package tn.esprit.chiccercle.model

data class Options (
        val enable: Boolean,
        val type: String, // Liste pour correspondre à `@IsEnum(TypesC, { each: true })`
        val id: String? = null, // Propriété optionnelle
        val color: String? = null // Propriété optionnelle
    )
