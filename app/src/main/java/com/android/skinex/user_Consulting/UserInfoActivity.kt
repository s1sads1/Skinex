package com.android.skinex.user_Consulting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skinex.R
import com.android.skinex.camera2Api.CameraX
import com.android.skinex.databinding.*
import com.android.skinex.publicObject.Location
import com.android.skinex.publicObject.Validation
import com.android.skinex.publicObject.Visiter
import com.android.skinex.user_Consulting.adapters.CauseOfBurnedAdapter
import com.android.skinex.user_Consulting.adapters.PartListAdapter
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDate.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class  UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: UserInfoBinding

    //on Activity Result Request Code 상수
    val REQUEST_TAKE_PHOTO_10 = 1
    val REQUEST_TAKE_PHOTO_20 = 2

        //storage 문자열
        val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagewpias;AccountKey=G+ZyYwRLvxTFebMpLqsSeNI/V+1ALImJqs1MAG1rD315BN1TRO7Q8CpcKv0KOmRB9hasKF4pJqZkTEJ3TEAlPw=="

        //AzureAsyncTask에 들어가는 image uri 및 input stream 배열
//        lateinit var imageUri: Uri
//        lateinit var imageUri2: Uri
//        var inputStreamArr = ArrayList<InputStream>()
//        var imageLengthArr = ArrayList<Int>()

        //사진 원본파일 이미지 저장용 path
//        var currentPhotoPath = ""

        //촬영 모드 구분용도
        var cameraMode = ""

        var MYyear = 0
        var MYmonth = 0
        var MYday = 0

//        var popup = false

        var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
        var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
        var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")
        var arr = ArrayList<String>()

    var fHead = arrayListOf("이마", "눈", "코", "입", "귀", "볼", "목", "뒤통수", "목덜미", "기타")
    var bHead = arrayListOf("이마", "눈", "코", "입", "귀", "볼", "목", "뒤통수", "목덜미", "기타")
    var fShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var bShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var chest = arrayListOf("좌측 가슴", "우측 가슴", "가슴 전체", "기타")
    var back = arrayListOf("좌측 등", "우측 등", "등 전체", "기타")
    var belly = arrayListOf("좌측 배", "우측 배", "배 전체", "기타")
    var waist = arrayListOf("좌측 허리", "우측 허리", "허리 전체", "기타")
    var fArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var bArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var fHands = arrayListOf(
        "좌측 손바닥",
        "우측 손바닥",
        "양쪽 손바닥",
        "좌측 손등",
        "우측 손등",
        "양쪽 손등",
        "좌측 손가락",
        "우측 손가락",
        "양쪽 손가락",
        "기타"
    )
    var bHands = arrayListOf(
        "좌측 손바닥",
        "우측 손바닥",
        "양쪽 손바닥",
        "좌측 손등",
        "우측 손등",
        "양쪽 손등",
        "좌측 손가락",
        "우측 손가락",
        "양쪽 손가락",
        "기타"
    )
    var privatePart = arrayListOf("음부", "기타")
    var hip = arrayListOf("좌측 엉덩이", "우측 엉덩이", "엉덩이 전체", "기타")
    var fLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var bLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var fFoot = arrayListOf(
        "좌측 발등",
        "우측 발등",
        "양쪽 발등",
        "좌측 발가락",
        "우측 발가락",
        "양쪽 발가락",
        "좌측 발바닥",
        "우측 발바닥",
        "양쪽 발바닥",
        "좌측 뒤꿈치",
        "우측 뒤꿈치",
        "양쪽 뒤꿈치",
        "기타"
    )
    var bFoot = arrayListOf(
        "좌측 발등",
        "우측 발등",
        "양쪽 발등",
        "좌측 발가락",
        "우측 발가락",
        "양쪽 발가락",
        "좌측 발바닥",
        "우측 발바닥",
        "양쪽 발바닥",
        "좌측 뒤꿈치",
        "우측 뒤꿈치",
        "양쪽 뒤꿈치",
        "기타"
    )
    var respiratory = arrayListOf("호흡기", "기타")
    var m_insertUserMap = HashMap<String, String>()
        //액티비티 초기화될때 validation 전역 변수들을 초기화 시켜준다
        init {
            Validation.valiInit()
            Location.init()
//            print("testprint")
        }
    var m_testMap = HashMap<String, String>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = UserInfoBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)





            //test용 곧 삭제할것
