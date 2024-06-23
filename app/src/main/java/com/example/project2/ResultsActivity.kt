import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

    private lateinit var adapterGame1: ResultsAdapter
    private lateinit var adapterGame2: ResultsAdapter
    private lateinit var adapterGame3: ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // Simulated results for testing
        val resultsGame1 = listOf(
            Result("1", 100, 12000),
            Result("2", 90, 15000),
            Result("3", 80, 18000)
        )

        val resultsGame2 = listOf(
            Result("4", 200, 25000),
            Result("5", 180, 28000),
            Result("6", 150, 30000)
        )

        val resultsGame3 = listOf(
            Result("7", 300, 32000),
            Result("8", 280, 35000),
            Result("9", 270, 38000)
        )

        // Configure RecyclerViews
        recyclerViewGame1.layoutManager = LinearLayoutManager(this)
        recyclerViewGame2.layoutManager = LinearLayoutManager(this)
        recyclerViewGame3.layoutManager = LinearLayoutManager(this)

        // Initialize adapters with simulated data
        adapterGame1 = ResultsAdapter(resultsGame1)
        adapterGame2 = ResultsAdapter(resultsGame2)
        adapterGame3 = ResultsAdapter(resultsGame3)

        // Set adapters to RecyclerViews
        recyclerViewGame1.adapter = adapterGame1
        recyclerViewGame2.adapter = adapterGame2
        recyclerViewGame3.adapter = adapterGame3
    }
}

//package com.example.project2
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.android.synthetic.main.activity_results.*
//
//class ResultsActivity : AppCompatActivity() {
//
//    private lateinit var db: FirebaseFirestore
//    private lateinit var adapterGame1: ResultsAdapter
//    private lateinit var adapterGame2: ResultsAdapter
//    private lateinit var adapterGame3: ResultsAdapter
//
//    private val TAG = "ResultsActivity"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_results)
//
//        db = FirebaseFirestore.getInstance()
//
//        // Configure RecyclerViews
//        recyclerViewGame1.layoutManager = LinearLayoutManager(this)
//        recyclerViewGame2.layoutManager = LinearLayoutManager(this)
//        recyclerViewGame3.layoutManager = LinearLayoutManager(this)
//
//        // Initialize adapters
//        adapterGame1 = ResultsAdapter()
//        adapterGame2 = ResultsAdapter()
//        adapterGame3 = ResultsAdapter()
//
//        // Set adapters to RecyclerViews
//        recyclerViewGame1.adapter = adapterGame1
//        recyclerViewGame2.adapter = adapterGame2
//        recyclerViewGame3.adapter = adapterGame3
//
//        // Fetch results from Firebase collections
//        fetchResults("game1_collection", adapterGame1)
//        fetchResults("game2_collection", adapterGame2)
//        fetchResults("game3_collection", adapterGame3)
//    }
//
//    private fun fetchResults(collectionName: String, adapter: ResultsAdapter) {
//        db.collection(collectionName)
//            .get()
//            .addOnSuccessListener { result ->
//                val resultsList = mutableListOf<Result>()
//                for (document in result.documents) {
//                    val resultItem = document.toObject(Result::class.java)
//                    resultItem?.let { resultsList.add(it) }
//                }
//                adapter.setData(resultsList)
//            }
//            .addOnFailureListener { exception ->
//                // Handle errors
//                Log.e(TAG, "Error fetching results from collection $collectionName", exception)
//                Toast.makeText(this@ResultsActivity, "Failed to fetch results", Toast.LENGTH_SHORT).show()
//            }
//    }
//}