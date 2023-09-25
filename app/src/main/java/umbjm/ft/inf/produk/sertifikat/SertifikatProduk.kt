package umbjm.ft.inf.produk.sertifikat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umbjm.ft.inf.produk.R
import umbjm.ft.inf.produk.idcard.IdcardAdapter
import umbjm.ft.inf.produk.idcard.IdcardItem

class SertifikatProduk : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var sertifItem: ArrayList<SertifikatItem>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sertifikat_produk)

        recyclerView = findViewById(R.id.rv_sertif)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        sertifItem = arrayListOf<SertifikatItem>()
        getData()
    }

    private fun getData() {
        database = FirebaseDatabase
            .getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Products/Sertifikat")
        database.orderByChild("idSertifikat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        sertifItem.clear()
                        for (itemProduk in snapshot.children) {
                            val item = itemProduk.getValue(SertifikatItem::class.java)
                            sertifItem.add(item!!)
                        }
                        recyclerView.adapter = SertifikatAdapter(sertifItem)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Error handling
                }
            })
    }
}