package com.tyehooney.fedyourpet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tyehooney.fedyourpet.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, FeedPetFragment())
        transaction.commit()*/
    }
}
