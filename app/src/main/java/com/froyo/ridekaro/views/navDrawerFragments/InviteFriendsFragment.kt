package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.InvitefriendsActivity
import com.froyo.ridekaro.views.OTPSecondActivity
import kotlinx.android.synthetic.main.activity_otpvalidation.*
import kotlinx.android.synthetic.main.activity_temp.*
import kotlinx.android.synthetic.main.activity_temp.btnShare
import kotlinx.android.synthetic.main.fragment_invite_friends.*

class InviteFriendsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        activity?.actionBar?.hide()
        super.onViewCreated(view, savedInstanceState)
        btnShare_invite.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Please download Rapido from plays tore and reach to your destiny https://play.google.com/store/apps/details?id=com.rapido.passenger"
            )
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Please select app: "))
        }

        unique_code_button.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                context,
                "Code Coppied ",
                Toast.LENGTH_SHORT
            ).show()
            var clipboard = getSystemService(
                requireContext(),
                ClipboardManager::class.java
            )

            var unieque_code_name =unique_code_text.text.toString()
            var clip = ClipData.newPlainText("Code Copied",unieque_code_name)



        })

        btn_share_contacts.setOnClickListener {
//            val fragment = ContactFragment()
//                val fragmentManager: FragmentManager = parentFragmentManager
//                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
////                fragment.arguments = (bundle)
//                fragmentTransaction.replace(R.id.fragmentContainerView, fragment,"ContactFragment").addToBackStack("ContactFragment").commit()

            val i = Intent(context, InvitefriendsActivity::class.java)
            startActivity(i)

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_support_button, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.supportButton -> {
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}