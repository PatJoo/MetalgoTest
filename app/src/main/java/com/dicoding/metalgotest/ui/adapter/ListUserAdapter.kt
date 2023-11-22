package com.dicoding.metalgotest.ui.adapter

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.metalgotest.data.model.DetailListUser
import com.dicoding.metalgotest.databinding.ItemListBinding
import com.dicoding.metalgotest.ui.view.UserDetail
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListUserAdapter (private val listUser: List<DetailListUser>) :

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
        holder.binding.apply {
            val imageBytes = Base64.decode(listUser[position].photo, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivUserPhoto.setImageBitmap(decodedImage)
        }

        holder.binding.name.text = listUser[position].name
        holder.binding.email.text = listUser[position].email

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = LocalDate.parse(listUser[position].dateOfBirth, formatter)
        val formattedDate = "${date.month.toString().lowercase().replaceFirstChar { it.uppercase() }} ${date.dayOfMonth}, ${date.year}"
        holder.binding.tvDateOfBirth.text = formattedDate

        holder.itemView.setOnClickListener {
            val dialog = UserDetail()
            val b = Bundle()
            b.putString("ID", listUser[position].id)
            dialog.arguments = b
            dialog.show((holder.itemView.context as FragmentActivity).supportFragmentManager, "detailUserDialog")
        }
    }

}