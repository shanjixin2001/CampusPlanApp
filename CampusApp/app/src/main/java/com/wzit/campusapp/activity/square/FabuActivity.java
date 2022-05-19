package com.wzit.campusapp.activity.square;

import static com.wzit.campusapp.utils.common.CommonUtils.DelayClosed;
import static com.wzit.campusapp.utils.common.CommonUtils.GetEditStr;
import static com.wzit.campusapp.utils.common.CommonUtils.GetTextStr;
import static com.wzit.campusapp.utils.common.CommonUtils.OKJsonPost;
import static com.wzit.campusapp.utils.common.CommonUtils.SelectImage;
import static com.wzit.campusapp.utils.common.CommonUtils.TInfo2;
import static com.wzit.campusapp.utils.common.CommonUtils.TWarning2;
import static com.wzit.campusapp.utils.common.CommonUtils.uploadFile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 圈子发布
 */
@SuppressLint("NonConstantResourceId")
public class FabuActivity extends BaseActivityWhite {


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
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.top5)
    RelativeLayout top5;
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
     * 加载布局
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_square;
    }

    /**
     * 初始化
     */
    @Override
    protected void init() {
        activity = this;
        icMore.setVisibility(View.GONE);
        tvTitle.setText("发布圈子");
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.ic_back, R.id.tianjia, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.tianjia:
                SelectImage(activity, REQUEST_SELECT_IMAGES_CODE);//选择图片
                break;
            case R.id.submit:
                SaveAction();
                break;
        }
    }

    /**
     * 发布圈子
     */
    private void SaveAction() {
        //获取文本框中的内容
        String content = GetEditStr(etContent);
        String titles = GetEditStr(tvName);

        //判断
        if (titles.isEmpty() || content.isEmpty()) {
            TWarning2("请完善信息");
        } else if (imgurl.isEmpty()) {
            TWarning2("请上传一张圈子图片");
        } else {
            //设置参数
            HashMap<Object, Object> map = new HashMap<>();
            map.put("communityName", titles);
            map.put("communityImg", imgurl);
            map.put("communityIntro", content);
            OKJsonPost(activity, new Gson().toJson(map), AppNetConfig.InsertCommunityInfo, new OkSuccessInterface() {
                @Override
                public void OnSuccess(String json) {
                    InsertCmtBean bean = new Gson().fromJson(json, InsertCmtBean.class);
                    if (bean.getCode() == 1){
                        TInfo2("发布成功");
                        DelayClosed(activity);
                    }else {
                        TWarning2("发布失败");
                    }
                }
            });
        }
    }

    /**
     * 选择图片回调
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
            path = imagePaths.get(0);//图片路径
            uploadFile(activity, path, AppNetConfig.upload, new OkSuccessInterface() {
                @Override
                public void OnSuccess(String json) {
                    UplaodBean bean = new Gson().fromJson(json, UplaodBean.class);
                    if (bean.getCode() == 200) {
                        imgurl = bean.getFileName();
                        Glide.with(activity).load(path).into(show);//把选中的图片显示出来
                        tianjia.setVisibility(View.GONE);
                    } else {
                        TWarning2("图片上传失败");
                    }
                }
            });
        }
    }
}
