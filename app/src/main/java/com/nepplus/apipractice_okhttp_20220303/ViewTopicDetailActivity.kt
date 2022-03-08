package com.nepplus.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nepplus.apipractice_okhttp_20220303.databinding.ActivityViewTopicDetailBinding
import com.nepplus.apipractice_okhttp_20220303.datas.TopicData
import com.nepplus.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewTopicDetailBinding

    // 보여주게될 토론 주제 데이터  -> 이벤트 처리, 데이터 표현등 여러함수에 사용
    lateinit var mTopicData:TopicData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topic_detail)
        mTopicData = intent.getSerializableExtra("topic") as TopicData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        binding.txtTitle.text = mTopicData.title
        Glide.with(mContext).load(mTopicData.imageUrl).into(binding.imgTopicBackground)

        getTopicDetailFromServer()
    }

    fun getTopicDetailFromServer(){

        ServerUtil.getRequestTopicDetail(mContext, mTopicData.id, object :ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObject: JSONObject) {

                val dataObj = jsonObject.getJSONObject("data")
                val topicObj = dataObj.getJSONObject("topic")

                // 토론 정보 JSONObject (topicObj) => TopicData() 형태로 변환 (여러화면에서 진행. 함수로 만들어두자)
                val topicData = TopicData.getTopicDataFromJson(topicObj)

                // 변환된 객체를, mTopicData로 다시 대입 => UI반영도 다시 실행
                mTopicData = topicData
            }
        })
    }
}