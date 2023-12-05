package be.ucll.tasklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: ExpandableBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        val navigationController = Navigation.findNavController(this, R.id.navigationHost)

        /**
         * Call looks like NavigationUI.setupWithNavController(bottomNavigation, navigationController)
         * for native BottomNavigationView
         */
        ExpandableBottomBarNavigationUI.setupWithNavController(bottomNavigation, navigationController)

        // hide navigationbar at login
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.login__FragmentLogin) {
                bottomNavigation.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }


    }
}