package com.tmobile.userapp.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tmobile.userapp.R
import com.tmobile.userapp.framework.UserUIModel


internal class UserAdapter(private val context: Context) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private var itemsList: List<UserUIModel> = emptyList()

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var firstName: TextView = view.findViewById(R.id.firstName)
        var lastName: TextView = view.findViewById(R.id.lastName)
        var email: TextView = view.findViewById(R.id.email)
        var imageView: ImageView = view.findViewById(R.id.avatar)
    }

    fun setData(itemsList: List<UserUIModel>) {
        this.itemsList = itemsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.firstName.text = item.firstName
        holder.lastName.text = item.lastName
        holder.email.text = item.email

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(context).load(item.avatarUrl)
            .apply(requestOptions)
            .skipMemoryCache(true) // for caching the image url in case phone is offline
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            launchUserDetailsActivity(item)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun launchUserDetailsActivity(item: UserUIModel) {
        val intent = Intent(context, UserDetailsActivity::class.java)
        intent.putExtra("FIRST_NAME", item.firstName)
        intent.putExtra("LAST_NAME", item.lastName)
        intent.putExtra("EMAIL", item.email)
        intent.putExtra("AVATAR_URL", item.avatarUrl)
        context.startActivity(intent)
    }
}