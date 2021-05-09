package com.ishant.dumboo.ui.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.home_fragment, container, false)
        (requireActivity() as DumbooBaseActivity).showCustomAlert(getString(R.string.contact_loading), binding.root)
        (requireActivity() as HomeActivity).Toolbar().headerName.text = getString(R.string.contacts)
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    viewModel.LoadContact(requireActivity())
                }
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    (requireActivity() as DumbooBaseActivity).showCustomAlert(getString(R.string.contact_permission), binding.root)
                }
                override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {} }).check()

        adapter = FavContactyAdapter(requireContext(), 1, viewModel)
        binding.ContactListRecycler.adapter = adapter
        viewModel.repository.GetContactList().observe(requireActivity(), Observer {
            if (it != null && it.size > 0) {
                binding.emptyContact.visibility = View.GONE
                val sortedList = it.sortedWith(compareBy({ it.Name }))
                adapter.UpdateList(sortedList)
            } else {
                adapter.UpdateList(emptyList())
                binding.emptyContact.visibility = View.VISIBLE
            }
        })
        binding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()||s.isNullOrBlank()){
                    viewModel.repository.GetContactList().observe(requireActivity(), {
                        if (it != null && it.size > 0) {
                            binding.emptyContact.visibility = View.GONE
                            val sortedList = it.sortedWith(compareBy({ it.Name }))
                            adapter.UpdateList(sortedList)
                        } else {
                            adapter.UpdateList(emptyList())
                            binding.emptyContact.visibility = View.VISIBLE
                        }
                    })
                }else{
                    viewModel.repository.GetContactList(s.toString()).observe(requireActivity(), {
                        if (it != null && it.size > 0) {
                            binding.emptyContact.visibility = View.GONE
                            val sortedList = it.sortedWith(compareBy({ it.Name }))
                            adapter.UpdateList(sortedList)
                        } else {
                            adapter.UpdateList(emptyList())
                            binding.emptyContact.visibility = View.VISIBLE
                        }
                    })
                }


            }
        })

        return binding.root
    }


}