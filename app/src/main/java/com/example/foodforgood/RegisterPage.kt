package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.util.PatternsCompat
import com.example.foodforgood.databinding.ActivityRegisterPageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterPage : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityRegisterPageBinding = ActivityRegisterPageBinding
            .inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.registerRegisterButton.setOnClickListener {
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerConfirmPassword.text.toString()


            if (checkName(name) && checkEmail(email) && checkPassword(password)
                && confirmPassword(password, confirmPassword)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            val userdata = hashMapOf("Name" to name)
                            db.collection("Users").document(uid).set(userdata)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Snackbar.make(binding.registerEmail, "Please try a different email address"
                                ,Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,LoginPage::class.java))
        finish()
    }

    private fun checkName(name : String?) : Boolean {
        return !TextUtils.isEmpty(name)
    }

    private fun checkEmail(email : String?) : Boolean {
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    private fun checkPassword(password: String?) : Boolean {
        if (password != null) {
            return !TextUtils.isEmpty(password) && password.length >= 6
        }
        return false
    }

    private fun confirmPassword(password: String?, confirmPassword: String?) : Boolean {
        if (password == null || confirmPassword == null) {
            return false
        }
        return confirmPassword == password
    }
}