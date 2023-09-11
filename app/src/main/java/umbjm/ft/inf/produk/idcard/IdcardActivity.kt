package umbjm.ft.inf.produk.idcard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import umbjm.ft.inf.produk.MainActivity
import umbjm.ft.inf.produk.databinding.ActivityIdcardBinding

class IdcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdcardBinding
    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageInput()
        upload()
    }


    private fun imageInput() {
        binding.imageIdcard.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageIdcard.setImageURI(it)
    }

    private fun upload() {
        binding.upload.setOnClickListener {
            val nameIdcard = binding.nameIdcard.text.toString()
            val hargaIdcard = binding.hargaIdcard.text.toString()


            val storageRef = FirebaseStorage.getInstance().reference.child("Products").child(System.currentTimeMillis().toString())

            imageUri?.let {
                storageRef.putFile(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika pengunggahan berhasil, dapatkan URL gambar
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Simpan URL gambar ke Firebase Realtime Database
                            val imageUrl = uri.toString()
                            database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Products")
                            val idIdcard = database.push().key!!
                            val products = IdcardItem(idIdcard, nameIdcard, hargaIdcard, imageUrl)

                            // Menambahkan item banner sebagai child dari id produk
                            database.child("Idcard").child(idIdcard).setValue(products).addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                                }
                                binding.nameIdcard.text?.clear()
                                binding.hargaIdcard.text?.clear()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}