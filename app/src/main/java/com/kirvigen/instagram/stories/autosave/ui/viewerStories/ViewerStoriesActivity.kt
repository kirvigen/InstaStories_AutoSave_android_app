package com.kirvigen.instagram.stories.autosave.ui.viewerStories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.ActivityViewerStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import com.kirvigen.instagram.stories.autosave.utils.setTransparentStatusBar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ViewerStoriesActivity : AppCompatActivity() {

    private var binding: ActivityViewerStoriesBinding? = null
    private val stories: Stories? by lazy { intent.getParcelableExtra(STORIES_KEY) }
    private val viewModel: ViewerStoriesViewModel by viewModel { parametersOf(stories) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewerStoriesBinding.inflate(layoutInflater)
        setTransparentStatusBar()
        setContentView(binding?.root)

        val recyclerViewInstance = binding?.viewPager?.children?.first { it is RecyclerView } as RecyclerView

        recyclerViewInstance.also {
            it.setPadding(PADDING_DEFAULT.dpToPx, 0, PADDING_DEFAULT.dpToPx, 0)
            it.clipToPadding = false
        }

        binding?.viewPager?.setOnApplyWindowInsetsListener { v, insets ->
            binding?.viewPager?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            return@setOnApplyWindowInsetsListener insets
        }

        viewModel.storiesData.observe(this) { storiesList ->
            val position = storiesList.indexOf(stories)
            binding?.viewPager?.adapter = StoriesPagerAdapter(this, storiesList)
            binding?.viewPager?.setCurrentItem(position, false)
        }
    }



    companion object {
        private const val PADDING_DEFAULT = 25
        private const val STORIES_KEY = "stories_key"

        fun startView(stories: Stories, context: Context) {
            val intent = Intent(context, ViewerStoriesActivity::class.java)
            intent.putExtra(STORIES_KEY, stories)
            context.startActivity(intent)
            (context as? Activity)?.overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
        }
    }
}