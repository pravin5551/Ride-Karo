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
        super.onViewCreated(view, savedInstanceState)
        btnShare_invite.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Please download Rapido from plays tore and reach to your destiny https://play.google.com/store/apps/details?id=com.rapido.passenger")
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Please select app: "))
        }

        btn_share_contacts.setOnClickListener {
            val i = Intent(context, InvitefriendsActivity::class.java)
            startActivity(i)

        }


    }

}