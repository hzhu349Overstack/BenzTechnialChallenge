package com.tps.challenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tps.challenge.TCApplication.R
import com.tps.challenge.features.UserFeedFragment

/**
 * The initial Activity for the app.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userFeedFragment = UserFeedFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container, userFeedFragment,
                UserFeedFragment.TAG
            )
            .commit()
    }
}