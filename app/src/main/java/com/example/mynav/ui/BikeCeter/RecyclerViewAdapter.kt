package com.example.mynav.ui.BikeCeter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mynav.IOnItemClickListener
import com.example.mynav.MainActivity
import com.example.mynav.R
import com.example.mynav.ui.getJsonData.IbikeData
import kotlinx.android.synthetic.main.recycleview_rowstyle.view.*
import org.json.JSONArray
import kotlin.coroutines.coroutineContext

//--------------------------------------以下放入RecyclerView.ViewHolder原因在可能會有多個ViewHolder，因此可整個我全都要
class Adapter(array:JSONArray?): RecyclerView.Adapter<Adapter.CustomViewHolder>() {
    private val listTitle = array
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { }
    //RecyclerView必備方法之1
    //創立數量隨著listTitle的長度改變
    override fun getItemCount(): Int {
        return listTitle?.length() ?: 0
    }

    //RecyclerView必備方法之2 - 建立ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //val layoutInflater = LayoutInflater.from(parent?.context)
        //以下是cellForRow這個變數可以參照R.layout.recycleview_rowstyle的XML樣板
        val cellForRow = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_rowstyle, parent, false)
        //parent.setOnClickListener(mOnClickListener)
        cellForRow.setOnClickListener {

                Log.d("click3", " + asclickasclickasda")

        }

        return CustomViewHolder(cellForRow)
    }

    //RecyclerView必備方法之3 - 使用ViewHolder做出你要的東西(可搭配自訂的ViewHolder)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val index = listTitle?.getJSONObject(position)
            //Log.d("tt",index.getString("Position"))
            //holder.bindto(index?.get("$"))
            holder.position.text = "${index?.get("Position")}"
            holder.eName.text = "${index?.get("EName")}"
            holder.cArea.text = "${index?.get("CArea")}"
            holder.eArea.text = "${index?.get("EArea")}"
            holder.cAddress.text = "${index?.get("CAddress")}"
            holder.availableCNT.text = "可借車輛：${index?.get("AvailableCNT")}"
            holder.empCNT.text = "可停空位：${index?.get("EmpCNT")}"
            holder.itemView.setOnClickListener {
                Log.d("click1", " + asclickasclickasda")
            }


        holder.itemView.setOnClickListener {
//            val inflater: LayoutInflater
//            val view = inflater.inflate(R.layout.fragment_login, container, false)
            Log.d("click2", "${holder.position.text}")

        }
    }

    //自訂的ViewHolder
    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindto(IbikeData: IbikeData){
            position.text = IbikeData.position
            eName.text = IbikeData.eName
            cArea.text = IbikeData.cArea
            eArea.text = IbikeData.eArea
            cAddress.text = IbikeData.cAddress
            availableCNT.text = IbikeData.availableCNT
            empCNT.text = IbikeData.empCNT
        }
        val position: TextView = v.findViewById<TextView>(R.id.textView_Position)
        val eName: TextView = v.findViewById<TextView>(R.id.textView_EName)
        val cArea: TextView = v.findViewById<TextView>(R.id.textView_CArea)
        val eArea: TextView = v.findViewById<TextView>(R.id.textView_EArea)
        val cAddress: TextView = v.findViewById<TextView>(R.id.textView_CAddress)
        val availableCNT: TextView = v.findViewById<TextView>(R.id.textView_AvailableCNT)
        val empCNT: TextView = v.findViewById<TextView>(R.id.textView_EmpCNT)
        val image = v.findViewById<ImageView>(R.id.imageView3)
    }
}