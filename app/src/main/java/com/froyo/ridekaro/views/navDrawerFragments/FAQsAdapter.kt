package com.froyo.ridekaro.views.navDrawerFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.FAQsModel

class FAQsAdapter(private val faqsList: List<FAQsModel>) : RecyclerView.Adapter<FAQsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.faqs_item_layout, parent, false)
        return FAQsViewHolder((view))
    }

    override fun onBindViewHolder(holder: FAQsViewHolder, position: Int) {
        val faq = faqsList[position]
        holder.setData(faq)
    }

    override fun getItemCount(): Int {
        return faqsList.size
    }
}