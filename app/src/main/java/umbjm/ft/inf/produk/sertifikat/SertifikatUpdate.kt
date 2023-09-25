package umbjm.ft.inf.produk.sertifikat

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
import umbjm.ft.inf.produk.databinding.ActivityIdcardUpdateBinding
import umbjm.ft.inf.produk.databinding.ActivitySertifikatUpdateBinding

class SertifikatUpdate : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null
    private lateinit var binding: ActivitySertifikatUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySertifikatUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idSertifikat = intent.getStringExtra("idSertifikat")
        val nameSertifikat = intent.getStringExtra("nameSertifikat")
        val imageSertifikat = intent.getStringExtra("imageSertifikat")
        val harga = intent.getStringExtra("harga")

        binding.nameSertifikat.setText(nameSertifikat)
        binding.hargaSertifikat.setText(harga)

        if (imageSertifikat != null) {
            // Tampilkan gambar menggunakan Picasso atau library lainnya
            Picasso.get().load(imageSertifikat).into(binding.imageSertifikat)
        } else {
            // Handle jika tidak ada gambar
        }

        imageInput()
        update(idSertifikat)
    }

    private fun imageInput() {
        binding.imageSertifikat.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.imageSertifikat.setImageURI(it)
    }

    private fun update(idSertifikat: String?) {
        binding.update.setOnClickListener {
            val nameSertifikat = binding.nameSertifikat.text.toString()
            val harga = binding.hargaSertifikat.text.toString()

            database =
                FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Products")
                    .child("Sertifikat")

            if (idSertifikat != null) {
                val update = HashMap<String, Any>()
                update["nameSertifikat"] = nameSertifikat
                update["harga"] = harga

                database.child(idSertifikat).updateChildren(update)
                    .addOnCompleteListener { databaseTask ->
                        if (databaseTask.isSuccessful) {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                imageUri?.let { uri ->
                    val storageRef = FirebaseStorage.getInstance().reference.child("Products")
                        .child("Sertifikat")

                    storageRef.putFile(uri).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Dapatkan URL gambar yang baru diunggah
                            storageRef.downloadUrl.addOnSuccessListener { newImageUrl ->
                                // Simpan URL gambar ke Firebase Realtime Database
                                val imageUrlUpdates = HashMap<String, Any>()
                                imageUrlUpdates["imageSertifikat"] = newImageUrl.toString()
                                database.child(idSertifikat).updateChildren(imageUrlUpdates)
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