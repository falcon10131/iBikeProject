package com.example.mynav.ui.BikeCeter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mynav.R
import com.example.mynav.ui.map.MapFragment
import org.json.JSONArray

//--------------------------------------以下如果放入RecyclerView.ViewHolder原因在可能會有多個ViewHolder，因此可整個我全都要
class Adapter(array:JSONArray, val fg:Fragment, arrayForEName:List<String>): RecyclerView.Adapter<Adapter.CustomViewHolder>() {
    private val arrayEName = arrayForEName
    private val listTitle = array
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { }
    //RecyclerView必備方法之1
    //創立數量隨著listTitle的長度改變
    override fun getItemCount(): Int {
        return listTitle.length() ?: 0
    }

    //RecyclerView必備方法之2 - 建立ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //val layoutInflater = LayoutInflater.from(parent?.context)
        //以下是cellForRow這個變數可以參照R.layout.recycleview_rowstyle的XML樣板
        val cellForRow = LayoutInflater.from(fg.requireContext())
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
            val index = listTitle.getJSONObject(position)
            val x = index.get("X")
            val y = index.get("Y")
            //var bd = Bundle().putString("")
            holder.position.text = "${index.get("Position")}"
            holder.eName.text = arrayEName.get(position)
            holder.cArea.text = "${index.get("CArea")}"
            holder.eArea.text = "${index.get("EArea")}"
            holder.cAddress.text = "${index.get("CAddress")}"
            holder.availableCNT.text = "可借車輛：${index.get("AvailableCNT")}"
            holder.empCNT.text = "可停空位：${index.get("EmpCNT")}"
            holder.itemView.setOnClickListener {
//              val inflater: LayoutInflater
//              val view = inflater.inflate(R.layout.fragment_login, container, false)
                Log.d("click2", "${holder.position.text}")
                fg.requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment,MapFragment.instance).commit()
                MapFragment.instance.takeMeToSomeWhereIClicked("$x","$y")
                Log.d("TAG:Fragment","adapter = ${MapFragment.instance.id}")
            }
    }

    //自訂的ViewHolder
    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
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