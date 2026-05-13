package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.User

class UserAdapter(
    private val listUser: List<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvAvatar: TextView =
            itemView.findViewById(R.id.tv_avatar)

        val tvUserNama: TextView =
            itemView.findViewById(R.id.tv_user_nama)

        val tvUserRole: TextView =
            itemView.findViewById(R.id.tv_user_role)

        val ivEditChevron: ImageView =
            itemView.findViewById(R.id.iv_edit_chevron)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adm_user_list_item,
                parent,
                false
            )

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {

        val user = listUser[position]

        holder.tvUserNama.text =
            user.nama

        holder.tvUserRole.text =
            "${user.role} • ${user.nimNip ?: "-"}"

        val avatarText =
            user.nama
                .split(" ")
                .take(2)
                .map { it.first().uppercase() }
                .joinToString("")

        holder.tvAvatar.text =
            avatarText

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }

        holder.ivEditChevron.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}