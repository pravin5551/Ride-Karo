package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.InvitefriendsActivity
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

        btn_share_contacts.setOnClickListener {
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