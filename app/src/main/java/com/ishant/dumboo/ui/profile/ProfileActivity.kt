package com.ishant.dumboo.ui.profile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.ActivityProfileBinding
import com.ishant.dumboo.ui.home.HomeViewModel
import com.ishant.dumboo.ui.profile.model.UserData
import com.ishant.dumboo.ui.setting.SettingFragment

class ProfileActivity : DumbooBaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var context: Context
    private var Gender: String = "Male"
    private var UserID: String = ""
    private var Profile: String = ""
    private var data: UserData = UserData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        context = this
        UserID = viewmodel.sharedPre.userId!!
        binding.phone.setText(viewmodel.sharedPre.userMobile!!)
        viewmodel.GetUserDetail().observe(this, {
            if (it != null) {
                binding.email.setText(it.Email)
                data.Name = it.Name
                data.Gender = it.Gender
                data.Profile = it.Profile
                data.Mobile = it.Mobile
                data.LastUpdatedProfile = it.LastUpdatedProfile
                data.UserId = viewmodel.sharedPre.userId!!
                data.UserId = viewmodel.sharedPre.userId!!
                binding.name.setText(it.Name)
                Gender = (it.Gender)
                if (Gender.equals("Male")) {
                    binding.maleRadio.setChecked(true)
                    binding.femaleRadio.setChecked(false)
                } else {
                    binding.femaleRadio.setChecked(true)
                    binding.maleRadio.setChecked(false)
                }
                Glide.with(context).load(it.Profile).into(binding.userDp)
            }
        })
        binding.maleRadio.setOnClickListener {
            Gender = "Male"
            binding.maleRadio.setChecked(true)
            binding.femaleRadio.setChecked(false)
        }
        binding.femaleRadio.setOnClickListener {
            Gender = "Female"
            binding.maleRadio.setChecked(false)
            binding.femaleRadio.setChecked(true)
        }
        binding.submit.setOnClickListener {
            if (binding.name.text.isNullOrEmpty()) {
                showCustomAlert("Please Enter Name", binding.root)

            } else if (binding.email.text.isNullOrEmpty()) {
                showCustomAlert("Please Enter Email", binding.root)
            } else if (binding.phone.text.isNullOrEmpty()) {
                showCustomAlert("Please Enter Phone Number", binding.root)
            } else if (Gender.isNullOrEmpty()) {
                showCustomAlert("Please Select Gender", binding.root)
            } else {
                data = UserData(
                    binding.name.text.toString(),
                    Gender,
                    binding.email.text.toString(),
                    UserID,
                    System.currentTimeMillis(),
                    Profile,
                    binding.phone.text.toString()
                )
                viewmodel.SetUserDetail(data).observe(this, Observer {
                    if (it != null) {
                        viewmodel.sharedPre.setName(binding.name.text.toString())
                        showCustomAlert(it, binding.root)

                    }
                })
            }
        }
binding.ivBack.setOnClickListener {
    finish()
}
    }
}