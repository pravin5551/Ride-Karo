package com.froyo.ridekaro.views.navDrawerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.froyo.ridekaro.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentA.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentA : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.welcomeName.text = "Welcome to Splitwise, \n${MyRidesFragment.name}!"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }


    companion object {
        var ARG_PARAM1 = ""

        fun newInstance(param1: String?): FragmentA {
            val fragment = FragmentA()
            val args = Bundle()
            ARG_PARAM1 = param1!!
            fragment.arguments = args
            return fragment
        }
    }
}