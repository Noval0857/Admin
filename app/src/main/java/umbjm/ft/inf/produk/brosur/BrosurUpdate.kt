package umbjm.ft.inf.produk.brosur

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
import umbjm.ft.inf.produk.databinding.ActivityBrosurUpdateBinding
import umbjm.ft.inf.produk.databinding.ActivityIdcardUpdateBinding

class BrosurUpdate : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null
    private lateinit var binding: ActivityBrosurUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrosurUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idBrosur = intent.getStringExtra("idBrosur")
        val nameBrosur = intent.getStringExtra("nameBrosur")
        val imageBrosur = intent.getStringExtra("imageBrosur")
        val harga = intent.getStringExtra("harga")

        binding.nameBrosur.setText(nameBrosur)
        binding.hargaBrosur.setText(harga)

        if (imageBrosur != null) {
            // Tampilkan gambar menggunakan Picasso atau library lainnya
            Picasso.get().load(imageBrosur).into(binding.imageBrosur)
        } else {
            // Handle jika tidak ada gambar
        }

        imageInput()
        update(idBrosur)
    }

    private fun imageInput() {
        binding.imageBrosur.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.imageBrosur.setImageURI(it)
    }

    private fun update(idBrosur: String?) {
        binding.update.setOnClickListener {
            val nameBrosur = binding.nameBrosur.text.toString()
            val harga = binding.hargaBrosur.text.toString()

            database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Products")
                .child("Brosur")

            if (idBrosur != null) {
                val updateBrosur = HashMap<String, Any>()
                updateBrosur["nameBrosur"] = nameBrosur
                updateBrosur["harga"] = harga

                database.child(idBrosur).updateChildren(updateBrosur)
                    .addOnCompleteListener { databaseTask ->
                        if (databaseTask.isSuccessful) {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                imageUri?.let { uri ->
                    val storageRef = FirebaseStorage.getInstance().reference.child("Products")
                        .child("Brosur")

                    storageRef.putFile(uri).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Dapatkan URL gambar yang baru diunggah
                            storageRef.downloadUrl.addOnSuccessListener { newImageUrl ->
                                // Simpan URL gambar ke Firebase Realtime Database
                                val imageUrlUpdates = HashMap<String, Any>()
                                imageUrlUpdates["imageBrosur"] = newImageUrl.toString()
                                database.child(idBrosur).updateChildren(imageUrlUpdates)
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