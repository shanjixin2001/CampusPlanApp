package com.wzit.campusapp.activity.square;

import static com.wzit.campusapp.utils.common.CommonUtils.DelayClosed;
import static com.wzit.campusapp.utils.common.CommonUtils.GetEditStr;
import static com.wzit.campusapp.utils.common.CommonUtils.OKJsonPost;
import static com.wzit.campusapp.utils.common.CommonUtils.SelectImage;
import static com.wzit.campusapp.utils.common.CommonUtils.TInfo2;
import static com.wzit.campusapp.utils.common.CommonUtils.TWarning2;
import static com.wzit.campusapp.utils.common.CommonUtils.getUserInfo;
import static com.wzit.campusapp.utils.common.CommonUtils.uploadFile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lcw.library.imagepicker.ImagePicker;
import com.wzit.campusapp.R;
import com.wzit.campusapp.base.BaseActivityWhite;
import com.wzit.campusapp.bean.InsertCmtBean;
import com.wzit.campusapp.bean.UplaodBean;
import com.wzit.campusapp.interfaces.OkSuccessInterface;
import com.wzit.campusapp.utils.api.AppNetConfig;
import com.wzit.campusapp.view.IconFontTextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class SubComtInfoActivity extends BaseActivityWhite {


    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_top2)
    ImageView ivTop2;
    @BindView(R.id.ic_back)
    IconFontTextView icBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ic_more)
    IconFontTextView icMore;
    @BindView(R.id.show)
    ImageView show;
    @BindView(R.id.tianjia)
    TextView tianjia;
    @BindView(R.id.rl_tupian)
    RelativeLayout rlTupian;
    @BindView(R.id.tv_tp)
    TextView tvTp;
    @BindView(R.id.submit)
    Button submit;
    private String path;
    private String imgurl = "";

    /**
     * ????????????
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_sub_comt_info;
    }

    /**
     * ?????????
     */
    @Override
    protected void init() {
        activity = this;
        icMore.setVisibility(View.GONE);
        tvTitle.setText("????????????");
    }

    /**
     * ????????????
     */
    @OnClick({R.id.ic_back, R.id.tianjia, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.tianjia:
                SelectImage(activity, REQUEST_SELECT_IMAGES_CODE);//????????????
                break;
            case R.id.submit:
                SaveAction();
                break;
        }
    }

    /**
     * ????????????
     */
    private void SaveAction() {

        //??????
        if (imgurl.isEmpty()) {
            TWarning2("???????????????????????????");
        } else {
            //????????????
            HashMap<Object, Object> map = new HashMap<>();
            map.put("comtId",getIntent().getStringExtra("comtId"));
            map.put("subComtImg", imgurl);
            map.put("userId",getUserInfo().getUserId());

            OKJsonPost(activity, new Gson().toJson(map), AppNetConfig.insertSubComtInfo, new OkSuccessInterface() {
                @Override
                public void OnSuccess(String json) {
                    InsertCmtBean bean = new Gson().fromJson(json, InsertCmtBean.class);
                    if (bean.getCode() == 1){
                        TInfo2("????????????");
                        DelayClosed(activity);
                    }else {
                        TWarning2("????????????");
                    }
                }
            });
        }
    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_SELECT_IMAGES_CODE) {
            List<String> imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            path = imagePaths.get(0);//????????????
            uploadFile(activity, path, AppNetConfig.upload, new OkSuccessInterface() {
                @Override
                public void OnSuccess(String json) {
                    UplaodBean bean = new Gson().fromJson(json, UplaodBean.class);
                    if (bean.getCode() == 200) {
                        imgurl = bean.getFileName();
                        Glide.with(activity).load(path).into(show);//??????????????????????????????
                        tianjia.setVisibility(View.GONE);
                    } else {
                        TWarning2("??????????????????");
                    }
                }
            });
        }
    }
}