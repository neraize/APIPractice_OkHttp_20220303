package com.nepplus.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.nepplus.apipractice_okhttp_20220303.databinding.ActivitySignUpBinding
import com.nepplus.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    lateinit var binding:ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        setupEvents()
        setValues()
    }


    override fun setupEvents() {

        // 닉네임 검사 버튼 기능
        binding.btnNicknameCheck.setOnClickListener {

            val inputNickname = binding.edtNickname.text.toString()

            ServerUtil.getRequestDuplicatedCheck("NICK_NAME", inputNickname, object :ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

                    val code = jsonObj.getInt("code")

                    runOnUiThread {
                        when(code){
                            200->{
                                binding.txtNicknameCheckResult.text = "사용해도 좋은 닉네임입니다"
                            }
                            else->{
                                binding.txtNicknameCheckResult.text = "다른 닉네임으로 다시검사해주세요"
                            }
                        }
                    }
                }
            })
        }

        binding.edtNickname.addTextChangedListener { 
            
            binding.txtNicknameCheckResult.text = "중복 확인을 해주세요"
        }



        binding.edtId.addTextChangedListener {

//            Log.d("입력내용: ", it.toString())
            // 내용이 한글자라도 바뀌면, 무조건 재검사 요구 문장
            binding.txtEmailCheckResult.text = "중복 확인을 해주세요"
        }

        binding.btnEmailCheck.setOnClickListener {

            // 입력한 이메일 값 추출
            val inputEmail = binding.edtId.text.toString()
            
            // 서버의 중복 확인 기능 (/user_check -GET) API 활용 => ServerUtil에 함수 추가. 가져다 활용
            // 그 응답 code값에 따라 다른 문구 배치
            ServerUtil.getRequestDuplicatedCheck("EMAIL", inputEmail, object :ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {
                    // code 값에 따라 이메일 사용 가능 여부
                    val code = jsonObj.getInt("code")
                    
                    runOnUiThread {
                        when(code){
                            200->{
                                binding.txtEmailCheckResult.text = "사용해도 좋은 이메일입니다"
                            }
                            else->{
                                binding.txtEmailCheckResult.text = "다른 이메일로 다시 검사해주세요"
                            }
                        }
                    }

                }
            })
            
        }


        binding.btnSignUp.setOnClickListener {

            // <도전과제>  이메일 -@ 가 반드시 포함되어야함. 비밀번호가 8자이상, 닉네임 2자이상

            // <도전과제> 만약 이메일/ 닉네임 중복거사를 통과하지 못한 상태라면,
            // 토스트로 "이메일 중복검사를 통과해야합니다" 등의 문구만 출력, 가입 진행 X

            // 힌트) 진행할 상황이 아니라면, return 처리하면 함수종료


            val inputEmail = binding.edtId.text.toString()
            val inputPw = binding.edtPassword.text.toString()
            val inputNickname =binding.edtNickname.text.toString()

            ServerUtil.putRequestSignUp(
                inputEmail,
                inputPw,
                inputNickname,
                object:ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {
                        // 회원가입 성공 / 실패 분기
                        val code = jsonObj.getInt("code")

                        if(code==200){
                            // 가입한 사람의 닉네임 추출 -> 000님 가입을 축하합니다.  토스트
                            // 회원가입화면 종료 -> 로그인화면 복귀
                            val dataObj = jsonObj.getJSONObject("data")
                            val userObj = dataObj.getJSONObject("user")
                            val nickname = userObj.getString("nick_name")
                            
                            runOnUiThread {
                                Toast.makeText(mContext, "${nickname}님, 가입을 축하합니다!", Toast.LENGTH_SHORT).show()
                            }

                            // 화면 종료 : 객체 소멸(UI 동작 X)
                            finish()

                        }
                        else{
                            val message = jsonObj.getString("message")

                            runOnUiThread {
                                Toast.makeText(mContext, "실패사유: ${message}", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            )


        }
    }


    override fun setValues() {

    }
}