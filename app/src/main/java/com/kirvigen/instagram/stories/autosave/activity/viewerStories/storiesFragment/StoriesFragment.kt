package com.kirvigen.instagram.stories.autosave.activity.viewerStories.storiesFragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
import com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.FragmentViewStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

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

        viewModel.profile.observe(viewLifecycleOwner, { profile ->
            binding?.profileTitle?.text = profile.name
            binding?.profileImage?.loadImage(profile.photo, false)
        })

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