package umbjm.ft.inf.produk.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import umbjm.ft.inf.produk.R

class UserAdapter(private val user:ArrayList<User>) : RecyclerView.Adapter<UserAdapter.GridViewHolder>() {

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userID: TextView = itemView.findViewById(R.id.userID)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.userlist, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val grid = user[position]
        holder.userID.text = grid.userID
    }
}