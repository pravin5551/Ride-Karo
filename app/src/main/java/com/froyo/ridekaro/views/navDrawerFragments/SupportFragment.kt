package com.froyo.ridekaro.views.navDrawerFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.FAQsModel
import kotlinx.android.synthetic.main.fragment_support.*

class SupportFragment : Fragment(R.layout.fragment_support) {

    lateinit var faQsAdapter: FAQsAdapter
    private var faqsList = arrayListOf<FAQsModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildData()
        faQsAdapter = FAQsAdapter(faqsList)
        rvFaqs.layoutManager = GridLayoutManager(context, 3)
        rvFaqs.adapter = faQsAdapter
    }

    private fun buildData() {
        faqsList.add(FAQsModel(R.drawable.ic_safety, "Safety &\nSecurity"))
        faqsList.add(FAQsModel(R.drawable.ic_ride, "Ride &\nBilling"))
        faqsList.add(FAQsModel(R.drawable.ic_services, "Services"))
        faqsList.add(FAQsModel(R.drawable.ic_profile, "Account\n& App"))
        faqsList.add(FAQsModel(R.drawable.referral, "Referrals"))
        faqsList.add(FAQsModel(R.drawable.ic_wallet, "Payment\n& Wallets"))
        faqsList.add(FAQsModel(R.drawable.ic_pass, "Power\nPass"))
    }
}