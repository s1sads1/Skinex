package com.android.skinex.restApi

//import com.android.skinex.dataClass.UserInfo
//import com.phonegap.WPIAS.dataClass.MycaseInfo
//import com.phonegap.WPIAS.dataClass.QuestionInfo
//import com.phonegap.WPIAS.dataClass.UserInfo
//import com.phonegap.WPIAS.dataClass.pushinfo
//import com.phonegap.WPIAS.dataClass.*
//import com.phonegap.WPIAS.doctor_Question.DoctorNewQuestionActivity
//import okhttp3.ResponseBody
import android.net.Uri
import com.android.skinex.dataclass.AnalyInfo
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // **************************** U S E R ****************************


    @FormUrlEncoded
    @POST("sshconnect")
    fun sshConnect(
        @Field("IMGURL") IMGURL:String,
        @Field("XTL") XTL:String,
        @Field("YTL")  YTL:String,
        @Field("XBR") XBR:String,
        @Field("YBR") YBR:String
    ):Call<AnalyInfo>

//    //sshConnect
//    @FormUrlEncoded
//    @POST("posts/sshConnect")
//    fun requestPostLogin(@Field reqLoginData:ReqLoginData)
//    :Call<ResLoginData>
    // 회원가입
    @POST("https://172.24.192.1:5000/INSERT_VISITER")
    @FormUrlEncoded
    fun insert_visiter(@FieldMap sql: Map<String, String>): Call<String>

//    // 회원정보조회
//    @POST("https://wpias.azurewebsites.net/SELECT_USERWITHTOKENOS")
//    @FormUrlEncoded
//    fun select_userwithtokenos(@FieldMap sql: Map<String, String>): Call<ArrayList<UserInfo>>
//
//
//    // 사용자 질문조회
//    @POST("https://wpias.azurewebsites.net/SELECT_MYQUESTION")
//    @FormUrlEncoded
//    fun select_myquestion(@FieldMap sql: Map<String, String>): Call<ArrayList<QuestionInfo>>
//
//    // 사용자 질문케이스조회
//    @POST("https://wpias.azurewebsites.net/SELECT_MYCASE")
//    @FormUrlEncoded
//    fun select_mycase(@FieldMap sql: Map<String, String>): Call<ArrayList<MycaseInfo>>
//
//    // 경과추가 답변미요청
//    @POST("https://wpias.azurewebsites.net/INSERT_CASE")
//    @FormUrlEncoded
//    fun insert_case(@FieldMap sql: Map<String, String>): Call<String>
//
//    // 경과추가 답변요청
//    @POST("https://wpias.azurewebsites.net/INSERT_CASEREQUEST")
//    @FormUrlEncoded
//    fun insert_caserequest(@FieldMap sql: Map<String, String>): Call<String>
//
//    // 의사 답변에 사용자가 리뷰 등록
//    @POST("https://wpias.azurewebsites.net/UPDATE_FEEDBACK")
//    @FormUrlEncoded
//    fun update_feedback(@FieldMap sql: Map<String, String>): Call<String>
//
//
    // 사용자 질문등록
    @POST("https://wpias.azurewebsites.net/INSERT_QUESTION")
    @FormUrlEncoded
    fun insert_question(@FieldMap sql: Map<String, String>): Call<String>

