package umbjm.ft.inf.produk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umbjm.ft.inf.produk.banner.BannerActivity

class ShowProducts : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var keranjangItem: ArrayList<KeranjangItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        recyclerView = findViewById(R.id.rv_keranjang)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        keranjangItem = arrayListOf<KeranjangItem>()
        getData()

        recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val selectedProduct = keranjangItem[position]
                val intent = Intent(this@ShowProducts, BannerActivity::class.java)
                intent.putExtra("id", selectedProduct.id)
                startActivity(intent)
            }
        })
    }

    private fun getData() {
        database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Products")
        database.orderByChild("id").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    keranjangItem.clear()  // Membersihkan list terlebih dahulu
                    for (itemProduk in snapshot.children) {
                        val item = itemProduk.getValue(KeranjangItem::class.java)
                        keranjangItem.add(item!!)
                    }
                    recyclerView.adapter = KeranjangAdapter(keranjangItem)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error handling
            }
        })
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }
}
