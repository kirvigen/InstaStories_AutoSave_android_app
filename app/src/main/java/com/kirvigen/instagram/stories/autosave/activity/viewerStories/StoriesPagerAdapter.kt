package com.kirvigen.instagram.stories.autosave.activity.viewerStories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kirvigen.instagram.stories.autosave.activity.viewerStories.storiesFragment.StoriesFragment
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

class StoriesPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val storiesItems: List<Stories>
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment = StoriesFragment.newInstance(storiesItems[position])

    override fun getItemCount(): Int = storiesItems.size
}