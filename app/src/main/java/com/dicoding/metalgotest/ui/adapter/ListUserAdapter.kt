package com.dicoding.metalgotest.ui.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.metalgotest.R
import com.dicoding.metalgotest.data.model.DataItem
import com.dicoding.metalgotest.databinding.ItemListBinding
import com.dicoding.metalgotest.ui.view.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListUserAdapter (private val listUser: List<DataItem>) :

    RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
        companion object {
            private val TAG = "ListStoryAdapter"
        }


    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

        override fun getItemCount() = listUser.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = listUser[position].name
        holder.binding.email.text = listUser[position].email

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = LocalDate.parse(listUser[position].dateOfBirth, formatter)

        val formattedDate = "${date.month.toString().lowercase().replaceFirstChar { it.uppercase() }} ${date.dayOfMonth}, ${date.year}"
        holder.binding.tvDateOfBirth.text = formattedDate

        // Glide untuk menampilkan gambar (jika diperlukan)
        // Glide.with(holder.ivStory)
        //    .load(listUser[position].photoUrl)
        //    .into(holder.ivStory)

        // Handle klik pada item
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, listUser[position].name, Toast.LENGTH_LONG).show()

            // Contoh penggunaan Intent (jika diperlukan)
            /*
            val intent = Intent(holder.itemView.context, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_ID, listUser[position].id)
            holder.itemView.context.startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity?
                ).toBundle()
            )
            */
        }
    }

}