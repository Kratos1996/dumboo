package com.ishant.dumboo.ui.contact.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ishant.dumboo.R
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.ContactList
import com.ishant.dumboo.databinding.FavContactItemBinding
import com.ishant.dumboo.ui.contact.adapter.FavContactyAdapter.FavContactViewHolder
import java.util.*
import kotlin.collections.ArrayList

class FavContactyAdapter(var context: Context,val type:Int) :
    RecyclerView.Adapter<FavContactViewHolder>() {
    private var getAllContact: List<ContactList> = ArrayList()
    private var selected: ArrayList<ContactList> = ArrayList()
    private var sharedPre:SharedPre?=null
    init {
        sharedPre= SharedPre.getInstance(context)
    }

    fun UpdateList(list: List<ContactList>) {
        this.getAllContact=list
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
        holder.binding.headername.text=getAllContact.get(position).Name
        holder.binding.contactNumber.text=getAllContact.get(position).PhoneNumber
        if(type==1){
            holder.binding.isContactSelect.visibility=View.VISIBLE
            holder.binding.isContactSelect.setOnClickListener {
                if(holder.binding.isContactSelect.isSelected){
                    for (i in 0.. getAllContact.size) {
                        if (selected.get(i).PhoneNumber.equals(getAllContact.get(i).PhoneNumber)) {
                            selected.removeAt(i)
                            sharedPre!!.setConatctList(Gson().toJson(selected,ContactList::class.java))
                        }
                    }
                } else {
                    selected.add(getAllContact.get(position))
                    sharedPre!!.setConatctList(Gson().toJson(selected,ContactList::class.java))
                }
            }
        }else{
            holder.binding.isContactSelect.visibility=View.INVISIBLE
        }


    }

    override fun getItemCount(): Int {
        return getAllContact.size
    }

    inner class FavContactViewHolder(val binding: FavContactItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    }

