package co.crossroadsapp.overwatch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import co.crossroadsapp.overwatch.data.GroupData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupDrawerAdapter {

    private final ImageView groupSelectedImage;
    private final TextView groupSelectedName;
    private final TextView groupSelectedMemberCount;
    private final TextView groupSelectedEventCount;
    private final ToggleButton groupSelectedMute;
    //private final CheckBox groupSelectedBtn;
    private ArrayList<GroupData> localGroupList;
    private RecyclerView mRecyclerView;
    private ActivityGroupViewAdapter mAdapter;
    private ControlManager mCntrlMngr;
    private View view;
    private Context c;
    private UserData user;
    private RelativeLayout empty_group_layout;
    private TextView emptyGrpText;
    private TextView gotoBungie;
    private RelativeLayout selectedGrpCard;

    public GroupDrawerAdapter(final ListActivityFragment act) {
        c = act;

        mCntrlMngr = ControlManager.getmInstance();
        mCntrlMngr.getGroupList(act);
        if (mCntrlMngr.getUserData() != null) {
            user = mCntrlMngr.getUserData();
        }

        groupSelectedImage = (ImageView) act.findViewById(R.id.group_selected_image);
        groupSelectedName = (TextView) act.findViewById(R.id.group_selected_name);
        groupSelectedMemberCount = (TextView) act.findViewById(R.id.group_selected_members);
        groupSelectedEventCount = (TextView) act.findViewById(R.id.group_selected_events);
        groupSelectedMute = (ToggleButton) act.findViewById(R.id.mute_toggle);
        selectedGrpCard = (RelativeLayout) act.findViewById(R.id.group_selected_mainlayout);

        empty_group_layout = (RelativeLayout) act.findViewById(R.id.empty_group_layout);
        emptyGrpText = (TextView) act.findViewById(R.id.empty_grp_text);
        gotoBungie = (TextView) act.findViewById(R.id.goto_bungie);

        emptyGrpText.setText(Html.fromHtml((c.getString(R.string.unverified_bungie_text))));
        emptyGrpText.setMovementMethod(LinkMovementMethod.getInstance());
        gotoBungie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bungie.net"));
                c.startActivity(browserIntent);
            }
        });

        localGroupList = new ArrayList<GroupData>();

        getGrpList();

        //checkSignleGrpView();

        setSelectedGroup();

        getSelectedGrp(localGroupList);

        //recyclerview for activity list
        mRecyclerView = (RecyclerView) act.findViewById(R.id.group_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));

        mAdapter = new ActivityGroupViewAdapter(localGroupList);

        mRecyclerView.setAdapter(mAdapter);

        if (empty_group_layout != null) {
            empty_group_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListActivityFragment) c).showUnverifiedUserMsg();
                }
            });
        }
    }

    private void getGrpList() {
        if (mCntrlMngr.getCurrentGroupList() != null) {
            ArrayList<GroupData> ll = mCntrlMngr.getCurrentGroupList();
            for (int i = 0; i < ll.size(); i++) {
                localGroupList.add(ll.get(i));
            }
        }
    }

    private void checkSignleGrpView() {
        if (user != null && user.getPsnVerify() != null && user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
            if (empty_group_layout != null) {
                empty_group_layout.setVisibility(View.GONE);
            }
        } else {
            if (empty_group_layout != null) {
                empty_group_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setMuteButton(final ToggleButton mute, boolean mn, final String id) {
        if (mute != null) {
            mute.setEnabled(true);
            mute.setChecked(!mn);

            mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (id != null) {
                        RequestParams muteParams = new RequestParams();
                        muteParams.add("groupId", id);
                        if (mute.isChecked()) {
                            muteParams.add("muteNotification", "false");
                        } else {
                            muteParams.add("muteNotification", "true");
                        }
                        mCntrlMngr.postMuteNoti((ListActivityFragment) c, muteParams);
                    }
                }
            });
        }
    }

    public void updateSelectedGroup(GroupData selectedGroup) {
        if (selectedGroup != null) {
            String imageUrl = selectedGroup.getGroupImageUrl();
            if (imageUrl != null && imageUrl.length() > 0) {
                Picasso.with(c).load(imageUrl)
                        .resizeDimen(R.dimen.group_icon_hgt, R.dimen.group_icon_width)
                        .centerCrop().placeholder(R.mipmap.img_c_r_o_w_logo)
                        .into(groupSelectedImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                TravellerLog.w(this, "success");
                            }

                            @Override
                            public void onError() {
                                TravellerLog.w(this, "error");
                            }
                        });
                //Util.picassoLoadIcon(c, groupSelectedImage, gd.getGroupImageUrl(), R.dimen.group_icon_hgt, R.dimen.group_icon_width, R.drawable.img_logo_badge);
            }
            int ge_num = selectedGroup.getEventCount();
            int gm_num = selectedGroup.getMemberCount();
            boolean mute = selectedGroup.getMuteNotification();
            groupSelectedEventCount.setText(String.format(c.getResources().getString(R.string.grp_member), ge_num));
            groupSelectedMemberCount.setText(String.format(c.getResources().getQuantityString(R.plurals.grp_event, gm_num), gm_num));
            if (ge_num > 0) {
                groupSelectedEventCount.setTextColor(c.getResources().getColor(R.color.activity_light_color));
            }
            groupSelectedName.setText(selectedGroup.getGroupName());
            if (selectedGroup.getGroupId() != null) {
                setMuteButton(groupSelectedMute, mute, selectedGroup.getGroupId());
                if (selectedGrpCard != null) {
                    if (selectedGroup.getGroupId().equalsIgnoreCase(Constants.FREELANCER_GROUP)) {
                        selectedGrpCard.setBackgroundResource(R.color.freelancer_background);
                    } else {
                        selectedGrpCard.setBackgroundResource(R.color.logout_btn_background);
                    }
                }
            }
        }
    }

    public GroupData setSelectedGroup() {
        if (mCntrlMngr.getUserData() != null) {
            user = mCntrlMngr.getUserData();
            if (user.getClanId() != null) {
                GroupData gd = mCntrlMngr.getGroupObj(user.getClanId());
                if (gd != null) {
                    if (gd.getGroupImageUrl() != null && gd.getGroupImageUrl().length() > 0) {
                        Picasso.with(c).load(gd.getGroupImageUrl())
                                .resizeDimen(R.dimen.group_icon_hgt, R.dimen.group_icon_width)
                                .centerCrop().placeholder(R.mipmap.img_c_r_o_w_logo)
                                .into(groupSelectedImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        TravellerLog.w(this, "success");
                                    }

                                    @Override
                                    public void onError() {
                                        TravellerLog.w(this, "error");
                                    }
                                });
                        //Util.picassoLoadIcon(c, groupSelectedImage, gd.getGroupImageUrl(), R.dimen.group_icon_hgt, R.dimen.group_icon_width, R.drawable.img_logo_badge);
                    }
                    int ge_num = gd.getEventCount();
                    int gm_num = gd.getMemberCount();
                    boolean mute = gd.getMuteNotification();
                    groupSelectedEventCount.setText(String.format(c.getResources().getString(R.string.grp_member), ge_num));
                    groupSelectedMemberCount.setText(String.format(c.getResources().getQuantityString(R.plurals.grp_event, gm_num), gm_num));
                    if (ge_num > 0) {
                        groupSelectedEventCount.setTextColor(c.getResources().getColor(R.color.activity_light_color));
                    }
                    groupSelectedName.setText(gd.getGroupName());
                    if (gd.getGroupId() != null) {
                        setMuteButton(groupSelectedMute, mute, gd.getGroupId());
                        if (selectedGrpCard != null) {
                            if (gd.getGroupId().equalsIgnoreCase(Constants.FREELANCER_GROUP)) {
                                selectedGrpCard.setBackgroundResource(R.color.freelancer_background);
                            } else {
                                selectedGrpCard.setBackgroundResource(R.color.logout_btn_background);
                            }
                        }
                    }
                    return gd;
                }
            }
        }
        return null;
    }

    public void update(Object data) {
        //localGroupList = (ArrayList<GroupData>) data;
        /*ArrayList<GroupData> ll = (ArrayList<GroupData>) data;
        localGroupList = new ArrayList<GroupData>();
        for (int i = 0; i < ll.size(); i++) {
            localGroupList.add(ll.get(i));
        }*/

        GroupData gd = null;
        List<GroupData> tmp = (ArrayList<GroupData>) data;
        localGroupList = new ArrayList<>();
        mCntrlMngr = ControlManager.getmInstance();
        if (tmp != null && mCntrlMngr != null) {
            UserData u = mCntrlMngr.getUserData();
            if (u == null || u.getClanId() == null || u.getClanId().equalsIgnoreCase(Constants.CLAN_NOT_SET)) {
                return;
            }
            String clanId = u.getClanId();
            for (int i = 0; i < tmp.size(); i++) {
                GroupData g = tmp.get(i);
                if (clanId.equalsIgnoreCase(g.getGroupId())) {
                    gd = g;
                } else {
                    localGroupList.add(g);
                }
            }
        }
        updateSelectedGroup(gd);
        if( mAdapter != null )
        {
            mAdapter.addItem(localGroupList);
            mAdapter.notifyDataSetChanged();
        }
        if (localGroupList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }


        /*localGroupList = new ArrayList<>((ArrayList<GroupData>) data);
        GroupData gd = setSelectedGroup();
        getSelectedGrp(localGroupList);
        //checkSignleGrpView();
        if (localGroupList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.addItem(localGroupList);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();*/
    }

    private void getSelectedGrp(ArrayList<GroupData> localGroupList) {
        if (mCntrlMngr != null) {
            if (localGroupList != null && (!localGroupList.isEmpty())) {
                if (mCntrlMngr.getUserData() != null) {
                    UserData u = mCntrlMngr.getUserData();
                    for (int i = 0; i < localGroupList.size(); i++) {
                        if (u.getClanId() != null && (!u.getClanId().equalsIgnoreCase(Constants.CLAN_NOT_SET))) {
                            if (u.getClanId().equalsIgnoreCase(localGroupList.get(i).getGroupId())) {
                                localGroupList.get(i).setGroupSelected(true);
                                localGroupList.remove(i);

                            }
                        }
                    }
                }
            }
        }
    }

    public void updateGrpData(Object data) {
        GroupData gd = (GroupData) data;
        if (mAdapter.glistLocal != null) {
            for (int i = 0; i < mAdapter.glistLocal.size(); i++) {
                if (gd.getGroupId() != null) {
                    if (mAdapter.glistLocal.get(i) != null && mAdapter.glistLocal.get(i).getGroupId() != null) {
                        if (gd.getGroupId().equalsIgnoreCase(mAdapter.glistLocal.get(i).getGroupId())) {
                            mAdapter.glistLocal.get(i).setMuteNotification(gd.getMuteNotification());
                            mAdapter.notifyItemChanged(i);
                        }
                    }
                }
            }
        }
    }

    private class ActivityGroupViewAdapter extends RecyclerView.Adapter<ActivityGroupViewAdapter.MyGroupViewHolder> {
        private ArrayList<GroupData> glistLocal;

        public ActivityGroupViewAdapter(ArrayList<GroupData> currentGroupList) {
            if (currentGroupList != null) {
                glistLocal = new ArrayList<GroupData>(currentGroupList);
            }
        }

        protected void addItem(ArrayList<GroupData> a) {
            glistLocal = new ArrayList<GroupData>(a);
        }

        protected ArrayList<GroupData> getList() {
            return glistLocal;
        }

        public class MyGroupViewHolder extends RecyclerView.ViewHolder {
            protected CardView groupCard;
            protected RelativeLayout groupCardLayout;
            protected ImageView groupImage;
            protected TextView groupName;
            protected TextView groupMemberCount;
            protected TextView groupEventCount;
            protected CheckBox groupBtn;
            protected TextView gotoBungie;
            protected TextView saveGrpBtnReady;
            protected ToggleButton muteBtn;
            protected TextView emptyGrpText;
            protected RelativeLayout group_layout;
            protected RelativeLayout empty_group_layout;
            protected ImageView grp_overlay;

            public MyGroupViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                groupCard = (CardView) view.findViewById(R.id.groups);
                groupCardLayout = (RelativeLayout) view.findViewById(R.id.group_mainlayout);
                groupImage = (ImageView) view.findViewById(R.id.group_image);
                groupName = (TextView) view.findViewById(R.id.group_name);
                groupMemberCount = (TextView) view.findViewById(R.id.group_members);
                groupEventCount = (TextView) view.findViewById(R.id.group_events);
                //groupBtn = (CheckBox) view.findViewById(R.id.group_radio_btn);
                muteBtn = (ToggleButton) view.findViewById(R.id.mute_toggle);
                emptyGrpText = (TextView) view.findViewById(R.id.empty_grp_text);
                //saveGrpBtnReady = (TextView) ((ListActivityFragment)c).findViewById(R.id.save_group_btn_ready);
                gotoBungie = (TextView) view.findViewById(R.id.goto_bungie);
                group_layout = (RelativeLayout) view.findViewById(R.id.group_layout);
                empty_group_layout = (RelativeLayout) view.findViewById(R.id.empty_group_layout);
                grp_overlay = (ImageView) view.findViewById(R.id.grp_overlay);
            }
        }

        @Override
        public ActivityGroupViewAdapter.MyGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh = null;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_cardview, null);
            return new MyGroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyGroupViewHolder holder, final int position) {
            if (glistLocal.size() > 0) {
                GroupData objGroup = glistLocal.get(position);
                if (user != null) {
                    if (user.getClanId() != null) {
                        if ((!user.getClanId().equalsIgnoreCase(Constants.CLAN_NOT_SET))) {
                            if (!objGroup.getGroupId().equalsIgnoreCase(user.getClanId())) {

                                if ((objGroup.getGroupId().equalsIgnoreCase(Constants.FREELANCER_GROUP))) {
                                    holder.groupCardLayout.setBackgroundColor(c.getResources().getColor(R.color.freelancer_background));
                                } else {
                                    holder.groupCardLayout.setBackgroundColor(c.getResources().getColor(R.color.logout_btn_background));
                                }

                                if (holder.empty_group_layout != null) {
                                    holder.empty_group_layout.setVisibility(View.GONE);
                                }
                                if (holder.group_layout != null) {
                                    holder.group_layout.setVisibility(View.VISIBLE);
                                }
                                if (holder.grp_overlay != null) {
                                    holder.grp_overlay.setVisibility(View.GONE);
                                }
                                String gName = null;
                                //String id = null;
                                int count = 0;
                                int eCount = 0;
                                String url = null;
                                boolean mn = false;
                                //boolean clan = true;
                                if (glistLocal.get(position) != null) {
                                    if (objGroup.getGroupName() != null) {
                                        gName = objGroup.getGroupName();
                                    }
                                    mn = objGroup.getMuteNotification();
                                    if (objGroup.getMemberCount() >= 0) {
                                        count = objGroup.getMemberCount();
                                    }
                                    if (objGroup.getEventCount() > 0) {
                                        eCount = objGroup.getEventCount();
                                    }
                                    if (objGroup.getGroupImageUrl() != null) {
                                        url = objGroup.getGroupImageUrl();
                                    }
//                                if (objGroup.getGroupId()!=null) {
//                                    id = objGroup.getGroupId();
//                                }
                                }

                                if (holder.groupName != null && gName != null) {
                                    holder.groupName.setText(gName);
                                }

                                if (holder.groupMemberCount != null) {
                                    //String gm = count + c.getResources().getQuantityString(R.plurals.grp_member, count);
                                    holder.groupMemberCount.setText(String.format(c.getResources().getString(R.string.grp_member), count));
                                }
                                if (holder.groupImage != null) {
                                    Util.picassoLoadIcon(c, holder.groupImage, url, R.dimen.group_icon_hgt, R.dimen.group_icon_width, R.drawable.img_logo_badge_group);
                                }

                                if (holder.groupEventCount != null) {
                                    if (eCount > 0) {
                                        holder.groupEventCount.setTextColor(c.getResources().getColor(R.color.activity_light_color));
                                    } else {
                                        holder.groupEventCount.setTextColor(c.getResources().getColor(R.color.orbit_text_color));
                                    }
                                    holder.groupEventCount.setText(String.format(c.getResources().getQuantityString(R.plurals.grp_event, eCount), eCount));
                                }

                                setMuteButton(holder.muteBtn, mn, glistLocal.get(position).getGroupId());

//                            if (holder.muteBtn!=null) {
//                                holder.muteBtn.setChecked(mn);
//
//                                holder.muteBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                    @Override
//                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                        if (id != null) {
//                                            RequestParams muteParams = new RequestParams();
//                                            muteParams.add("groupId", id);
//                                            if (!isChecked) {
//                                                muteParams.add("muteNotification", "1");
//                                            } else {
//                                                muteParams.add("muteNotification", "0");
//                                            }
//                                            mCntrlMngr.postMuteNoti((ListActivityFragment) c, muteParams);
//                                        }
//                                    }
//                                });
//                            }

                                //in some cases, it will prevent unwanted situations
                                //holder.groupBtn.setOnCheckedChangeListener(null);

                                //if true, your checkbox will be selected, else unselected
                                //holder.groupBtn.setChecked(objGroup.isSelected);
                                holder.groupCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //holder.groupBtn.performClick();
                                        for (int y = 0; y < glistLocal.size(); y++) {
                                            if (y == position) continue;
                                            GroupData objGroup = glistLocal.get(y);
                                            if (objGroup.isSelected) {
                                                objGroup.setGroupSelected(false);
                                                notifyItemChanged(y);
                                                break;
                                            }
                                        }
                                        glistLocal.get(position).setGroupSelected(true);
                                        notifyItemChanged(position);

                                        //change grp icon
                                        ((ListActivityFragment) c).setgrpIcon(glistLocal.get(position).getGroupImageUrl());

                                        //close drawer
                                        ((ListActivityFragment) c).closeAllDrawers();

                                        //save selected grp
                                        RequestParams params = new RequestParams();
                                        if (mCntrlMngr != null) {
                                            if (mCntrlMngr.getUserData() != null) {
                                                UserData user = mCntrlMngr.getUserData();
                                                if (user.getUserId() != null) {
                                                    params.add("id", user.getUserId());
                                                }
                                                params.add("clanId", glistLocal.get(position).getGroupId());
                                                params.add("clanName", glistLocal.get(position).getGroupName());
                                                params.add("clanImage", glistLocal.get(position).getGroupImageUrl());
                                                ((ListActivityFragment) c).hideProgress();
                                                ((ListActivityFragment) c).showProgress();
                                                mCntrlMngr.postSetGroup((ListActivityFragment) c, params);
                                            }
                                        }
                                    }
                                });
                                holder.groupEventCount.invalidate();
                                holder.groupMemberCount.invalidate();
                                holder.muteBtn.invalidate();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (glistLocal != null) {
                    return this.glistLocal.size();
            }
            return 0;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }
}
