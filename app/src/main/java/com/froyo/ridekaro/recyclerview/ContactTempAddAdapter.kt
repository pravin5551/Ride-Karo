package com.exaple.splitwise_clone.vinod.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.froyo.ridekaro.R

import kotlinx.android.synthetic.main.temp_contact_item_layout.view.*

class ContactTempAddAdapter(
    val contactList: List<ContactTempModel>,
    val contactCommunicator: ContactCommunicator
) :
    RecyclerView.Adapter<ContactTempViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactTempViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.temp_contact_item_layout, parent, false)
        return ContactTempViewHolder(
            view,
            contactCommunicator
        )
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactTempViewHolder, position: Int) {
        val tempModel = contactList[position]
        holder.setData(tempModel)
    }
}

class ContactTempViewHolder(itemView: View, val contactCommunicator: ContactCommunicator) :
    RecyclerView.ViewHolder(itemView) {
    fun setData(tempModel: ContactTempModel) {
        itemView.tvContactNameTemp.text = tempModel.name
        itemView.imgCv.setOnClickListener {
            contactCommunicator.onContactDelete(tempModel)
        }
    }
}