package com.tmobile.userapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tmobile.userapp.R


class UserDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details_activity)

        val intent = intent
        val firstName = findViewById<TextView>(R.id.firstName)
        val lastName = findViewById<TextView>(R.id.lastName)
        val email = findViewById<TextView>(R.id.email)
        val avatar = findViewById<ImageView>(R.id.avatar)
        firstName.text = intent.getStringExtra("FIRST_NAME")
        lastName.text = intent.getStringExtra("LAST_NAME")
        email.text = intent.getStringExtra("EMAIL")
        setAvatarImage(intent, avatar)
    }

    private fun setAvatarImage(intent: Intent, avatar: ImageView) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(this).load(intent.getStringExtra("AVATAR_URL"))
            .apply(requestOptions)
            .skipMemoryCache(true) // for caching the image url in case phone is offline
            .into(avatar);
    }
}