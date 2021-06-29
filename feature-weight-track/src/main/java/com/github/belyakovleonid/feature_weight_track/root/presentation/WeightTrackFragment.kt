package com.github.belyakovleonid.feature_weight_track.root.presentation

import android.os.Bundle
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.belyakovleonid.core.presentation.base.BaseFragment
import com.github.belyakovleonid.core.presentation.getCurrentDate
import com.github.belyakovleonid.core.presentation.getDependencies
import com.github.belyakovleonid.core.presentation.viewModel
import com.github.belyakovleonid.core.presentation.withParams
import com.github.belyakovleonid.feature_weight_track.R
import com.github.belyakovleonid.feature_weight_track.base.di.WeightTrackApiProvider
import com.github.belyakovleonid.feature_weight_track.base.di.WeightTrackComponentHolder
import com.github.belyakovleonid.feature_weight_track.databinding.EmptyChartLayoutBinding
import com.github.belyakovleonid.feature_weight_track.databinding.EmptyGoalLayoutBinding
import com.github.belyakovleonid.feature_weight_track.databinding.FWeightTrackBinding
import com.github.belyakovleonid.feature_weight_track.goalpicker.presentation.GoalPickerDialogFragment
import com.github.belyakovleonid.feature_weight_track.weightpicker.presentation.WeightPickerDialogFragment
import com.github.belyakovleonid.feature_weight_track.weightpicker.presentation.params.WeightPickerParams
import java.time.LocalDate

class WeightTrackFragment : BaseFragment<WeightTrackContract.State, WeightTrackContract.SideEffect>(
    R.layout.f_weight_track
) {

    private lateinit var injector: WeightTrackApiProvider
    override val viewModel: WeightTrackViewModel by viewModel { injector.weightTrackViewModel }

    private val binding by viewBinding(FWeightTrackBinding::bind)
    private val emptyGoalBinding by viewBinding(EmptyGoalLayoutBinding::bind)
    private val emptyChartBinding by viewBinding(EmptyChartLayoutBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector = WeightTrackComponentHolder.getInstance(getDependencies())
    }

    override fun setupView() {
        binding.chartView.onItemSelectListener = {
            submitEvent(WeightTrackContract.Event.ChartItemClicked(it))
        }
        binding.fabAdd.setOnClickListener {
            updateWeightTrack()
        }
        binding.goalSubtitle.setOnClickListener {
            submitEvent(WeightTrackContract.Event.ChooseGoalClick)
            //todo вынести в навигацию ч/з cicerone
            GoalPickerDialogFragment().show(childFragmentManager, null)
        }
        binding.fabDelete.setOnClickListener {
            submitEvent(WeightTrackContract.Event.DeleteSelectedTrack)
        }
        binding.fabEdit.setOnClickListener {
            viewModel.getSelectedDate()?.let { selectedDate ->
                updateWeightTrack(selectedDate)
            }
        }
        emptyGoalBinding.chooseGoalButton.setOnClickListener {
            submitEvent(WeightTrackContract.Event.ChooseGoalClick)
            //todo вынести в навигацию ч/з cicerone
            GoalPickerDialogFragment().show(childFragmentManager, null)
        }
        emptyChartBinding.emptyChartFabAdd.setOnClickListener {
            updateWeightTrack()
        }
    }

    private fun updateWeightTrack(date: LocalDate? = null) {
        //todo вынести в навигацию ч/з cicerone
        WeightPickerDialogFragment().withParams(WeightPickerParams(date ?: getCurrentDate()))
            .show(childFragmentManager, null)
    }

    override fun renderState(state: WeightTrackContract.State): Unit = with(binding) {
        goalSubtitle.text =
            resources.getString(R.string.weight_track_goal_subtitle, state.goalWeight)
        titleGroup.isVisible = state.isGoalVisible
        chartGroup.isVisible = state.isChartVisible
        fabEditGroup.isVisible = state.isEditFabVisible
        fabAdd.isVisible = state.isAddFabVisible
        chartView.setData(state.chartData)
        emptyGoalBinding.emptyGoalGroup.isVisible = state.isEmptyGoalVisible
        emptyChartBinding.emptyChartGroup.isVisible = state.isEmptyChartVisible
    }

    override fun reactToSideEffect(effect: WeightTrackContract.SideEffect) = with(binding) {
        chartView.animateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity?.isFinishing == true) {
            WeightTrackComponentHolder.release()
        }
    }
}