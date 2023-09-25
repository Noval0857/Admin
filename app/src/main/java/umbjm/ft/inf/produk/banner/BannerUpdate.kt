package umbjm.ft.inf.produk.banner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import umbjm.ft.inf.produk.MainActivity
import umbjm.ft.inf.produk.R
import umbjm.ft.inf.produk.databinding.ActivityBannerUpdateBinding
import umbjm.ft.inf.produk.databinding.ActivityIdcardUpdateBinding

class BannerUpdate : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null
    private lateinit var binding: ActivityBannerUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idBanner = intent.getStringExtra("idBanner")
        val nameBanner = intent.getStringExtra("nameBanner")
        val imageBanner = intent.getStringExtra("imageBanner")
        val harga = intent.getStringExtra("harga")

        binding.nameBanner.setText(nameBanner)
        binding.hargaBanner.setText(harga)

        if (imageBanner != null) {
            // Tampilkan gambar menggunakan Picasso atau library lainnya
            Picasso.get().load(imageBanner).into(binding.imageBanner)
        } else {
            // Handle jika tidak ada gambar
        }

        imageInput()
        update(idBanner)
    }

    private fun imageInput() {
        binding.imageBanner.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.imageBanner.setImageURI(it)
    }

    private fun update(idBanner: String?) {
        binding.update.setOnClickListener {
            val nameBanner = binding.nameBanner.text.toString()
            val harga = binding.hargaBanner.text.toString()

            database =
                FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Products")
                    .child("Banner")

            if (idBanner != null) {
                val update = HashMap<String, Any>()
                update["nameBanner"] = nameBanner
                update["harga"] = harga

                database.child(idBanner).updateChildren(update)
                    .addOnCompleteListener { databaseTask ->
                        if (databaseTask.isSuccessful) {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                imageUri?.let { uri ->
                    val storageRef = FirebaseStorage.getInstance().reference.child("Products")
                        .child("Banner")

                    storageRef.putFile(uri).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Dapatkan URL gambar yang baru diunggah
                            storageRef.downloadUrl.addOnSuccessListener { newImageUrl ->
                                // Simpan URL gambar ke Firebase Realtime Database
                                val imageUrlUpdates = HashMap<String, Any>()
                                imageUrlUpdates["imageBanner"] = newImageUrl.toString()
                                database.child(idBanner).updateChildren(imageUrlUpdates)
                            }
                        } else {
                            Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}