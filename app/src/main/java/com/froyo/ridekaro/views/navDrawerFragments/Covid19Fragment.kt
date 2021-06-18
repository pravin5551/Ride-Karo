package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.froyo.ridekaro.R
import com.froyo.ridekaro.fragments.GetVaccinatedFragment
import com.froyo.ridekaro.views.Book_Vaccine_Activity
import com.froyo.ridekaro.views.LocationSearchFragment
import kotlinx.android.synthetic.main.fragment_covid19.*

class Covid19Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid19, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnbook_vaccine.setOnClickListener(View.OnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            ft.add(
                R.id.fragmentContainerView,
                GetVaccinatedFragment(),
                "CovidFragment"
            ).addToBackStack(null)
            ft.commit()
        })
    }
}