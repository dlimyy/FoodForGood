package com.example.foodforgood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ReserveAdaptor(private val giverArray : ArrayList<Givers>)
    : RecyclerView.Adapter<ReserveAdaptor.ViewHolder>() {


    private lateinit var giverListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        giverListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val foodType : TextView
        val foodQuantity : TextView
        val address : TextView
        val halal : TextView

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            foodType = view.findViewById<TextView>(R.id.foodTypeGiver)
            foodQuantity = view.findViewById<TextView>(R.id.foodQuantityGiver)
            address = view.findViewById<TextView>(R.id.giverAddress)
            halal = view.findViewById<TextView>(R.id.giverHalal)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.foodcardview, viewGroup, false)
        return ViewHolder(view, giverListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foodType.text = giverArray[position].foodType
        holder.foodQuantity.text = "Food Quantity : " + giverArray[position].foodQuantity
        holder.address.text = giverArray[position].address + ", S" + giverArray[position].postalCode
        holder.halal.text = "Halal: " + giverArray[position].halal
    }

    override fun getItemCount(): Int {
        return giverArray.size
    }
}