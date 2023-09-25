package umbjm.ft.inf.produk.idcard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umbjm.ft.inf.produk.R

class IdcardProduk : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var idcardItem: ArrayList<IdcardItem>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idcard_produk)

        recyclerView = findViewById(R.id.rv_idcard)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        idcardItem = arrayListOf<IdcardItem>()
        getData()
    }

    private fun getData() {
        database = FirebaseDatabase
            .getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Products/Idcard")
        database.orderByChild("idCard")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        idcardItem.clear()
                        for (itemProduk in snapshot.children) {
                            val item = itemProduk.getValue(IdcardItem::class.java)
                            idcardItem.add(item!!)
                        }
                        recyclerView.adapter = IdcardAdapter(idcardItem)
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error handling
            }
        })
    }
}