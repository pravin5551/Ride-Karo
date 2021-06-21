package com.froyo.ridekaro.views.navDrawerFragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exaple.splitwise_clone.vinod.recyclerviews.ContactCommunicator
import com.exaple.splitwise_clone.vinod.recyclerviews.ContactTempAddAdapter
import com.exaple.splitwise_clone.vinod.recyclerviews.ContactTempModel
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_invitefriends.*


class ContactFragment : Fragment(R.layout.fragment_contact), ContactCommunicator {

    private val REQ_CODE = 1
    private lateinit var cursor: Cursor
    private var contactList = mutableListOf<ContactTempModel>()

    //    private var usersList = mutableListOf<UserEntity>()+66
    private lateinit var contactAdapter: ContactTempAddAdapter
    private lateinit var to: IntArray


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        shareAllUsers.setOnClickListener {
//
//        }

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            ), REQ_CODE
        )


//        etNameEmailPhone.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                lvContacts.visibility = View.VISIBLE
//                ibClose.visibility = View.VISIBLE
//
//            } else {
//                lvContacts.visibility = View.GONE
//                ibClose.visibility = View.GONE
//
//            }
//        }

        lvContacts.setOnItemClickListener { parent, view, position, id ->
            var cTM =
                ContactTempModel(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                    cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"))
                )
            var flag = true
            for (i in contactList) {
                if (i.name == cTM.name && i.number == cTM.number) {
                    Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show()
                    flag = false
                    break;
                }
            }
            if (flag) {
                contactList.add(cTM)
                contactAdapter =
                    ContactTempAddAdapter(
                        contactList,
                        this
                    )
                rvContactAddTemp.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rvContactAddTemp.adapter = contactAdapter
            }
        }
    }


    private fun fetchContacts() {
        val resolver = requireActivity().contentResolver
        val cursor: Cursor? = resolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        Log.d("Inside contain resolver", activity?.contentResolver.toString())
        this.cursor = cursor!!
        activity?.startManagingCursor(cursor)

        val from = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID
        )

        to = IntArray(2)
        to[0] = android.R.id.text1
        to[1] = android.R.id.text2

        val simpleCursorAdapter =
            SimpleCursorAdapter(context, android.R.layout.simple_list_item_2, cursor, from, to)

        lvContacts.adapter = simpleCursorAdapter
        lvContacts.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        fetchContacts()
                    } else {
                        Toast.makeText(
                            context,
                            "Contact Permission Denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    override fun onContactDelete(tempModel: ContactTempModel) {
        var x = 0
        for (i in contactList) {
            if (i.name == tempModel.name && i.number == tempModel.number) {
                contactList.removeAt(x)
                break;
            }
            x++
        }
        contactAdapter.notifyDataSetChanged()
    }


}