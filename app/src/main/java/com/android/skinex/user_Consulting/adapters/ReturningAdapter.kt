package com.android.skinex.user_Consulting.adapters

//import kotlinx.android.synthetic.main.activity_consulting.*
//import kotlinx.android.synthetic.main.body_part_list.view.*
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.skinex.R
import com.android.skinex.activity.SeclectVisiter
import com.android.skinex.databinding.*
import com.android.skinex.publicObject.Validation
import com.android.skinex.publicObject.Visiter
import com.android.skinex.room.Visit
import com.android.skinex.room.VisitDatabase
import com.android.skinex.user_Consulting.ReturningInfoActivity
import com.android.skinex.user_Consulting.UserInfoActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ReturningAdapter(var select: Array<Visit>) : RecyclerView.Adapter<ReturningAdapter.ReturningViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturningViewHolder {
        context = parent.context

        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_list, parent, false)

        return ReturningViewHolder(view)

    }

    override fun getItemCount(): Int {
        return select.size
    }

    override fun onBindViewHolder(holder: ReturningViewHolder, position: Int) {
GlobalScope.launch {
    holder.patinum.text = select[position].patientNum
    holder.patiname.text = select[position].name
    holder.patigender.text = select[position].gender
    holder.patibirth.text = select[position].birthDay
    holder.layout.setOnClickListener {
        val intent = Intent(holder.itemView.context, UserInfoActivity::class.java)
        ContextCompat.startActivity(holder.itemView.context,intent,null)
        Visiter.Visi.name = select[position].name.toString()
        Visiter.Visi.gender = select[position].gender.toString()
        Visiter.Visi.birth = select[position].birthDay.toString()
        Visiter.Visi.burndate = select[position].burnDate.toString()

    }

}
    }



    //리싸이클러 뷰 실행시 이미지 및 부위 설정을 설정하는 펑션
    inner class ReturningViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var patinum : TextView = itemView.findViewById(R.id.pati_num)
        var patiname : TextView = itemView.findViewById(R.id.pati_name)
        var patigender :TextView = itemView.findViewById(R.id.pati_gender)
        var patibirth : TextView = itemView.findViewById(R.id.pati_birth)

        var layout : LinearLayout = itemView.findViewById(R.id.listLayout)



    }

}
