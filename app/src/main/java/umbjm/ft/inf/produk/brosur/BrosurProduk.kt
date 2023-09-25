package umbjm.ft.inf.produk.brosur

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

class BrosurProduk : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var brosurItem: ArrayList<BrosurItem>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brosur_produk)

        recyclerView = findViewById(R.id.rv_brosur)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        brosurItem = arrayListOf<BrosurItem>()
        getData()
    }

    private fun getData() {
        database = FirebaseDatabase
            .getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Products/Brosur")
        database.orderByChild("idBrosur")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        brosurItem.clear()
                        for (itemProduk in snapshot.children) {
                            val item = itemProduk.getValue(BrosurItem::class.java)
                            brosurItem.add(item!!)
                        }
                        recyclerView.adapter = BrosurAdapter(brosurItem)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Error handling
                }
            })
    }
}