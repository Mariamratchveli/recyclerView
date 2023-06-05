package com.example.mariamrachveishvili

import android.annotation.SuppressLint
import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        class MainActivity : AppCompatActivity() {
            data class Item(val id: String, val name: String, val description: String)

            private lateinit var recyclerView: RecyclerView
            private lateinit var adapter: ItemAdapter

            private val database = FirebaseDatabase.getInstance()
            private val itemsRef = database.getReference("items")

            @SuppressLint("MissingInflatedId")
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)

                val items = mutableListOf<Item>()

                itemsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        items.clear()

                        for (itemSnapshot in dataSnapshot.children) {
                            val id = itemSnapshot.key.toString()
                            val name = itemSnapshot.child("name").value.toString()
                            val description = itemSnapshot.child("description").value.toString()

                            val item = ClipData.Item(id, name, description)
                            items.add(item)
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("Firebase", "onCancelled: ${databaseError.message}")
                    }
                })

                adapter = ItemAdapter(items)
                recyclerView.adapter = adapter
            }
        }

    }
}
class ItemAdapter(private val items: List<ClipData.Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
