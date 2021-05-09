package com.ishant.dumboo.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ishant.dumboo.R
import com.ishant.dumboo.databinding.SettingFragmentBinding
import com.ishant.dumboo.ui.home.HomeActivity
import com.ishant.dumboo.ui.home.HomeViewModel
import com.ishant.dumboo.ui.profile.ProfileActivity
import com.ishant.dumboo.ui.webbrowser.webui.WebActivity


class SettingFragment : Fragment() {
    private lateinit var binding: SettingFragmentBinding

    companion object {
        lateinit var viewModel: HomeViewModel
        var Instance: SettingFragment? = null
        fun newInstance(viewModelM: HomeViewModel): SettingFragment? {
            viewModel = viewModelM
            if (Instance == null) {
                Instance = SettingFragment()
            }
            return Instance
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.setting_fragment,
            container,
            false
        )
        binding.name.setText(viewModel.sharedPre.name)
        binding.mobileNumber.setText(viewModel.sharedPre.userMobile)
        if (viewModel.sharedPre.getNameAnnouncer()) {
            binding.isAnouncer.setImageResource(R.drawable.check1)
        } else {
            binding.isAnouncer.setImageResource(R.drawable.check_not_select)
        }
        OnClickListners()
        (requireActivity() as HomeActivity).Toolbar().headerName.text = getString(R.string.setting)
        return binding.root
    }

    private fun OnClickListners() {

        binding.isAnouncer.setOnClickListener(View.OnClickListener {
            if (viewModel.sharedPre.getNameAnnouncer()) {
                viewModel.sharedPre.setNameAnnouncer(false)
                binding.isAnouncer.setImageResource(R.drawable.check_not_select)
            } else {
                viewModel.sharedPre.setNameAnnouncer(true)
                binding.isAnouncer.setImageResource(R.drawable.check1)
            }
        })
        binding.meetDeveloper.setOnClickListener {
            startActivity(
                Intent(activity, WebActivity::class.java).putExtra(
                    "url", getString(
                        R.string.meetDeveloper
                    )
                )
            )
        }
        binding.profile.setOnClickListener {
            startActivity(
                Intent(activity, ProfileActivity::class.java)
            )
        }
    }


}