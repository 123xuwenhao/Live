package cn.nodemedia.qlive.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.openqq.protocol.imsdk.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.nodemedia.NodePlayerView;
import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.contract.PlayContract;
import cn.nodemedia.qlive.entity.ChatMsgInfo;
import cn.nodemedia.qlive.entity.Constants;
import cn.nodemedia.qlive.entity.GiftCmdInfo;
import cn.nodemedia.qlive.entity.GiftInfo;
import cn.nodemedia.qlive.entity.ILVCustomCmd;
import cn.nodemedia.qlive.entity.ILVLiveConstants;
import cn.nodemedia.qlive.entity.ILVText;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.utils.GenerateTestUserSig;
import cn.nodemedia.qlive.utils.view.BottomControlView;
import cn.nodemedia.qlive.utils.view.ChatMsgListView;
import cn.nodemedia.qlive.utils.view.ChatView;
import cn.nodemedia.qlive.utils.view.DanmuView;
import cn.nodemedia.qlive.utils.view.GiftFullView;
import cn.nodemedia.qlive.utils.view.GiftRepeatView;
import cn.nodemedia.qlive.utils.view.SizeChangeRelativeLayout;
import cn.nodemedia.qlive.utils.view.TitleView;
import cn.nodemedia.qlive.utils.view.VipEnterView;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetUserInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetWatcherRequest;
import cn.nodemedia.qlive.view.MyRequest.HeartBeatRequest;
import cn.nodemedia.qlive.view.MyRequest.JoinRoomRequst;
import cn.nodemedia.qlive.view.MyRequest.QuitRoomRequest;
import cn.nodemedia.qlive.widget.GiftSelectDialog;
import tyrantgit.widget.HeartLayout;
import xyz.tanwb.airship.utils.Log;
import xyz.tanwb.airship.utils.StatusBarUtils;
import xyz.tanwb.airship.view.BaseActivity;

public class PlayActivity extends BaseActivity<PlayContract.Presenter> implements PlayContract.View {

    private NodePlayerView playSurface;
    private int userId;
    private String userName;
    private String userAvatar;
    private String streamId;

    private static final String TAG = "gift";
    private SizeChangeRelativeLayout mSizeChangeLayout;
    private TitleView titleView;
    private BottomControlView mControlView;
    private ChatView mChatView;
    private ChatMsgListView mChatListView;
    private VipEnterView mVipEnterView;
    private DanmuView mDanmuView;
    private GiftSelectDialog giftSelectDialog;

    private Timer heartTimer = new Timer();
    private Random heartRandom = new Random();
    private HeartLayout heartLayout;
    private GiftRepeatView giftRepeatView;
    private GiftFullView giftFullView;

    private HeartBeatRequest mHeartBeatRequest = null;
    private Timer heartBeatTimer = new Timer();

    private int hostId;

