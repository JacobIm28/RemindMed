package com.gradle.apiCalls

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class rxNormData(
    val approximateGroup: ApproximateGroup
)

@Serializable
data class ApproximateGroup(
    @SerialName("inputTerm")
    val inputTerm: String?, // Nullable as per the JSON structure
    val candidate: List<Candidate>
)

@Serializable
data class Candidate(
    val rxcui: String,
    val rxaui: String,
    val score: String,
    val rank: String,
    val name: String?, // Nullable as per the JSON structure
    val source: String
)
