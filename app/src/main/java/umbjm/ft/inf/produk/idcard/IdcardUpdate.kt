package umbjm.ft.inf.produk.idcard

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

class IdcardUpdate : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null
    private lateinit var binding: ActivityIdcardUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdcardUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idCard = intent.getStringExtra("idCard")
        val nameIdcard = intent.getStringExtra("nameIdcard")
        val imageIdcard = intent.getStringExtra("imageIdcard")
        val harga = intent.getStringExtra("harga")

        binding.nameIdcard.setText(nameIdcard)
        binding.hargaIdcard.setText(harga)

        if (imageIdcard != null) {
            // Tampilkan gambar menggunakan Picasso atau library lainnya
            Picasso.get().load(imageIdcard).into(binding.imageIdcard)
        } else {
            // Handle jika tidak ada gambar
        }

        imageInput()
        update(idCard)
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

    private fun update(idCard: String?) {
        binding.update.setOnClickListener {
            val nameIdcard = binding.nameIdcard.text.toString()
            val harga = binding.hargaIdcard.text.toString()

            database =
                FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Products")
                    .child("Idcard")

            if (idCard != null) {
                val updateIdcard = HashMap<String, Any>()
                updateIdcard["nameIdcard"] = nameIdcard
                updateIdcard["harga"] = harga

                database.child(idCard).updateChildren(updateIdcard)
                    .addOnCompleteListener { databaseTask ->
                        if (databaseTask.isSuccessful) {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                imageUri?.let { uri ->
                    val storageRef = FirebaseStorage.getInstance().reference.child("Products")
                        .child("Idcard")

                    storageRef.putFile(uri).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Dapatkan URL gambar yang baru diunggah
                            storageRef.downloadUrl.addOnSuccessListener { newImageUrl ->
                                // Simpan URL gambar ke Firebase Realtime Database
                                val imageUrlUpdates = HashMap<String, Any>()
                                imageUrlUpdates["imageIdcard"] = newImageUrl.toString()
                                database.child(idCard).updateChildren(imageUrlUpdates)
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
