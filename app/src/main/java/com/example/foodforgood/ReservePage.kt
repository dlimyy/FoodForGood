package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodforgood.databinding.ActivityReservePageBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ReservePage : AppCompatActivity() {
    private val giverArray : ArrayList<Givers> = ArrayList()
    private var filteredArray : ArrayList<Givers> = ArrayList()
    private lateinit var recylerView : RecyclerView
    private lateinit var adaptor: ReserveAdaptor
    private var currentSearchText = ""
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding : ActivityReservePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservePageBinding
            .inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getData()
        recylerView = binding.reserveRecyclerView
        binding.reserveRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = ReserveAdaptor(filteredArray)
        binding.reserveRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : ReserveAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {

                intent = Intent(this@ReservePage, Collector::class.java)
                val giver = filteredArray[position]
                Snackbar.make(binding.regionGroup,"Made a reservation succesfully",
                    Snackbar.LENGTH_SHORT).show()
                val data = hashMapOf("Address" to giver.address, "Postal Code" to giver.postalCode,
                    "Food Type" to giver.foodType, "Food Quantity" to giver.foodQuantity,
                    "Region" to giver.region, "Reserved" to true, "Halal" to
                            giver.halal)
                db.collection("Givers").document(giver.uid).update("Reserved", true)
                db.collection("Collectors").document(auth.currentUser!!.uid)
                    .collection("SubCollections").document(giver.uid).set(data)
                finish()
            }
        })
        binding.regionGroup.setOnCheckedStateChangeListener { group, _ ->
            if (binding.regionGroup.checkedChipId == View.NO_ID) {
                filter("All")
                return@setOnCheckedStateChangeListener
            }
            filter(findViewById<Chip>(binding.regionGroup.checkedChipId).text.toString())
        }
        binding.reserveSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                currentSearchText = newText
                if (binding.regionGroup.checkedChipId == View.NO_ID) {
                    filter("All")
                    return false
                }
                filter(findViewById<Chip>(binding.regionGroup.checkedChipId).text.toString())
                return false
            }

        })

    }

    private fun getData() {
        db.collection("Givers").whereNotEqualTo("Requested",true)
            .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var counter : Int = 0
                for (giver in task.result) {
                    if (giver.id != FirebaseAuth.getInstance().currentUser!!.uid) {
                        giverArray.add(Givers(giver.id, giver.get("Food Type") as String
                            ,giver.get("Food Quantity") as String ,
                            giver.get("Address") as String, giver.get("Postal Code") as String
                            , giver.get("Region") as String, giver.get("Halal") as String))
                        filteredArray.add(Givers(giver.id, giver.get("Food Type") as String
                            ,giver.get("Food Quantity") as String ,
                            giver.get("Address") as String, giver.get("Postal Code") as String
                            , giver.get("Region") as String, giver.get("Halal") as String))
                        adaptor.notifyItemInserted(counter)
                        counter++
                    }
                }
            }

        }
    }

    private fun filter(region : String) {
        filteredArray = ArrayList()
        for (giver in giverArray) {
            if (giver.region == region || region == "All") {
                if (currentSearchText == "") {
                    filteredArray.add(giver)
                    continue
                } else if (giver.foodType.lowercase().contains(currentSearchText.lowercase())) {
                    filteredArray.add(giver)
                }
            }
        }
        adaptor = ReserveAdaptor(filteredArray)
        recylerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : ReserveAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {
                intent = Intent(this@ReservePage, Collector::class.java)
                val giver = filteredArray[position]
                Snackbar.make(binding.regionGroup,"Made a reservation succesfully",
                    Snackbar.LENGTH_SHORT).show()
                val data = hashMapOf("Address" to giver.address, "Postal Code" to giver.postalCode,
                    "Food Type" to giver.foodType, "Food Quantity" to giver.foodQuantity,
                    "Region" to giver.region, "Reserved" to true, "Halal" to
                            giver.halal)
                db.collection("Givers").document(giver.uid).update("Reserved", true)
                db.collection("Collectors").document(auth.currentUser!!.uid)
                    .collection("SubCollections").document(giver.uid).set(data)
                finish()
            }
        })
    }
}