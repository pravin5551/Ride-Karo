package com.froyo.ridekaro.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_first_screen.*


class FirstScreenActivity : AppCompatActivity() {

    val FINE_LOCATION_RO = 101
//    val sharePref=getSharedPreferences("myPre", Context.MODE_PRIVATE)
//    val editor=sharePref.edit()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        buttonTaps()

    }

    private fun buttonTaps() {
        btnAllowPermission.setOnClickListener {
            checkForPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "location",
                FINE_LOCATION_RO
            )
        }


    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        applicationContext,
                        "$name permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, SecondScreenActivity::class.java)
                    startActivity(intent)

                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, SecondScreenActivity::class.java)
                startActivity(intent)
            }
        }
        when (requestCode) {
            FINE_LOCATION_RO -> innerCheck("location")
        }
    }


    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("permission to access your $name is requested to use this app")
            setTitle("permission requested")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@FirstScreenActivity,
                    arrayOf(permission), requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()

    }


}