//            fun frontActivated() {
//                var partListAdapter =
//                    PartListAdapter(bodyPartFront)
//                binding.bodyPartRecyclerView.layoutManager = LinearLayoutManager(
//                    this,
//                    RecyclerView.VERTICAL,
//                    false
//                )
//                binding.bodyPartRecyclerView.adapter = partListAdapter
//
//                binding.bodyImage.setImageResource(R.drawable.f)
//        val intent = intent
//        var partString= intent.getStringExtra("part")
//
//        selectArrFront(partString)
//
//        Log.d("come", "Ok")
//        Log.d("partString", partString)

//var part = PartListAdapter.arr
//var position = partListAdapter.onBindViewHolder
//        binding.bodyPartRecyclerView.adapter.bindViewHolder()
//        selectArrFront(binding.bodyPartRecyclerView.adapter)

//            }

//            setContentView(R.layout.user_info)
//            SetTransparentBar()
//            setTitle()
            birthCalendar()
            popupCalendar()
//            burnedHistory()
            bodyPartCheck()
//            val partString = Validation.vali.homeAreaV.toString()
//            selectArrFront(partString)
            genderTouch()
//            ageSpinnerPopup()
            causeRecyclerViewActivated()
//            deptRecyclerViewActivated()
//            questionRecyclerViewActivated()
            photoGraphingAlert()
            submitBurnConsulting()
            setDescendentViews(window.decorView.rootView)
            eventInit()
            Setting()
            radiobuutonset()
            returningset()

        }

    fun genderset() {
        if(Visiter.Visi.gender =="남성"|| Visiter.Visi.gender == "") {
            binding.genderMan.performClick()
        } else {
            binding.genderWoman.performClick()
        }
    }

    fun returningset() {
        binding.userInfoText1.setText(Visiter.Visi.name)
        binding.birth.setText(Visiter.Visi.birth)
        binding.whenBurned.setText(Visiter.Visi.burndate)
        Log.d("성별", Visiter.Visi.gender)
        genderset()
    }

    //라디오버튼 성별 클릭시 하나만 나머지 해제
    fun radiobuutonset() {
        binding.genderRadioGroup.setOnCheckedChangeListener{
            radioGroup, checkId ->
            when(checkId) {
                R.id.genderMan ->{}
                R.id.genderWoman ->{}

            }
        }
    }

        //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
        //validation을 위한 이벤트 이 펑션에 등록
        //액티비티 시작될때 초기화될 설정들 정리
        @SuppressLint("SimpleDateFormat")
        fun eventInit() {


            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

            binding.bodyFront.isChecked = true

            Log.d("Check", binding.bodyFront.isChecked.toString())

        }

        //앞 뒤 구분 버튼 체크 이벤트
        //버튼 및 버튼 글자 색 변환이 이벤트에 걸려있음

    //생년월일 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun birthCalendar(){
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        binding.birth.setOnClickListener{
            val dp = DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.birth.setText(
                        "${year}-${(month + 1).toString().padStart(2, '0')}-${
                            dayOfMonth.toString().padStart(
                                2,
                                '0'
                            )
                        }"
                    )
                },
                MYyear,
                MYmonth,
                MYday

            )
            dp.setOnCancelListener {
            }
            dp.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dp.show()
        }
    }

    //화상입은 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun popupCalendar(){
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        binding.whenBurned.setOnClickListener{
            val dp = DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.whenBurned.setText(
                        "${year}-${(month + 1).toString().padStart(2, '0')}-${
                            dayOfMonth.toString().padStart(
                                2,
                                '0'
                            )
                        }"
                    )
                },
                MYyear,
                MYmonth,
                MYday

            )
            dp.setOnCancelListener {
            }
            dp.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dp.show()
        }
    }


        fun bodyPartCheck() {
            Log.d("오나", "왔다")

            binding.bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                    binding.bodyBack.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    frontActivated()
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    binding.bodyBack.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                }
            }

            binding.bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                    binding.bodyFront.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    backActivated()
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.ocean_blue
                        )
                    )
                    binding.bodyFront.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                }
            }
        }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
        fun frontActivated() {
            var partListAdapter =
                    PartListAdapter(bodyPartFront)
            binding.bodyPartRecyclerView.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            binding.bodyPartRecyclerView.adapter = partListAdapter

        binding.bodyImage.setImageResource(R.drawable.f)

        }



        //뒤 버튼 터치시 실행되는 리싸이클러 뷰
        fun backActivated() {
            var partListAdapter =
                    PartListAdapter(bodyPartBack)

            binding.bodyPartRecyclerView.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            binding.bodyPartRecyclerView.adapter = partListAdapter

            binding.bodyImage.setImageResource(R.drawable.b)

        }


    fun Setting() {
        m_insertUserMap["PATIENTNUM"] = "Patientnum817"
        m_insertUserMap["NAME"] = "Name"
        m_insertUserMap["GENDER"] = "Patient"
        m_insertUserMap["BIRTHDAY"] = "BIRTHDAY"
        m_insertUserMap["INSERTDAY"] = "InsertDay"
        m_insertUserMap["BURNDATE"] = "BurnDate"
        m_insertUserMap["BODYSTYLE"] = "BodyStyle"
        m_insertUserMap["BODYDETAIL"] = "BodyDetail"
        m_insertUserMap["BURNSTYLE"] = "BurnStyle"
        m_insertUserMap["CAMERAURL1"] = "CAMERAURL1"
        m_insertUserMap["CAMERAURL2"] = "CAMERAURL2"
        m_insertUserMap["RESULT"] = "RESULT"


        //이전버튼으로 데이터 submit 임시변경
//        binding.submit.setOnClickListener{ userInfoSubmit() }
    }


    fun userInfoSubmit() {

        //        ApiUtill().getINSERT_VISITER().insert_visiter(m_insertUserMap).enqueue(object : Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Toast.makeText(this@UserInfoActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//
//                Log.d("response.body", response.body().toString())
//                if(response.body() == "S"){
//
//                    Toast.makeText(this@UserInfoActivity, "성공!!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@UserInfoActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

    }





