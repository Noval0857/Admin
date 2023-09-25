package umbjm.ft.inf.produk.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umbjm.ft.inf.produk.R

class UserActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recylerView: RecyclerView
    private lateinit var user: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        recylerView = findViewById(R.id.rv_user)
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.hasFixedSize()

        user = arrayListOf<User>()

        val userr = FirebaseAuth.getInstance().currentUser

        if (userr != null) {
            val userID = userr.uid
            getData(userID)
        }
    }

    private fun getData(userID: String) {
        database = FirebaseDatabase.getInstance("https://mydigitalprinting-60323-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
        database.child(userID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user.clear()  // Membersihkan list terlebih dahulu
                    for (userList in snapshot.children) {
                        val item = userList.getValue(User::class.java)
                        user.add(item!!)
                    }
                    recylerView.adapter = UserAdapter (user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error handling
            }
        })
    }
}