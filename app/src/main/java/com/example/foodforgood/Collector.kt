package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.databinding.ActivityCollectorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Collector : AppCompatActivity() {
    private lateinit var binding : ActivityCollectorBinding
    private lateinit var adaptor : GiverCardViewAdaptor
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val collectorArray : ArrayList<Givers> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getData()
        binding.collectorReserveButton.setOnClickListener {
            startActivity(Intent(this, ReservePage::class.java))
        }
        binding.collectorRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = GiverCardViewAdaptor(collectorArray)
        binding.collectorRecyclerView.adapter = adaptor
        binding.collectorBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getData() {
        db.collection("Collectors").document(uid).collection("SubCollections")
            .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var counter = 0
                for (collector in task.result) {
                    collectorArray.add(
                        Givers(uid, collector.get("Food Type") as String
                            ,collector.get("Food Quantity") as String ,
                            collector.get("Address") as String, collector.get("Postal Code") as String
                            , collector.get("Region") as String, collector.get("Halal") as String)
                    )
                    binding.reserveFood.visibility = View.GONE
                    adaptor.notifyItemInserted(counter)
                    counter++
                }
            }
        }
    }
}