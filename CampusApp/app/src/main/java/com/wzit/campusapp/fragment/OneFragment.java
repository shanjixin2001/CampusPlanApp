package com.wzit.campusapp.fragment;

import static com.wzit.campusapp.utils.common.CommonUtils.OKJsonPost;
import static com.wzit.campusapp.utils.common.CommonUtils.ToPage;
import static com.wzit.campusapp.utils.common.CommonUtils.getUserInfo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.v2.BottomMenu;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wzit.campusapp.R;
import com.wzit.campusapp.activity.home.InvalidActivity;
import com.wzit.campusapp.activity.home.IsFinishedActivity;
import com.wzit.campusapp.activity.home.NotFinishedActivity;
import com.wzit.campusapp.activity.home.PlanActivity;
import com.wzit.campusapp.activity.home.SXXActivity;
import com.wzit.campusapp.adapter.NotFinishedExpandableListviewAdapter;
import com.wzit.campusapp.base.BaseFragment;
import com.wzit.campusapp.bean.GetPlanBean;
import com.wzit.campusapp.bean.NewPlanBean;
import com.wzit.campusapp.bean.UpdatePlaneBean;
import com.wzit.campusapp.bean.UserLoginBean;
import com.wzit.campusapp.interfaces.OkSuccessInterface;
import com.wzit.campusapp.interfaces.PlanCheckInterface;
import com.wzit.campusapp.utils.api.AppNetConfig;
import com.wzit.campusapp.utils.common.CommonUtils;
import com.wzit.campusapp.view.IconFontTextView;
import com.wzit.campusapp.view.NestedExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ??????
 */
@SuppressLint("NonConstantResourceId")
public class OneFragment extends BaseFragment {

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
    @BindView(R.id.iv_fabu)
    ImageView ivFabu;
    @BindView(R.id.expandlistview)
    NestedExpandableListView expandlistview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    /**
     * ????????????
     */
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one;
    }

    /**
     * ?????????
     */
    @Override
    protected void init() {
        icBack.setVisibility(View.GONE);
        tvTitle.setText("??????");
        getData();

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setHeaderHeight(100);
        refreshLayout.setRefreshHeader(new ClassicsHeader(activity));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    /**
     * ????????????
     */
    @OnClick({R.id.ic_more, R.id.iv_fabu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_more://??????
                List<String> list = new ArrayList<>();
//                list.add("????????????");
                list.add("?????????");
                list.add("?????????");
                list.add("?????????");
                list.add("?????????");
                BottomMenu.show((AppCompatActivity) activity, list, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
//                      if (index == 0){//????????????
//                          ToPage(IsFinishedActivity.class);
//                      }else
                        if (index == 0) {//?????????
                            ToPage(SXXActivity.class);
                        } else if (index == 1) {//?????????
                            ToPage(IsFinishedActivity.class);
                        } else if (index == 2) {//?????????
                            ToPage(NotFinishedActivity.class);
                        } else if (index == 3) {//?????????
                            ToPage(InvalidActivity.class);
                        }
                    }
                }, true);
                break;
            case R.id.iv_fabu://??????
                CommonUtils.ToPage(PlanActivity.class);
                break;
        }
    }

    /**
     * ????????????
     */
    private void getData() {
        List<List<NewPlanBean>> list3 = new ArrayList<>();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("planIsFinished", 2);
        UserLoginBean.DataDTO userInfo = getUserInfo();
        System.out.println(userInfo);
        OKJsonPost(activity, new Gson().toJson(map), AppNetConfig.getPlan, new OkSuccessInterface() {
            @Override
            public void OnSuccess(String json) {
                System.err.println(json);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                GetPlanBean bean = new Gson().fromJson(json, GetPlanBean.class);
                if (bean.getCode() == 1) {
                    List<List<GetPlanBean.DataDTO.ChildrenDTO>> listList = bean.getData().getChildren();
                    for (int i = 0; i < listList.size(); i++) {
                        List<GetPlanBean.DataDTO.ChildrenDTO> children = bean.getData().getChildren().get(i);
                        List<NewPlanBean> list1 = new ArrayList<>();
                        for (int j = 0; j < children.size(); j++) {
                            NewPlanBean bean1 = new NewPlanBean();
                            bean1.setId(children.get(j).getPlanId());
                            bean1.setPlanName(children.get(j).getPlanName());
                            bean1.setRank(children.get(j).getPlanDegree());
                            bean1.setPlanTime(children.get(j).getDay());
                            bean1.setPlanContent(children.get(j).getPlanContent());
                            list1.add(bean1);
                        }
                        list3.add(list1);
                    }
                    NotFinishedExpandableListviewAdapter adapter = new NotFinishedExpandableListviewAdapter(activity, bean.getData().getGroup(), list3);
                    expandlistview.setGroupIndicator(null);
                    expandlistview.setAdapter(adapter);
                    //??????
                    adapter.setPlanCheckInterface(new PlanCheckInterface() {
                        @Override
                        public void IsRedCheck(CompoundButton compoundButton, NewPlanBean planBean, boolean b) {
                            if (b) {
                                new XPopup.Builder(activity).asConfirm("??????", "??????????????????????????????????????????",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {//??????
                                                updatePlan(planBean.getId());
                                            }
                                        }, new OnCancelListener() {//??????
                                            @Override
                                            public void onCancel() {
                                                compoundButton.setChecked(false);
                                            }
                                        })
                                        .show();
                            }
                        }

                        @Override
                        public void IsYellowCheck(CompoundButton compoundButton, NewPlanBean planBean, boolean b) {
                            if (b) {
                                new XPopup.Builder(activity).asConfirm("??????", "??????????????????????????????????????????",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {//??????
                                                updatePlan(planBean.getId());
                                            }
                                        }, new OnCancelListener() {//??????
                                            @Override
                                            public void onCancel() {
                                                compoundButton.setChecked(false);
                                            }
                                        })
                                        .show();
                            }
                        }

                        @Override
                        public void IsBlueCheck(CompoundButton compoundButton, NewPlanBean planBean, boolean b) {
                            if (b) {
                                new XPopup.Builder(activity).asConfirm("??????", "??????????????????????????????????????????",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {//??????
                                                updatePlan(planBean.getId());
                                            }
                                        }, new OnCancelListener() {//??????
                                            @Override
                                            public void onCancel() {
                                                compoundButton.setChecked(false);
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                } else {
                    expandlistview.setAdapter(new NotFinishedExpandableListviewAdapter(activity, new ArrayList<>(),  new ArrayList<>()));
                }

            }
        });
    }

    /**
     * ????????????
     */
    private void updatePlan(int planId) {
        //????????????
        HashMap<Object, Object> map = new HashMap<>();
        map.put("planIsFinished", 1);
        map.put("planId", planId);
        OKJsonPost(activity, new Gson().toJson(map), AppNetConfig.PlanInfoUpdate, new OkSuccessInterface() {
            @Override
            public void OnSuccess(String json) {
                UpdatePlaneBean bean = new Gson().fromJson(json, UpdatePlaneBean.class);
                if (bean.getCode() == 1) {
                    CommonUtils.TInfo2("????????????");
                    init();
                } else {
                    CommonUtils.TWarning2("????????????");
                }
            }
        });
    }

    /**
     * ??????
     */
    @Override
    public void onResume() {
        init();
        super.onResume();
    }
}
