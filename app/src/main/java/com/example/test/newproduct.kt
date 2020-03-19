package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class newproduct : AppCompatActivity() {

    lateinit var productType : EditText
    lateinit var productName : EditText
    lateinit var specfic : EditText
    lateinit var productPrice : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newproduct)

        productType = findViewById(R.id.product_type)
        productName = findViewById(R.id.product_name)
        specfic = findViewById(R.id.specification)
        productPrice = findViewById(R.id.price)
        val enter = findViewById<Button>(R.id.btn_enter)
        enter.setOnClickListener {
            saveData()
        }
    }
    private fun saveData(){
        val type = productType.text.toString().trim()
        val name = productName.text.toString().trim()
        var specs = specfic.text.toString().trim()
        var pr = productPrice.text.toString().trim()
        var price = 0
        try {
            price = pr.toInt()
        } catch (nfe: NumberFormatException) {
            Toast.makeText(applicationContext, "make sure price is a number", Toast.LENGTH_LONG).show()
        }
        var quantity = 0
        if (type.isEmpty()){
            productType.error = "please enter the product type"
            return
        }
        else if(name.isEmpty()){
            productType.error = "please enter the product name"
            return
        }
        else if(pr.isEmpty()){
            productPrice.error = "please enter the product name"
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("products")
        val productId = ref.push().key.toString()
        val product = Product(productId,type,name,specs,price,quantity)
        ref.child(productId).setValue(product).addOnCompleteListener {
            Toast.makeText(applicationContext, "product added successfully", Toast.LENGTH_LONG).show()
            productType.setText("")
            productName.setText("")
            specfic.setText("")
            productPrice.setText("")
            val intent = Intent(this,home::class.java)
            startActivity(intent)
        }
    }
}