//        //성별 버튼 터치 펑션
//        fun genderTouch() {
//            binding.male.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) {
//                    buttonView.setTextColor(ContextCompat.getColor(this,
//                            R.color.white
//                    ))
//                    female.setTextColor(ContextCompat.getColor(this,
//                            R.color.ocean_blue
//                    ))
//                    Validation.vali.genderV = "M"
//                } else {
//                    buttonView.setTextColor(ContextCompat.getColor(this,
//                            R.color.ocean_blue
//                    ))
//                    female.setTextColor(ContextCompat.getColor(this,
//                            R.color.white
//                    ))
//                }
//            }
//
//            female.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) {
//                    buttonView.setTextColor(ContextCompat.getColor(this,
//                            R.color.white
//                    ))
//                    male.setTextColor(ContextCompat.getColor(this,
//                            R.color.ocean_blue
//                    ))
//                    Validation.vali.genderV = "F"
//                } else {
//                    buttonView.setTextColor(ContextCompat.getColor(this,
//                            R.color.ocean_blue
//                    ))
//                    male.setTextColor(ContextCompat.getColor(this,
//                            R.color.white
//                    ))
//                }
//            }
//        }

        //연령 spinner 펑션
//        fun ageSpinnerPopup() {
//            var arrayAdapter: ArrayAdapter<String>? = null
//            var generation = arrayListOf("", "0~3세", "4~6세", "7~15세", "16~20세", "21~30세", "31~40세", "41~50세", "51~60세", "61세이상")
//
//            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, generation)
//            age.adapter = arrayAdapter
//            age.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//
//                override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View?,
//                        position: Int,
//                        id: Long
//                ) {
//                    Validation.vali.ageV = if (position == 0) {
//                        ""
//                    } else {
//                        age.selectedItem.toString()
//                    }
//                }
//            }
//        }


        //나이 구하는 펑션
