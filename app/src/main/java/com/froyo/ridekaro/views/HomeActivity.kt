package com.froyo.ridekaro.views

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.navDrawerFragments.Run
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.nav_header.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        val hView = navigationView.getHeaderView(0)

        val nav_user = hView.findViewById<TextView>(R.id.tv_user_name)

        val nav_user_id = hView.findViewById<TextView>(R.id.tv_user_email_id)

        val nav_profile = hView.findViewById<CircleImageView>(R.id.ivProfile)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navigationView.itemIconTintList = null


        NavigationUI.setupActionBarWithNavController(this, navController, drawer)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(this)
        if (intent != null && intent.extras != null) {
            val User_Name: String = intent.getStringExtra("UserName").toString()
            nav_user.text = User_Name

            val User_Email: String = intent.getStringExtra("UserEmail").toString()
            nav_user_id.text = User_Email

            val User_Photo = intent.getStringExtra("UserPhoto")
            Glide.with(nav_profile).load(User_Photo).into(nav_profile)
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(
                this,
                R.id.fragmentContainerView
            ), drawer
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> return if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
                true
            } else false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home -> {
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.mapFragment, true).build()
                Navigation.findNavController(this, R.id.fragmentContainerView)
                    .navigate(R.id.mapFragment, null, navOptions)
            }
            R.id.nav_covid_19 -> {
                if (isValidDestination(R.id.covid19Fragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.covid19Fragment)
                }
            }
            R.id.nav_payment -> {
                if (isValidDestination(R.id.paymentFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.paymentFragment)
                }
            }
            R.id.nav_myRides -> {
                if (isValidDestination(R.id.myRidesFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.myRidesFragment)
                }
            }
            R.id.nav_inviteFriends -> {
                if (isValidDestination(R.id.inviteFriendsFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.inviteFriendsFragment)
                }
            }
            R.id.nav_powerPass -> {
                if (isValidDestination(R.id.powerPassFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.powerPassFragment)
                }
            }
            R.id.nav_notifications -> {
                if (isValidDestination(R.id.notificationsFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.notificationsFragment)
                }
            }
            R.id.nav_insurance -> {
                if (isValidDestination(R.id.insuranceFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.insuranceFragment)
                }
            }
            R.id.nav_settings -> {
                if (isValidDestination(R.id.settingsFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.settingsFragment)
                }
            }
            R.id.nav_support -> {
                if (isValidDestination(R.id.supportFragment)) {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.supportFragment)
                }
            }
        }

        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    private fun isValidDestination(destination: Int): Boolean {
        return destination != Navigation.findNavController(this, R.id.fragmentContainerView)
            .currentDestination?.id
    }
}