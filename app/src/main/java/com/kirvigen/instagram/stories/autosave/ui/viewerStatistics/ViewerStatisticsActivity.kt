package com.kirvigen.instagram.stories.autosave.ui.viewerStatistics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import circlegraph.velmurugan.com.circlegraph.CircleItems
import circlegraph.velmurugan.com.circlegraph.CircleType
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.databinding.ActivityViewerStatisticsBinding
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.AdapterStatisticsProfile
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import org.koin.android.viewmodel.ext.android.viewModel

class ViewerStatisticsActivity : AppCompatActivity(), MenuProfileCreator.MenuProfileCallbacks {

    private val viewModel: ViewerStatisticViewModel by viewModel()
    private var binding: ActivityViewerStatisticsBinding? = null
    private val adapter = AdapterStatisticsProfile(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewerStatisticsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.back?.setOnClickListener {
            onBackPressed()
        }
        binding?.recyclerProfiles?.adapter = adapter

        viewModel.mainList.observe(this) { listData ->
            listData?.let { data -> adapter.submitList(data) }
        }
        viewModel.mainInfo.observe(this) { statisticsInfo ->
            binding?.videoDescTv?.text = getString(R.string.video) + ": " + statisticsInfo.photoSize
            binding?.imageDescTv?.text = getString(R.string.photo) + ": " + statisticsInfo.videoSize
            binding?.textUnit?.text = statisticsInfo.commonSize.split(" ")[1]
            binding?.textSize?.text = statisticsInfo.commonSize.split(" ")[0]
            setGraph(statisticsInfo.videoPercent, statisticsInfo.photoPercent)
        }
    }

    private fun setGraph(videoPercent: Int, photoPercent: Int) {
        val circleItemsList = mutableListOf<CircleItems>()
        binding?.graph?.strokeSize = WIDTH_GRAPH.dpToPx
        binding?.graph?.isTextEnabled = false
        binding?.graph?.setselectedItem(2)
        binding?.graph?.setCircleType(CircleType.FULL_CIRCLE)

        val videoItem = CircleItems(videoPercent, ContextCompat.getColor(this, R.color.insta_color_3))
        circleItemsList.add(videoItem)

        val photoItem = CircleItems(photoPercent, ContextCompat.getColor(this, R.color.red_orange))
        circleItemsList.add(photoItem)
        binding?.graph?.addItems(circleItemsList)
    }

    override fun onOpenProfileListener(profileId: Long) {
        viewModel.goToProfileInstagram(profileId, this)
    }

    override fun onDeleteProfileListener(profileId: Long) {
        viewModel.deleteProfile(profileId)
    }

    companion object {
        private const val WIDTH_GRAPH = 5
    }
}