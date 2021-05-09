package com.ishant.dumboo.ui.home

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.HomeFragmentBinding
import com.ishant.dumboo.ui.contact.adapter.FavContactyAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class HomeFragment : Fragment() {
    private lateinit var adapter: FavContactyAdapter
    private lateinit var binding: HomeFragmentBinding

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        lateinit var viewModel: HomeViewModel
        var Instance: HomeFragment? = null
        fun newInstance(viewModelM: HomeViewModel): HomeFragment? {
            viewModel = viewModelM
            if (Instance == null) {
                Instance = HomeFragment()
            }
            return Instance
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.home_fragment, container, false)
        (requireActivity() as DumbooBaseActivity).showCustomAlert("Please Wait While Favourite Contacts Is Loading",binding.root)
        (requireActivity() as HomeActivity).Toolbar().headerName.text=getString(R.string.fav)
        adapter = FavContactyAdapter(requireContext(), 1, viewModel)
        binding.ContactListRecycler.adapter = adapter
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    viewModel.repository.GetFavList().observe(requireActivity(), {
                        if (it != null && it.size > 0) {
                            binding.emptyContact.visibility=View.GONE
                            val sortedList = it.sortedWith(compareBy({ it.Name }))
                            adapter.UpdateList(sortedList)
                        }else{
                            binding.emptyContact.visibility=View.VISIBLE
                        }
                    })
                    binding.search.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            if(s.isNullOrEmpty()||s.isNullOrBlank()){
                                viewModel.repository.GetFavList().observe(requireActivity(), {
                                    if (it != null && it.size > 0) {
                                        binding.emptyContact.visibility=View.GONE
                                        val sortedList = it.sortedWith(compareBy({ it.Name }))
                                        adapter.UpdateList(sortedList)
                                    }else{
                                        binding.emptyContact.visibility=View.VISIBLE
                                    }
                                })
                            }else{
                                viewModel.repository.GetFavList(s.toString()).observe(requireActivity(),
                                    {
                                        if (it != null && it.size > 0) {
                                            binding.emptyContact.visibility=View.GONE
                                            val sortedList = it.sortedWith(compareBy({ it.Name }))
                                            adapter.UpdateList(sortedList)
                                        }else{
                                            binding.emptyContact.visibility=View.VISIBLE
                                        }
                                    })
                            }


                        }
                    })
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    (requireActivity() as DumbooBaseActivity).showCustomAlert(getString(R.string.require_phone_state),binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                }
            })
            .check()

        return binding.root

    }



}