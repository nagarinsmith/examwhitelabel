package com.examwhitelabel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class TheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<TheActivityBinding>(this, R.layout.activity)
        findNavController(R.id.navigation).let { navController ->
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, arguments ->
                if (destination.id == R.id.add_or_edit) {
                    supportActionBar?.setTitle(if (arguments?.get("item") != null) R.string.edit else R.string.add)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navigation).navigateUp() || super.onSupportNavigateUp()
    }
}
