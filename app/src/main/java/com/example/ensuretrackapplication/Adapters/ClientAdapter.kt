package com.example.ensuretrackapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ensuretrackapplication.ClientModel
import com.example.ensuretrackapplication.R


class ClientAdapter(
    private val clientList: List<ClientModel>,
    private val onDetailsClick: (ClientModel) -> Unit   // callback jab "View Details" click ho
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPolicyId: TextView = itemView.findViewById(R.id.tvPolicyId)
        val tvPlan: TextView = itemView.findViewById(R.id.tvPlan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnViewDetails: Button = itemView.findViewById(R.id.btnViewDetails)

        val tvCnic: TextView = itemView.findViewById(R.id.tvCnic)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvPremium: TextView = itemView.findViewById(R.id.tvPremium)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clientList[position]

        // Set text data
        holder.tvName.text = client.name
        holder.tvPolicyId.text = client.id
        holder.tvCnic.text = "CNIC\n${client.cnic}"
        holder.tvPhone.text = "Phone\n${client.phone}"
        holder.tvPremium.text = "Premium\n${client.premium}"
        holder.tvDueDate.text = "Due Date\n${client.dueDate}"
        holder.tvPlan.text = client.plan
        holder.tvStatus.text = client.status

        // Load profile image (if available)
        Glide.with(holder.itemView.context)
            .load(client.imageUrl ?: R.drawable.baseline_person_24) // fallback icon
            .circleCrop()
            .into(holder.imgProfile)

        // Handle button click
        holder.btnViewDetails.setOnClickListener {
            onDetailsClick(client)
        }
    }

    override fun getItemCount(): Int = clientList.size
}
