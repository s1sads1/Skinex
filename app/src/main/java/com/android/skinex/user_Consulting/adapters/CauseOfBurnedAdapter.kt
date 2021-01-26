package com.android.skinex.user_Consulting.adapters

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skinex.R
import com.android.skinex.databinding.CauseOfBurnedBinding
import com.android.skinex.databinding.CauseOfDetailBurnedBinding
import com.android.skinex.databinding.UserInfoBinding
import com.android.skinex.publicObject.Validation

//import com.phonegap.WPIAS.user_Consulting.ConsultingActivity
//import com.phonegap.WPIAS.R
//import com.phonegap.WPIAS.publicObject.Validation
//import kotlinx.android.synthetic.main.activity_consulting.*
//import kotlinx.android.synthetic.main.cause_of_burned.view.*

class CauseOfBurnedAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<CauseOfBurnedAdapter.CauseOfBurnedViewHolder>() {

    private lateinit var context : Context

    var viewArr = ArrayList<View>()

    var isFirst = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CauseOfBurnedViewHolder {

        context = parent.context
        var view = LayoutInflater.from(parent.context).inflate(R.layout.cause_of_burned, parent, false)

        return CauseOfBurnedViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: CauseOfBurnedViewHolder, position: Int) {

        holder.cause.text = arr[position]

    }

    //화상 원인 클릭시 메서드
    fun whenLoad(position: Int){

        if(position == 0 && isFirst){

            viewArr[0].dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN,
                    viewArr[0].x,
                    viewArr[0].y,
                    0
                )
            )

            viewArr[0].dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis() + 100,
                    MotionEvent.ACTION_UP,
                    viewArr[0].x,
                    viewArr[0].y,
                    0
                )
            )

            isFirst = false

        }

    }


    inner class CauseOfBurnedViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var cause = view.findViewById<TextView>(R.id.cause)
        var causeWrapper = view.findViewById<ConstraintLayout>(R.id.causeWrapper)

        init {

            viewArr.add(view)

            //화상 원인 버튼 클릭 이벤트
            //클릭시 버튼 변화 화상 원인 상세 사항 체크박스 나열
            causeWrapper.setOnClickListener {

                for((i, causeView) in viewArr.withIndex()){

                    if(i == absoluteAdapterPosition) {

                        causeView.findViewById<ConstraintLayout>(R.id.causeWrapper).setBackgroundResource(
                            R.drawable.btn_cause_focused
                        )

                        causeView.findViewById<TextView>(R.id.cause).setTextColor(ContextCompat.getColor(
                            context,
                            R.color.white
                        ))

                    }else{

                        causeView.findViewById<ConstraintLayout>(R.id.causeWrapper).setBackgroundResource(
                            R.drawable.btn_cause_unfocused
                        )

                        causeView.findViewById<TextView>(R.id.cause).setTextColor(ContextCompat.getColor(
                            context,
                            R.color.black
                        ))

                    }

                }

                Validation.vali.burnStyleV = (absoluteAdapterPosition + 1).toString().padStart(3, '0')
                whenLoad(absoluteAdapterPosition)

            }

            whenLoad(0)

        }

    }

}