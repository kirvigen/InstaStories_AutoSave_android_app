package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.ProfileAdapter
import com.kirvigen.instagram.stories.autosave.databinding.ActivitySelectedProfilesBinding
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import org.koin.android.viewmodel.ext.android.viewModel

class SelectedProfilesActivity : AppCompatActivity() {

    private val viewModel: SelectedProfilesViewModel by viewModel()
    private var binding: ActivitySelectedProfilesBinding? = null
    private val adapterProfiles = ProfileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedProfilesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.recyclerProfiles?.layoutManager = GridLayoutManager(this, 3)
        binding?.recyclerProfiles?.adapter = adapterProfiles
//        binding?.recyclerProfiles?.itemAnimator = null

        binding?.back?.setOnClickListener {
            onBackPressed()
        }
        binding?.searchProfiles?.addTextChangedListener {
            viewModel.search(it.toString())
        }

        viewModel.profilesSearch.observe(this, { profilesList ->
            if (profilesList.isEmpty()) {
                binding?.recyclerProfiles?.animateAlpha(0f)
            } else {
                binding?.recyclerProfiles?.animateAlpha(1f)
                adapterProfiles.setData(profilesList)
            }
        })

        adapterProfiles.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding?.recyclerProfiles?.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding?.recyclerProfiles?.scrollToPosition(0)
            }
        })
    }
}