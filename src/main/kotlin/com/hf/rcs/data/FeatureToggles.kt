package com.hf.rcs.data
import kotlinx.serialization.Serializable

@Serializable
data class FeatureToggles(val singleFeature: List<SingleFeature>)

@Serializable
data class SingleFeature(val id: String, val enabled: Boolean)

val simpleFeatures = mutableListOf(SingleFeature("1", false), SingleFeature("2",false))