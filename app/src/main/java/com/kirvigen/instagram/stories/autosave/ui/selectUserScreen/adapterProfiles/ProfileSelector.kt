package com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.adapterProfiles

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface ProfileSelector {

    fun onSelect(profile: Profile)

    fun onUnselect(profile: Profile)

}