//        fun getAge(birthYear: String): Int {
//            var current = Calendar.getInstance()
//            var currentYear = current.get(Calendar.YEAR)
//            var age = currentYear - birthYear.split('-')[0].toInt()
//
//            return age
//        }

        //화상 원인 버튼 카테고리 뿌려주는 펑션
        fun causeRecyclerViewActivated() {
            binding.causeOfBurnedRecyclerView.layoutManager = GridLayoutManager(this, 4)
            binding.causeOfBurnedRecyclerView.adapter =
                CauseOfBurnedAdapter(
                    causeCategory
                )
        }

        //최종적으로 글 올리는 펑션
        //inputStream 배열에 imageUri를 다 넣은다음 imageuri와 imageurlv를 초기화 한다
        @SuppressLint("SimpleDateFormat")
        fun submitBurnConsulting() {

//            binding.submit.setOnClickListener {

//                if (validationConsulting()) {
//                    inputStreamArr.add(contentResolver.openInputStream(imageUri)!!)
//                    inputStreamArr.add(contentResolver.openInputStream(imageUri2)!!)
//
//                    imageUri = Uri.EMPTY
//                    imageUri2 = Uri.EMPTY
//                    Validation.vali.imageUrl1V = ""
//                    Validation.vali.imageUrl2V = ""
//
//                    for (inputStream in inputStreamArr) {
//                        imageLengthArr.add(inputStream.available())
//                    }
//
//
//                    AzureAsyncTask(
//                            this,
//                            inputStreamArr,
//                            imageLengthArr
//                    ).execute(storageConnectionString)
//
//                } else {
//                    when {
//                        Validation.vali.imageUrl1V.isEmpty() -> failAlert("상세사진 촬영을 진행해주세요")
//                        Validation.vali.imageUrl2V.isEmpty() -> failAlert("전체사진 촬영을 진행해주세요")
//                        Validation.vali.scarStyleV.isEmpty() -> failAlert("화상 시기를 확인해주세요")
//                        Validation.vali.burnDateV.isEmpty() -> failAlert("화상입은 날짜를 확인해주세요")
//                        Validation.vali.bodyStyleV.isEmpty() -> failAlert("신체부위를 확인해주세요")
//                        Validation.vali.bodyDetailV.isEmpty() -> failAlert("상세 촬영부위를 확인해주세요")
////                    Validation.vali.bodyGitaV.isEmpty() -> Toast.makeText(this, "신체부위 기타를 확인해주세요", Toast.LENGTH_LONG).show()
//                        Validation.vali.burnStyleV.isEmpty() -> failAlert("화상원인을 선택해주세요")
//                        Validation.vali.burnDetailV.isEmpty() -> failAlert("자세한 화상원인을 선택해주세요")
////                    Validation.vali.burnGitaV.isEmpty() -> Toast.makeText(this, "화상 기타를 확인해주세요", Toast.LENGTH_LONG).show()
//                        Validation.vali.careStyleV.isEmpty() -> failAlert("최근에 치료받은곳을 선택해주세요")
//                        Validation.vali.genderV.isEmpty() -> failAlert("성별을 확인해주세요")
//                        Validation.vali.ageV.isEmpty() -> failAlert("연령을 확인해주세요")
//                        Validation.vali.consultingTitleV.isEmpty() -> failAlert("상담 제목을 입력해주세요")
//                        Validation.vali.contentsV.isEmpty() -> failAlert("상담 내용을 입력해주세요")
//                    }
//                }
//            }
        }

        //등록하기전 app 내 validation 체크
