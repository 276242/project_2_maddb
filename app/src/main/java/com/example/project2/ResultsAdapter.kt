package com.example.project2



//package com.example.project2
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//
//import kotlinx.android.synthetic.main.item_result.view.*
//
//class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {
//
//    private var resultsList: List<Result> = listOf()
//
//    fun setData(results: List<Result>) {
//        this.resultsList = results
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
//        return ResultViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
//        val result = resultsList[position]
//        holder.bind(result)
//    }
//
//    override fun getItemCount(): Int {
//        return resultsList.size
//    }
//
//    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val textViewScore: TextView = itemView.textViewScore
//        private val textViewCompletionTime: TextView = itemView.textViewCompletionTime
//
//        fun bind(result: Result) {
//            textViewScore.text = "Score: ${result.score}"
//            textViewCompletionTime.text = "Time: ${result.completionTime}"
//        }
//    }
//}