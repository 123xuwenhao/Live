package cn.nodemedia.qlive.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.nodemedia.qlive.R;
import xyz.tanwb.airship.view.BaseFragment;
import xyz.tanwb.airship.view.BasePresenter;

public abstract class ActionbarFragment<T extends BasePresenter> extends BaseFragment<T> {

    protected FrameLayout content;

    private RelativeLayout actionbar;
    private ImageView actionbarBack;
    private ImageView actionbarTips;
    private TextView actionbarTitle;
    private LinearLayout actionbarMenu;
    private View actionbarBottomLine;

    private LinearLayout functionbar;
    private TextView functionbarButton;
    private RelativeLayout loading;
    private ImageView loadingImage;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.layout_actionbar, null);
        if (getLayoutId() > 0) {
            FrameLayout content = (FrameLayout) rootView.findViewById(R.id.content);
            View contentView = inflater.inflate(getLayoutId(), null);
            content.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return rootView;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        actionbar = getView(R.id.actionbar);
        actionbarBack = getView(R.id.actionbar_back);
        actionbarTips = getView(R.id.actionbar_tips);
        actionbarTitle = getView(R.id.actionbar_title);
        actionbarMenu = getView(R.id.actionbar_menu);
        actionbarBottomLine = getView(R.id.actionbar_bottom_line);
        content = getView(R.id.content);
        functionbar = getView(R.id.functionbar);
        functionbarButton = getView(R.id.functionbar_button);
        loading = getView(R.id.loading);
        loadingImage = getView(R.id.loading_image);

        actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        verifyNetwork();//????????????????????????
    }

    /**
     * ?????????????????????????????????
     *
     * @param visible ????????????
     * @param views   ?????????
     */
    public void setVisibility(int visible, View... views) {
        for (View view : views) view.setVisibility(visible);
    }

    /**
     * ??????????????????????????????
     *
     * @param visible ????????????
     */
    public void hasProgress(int visible) {
        hasProgress(loading, visible);
    }

    /**
     * ??????????????????????????????
     *
     * @param visible ????????????
     */
    public void hasProgress(RelativeLayout loading, int visible) {
        if (loading != null) {
            if (loading.getVisibility() == visible) {
                return;
            }
            AnimationDrawable animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
            if (visible == View.VISIBLE) {
                animationDrawable.start();
            } else {
                animationDrawable.stop();
            }
            loading.setVisibility(visible);
        }
    }

    /**
     * ?????????????????????
     */
    public void verifyNetwork() {
//        if (!NetUtils.isConnected()) {
//            if(mActivity instanceof MainActivity) isRemindshow(View.VISIBLE);
//            else if(mActivity instanceof StartActivity) isRemindshow(View.GONE);
//            else
        //if (mActivity instanceof StartActivity) isRemindshow(View.GONE);
        //else
        // disconnectPage.setVisibility(View.VISIBLE);         //ToastUtils.show(mActivity, "??????????????????????????????????????????");
//        } else {
//            isRemindshow(View.GONE);
        //disconnectPage.setVisibility(View.GONE);
//        }
    }

    /**
     * ????????????????????????
     */
    public void isRemindshow(int visible) {
//        if(visible == View.VISIBLE) networkRemind.setVisibility(View.VISIBLE);
//        else networkRemind.setVisibility(View.GONE);
    }

    /**
     * ??????????????????ActionBar
     *
     * @param visible ????????????
     */
    public void hasActionBar(int visible) {
        actionbar.setVisibility(visible);
    }

    /**
     * ??????title????????????
     *
     * @param resId ??????ID
     */
    public void setBackgroundByRes(int resId) {
        actionbar.setBackgroundResource(resId);
    }

    /**
     * ??????title????????????
     *
     * @param color ?????????
     */
    public void setBackgroundByColor(String color) {
        actionbar.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * ???????????????????????????
     *
     * @param visible ????????????
     */
    public void hasBack(int visible) {
        actionbarBack.setVisibility(visible == View.GONE ? View.INVISIBLE : visible);
    }

    /**
     * ?????????????????????
     *
     * @param resId ??????ID
     */
    public void setBackRes(int resId) {
        actionbarBack.setImageResource(resId);
    }

//    /**
//     * ?????????????????????
//     *
//     * @param imagePath ????????????????????????
//     */
//    public void setBackRes(ImagePath imagePath) {
//        GlideUtils.getCircleImageToUrl(mActivity, imagePath, actionbarBack, R.drawable.actionbar_head);
//        // Glide.with(mActivity).load("http://img3.imgtn.bdimg.com/it/u=2631545466,886965387&fm=21&gp=0.jpg").transform(new TransformToCircle(mContext)).into(actionbarBack);
//    }

    /**
     * ??????????????????
     */
    public void onBackClick() {
        exit();
    }

    /**
     * ??????????????????????????????
     *
     * @param visible ????????????
     */
    public void hasActionBarTips(int visible) {
        actionbarTips.setVisibility(visible);
    }

    /**
     * ????????????????????????
     *
     * @param visible ????????????
     */
    public void hasTitle(int visible) {
        actionbarTitle.setVisibility(visible);
    }

    /**
     * ????????????
     *
     * @param resId ??????StringID
     */
    public void setTitle(int resId) {
        setTitle(getResources().getString(resId));
    }

    /**
     * ????????????
     *
     * @param title ??????
     */
    public void setTitle(String title) {
        actionbarTitle.setText(title);
    }

    /**
     * ??????????????????
     *
     * @param color ?????????
     */
    public void setTitleColor(String color) {
        actionbarTitle.setTextColor(Color.parseColor(color));
    }

    /**
     * ??????????????????
     *
     * @param colorRes ?????????
     */
    public void setTitleColor(int colorRes) {
        actionbarTitle.setTextColor(colorRes);
    }

    /**
     * ????????????????????????
     *
     * @param visible ????????????
     */
    public void hasMenu(int visible) {
        actionbarMenu.setVisibility(visible);
    }

    /**
     * ??????Title??????
     *
     * @param view ???????????????
     */
    public void addMenuItme(View view, LinearLayout.LayoutParams layoutParams) {
        hasMenu(View.VISIBLE);
        actionbarMenu.addView(view, layoutParams);
    }

    /**
     * ??????Title??????
     *
     * @param resId ??????ID
     */
    public ImageView addMenuImageItme(int resId, View.OnClickListener mOnClickListener) {
        ImageView mMenuItme = new ImageView(mActivity);
        mMenuItme.setScaleType(ImageView.ScaleType.CENTER);
        mMenuItme.setImageResource(resId);
        mMenuItme.setOnClickListener(mOnClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_48), LinearLayout.LayoutParams.MATCH_PARENT);
        // params.leftMargin = (int) getResources().getDimension(R.dimen.dp_4);
        addMenuItme(mMenuItme, params);
        return mMenuItme;
    }

    /**
     * ??????Title??????
     *
     * @param textId ??????ID
     */
    public TextView addMenuTextItme(int textId, View.OnClickListener mOnClickListener) {
        return addMenuTextItme(textId, R.color.colorTheme, mOnClickListener);
    }

    /**
     * ??????Title??????
     *
     * @param textId       ??????ID
     * @param textColorRes ??????????????????id
     */
    public TextView addMenuTextItme(int textId, int textColorRes, View.OnClickListener mOnClickListener) {
        TextView mTextView = new TextView(mActivity);
        mTextView.setTextAppearance(mActivity, R.style.ToolBarStyle);
        // mTextView.setMaxWidth((int) getResources().getDimension(R.dimen.dp_144));
        int padding = (int) getResources().getDimension(R.dimen.dp_6);
        mTextView.setPadding(padding, 0, padding, 0);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        mTextView.setTextColor(ContextCompat.getColor(mActivity, textColorRes));
        mTextView.setText(textId);
        mTextView.setOnClickListener(mOnClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addMenuItme(mTextView, params);
        return mTextView;
    }

    /**
     * ?????????????????????????????????
     */
    public boolean isFunctionbar() {
        return functionbar.getVisibility() == View.VISIBLE;
    }

    /**
     * ?????????????????????????????????
     *
     * @param visible ????????????
     */
    public void hasFunctionbar(int visible) {
        functionbar.setVisibility(visible);
    }

    /**
     * ????????????????????????
     *
     * @param resId ??????StringID
     */
    public void setFunctionbarButton(int resId) {
        setFunctionbarButton(getResources().getString(resId));
    }

    /**
     * ????????????????????????
     *
     * @param title ??????
     */
    public void setFunctionbarButton(String title) {
        hasFunctionbar(View.VISIBLE);
        functionbarButton.setText(title);
    }

    /**
     * ????????????????????????????????????
     *
     * @param isClickable ??????????????????
     */
    public void setFunctionbarButtonClickable(boolean isClickable) {
        functionbarButton.setBackgroundResource(isClickable ? R.drawable.common_button_background : R.drawable.common_round_gray);
        functionbarButton.setEnabled(isClickable);
    }

    /**
     * ???????????????????????????
     *
     * @param visibility ?????????
     */
    public void setBottomLineVisibility(int visibility) {
        actionbarBottomLine.setVisibility(visibility);
    }

    @Override
    public void showProgress() {
        hasProgress(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        hasProgress(View.GONE);
    }

}
