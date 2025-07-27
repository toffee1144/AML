package com.example.aml.model

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aml.R // Ini penting untuk akses R.layout dan R.id

class HospitalAdapter(private val hospitals: List<Hospital>) :
    RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    inner class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val tvDistance: TextView = itemView.findViewById(R.id.tvHospitalDistance)
        val btnMap: Button = itemView.findViewById(R.id.btnOpenMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    override fun getItemCount() = hospitals.size

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.tvName.text = hospital.name
        holder.tvDistance.text = "${hospital.distance} m"

        holder.btnMap.setOnClickListener {
            val uri = Uri.parse("geo:${hospital.lat},${hospital.lon}?q=${Uri.encode(hospital.name)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            holder.itemView.context.startActivity(intent)
        }
    }
}