//    // 사용자 질문등록 + 위치추가
//    @POST("https://wpias.azurewebsites.net/INSERT_QUESTIONWITHAREA")
//    @FormUrlEncoded
//    fun insert_questionwitharea(@FieldMap sql: Map<String, String>): Call<String>
//
//
//    // 설정창 스위치1 상태 변경
//    @POST("https://wpias.azurewebsites.net/UPDATE_SWITCH1")
//    @FormUrlEncoded
//    fun update_switch1(@FieldMap sql: Map<String, String>): Call<String>
//
//    // 설정창 스위치2 상태 변경
//    @POST("https://wpias.azurewebsites.net/UPDATE_SWITCH2")
//    @FormUrlEncoded
//    fun update_switch2(@FieldMap sql: Map<String, String>): Call<String>
//
//
//    // FCM 전송
//    @Headers(
//        "Authorization: key=AAAAFqobJsQ:APA91bEHj_c08w_2GrtOMFyOBwb6S6cUQx-K3E56VkoG4Tq6NBR48T64JFvQyMqczVbr6ZJjlMMNXEPQuu5Gb6XB6OYetxXxXS894Amv2j1CmsFFurVYp_T5CQjTTh9ofssUt9AHA7ju",
//        "Content-Type:application/json"
//    )
//    @POST("https://fcm.googleapis.com/fcm/send")
//    fun sendFCM(@Body FCMInteraction: Any): Call<ResponseBody>
//
//
//    // 로그아웃시 토큰 공백으로 변경
//    @POST("https://wpias.azurewebsites.net/UPDATE_TOKENLOGOUT")
//    @FormUrlEncoded
//    fun update_tokenlogout(@FieldMap sql: Map<String, String>): Call<String>
//
//
//    // 상대방 푸시정보 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_CHECKAGREE")
//    @FormUrlEncoded
//    fun select_checkagree(@FieldMap sql: Map<String, String>): Call<ArrayList<pushinfo>>
//
//    // 시,도 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_CITY")
//    fun select_city(): Call<ArrayList<CityInfo>>
//
//    // 구 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_DISTRICT")
//    @FormUrlEncoded
//    fun select_district(@FieldMap sql: Map<String, String>): Call<ArrayList<DistrictInfo>>

    // 사용자 질문등록 + 위치추가 + 거주지 추가
    @POST("https://wpias.azurewebsites.net/INSERT_QUESTIONWITHAREA2")
    @FormUrlEncoded
    fun insert_questionwitharea2(@FieldMap sql: Map<String, String>): Call<String>


//    // **************************** DOCTOR ****************************
//
//    // 신규 질문 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_NEWQUESTION200103")
//    fun select_newquestion(): Call<ArrayList<NewQuestionInfo>>
//
//    // 의사 답변횟수 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_MYANSWERCOUNT")
//    @FormUrlEncoded
//    fun select_myanswercount(@FieldMap sql: Map<String, String>): Call<Any>
//
//    // 질문 Lock Check
//    @POST("https://wpias.azurewebsites.net/SELECT_LOCKCHECK")
//    @FormUrlEncoded
//    fun select_lockcheck(@FieldMap sql: Map<String, String>): Call<ArrayList<LockInfo>>
//
//    // Lock 상태 업데이트
//    @POST("https://wpias.azurewebsites.net/UPDATE_LOCK")
//    @FormUrlEncoded
//    fun update_lock(@FieldMap sql: Map<String, String>): Call<String>
//
//    // 신규질문, 케이스 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_NEWCASE")
//    @FormUrlEncoded
//    fun select_newcase(@FieldMap sql: Map<String, String>): Call<ArrayList<NewCaseInfo>>
//
//    // 신규질문, 케이스 조회
//    @POST("https://wpias.azurewebsites.net/UPDATE_FIRSTANSWER")
//    @FormUrlEncoded
//    fun update_firstanswer(@FieldMap sql: Map<String, String>): Call<String>
//
//    // 내가 한 답변 조회(의사)
//    @POST("https://wpias.azurewebsites.net/SELECT_MYANSWER200106")
//    @FormUrlEncoded
//    fun select_myanswer200106(@FieldMap sql: Map<String, String>): Call<ArrayList<MyAnswerInfo>>
//
//    // 의사 계정 내가 한 답변 경과 조회
//    @POST("https://wpias.azurewebsites.net/SELECT_MYANSWERCASE")
//    @FormUrlEncoded
//    fun select_myanswercase(@FieldMap sql: Map<String, String>): Call<ArrayList<MyAnswerCaseInfo>>
//
//    // 사용자 리뷰에 댓글 등록
//    @POST("https://wpias.azurewebsites.net/UPDATE_REPLY")
//    @FormUrlEncoded
//    fun update_reply(@FieldMap sql: Map<String, String>): Call<String>


}