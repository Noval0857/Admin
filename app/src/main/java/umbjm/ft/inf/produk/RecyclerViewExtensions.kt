package umbjm.ft.inf.produk

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addOnItemClickListener(onClickListener: ShowProducts.OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            // No implementation needed
        }
    })
}


//fun RecyclerView.addOnItemClickListener(onClickListener: umbjm.ft.inf.produk.ShowProducts.OnItemClickListener) {
//    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
//        override fun onChildViewAttachedToWindow(view: View) {
//            view.setOnClickListener {
//                val holder = getChildViewHolder(view)
//                onClickListener.invoke(holder.adapterPosition, view)
//            }
//        }
//
//        override fun onChildViewDetachedFromWindow(view: View) {
//            // No implementation needed
//        }
//    })
//}