//        fun validationConsulting(): Boolean {
//            return Validation.vali.consultingTitleV.isNotEmpty() && Validation.vali.burnDateV.isNotEmpty() && Validation.vali.ageV.isNotEmpty()
//                    && Validation.vali.genderV.isNotEmpty() && (Validation.vali.bodyDetailV.isNotEmpty() || Validation.vali.bodyGitaV.isNotEmpty())
//                    && Validation.vali.burnStyleV.isNotEmpty() && (Validation.vali.burnDetailV.isNotEmpty() || Validation.vali.burnGitaV.isNotEmpty())
//                    && (Validation.vali.careStyleV.isNotEmpty() || Validation.vali.careGitaV.isNotEmpty()) && Validation.vali.scarStyleV.isNotEmpty()
//                    && Validation.vali.proStatusV.isNotEmpty() && Validation.vali.imageUrl1V.isNotEmpty() && Validation.vali.imageUrl2V.isNotEmpty()
//                    && Validation.vali.contentsV.isNotEmpty()
//        }

        //업로드 실패 알럿
        fun failAlert(ment: String) {
            Toast.makeText(this, ment, Toast.LENGTH_SHORT).show()
//            var dialog = Dialog(this)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.custom_alert)
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            var img = dialog.img_alert
//            var title = dialog.txt_alert_title
//            var sub = dialog.txt_alert_sub
//            var btn_left = dialog.btn_alert_left
//            var btn_right = dialog.btn_alert_right
//
//            img.setImageResource(R.drawable.alert_ck_x)
//
//            title.text = "실패"
//            sub.text = ment
//
//            btn_left.visibility = View.GONE
//
//            btn_right.text = "OK"
//            btn_right.setBackgroundResource(R.drawable.btn_blue)
//
//            popup = true
//
//            dialog.setOnDismissListener {
//                popup = false
//                dialog = Dialog(this)
//            }
//
//            btn_right.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialog.show()

        }

        //타이틀바 set Text 펑션
//        fun setTitle() {
//            txt_title.text = "상처상담하기"
//            btn_back.setOnClickListener {
//                onBackPressed()
//            }
//        }


        //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
        fun hideKeyboard() {
            var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            window?.decorView?.clearFocus()
        }

        //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
        fun setDescendentViews(view: View) {

            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideKeyboard()
                    return@setOnTouchListener false
                }
            }

            if (view is RecyclerView) {
                view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                        hideKeyboard()
                    }

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        hideKeyboard()
                        return false
                    }

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    }
                })
            }

            if (view is ViewGroup) {
                for (innerview in view) {
                    setDescendentViews(innerview)
                }
            }
        }

//    fun genderTouch() {
//        binding.genderMan.setOnCheckedChangeListener {  radioGroup, checkId ->
//            if(checkId){
//                Visiter.Visi.gender = 1
//            }
//        }
//        binding.genderWoman.setOnCheckedChangeListener { radioGroup, checkId ->
//            if (checkId) {
//                Visiter.Visi.gender = 2
//            }
//        }
//
//    }

    //라디오버튼 성별 클릭시 하나만 나머지 해제
    fun genderTouch() {
        binding.genderMan.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Visiter.Visi.gender = binding.genderMan.text.toString()
            }
        }
        binding.genderWoman.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Visiter.Visi.gender = binding.genderWoman.text.toString()
            }
        }
    }

    //촬영 클릭시 전체촬영 이벤트
    fun photoGraphingAlert(){

        m_testMap["NAME"] = binding.userInfoText1.text.toString()
//        m_testMap["BIRTH"] = binding.birth.text.toString()
//        m_testMap["BRUNDATE"] = binding.whenBurned.text.toString()


        binding.longDistanceShot.setOnClickListener {
            longDistancePopup()

            var intent0 = Intent(this, CameraX::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
            intent0.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
            Visiter.Visi.name = binding.userInfoText1.text.toString()
//            Visiter.Visi.gender = binding.genderRadioGroup.checkedRadioButtonId
            Visiter.Visi.birth = binding.birth.text.toString()
            Visiter.Visi.burndate = binding.whenBurned.text.toString()
            startActivity(intent0)

            //사진나오는 화면에 유저 정보를 담아서 보내주기위함
//            var intent3 = Intent(this, ResultInfoActivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체 생성
//            intent3.putExtra("username", binding.userInfoText1.text.toString()) // 입력해준 텍스트 값을 담은 뒤 username라는 키로
//            startActivity(intent3)

        }
    }

    //전체촬영
    @SuppressLint("SetTextI18n")
    fun longDistancePopup(){
        cameraMode = "long"
        dispatchTakePictureIntent()
    }

    //핸드폰에 내장된 카메라 관련 어플들을 불러오는 펑션
    //촬영 모드 구분
    private fun dispatchTakePictureIntent() {
        startActivityForResult(
           Intent(this, CameraX::class.java), REQUEST_TAKE_PHOTO_20)
    }
}