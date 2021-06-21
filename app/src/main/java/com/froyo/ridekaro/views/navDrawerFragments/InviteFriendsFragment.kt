package com.froyo.ridekaro.views.navDrawerFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.InvitefriendsActivity
import kotlinx.android.synthetic.main.fragment_invite_friends.*
import java.util.*

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

        unique_code_text.text = generateRandomString()
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
//            Toast.makeText(
//                context,
//                "Code Coppied ",
//                Toast.LENGTH_SHORT
//            ).show()
//            var clipboard = getSystemService(
//                requireContext(),
//                ClipboardManager::class.java
//            )
//
//            var unieque_code_name =unique_code_text.text.toString()
//            var clip = ClipData.newPlainText("Code Copied",unieque_code_name)

            copyTextToClipboard()
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

    private fun copyTextToClipboard() {
        val textToCopy = unique_code_text.text
//        val clipboardManager = getSystemService(requireContext(),CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        getSystemService(
            requireContext(),
            ClipboardManager::class.java
        )?.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_LONG).show()
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


    fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start

//    fun generateRandomNumberList(len: Int, low: Int = 0, high: Int = 255): List<Int> {
//        (0..len-1).map {
//            (low..high).random()
//        }.toList()
//    }

    fun List<Char>.random() = this[Random().nextInt(this.size)]

    fun generateRandomString(len: Int = 6): String{
        val alphanumerics = CharArray(26) { it -> (it + 65).toChar() }.toSet()
            .union(CharArray(9) { it -> (it + 48).toChar() }.toSet())
        return (0..len-1).map {
            alphanumerics.toList().random()
        }.joinToString("")

    }

}