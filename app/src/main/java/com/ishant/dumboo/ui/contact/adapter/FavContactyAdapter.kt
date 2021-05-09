package com.ishant.dumboo.ui.contact.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ishant.dumboo.R
import com.ishant.dumboo.custom.RecyclerViewFastScroller
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.ContactList
import com.ishant.dumboo.databinding.FavContactItemBinding
import com.ishant.dumboo.ui.contact.adapter.FavContactyAdapter.FavContactViewHolder
import com.ishant.dumboo.ui.home.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.ln
import kotlin.math.pow

class FavContactyAdapter(var context: Context, val type: Int, val viewModel: HomeViewModel) :
    RecyclerView.Adapter<FavContactViewHolder>() , RecyclerViewFastScroller.OnPopupTextUpdate {
    private var getAllContact: List<ContactList> = ArrayList()
    private var sharedPre:SharedPre?=null
    private var handleDrawable: Drawable?=null
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
        if(getAllContact.get(position).isFav){
            holder.binding.isContactSelect.setImageResource(R.drawable.check1)
        }else{
            holder.binding.isContactSelect.setImageResource(R.drawable.check_not_select)
        }
        holder.binding.contactNumber.text=getAllContact.get(position).PhoneNumber
        if(type==1){
            holder.binding.isContactSelect.visibility=View.VISIBLE
            holder.binding.isContactSelect.setOnClickListener {
                if(getAllContact.get(position).isFav){
                    holder.binding.isContactSelect.setImageResource(R.drawable.check_not_select)
                    GlobalScope.launch {
                        viewModel.repository.SetFavContact(getAllContact.get(position).PhoneNumber,false)
                    }
                } else {
                    holder.binding.isContactSelect.setImageResource(R.drawable.check1)
                    GlobalScope.launch {
                        viewModel.repository.SetFavContact(getAllContact.get(position).PhoneNumber,true)
                    }
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
    override fun onChange(position: Int): CharSequence {
        return if (getAllContact[position].Name.matches(Patterns.PHONE.toRegex()))
            "#"
        else
            getAllContact[position].Name[0].toString().toUpperCase()
    }
     fun onUpdate(position: Int, popupTextView: TextView) {
        popupTextView.background.colorFilter = PorterDuffColorFilter(
            getColor(power = position),
            PorterDuff.Mode.SRC_IN
        )
        handleDrawable?.colorFilter = PorterDuffColorFilter(
            getColor(power = position),
            PorterDuff.Mode.SRC_IN
        )

        popupTextView.text = getAllContact[position].PhoneNumber.toLong().withSuffix()
    }
    private fun Long.withSuffix(): String {
        if (this < 1000) return "" + this
        val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
        return String.format(
            "%.1f%c",
            this / 1000.0.pow(exp.toDouble()),
            "kMBTPE"[exp - 1]
        )
    }

    private fun getColor(power: Int): Int {
        val h = (itemCount - power) * 100 / itemCount
        val s = 1 // Saturation
        val v = 0.8 // Value
        return Color.HSVToColor(floatArrayOf(h.toFloat(), s.toFloat(), v.toFloat()))
    }

}

