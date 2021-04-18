package com.ishant.dumboo.ui.contact.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ishant.dumboo.R
import com.ishant.dumboo.databinding.FavContactItemBinding
import com.ishant.dumboo.ui.contact.adapter.FavContactyAdapter.FavContactViewHolder
import java.util.*
import kotlin.collections.ArrayList

class FavContactyAdapter( var context: Context) :
    RecyclerView.Adapter<FavContactViewHolder>() {
    private val getAllContact: ArrayList<String> = ArrayList()

    fun UpdateList(number: String) {
        getAllContact.add(number)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavContactViewHolder {
        var binding: FavContactItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fav_contact_item,
            parent,
            false
        )
        return FavContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavContactViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return getAllContact.size
    }

    inner class FavContactViewHolder( binding: FavContactItemBinding) : RecyclerView.ViewHolder(binding.root)

    }

