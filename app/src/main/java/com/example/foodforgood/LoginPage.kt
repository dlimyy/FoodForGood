package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.core.util.PatternsCompat
import com.example.foodforgood.databinding.ActivityLoginPageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPage : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginPageBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.loginLoginButton.setOnClickListener {
            val loginEmail = binding.loginEmail.text.toString()
            val loginPassword = binding.loginPassword.text.toString()
            binding.loginEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.loginPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
            if (checkEmail(loginEmail) && checkPassword(loginPassword))
            FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail,loginPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Snackbar.make(binding.loginEmail, "Please key in a valid email or password"
                            ,Snackbar.LENGTH_SHORT).show()
                    }
                }
        }

        binding.loginRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkEmail(email : String?) : Boolean {
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!email?.let { PatternsCompat.EMAIL_ADDRESS.matcher(it).matches() }!!) {
            return false
        }
        return true
    }

    private fun checkPassword(password : String?) : Boolean {
        if (password != null) {
            return !TextUtils.isEmpty(password) && password.length >= 6
        }
        return false
    }
}