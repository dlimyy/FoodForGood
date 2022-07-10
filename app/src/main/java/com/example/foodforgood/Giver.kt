package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.databinding.ActivityGiverBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Giver : AppCompatActivity() {
    private val giverArray : ArrayList<Givers> = ArrayList()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adaptor : GiverCardViewAdaptor
    private lateinit var binding : ActivityGiverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiverBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getData()
        binding.giverRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = GiverCardViewAdaptor(giverArray)
        binding.giverRecyclerView.adapter = adaptor
        binding.giverGiveButton.setOnClickListener {
            startActivity(Intent(this, GiveDetails::class.java))
            finish()
        }
    }

    private fun getData() {
        db.collection("Givers").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var counter = 0
                for (giver in task.result) {
                    if (giver.id == FirebaseAuth.getInstance().currentUser!!.uid) {
                        giverArray.add(
                            Givers(giver.id, giver.get("Food Type") as String
                                ,giver.get("Food Quantity") as String ,
                                giver.get("Address") as String, giver.get("Postal Code") as String
                                , giver.get("Region") as String, giver.get("Halal") as String)
                        )
                        binding.offerGift.visibility = View.GONE
                        adaptor.notifyItemInserted(counter)
                        counter++
                    }
                }
            }
        }
    }
}