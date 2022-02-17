package com.kirvigen.instagram.stories.autosave.ui.viewerStories.storiesFragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.FragmentViewStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class StoriesFragment : Fragment() {

    private var binding: FragmentViewStoriesBinding? = null
    private val stories: Stories? by lazy { arguments?.getParcelable(STORIES_KEY) }
    private val viewModel: StoriesViewModule by inject { parametersOf(stories) }
    private var simpleExoPlayer: ExoPlayer? = null
    private var isPlayingBeforeClose: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_stories, container, false).also { v ->
            binding = FragmentViewStoriesBinding.bind(v)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            val simpleDateFormat = SimpleDateFormat("d MMM в HH:mm", Locale.getDefault())
            val date = " • " + simpleDateFormat.format(Date((stories?.date ?: return@observe) * 1000))
            binding?.profileTitle?.text = "${profile.nickname} $date"
            binding?.profileImage?.loadImage(profile.photo, false)
        }
        viewModel.savedLoader.observe(viewLifecycleOwner) {
            setBlockScreen(it)
        }

        binding?.playerView?.isVisible = stories?.isVideo ?: false
        binding?.imageView?.isVisible = !(stories?.isVideo ?: false)

        val urlContent = if (stories?.localUri?.isEmpty() == true) {
            stories?.sourceMedia
        } else {
            stories?.localUri
        }

        if (stories?.isVideo == true) {
            setVideo(urlContent ?: return)
        } else {
            setImage(urlContent ?: return)
        }

        binding?.share?.setOnClickListener {
            stories?.let { stories -> viewModel.shareStory(stories, context ?: return@let) }
        }
        binding?.saveToGallery?.setOnClickListener {
            stories?.let { stories -> viewModel.saveToGallery(stories, context ?: return@let) }
        }

    }

    private fun setBlockScreen(block: Boolean) {
        if (block) {
            binding?.loadingBlock?.isVisible = true
        }
        binding?.loadingBlock?.animateAlpha(if (block) 1f else 0f) {
            if (!block) {
                binding?.loadingBlock?.isVisible = false
            }
        }

        val alphaCommonElement = if (block) 0f else 1f
        binding?.share?.animateAlpha(alphaCommonElement)
        binding?.saveToGallery?.animateAlpha(alphaCommonElement)
        binding?.profileImage?.animateAlpha(alphaCommonElement)
        binding?.profileTitle?.animateAlpha(alphaCommonElement)
    }

    private fun setImage(source: String) {
        binding?.imageView?.loadImage(source)
    }

    private fun setVideo(source: String) {
        context ?: return
        simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()

        simpleExoPlayer?.addMediaItem(
            MediaItem.Builder()
                .setUri(Uri.parse(source))
                .build()
        )

        simpleExoPlayer?.playWhenReady = false
        simpleExoPlayer?.repeatMode = Player.REPEAT_MODE_ALL
//        simpleExoPlayer?.videoScalingMode = VIDEO_SCALING_MODE_SCALE_TO_FIT
        binding?.playerView?.useController = false
        binding?.playerView?.player = simpleExoPlayer

        binding?.mainContainer?.setOnClickListener {
            if (simpleExoPlayer?.isPlaying == true) {
                simpleExoPlayer?.pause()
            } else {
                simpleExoPlayer?.play()
            }
        }

        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        simpleExoPlayer?.playWhenReady = false
        simpleExoPlayer?.prepare()

        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onRenderedFirstFrame() {
                binding?.playerView?.animateAlpha(1f)
                if (isResumed)
                    simpleExoPlayer?.play()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isPlayingBeforeClose = simpleExoPlayer?.isPlaying ?: false
        simpleExoPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer?.seekTo(0)
        simpleExoPlayer?.play()
    }

    override fun onDetach() {
        simpleExoPlayer?.stop()
        simpleExoPlayer?.release()
        simpleExoPlayer = null
        super.onDetach()
    }

    companion object {
        private const val STORIES_KEY = "stories"

        fun newInstance(stories: Stories): StoriesFragment = StoriesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(STORIES_KEY, stories)
            }
        }
    }
}