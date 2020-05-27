package com.example.mynav.ui.BikeCeter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mynav.IOnItemClickListener
import com.example.mynav.MainActivity
import com.example.mynav.R
import org.json.JSONArray

//--------------------------------------以下放入RecyclerView.ViewHolder原因在可能會有多個ViewHolder，因此可整個我全都要
class Adapter(array:JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() ,AdapterView.OnItemClickListener{
    private val listTitle = array
    private val mOnClickListener : View.OnClickListener = View.OnClickListener {  }



    //RecyclerView必備方法之1
    //創立數量隨著listTitle的長度改變
    override fun getItemCount(): Int {
        return listTitle.length()
    }
    //RecyclerView必備方法之2 - 建立ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //val layoutInflater = LayoutInflater.from(parent?.context)
        //以下是cellForRow這個變數可以參照R.layout.recycleview_rowstyle的XML樣板
        val cellForRow = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_rowstyle,parent,false)
        parent.setOnClickListener(mOnClickListener)
        return CustomViewHolder(cellForRow)
    }



    //RecyclerView必備方法之3 - 使用ViewHolder做出你要的東西(可搭配自訂的ViewHolder)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomViewHolder) {
            val tt = listTitle
            Log.d("tt",tt.getString(0))
            holder.position.text = "${tt.getJSONObject(position).get("Position")}"
            holder.eName.text = "${tt.getJSONObject(position).get("EName")}"
            holder.cArea.text = "${tt.getJSONObject(position).get("CArea")}"
            holder.eArea.text = "${tt.getJSONObject(position).get("EArea")}"
            holder.cAddress.text = "${tt.getJSONObject(position).get("CAddress")}"
            holder.availableCNT.text = "可借車輛：${tt.getJSONObject(position).get("AvailableCNT")}"
            holder.empCNT.text = "可停空位：${tt.getJSONObject(position).get("EmpCNT")}"
        }
       // holder.itemView.setOnClickListener(View.OnClickListener {
           // Toast.makeText(this@MainActivity,"$position had been click",Toast.LENGTH_SHORT).show()
        //})
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemClick(parent,view, position, id)
    }

    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
       val mOnItemClickListener = listener
    }

}
//自訂的ViewHolder
class CustomViewHolder(v: View): RecyclerView.ViewHolder(v){
    val position: TextView = v.findViewById<TextView>(R.id.textView_Position)
    val eName: TextView = v.findViewById<TextView>(R.id.textView_EName)
    val cArea: TextView = v.findViewById<TextView>(R.id.textView_CArea)
    val eArea: TextView = v.findViewById<TextView>(R.id.textView_EArea)
    val cAddress: TextView = v.findViewById<TextView>(R.id.textView_CAddress)
    val availableCNT: TextView = v.findViewById<TextView>(R.id.textView_AvailableCNT)
    val empCNT: TextView = v.findViewById<TextView>(R.id.textView_EmpCNT)
}