package umbjm.ft.inf.produk.sticker

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
import umbjm.ft.inf.produk.R
import umbjm.ft.inf.produk.banner.BannerItem
import umbjm.ft.inf.produk.databinding.ActivityBannerBinding
import umbjm.ft.inf.produk.databinding.ActivityStickerBinding

class StickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStickerBinding
    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageInput()
        upload()
    }


    private fun imageInput() {
        binding.imageSticker.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageSticker.setImageURI(it)
    }

    private fun upload() {
        binding.upload.setOnClickListener {
            val nameSticker = binding.nameSticker.text.toString()
            val hargaSticker = binding.hargaSticker.text.toString()


            val storageRef = FirebaseStorage.getInstance().reference.child("Products").child(System.currentTimeMillis().toString())

            imageUri?.let {
                storageRef.putFile(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika pengunggahan berhasil, dapatkan URL gambar
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Simpan URL gambar ke Firebase Realtime Database
                            val imageUrl = uri.toString()
                            database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Products")
                            val idSticker = database.push().key!!
                            val products = StickerItem(idSticker, nameSticker, hargaSticker, imageUrl)

                            // Menambahkan item banner sebagai child dari id produk
                            database.child("Sticker").child(idSticker).setValue(products).addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                                }
                                binding.nameSticker.text?.clear()
                                binding.hargaSticker.text?.clear()
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