    private String UserSig;
    private int status;
    private static Gson GsonInstance=new Gson();
    private static UserInfo appUserProfile=MyApplication.getApplication().getSelfProfile();

    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getParams();
        StatusBarUtils.setColorToTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        assignViews();
        login();

    }

    private void getParams() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        userName = intent.getStringExtra("userName");
        userAvatar = intent.getStringExtra("userAvatar");
        streamId = intent.getStringExtra("streamId");
        hostId = intent.getIntExtra("hostId",-1);
        UserSig = GenerateTestUserSig.genTestUserSig(userId + "");
        status = V2TIMManager.getInstance().getLoginStatus();
    }

    private void joinRoom() {
        if (hostId < 0 || TextUtils.isEmpty(streamId)) {
            return;
        }

        //????????????
        V2TIMManager.getInstance().joinGroup(streamId, "AVChatroom", new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PlayActivity.this,"???????????????",Toast.LENGTH_SHORT).show();
                quitRoom();
            }

            @Override
            public void onSuccess() {
                //????????????
                joinRoomRequst();
                //???????????????
                assignIMViews();
                //??????????????????
                startHeartAnim();
                //????????????????????????????????????
                sendEnterRoomMsg();
                //?????????????????????
                updateTitleView();
                //???????????????
                startHeartBeat();
                //?????????????????????
                addMsgListener();
            }
        });
    }

    private void joinRoomRequst() {
        JoinRoomRequst requst=new JoinRoomRequst();
        JoinRoomRequst.getWatcherParam param=new JoinRoomRequst.getWatcherParam();
        param.streamId=streamId;
        param.userId=userId+"";
        requst.request(param);
    }

    private void addMsgListener() {
        //?????????????????????
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
                ILVCustomCmd cmd = GsonInstance.fromJson(new String(customData), ILVCustomCmd.class);
                UserInfo userProfile = cmd.getUserProfile();
                String cmdStreamId = cmd.getStreamId();

                //????????????????????????
                if (cmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);

                    String name = userProfile.getUser_name();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getUser_id()+"";
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(content, cmdStreamId, userProfile.getUser_avatar(), name);
                    mDanmuView.addMsgInfo(danmuInfo);
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //??????????????????
                    if (hostId==userProfile.getUser_id()) {
                        //?????????????????????
                        quitRoom();
                    } else {
                        //??????????????????
                        titleView.removeWatcher(userProfile);
                    }
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {

                    titleView.addWatcher(userProfile);
                    mVipEnterView.showVipEnter(userProfile);
                }

            }
        });
    }

    private void startHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //???????????????
                if (mHeartBeatRequest == null) {
                    mHeartBeatRequest = new HeartBeatRequest();
                }
                String userId = MyApplication.getApplication().getSelfProfile().getUser_id()+"";
                HeartBeatRequest.getHeartBeatParam heartBeatParam=new HeartBeatRequest.getHeartBeatParam();
                heartBeatParam.streamId=streamId;
                heartBeatParam.userId=userId;
                mHeartBeatRequest.request(heartBeatParam);
            }
        }, 0, 4000); //4?????? ???????????????10????????????????????????
    }

    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255), heartRandom.nextInt(255), heartRandom.nextInt(255));
    }


    private void updateTitleView() {
//        List<String> list = new ArrayList<String>();
//        list.add(hostId+"");
        GetSingleUserInfoRequest getSingleUserInfoRequest=new GetSingleUserInfoRequest();
        GetSingleUserInfoRequest.getUserParam param=new GetSingleUserInfoRequest.getUserParam();
        param.userId=hostId+"";
        getSingleUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(mContext,"??????title?????????",Toast.LENGTH_SHORT).show();
                Log.e("abc","??????title?????????",null);
                titleView.setHost(null);
            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                Toast.makeText(mContext,"??????title?????????",Toast.LENGTH_SHORT).show();
                Log.e("abc","??????title?????????",null);
                titleView.setHost(userInfo);
            }
        });
        getSingleUserInfoRequest.request(param);

        // ????????????????????????titleView??????
        titleView.addWatcher(appUserProfile);

        //???????????????????????????????????????
        GetWatcherRequest watcherRequest = new GetWatcherRequest();
        GetWatcherRequest.getWatcherParam watcherParam =new GetWatcherRequest.getWatcherParam();
        watcherParam.streamId=streamId;
        watcherRequest.setOnResultListener(new BaseRequest.OnResultListener<Set<String>>() {
            @Override
            public void onFail(int code, String msg) {

            }

            @Override
            public void onSuccess(Set<String> watchers) {
                if (watchers == null) {
                    return;
                }

                List<String> watcherList = new ArrayList<String>();
                watcherList.addAll(watchers);
                GetUserInfoRequest getUserInfoRequest=new GetUserInfoRequest();
                GetUserInfoRequest.getUserParam param1= new GetUserInfoRequest.getUserParam();
                param1.userIdList=watcherList.toString();
                getUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<List<UserInfo>>() {
                    @Override
                    public void onFail(int code, String msg) {

                    }

                    @Override
                    public void onSuccess(List<UserInfo> userInfos) {
                        titleView.addWatchers(userInfos);
                    }
                });
                getUserInfoRequest.request(param1);
            }
        });
        watcherRequest.request(watcherParam);
    }

    private void sendEnterRoomMsg() {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_ENTER);
        customCmd.setStreamId(streamId);
        customCmd.setUserProfile(appUserProfile);
        byte[] customData=GsonInstance.toJson(customCmd).getBytes();
        V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PlayActivity.this,"????????????????????????:"+desc,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
               Toast.makeText(PlayActivity.this,"????????????????????????",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(() -> heartLayout.addHeart(getRandomColor()));
            }
        }, 0, 1000); //1??????

    }






    private void assignIMViews() {

        mSizeChangeLayout = findViewById(R.id.size_change_layout);
        mSizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelativeLayout.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                //????????????
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                //????????????
            }
        });

        titleView = findViewById(R.id.title_view);
        mChatView = findViewById(R.id.chat_view);
        mControlView = findViewById(R.id.control_view);
        mControlView.setIsHost(false);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //?????????????????????????????????????????????
                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                // ????????????????????????????????????
                quitRoom();
            }

            @Override
            public void onGiftClick() {
                //?????????????????????
                if (giftSelectDialog == null) {
                    giftSelectDialog = new GiftSelectDialog(PlayActivity.this);

                    giftSelectDialog.setGiftSendListener(giftSendListener);
                }
                giftSelectDialog.show();
            }

            @Override
            public void onOptionClick(View view) {
                //????????????????????????????????????
            }
        });



        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(final ILVCustomCmd customCmd) {
                //????????????
                customCmd.setStreamId(streamId);
                customCmd.setUserProfile(appUserProfile);
                byte[] customData=GsonInstance.toJson(customCmd).getBytes();
                V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                        if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                            //??????????????????????????????????????????????????????
                            String chatContent = customCmd.getParam();
                            String userId = appUserProfile.getUser_id()+"";
                            String avatar = appUserProfile.getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);
                        } else if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                            String chatContent = customCmd.getParam();
                            String userId = appUserProfile.getUser_id()+"";
                            String avatar = appUserProfile.getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);

                            String name = appUserProfile.getUser_name();
                            if (TextUtils.isEmpty(name)) {
                                name = userId;
                            }
                            ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(chatContent, userId, avatar, name);
                            mDanmuView.addMsgInfo(danmuInfo);
                        }

                    }
                });

            }
        });

        mControlView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);

        mChatListView = findViewById(R.id.chat_list);
        mVipEnterView = findViewById(R.id.vip_enter);
        mDanmuView = findViewById(R.id.danmu_view);

        heartLayout = findViewById(R.id.heart_layout);
        giftRepeatView = findViewById(R.id.gift_repeat_view);
        giftFullView = findViewById(R.id.gift_full_view);
    }

    private GiftSelectDialog.OnGiftSendListener giftSendListener = new GiftSelectDialog.OnGiftSendListener() {
        @Override
        public void onGiftSendClick(final ILVCustomCmd customCmd) {
            customCmd.setStreamId(streamId);
            customCmd.setUserProfile(appUserProfile);
            byte[] customData=GsonInstance.toJson(customCmd).getBytes();
            V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {

                @Override
                public void onError(int code, String desc) {


                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {

                    if (customCmd.getCmd() == Constants.CMD_CHAT_GIFT) {
                        //???????????????????????????
                        GiftCmdInfo giftCmdInfo = new Gson().fromJson(customCmd.getParam(), GiftCmdInfo.class);
                        int giftId = giftCmdInfo.giftId;
                        String repeatId = giftCmdInfo.repeatId;
                        GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                        if (giftInfo == null) {
                            return;
                        }
                        if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                            giftRepeatView.showGift(giftInfo, repeatId, MyApplication.getApplication().getSelfProfile());
                        } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                            //????????????
                            giftFullView.showGift(giftInfo, MyApplication.getApplication().getSelfProfile());
                        }
                    }
                }
            });
        }
    };

    private void quitRoom() {

        V2TIMManager.getInstance().quitGroup(streamId, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PlayActivity.this, "???????????????" + code + ",?????????????????????" + desc, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {

                //??????????????????????????????

                ILVCustomCmd customCmd = new ILVCustomCmd();
                customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
                customCmd.setStreamId(streamId);
                customCmd.setUserProfile(appUserProfile);
                byte[] customData=GsonInstance.toJson(customCmd).getBytes();
                V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {

                    }
                });

            }
        });
        QuitRoomRequest request = new QuitRoomRequest();
        QuitRoomRequest.getRoomParam param = new QuitRoomRequest.getRoomParam();
        param.userId = userId + "";
        param.streamId = streamId;
        request.request(param);

        logout();
    }

    private void logout() {
        finish();
    }


    private void login() {
        if (status == V2TIMManager.V2TIM_STATUS_LOGINED) {
            joinRoom();
        } else if (status == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            V2TIMManager.getInstance().login(userId+"", UserSig, new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    Toast.makeText(getApplication(),"????????????",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplication(),"????????????",Toast.LENGTH_SHORT).show();
                    joinRoom();
                }
            });
        }
    }

    /**
     * ?????????????????????
     */
    private void assignViews() {
        playSurface = getView(R.id.play_surface);
    }

    @Override
    public void initPresenter() {
        mPresenter.initPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        heartTimer.cancel();
        heartBeatTimer.cancel();

    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public NodePlayerView getNodePlayerView() {
        return playSurface;
    }

}
