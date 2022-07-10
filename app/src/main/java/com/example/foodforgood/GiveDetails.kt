package com.example.foodforgood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.example.foodforgood.databinding.ActivityGiveDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class GiveDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityGiveDetailsBinding = ActivityGiveDetailsBinding
            .inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val db = FirebaseFirestore.getInstance()
        binding.giverSubmitButton.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val address = binding.giverAddress.text.toString()
            val postalCode = binding.postalCode.text.toString()
            val foodType = binding.foodType.text.toString()
            val foodQuantity = binding.foodQuantity.text.toString()
            val checkedHalal = binding.halalButtonToggle.checkedButtonId
            if (checkAddress(address) && checkPostalCode(postalCode)
                && checkFoodType(foodType) && checkfoodQuantity(foodQuantity)
                && checkHalal(checkedHalal)) {
                if (postalCodeRegion(postalCode) != "") {
                    val region = postalCodeRegion(postalCode)
                    val data = hashMapOf("Address" to address, "Postal Code" to postalCode,
                        "Food Type" to foodType, "Food Quantity" to foodQuantity,
                        "Region" to region, "Reserved" to false, "Halal" to
                                findViewById<Button>(checkedHalal).text.toString())
                    db.collection("Givers").document(uid).set(data)
                    Snackbar.make(binding.giverAddress, "Submission has been successful"
                        ,Snackbar.LENGTH_SHORT).show()
                }
                else {
                    Snackbar.make(binding.giverAddress, "Please key in a valid postal code"
                        ,Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.giverAddress, "Please fill in all fields"
                    ,Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAddress(address : String) : Boolean {
        return !TextUtils.isEmpty(address)
    }

    private fun checkPostalCode(postalCode : String) : Boolean {
        return postalCode.length == 6
    }

    private fun checkFoodType(foodType : String) : Boolean {
        return !TextUtils.isEmpty(foodType)
    }

    private fun checkfoodQuantity(foodQuantity : String) : Boolean {
        return !TextUtils.isEmpty(foodQuantity)
    }

    private fun postalCodeRegion(postalCode: String) : String {
        val postalCodeFrontDigits = postalCode.substring(0,2).toInt()
        if ((postalCodeFrontDigits in 34..55)
            || postalCodeFrontDigits in 81..82) {
            return "East"
        }
        if ((postalCodeFrontDigits in 1..10) || (postalCodeFrontDigits in 14..33)
            || (postalCodeFrontDigits in 56..57)) {
            return "South"
        }

        if (postalCodeFrontDigits in 72..80) {
            return "North"
        }

        if ((postalCodeFrontDigits in 11..13) || (postalCodeFrontDigits in 58..71)) {
            return "West"
        }

        return ""
    }

    private fun checkHalal(ID : Int) : Boolean {
        return ID != View.NO_ID
    }

}