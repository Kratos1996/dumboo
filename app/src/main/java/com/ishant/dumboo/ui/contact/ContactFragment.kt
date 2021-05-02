package com.ishant.dumboo.ui.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.HomeFragmentBinding
import com.ishant.dumboo.ui.contact.adapter.FavContactyAdapter
import com.ishant.dumboo.ui.home.HomeActivity
import com.ishant.dumboo.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.view.*

class ContactFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: FavContactyAdapter

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        lateinit var viewModel: HomeViewModel
        var Instance: ContactFragment? = null
        fun newInstance(viewModelM: HomeViewModel): ContactFragment? {
            viewModel = viewModelM
            if (Instance == null) {
                Instance = ContactFragment()
            }
            return Instance
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.home_fragment,
            container,
            false
        )
        (requireActivity() as DumbooBaseActivity).showCustomAlert("Please Wait While Contacts Is Loading",binding.root)
        (requireActivity() as HomeActivity).toolbar.headerName.text=getString(R.string.contacts)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            viewModel.LoadContact(requireActivity())
        }
        adapter = FavContactyAdapter(requireContext(), 1, viewModel)
        binding.ContactListRecycler.adapter = adapter
        viewModel.repository.GetContactList().observe(requireActivity(), Observer {
            if (it != null && it.size > 0) {
                var sortedList = it.sortedWith(compareBy({ it.Name }))
                adapter.UpdateList(sortedList)
            }
        })

        return binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.LoadContact(requireActivity())
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

}