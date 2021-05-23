package com.github.belyakovleonid.feature_statistics.presentation.model

import com.github.belyakovleonid.core.presentation.getDateString
import com.github.belyakovleonid.core.presentation.getWeightString
import com.github.belyakovleonid.feature_statistics.domain.model.WeightTrack
import kotlinx.datetime.LocalDate

data class WeightTrackUiModel(
    val isSelected: Boolean,
    val weight: Float,
    val date: LocalDate,
    val weightText: String,
    val dateText: String
)


fun WeightTrack.toUi() = WeightTrackUiModel(
    isSelected = false,
    weight = weight,
    date = date,
    weightText = getWeightString(weight),
    dateText = getDateString(date)
)