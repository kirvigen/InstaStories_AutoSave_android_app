package com.kirvigen.instagram.stories.autosave.ui.selectUserScreen

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.adapterProfiles.ProfileAdapter
import com.kirvigen.instagram.stories.autosave.databinding.ActivitySelectedProfilesBinding
import com.kirvigen.instagram.stories.autosave.utils.AdapterAnyActionObserver
import com.kirvigen.instagram.stories.autosave.utils.WrapContentGridLayoutManager
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import com.kirvigen.instagram.stories.autosave.utils.hideKeyboard
import com.kirvigen.instagram.stories.autosave.utils.showKeyboard
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.ArrayList

class SelectedProfilesActivity : AppCompatActivity() {

    private val viewModel: SelectedProfilesViewModel by viewModel()
    private var binding: ActivitySelectedProfilesBinding? = null
    private val adapterProfiles = ProfileAdapter({
        changedSelectedUser()
    }, {
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedProfilesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.recyclerProfiles?.layoutManager = WrapContentGridLayoutManager(this, 3)
        binding?.recyclerProfiles?.adapter = adapterProfiles

        binding?.back?.setOnClickListener {
            onBackPressed()
        }

        binding?.searchProfiles?.addTextChangedListener {
            viewModel.search(it.toString())
            binding?.clearText?.isVisible = it?.isNotEmpty() == true

        }

        binding?.clearText?.setOnClickListener {
            binding?.searchProfiles?.setText("")
        }

        viewModel.profilesSearch.observe(this) { profilesList ->
            if (profilesList.isEmpty() && adapterProfiles.getSelectedProfiles().isEmpty()) {
                binding?.recyclerProfiles?.animateAlpha(0f)
                binding?.descriptionFunction?.animateAlpha(1f)
            } else {
                binding?.descriptionFunction?.animateAlpha(0f)
                binding?.recyclerProfiles?.animateAlpha(1f)
            }
            TransitionManager.beginDelayedTransition(binding?.root, AutoTransition())
            binding?.descSelected?.isVisible =
                profilesList.isEmpty() && adapterProfiles.getSelectedProfiles().isNotEmpty()
            adapterProfiles.setData(profilesList)
        }

        adapterProfiles.registerAdapterDataObserver(AdapterAnyActionObserver {
            binding?.recyclerProfiles?.scrollToPosition(0)
        })

        binding?.searchProfiles?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding?.searchProfiles.hideKeyboard()
                true
            } else false
        })
        binding?.searchProfiles?.post {
            binding?.searchProfiles?.requestFocus()
            binding?.searchProfiles.showKeyboard()
        }
    }

    private fun initBtnSuccess() {
        binding?.btnSuccess?.setBackgroundResource(R.drawable.btn_accent_bg)
        binding?.btnSuccess?.setOnClickListener {
            binding?.btnSuccess?.setOnClickListener(null)
            val intent = Intent().apply {
                putParcelableArrayListExtra(
                    SelectedProfilesResultCallback.KEY_RESULT_PROFILES_RESULT,
                    ArrayList(adapterProfiles.getSelectedProfiles())
                )
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun disableBtnSuccess() {
        binding?.btnSuccess?.setBackgroundResource(R.drawable.btn_disable_bg)
        binding?.btnSuccess?.setOnClickListener(null)
    }

    private fun changedSelectedUser() {
        val count = adapterProfiles.getSelectedProfiles().size
        binding?.selectedUser?.text = getString(R.string.select_users, count)
        binding?.selectedUser?.isVisible = count != 0
        if (count == 0) {
            disableBtnSuccess()
        } else {
            initBtnSuccess()
        }
    }
}