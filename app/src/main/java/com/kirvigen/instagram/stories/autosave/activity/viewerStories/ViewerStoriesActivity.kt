package com.kirvigen.instagram.stories.autosave.activity.viewerStories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
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
import org.koin.core.parameter.parametersOf

class ViewerStoriesActivity : AppCompatActivity() {

    private var binding: ActivityViewerStoriesBinding? = null
    private val stories: Stories? by lazy { intent.getParcelableExtra(STORIES_KEY) }
    private val viewModel: ViewerStoriesViewModel by inject { parametersOf(stories) }

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

        viewModel.storiesData.observe(this, { storiesList ->
            val position = storiesList.indexOf(stories)
            binding?.viewPager?.adapter = StoriesPagerAdapter(this, storiesList)
            binding?.viewPager?.setCurrentItem(position, false)
        })
//
//        val onThumbTouch: OnTouchListener = object : OnTouchListener {
//            var previouspoint = 0f
//            var startPoint = 0f
//            override fun onTouch(v: View, event: MotionEvent): Boolean {
//                when (v.id) {
//                    R.id.tvDetailsalaujairiyat -> when (event.action) {
//                        MotionEvent.ACTION_DOWN -> {
//                            startPoint = event.x
//                            println("Action down,..." + event.x)
//                        }
//                        MotionEvent.ACTION_MOVE -> {}
//                        MotionEvent.ACTION_CANCEL -> {
//                            previouspoint = event.x
//                            if (previouspoint > startPoint) {
//                                //Right side swipe
//                            } else {
//                                // Left side swipe
//                            }
//                        }
//                    }
//                }
//                return true
//            }
//        }
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