package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.PlayerData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharmha on 9/9/16.
 */
public class EventCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ControlManager mManager;
    private View view;
    protected ArrayList<EventData> elistLocal;
    protected ArrayList<ActivityData> adList;
    boolean publicEventCard;
    boolean showFullEvent;
    UserData user;
    Activity mContext;

    public EventCardAdapter(ArrayList<EventData> currentEventList, ArrayList<ActivityData> currentAdList, Activity act, ControlManager manager, int feed) {
        elistLocal = new ArrayList<EventData>();
        adList = new ArrayList<ActivityData>();
        mManager = manager != null ? manager : null;
        if (mManager != null && mManager.getUserData() != null) {
            user = mManager.getUserData();
        }
        mContext = act != null ? act : null;
        if (feed == Constants.PUBLIC_EVENT_FEED) {
            publicEventCard = true;
            if (mManager != null && mManager.getshowFullEvent()) {
                showFullEvent = true;
            }
        }

        if (currentEventList != null) {
            elistLocal = currentEventList;
        }
        if (currentAdList != null) {
            adList = currentAdList;
        }
        checkFullEventPreview();
    }

    private void checkFullEventPreview() {
        if (elistLocal != null && !elistLocal.isEmpty()) {
            if (publicEventCard && !showFullEvent) {
                for (int i = 0; i < elistLocal.size(); i++) {
                    if (elistLocal.get(i) != null) {
                        if (elistLocal.get(i).getEventStatus() != null && elistLocal.get(i).getEventStatus().equalsIgnoreCase("full")) {
                            elistLocal.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
    }

    protected void addItem(ArrayList<EventData> a, ArrayList<ActivityData> ad) {
        if (a != null) {
            this.elistLocal.addAll(a);
        }
        if (ad != null) {
            this.adList.addAll(ad);
        }
        checkFullEventPreview();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout event_card_mainLayout;
        private CardView event_card;
        protected TextView eventSubType;
        protected TextView eventPlayerNames;
        protected TextView eventaLight;
        protected TextView eventPlayerNameCnt;
        protected ImageView eventIcon;
        protected ImageView playerProfileImage1;
        protected ImageView playerProfileImage2;
        protected ImageView playerProfileImage3;
        protected TextView playerCountImage3;
        protected ImageView joinBtn;
        protected ImageView unjoinBtn;
        protected TextView eventDate;
        protected TextView checkpointText;
        protected TextView tagText;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            event_card_mainLayout = (RelativeLayout) v.findViewById(R.id.fragment_event_mainlayout);
            event_card = (CardView) v.findViewById(R.id.event);
            eventSubType = (TextView) v.findViewById(R.id.activity_name);
            eventPlayerNames = (TextView) v.findViewById(R.id.activity_player_name);
            eventaLight = (TextView) v.findViewById(R.id.activity_aLight);
            eventIcon = (ImageView) v.findViewById(R.id.event_icon);
            playerProfileImage1 = (ImageView) v.findViewById(R.id.player_picture_1);
            playerProfileImage2 = (ImageView) v.findViewById(R.id.player_picture_2);
            playerProfileImage3 = (ImageView) v.findViewById(R.id.player_picture_3);
            playerCountImage3 = (TextView) v.findViewById(R.id.player_picture_text_3);
            joinBtn = (ImageView) v.findViewById(R.id.join_btn);
            unjoinBtn = (ImageView) v.findViewById(R.id.unjoin_btn);
            eventPlayerNameCnt = (TextView) v.findViewById(R.id.activity_player_name_lf);
            eventDate = (TextView) v.findViewById(R.id.event_time);
            checkpointText = (TextView) v.findViewById(R.id.checkoint_text);
            tagText = (TextView) v.findViewById(R.id.tag_text);
        }
    }

    //Viewholder for ad cards
    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        private RelativeLayout event_adcard_mainLayout;
        private ImageView adCardImg;
        private CardView event_adcard;
        private CardView addBtn;
        protected TextView eventadHeader;
        protected TextView eventAdSubheader;
        protected ImageView eventAdIcon;

        public MyViewHolder2(View v) {
            super(v);
            view = v;
            adCardImg = (ImageView) v.findViewById(R.id.adcard_img);
            event_adcard_mainLayout = (RelativeLayout) v.findViewById(R.id.fragment_adevent_mainlayout);
            event_adcard = (CardView) v.findViewById(R.id.ad_event);
            eventadHeader = (TextView) v.findViewById(R.id.ad_header);
            eventAdSubheader = (TextView) v.findViewById(R.id.ad_subheader);
            eventAdIcon = (ImageView) v.findViewById(R.id.adevent_icon);
            addBtn = (CardView) v.findViewById(R.id.adcard_addbtn);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position == 0 && elistLocal.size() == 0) {
            return 2;
        } else if (position < elistLocal.size()) {
            return 0;
        } else {
            return 2;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh = null;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_event, null);
                return new MyViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_adevent, null);
                return new MyViewHolder2(view);
        }
        //return new MyViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {

            case 0:
                final MyViewHolder holder = (MyViewHolder) viewHolder;
                if (this.elistLocal.size() > 0) {
                    boolean CreatorIn = false;
                    boolean CreatorIsPlayer = false;
                    String allNames = "";
                    String allNamesRem = "";
                    String checkpoint = "";
                    String tag = "";
                    String clanT = "";
                    final EventData currEvent = elistLocal.get(position);
                    final String eId = this.elistLocal.get(position).getEventId();
                    String aType = this.elistLocal.get(position).getActivityData().getActivityType();
                    String aSubtype = this.elistLocal.get(position).getActivityData().getActivitySubtype();
                    int l = this.elistLocal.get(position).getActivityData().getActivityLight();
                    String url = this.elistLocal.get(position).getActivityData().getActivityIconUrl();
                    int reqPlayer = this.elistLocal.get(position).getActivityData().getMaxPlayer() - this.elistLocal.get(position).getPlayerData().size();
                    // get players
                    int i = this.elistLocal.get(position).getPlayerData().size();
                    int level = this.elistLocal.get(position).getActivityData().getActivityLevel();
                    String creatorId = this.elistLocal.get(position).getCreatorData().getPlayerId();
                    final String status = this.elistLocal.get(position).getEventStatus();
                    if (!publicEventCard) {
                        tag = this.elistLocal.get(position).getActivityData().getTag();
                    }
                    checkpoint = this.elistLocal.get(position).getActivityData().getActivityCheckpoint();
                    holder.checkpointText.setVisibility(View.GONE);
                    holder.eventDate.setVisibility(View.GONE);
                    holder.tagText.setVisibility(View.GONE);
                    if (tag != null && !tag.isEmpty()) {
                        setCardViewLayoutParams(holder.event_card_mainLayout, 172);
                        holder.tagText.setVisibility(View.VISIBLE);
                        holder.tagText.setText(tag);
                        Util.roundCorner(holder.tagText, mContext);
                    } else {
                        setCardViewLayoutParams(holder.event_card_mainLayout, 137);
                    }
                    if (creatorId != null) {
                        if (user != null && user.getUserId() != null) {
                            if (creatorId.equalsIgnoreCase(user.getUserId())) {
                                CreatorIn = true;
                                CreatorIsPlayer = true;
                            }
                        }
                    }
                    if (elistLocal.get(position).getLaunchEventStatus().equalsIgnoreCase(Constants.LAUNCH_STATUS_UPCOMING) || (checkpoint != null)) {
                        if (elistLocal.get(position).getLaunchEventStatus().equalsIgnoreCase(Constants.LAUNCH_STATUS_UPCOMING)) {
                            String date = Util.convertUTCtoReadable(elistLocal.get(position).getLaunchDate());
                            if (date != null) {
                                holder.eventDate.setVisibility(View.VISIBLE);
                                holder.eventDate.setText(date);
                            }
                            if (checkpoint != null && checkpoint.length() > 0 && (!checkpoint.equalsIgnoreCase("null"))) {
                                holder.checkpointText.setVisibility(View.VISIBLE);
                                holder.checkpointText.setText(aSubtype);
                                if (tag != null && !tag.isEmpty()) {
                                    setCardViewLayoutParams(holder.event_card_mainLayout, 212);
                                    holder.tagText.setVisibility(View.VISIBLE);
                                    holder.tagText.setText(tag);
                                    Util.roundCorner(holder.tagText, mContext);
                                } else {
                                    setCardViewLayoutParams(holder.event_card_mainLayout, 177);
                                }
                            } else {
                                holder.checkpointText.setVisibility(View.GONE);
                                if (tag != null && !tag.isEmpty()) {
                                    setCardViewLayoutParams(holder.event_card_mainLayout, 190);
                                    holder.tagText.setVisibility(View.VISIBLE);
                                    holder.tagText.setText(tag);
                                    Util.roundCorner(holder.tagText, mContext);
                                } else {
                                    setCardViewLayoutParams(holder.event_card_mainLayout, 155);
                                }
                            }
                        } else if (checkpoint != null && checkpoint.length() > 0 && (!checkpoint.equalsIgnoreCase("null"))) {
                            holder.checkpointText.setVisibility(View.VISIBLE);
                            holder.checkpointText.setText(aSubtype);
                            if (tag != null && !tag.isEmpty()) {
                                setCardViewLayoutParams(holder.event_card_mainLayout, 190);
                                holder.tagText.setVisibility(View.VISIBLE);
                                holder.tagText.setText(tag);
                                Util.roundCorner(holder.tagText, mContext);
                            } else {
                                setCardViewLayoutParams(holder.event_card_mainLayout, 155);
                            }
                        }
                    }

                    for (int y = 0; y < i; y++) {
                        boolean thisIsUnverifiedUser = false;
                        String n = this.elistLocal.get(position).getPlayerData().get(y).getPsnId();
                        String profileUrl = this.elistLocal.get(position).getPlayerData().get(y).getPlayerImageUrl();
                        String pId = this.elistLocal.get(position).getPlayerData().get(y).getPlayerId();
                        if (user != null && user.getUserId() != null) {
                            if (user.getUserId().equalsIgnoreCase(pId)) {
                                CreatorIn = true;
                                if (user.getPsnVerify() != null && !user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
                                    thisIsUnverifiedUser = true;
                                }
                            }
                        }

                        if (y < 4) {
                            uploadPlayerImg(holder, profileUrl, y, i);
                        }
                    }

                    allNames = this.elistLocal.get(position).getCreatorData().getPsnId();

//                    if(CreatorIsPlayer) {
//                        if(user!=null && user.getPsnVerify()!=null && user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                            clanT = this.elistLocal.get(position).getCreatorData().getClanTag()!=null?this.elistLocal.get(position).getCreatorData().getClanTag():"";
//                        }
//                    } else {
//                        clanT = this.elistLocal.get(position).getCreatorData().getClanTag()!=null?this.elistLocal.get(position).getCreatorData().getClanTag():"";
//                    }

                    if (this.elistLocal.get(position).getCreatorData().getClanTag() != null) {
                        clanT = this.elistLocal.get(position).getCreatorData().getClanTag();
                        if (clanT != null && !clanT.isEmpty()) {
                            allNames = allNames + " [" + clanT + "]";
                        }
                    }
                    if (!status.equalsIgnoreCase("full")) {
                        allNamesRem = " " + "LF" + reqPlayer + "M";
                    }

                    if (!publicEventCard) {
                        if (status != null && !status.equalsIgnoreCase("")) {
                            if (!status.equalsIgnoreCase(Constants.STATUS_FULL) && !CreatorIn) {
                                holder.joinBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RequestParams rp = new RequestParams();
                                        rp.put("eId", eId);
                                        rp.put("player", user.getUserId());
                                        if (mContext != null) {
                                            ((ListActivityFragment) mContext).hideProgress();
                                            ((ListActivityFragment) mContext).showProgress();
                                            mManager.postJoinEvent(mContext, rp);
                                            holder.joinBtn.setClickable(false);
                                        }
                                    }
                                });
                            } else if (CreatorIn) {
                                holder.unjoinBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RequestParams rp = new RequestParams();
                                        rp.put("eId", eId);
                                        rp.put("player", user.getUserId());
                                        if (mContext != null) {
                                            ((ListActivityFragment) mContext).hideProgress();
                                            ((ListActivityFragment) mContext).showProgress();
                                            mManager.postUnJoinEvent((ListActivityFragment) mContext, rp);
                                            holder.unjoinBtn.setClickable(false);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    final boolean showJoin;
                    if (!status.equalsIgnoreCase(Constants.STATUS_FULL) && !CreatorIn) {
                        showJoin = true;
                    } else {
                        showJoin = false;
                    }

                    if (aType != null) {
                        holder.eventSubType.setText(aType);
                    }

                    if (!publicEventCard) {
                        holder.event_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currEvent != null) {
                                    if (currEvent.getEventId() != null) {
                                        ((ListActivityFragment) mContext).hideProgressBar();
                                        ((ListActivityFragment) mContext).showProgressBar();
                                        ((ListActivityFragment) mContext).getEventById(currEvent.getEventId());
                                    }
//                                    CurrentEventDataHolder ins = CurrentEventDataHolder.getInstance();
//                                    ins.setData(currEvent);
//                                    if (showJoin) {
//                                        ins.setJoinVisible(true);
//                                    } else {
//                                        ins.setJoinVisible(false);
//                                    }
                                    //setCurrEventData(currEvent);
                                }
//                                if (mContext != null) {
//                                    //start new activity for event
//                                    Intent regIntent = new Intent(mContext,
//                                            EventDetailActivity.class);
//                                    if (regIntent != null) {
//                                        //regIntent.putExtra("userdata", user);
//                                        mContext.startActivity(regIntent);
//                                        //mContext.finish();
//                                    }
//                                }
                            }
                        });
                    }

                    String feed = this.elistLocal.get(position).getActivityData().getaFeedMode() != null ? this.elistLocal.get(position).getActivityData().getaFeedMode() : "";
                    holder.eventaLight.setText(feed.toUpperCase());
//                        if (l > 0) {
//                            // unicode to show star
//                            String st = "\u2726";
//                            holder.eventaLight.setText(st + String.valueOf(l));
//                        } else if (level > 0) {
//                            holder.eventaLight.setText("lvl " + String.valueOf(level));
//                        }
                    if (publicEventCard) {
                        if (status.equalsIgnoreCase("full")) {
                            holder.unjoinBtn.setVisibility(View.GONE);
                            holder.joinBtn.setImageResource(R.drawable.btn_f_u_l_l);
                        } else {
                            holder.unjoinBtn.setVisibility(View.GONE);
                            holder.joinBtn.setImageResource(R.drawable.btn_j_o_i_n_public);
                        }

                        setCardViewLayoutParams(holder.event_card_mainLayout, 165);
                    } else {
                        updateJoinButton(holder, status, CreatorIn, CreatorIsPlayer, ifUserIsInvited(currEvent.getPlayerData()));
                    }

                    holder.eventPlayerNames.setText(allNames);
                    holder.eventPlayerNameCnt.setText(allNamesRem);
                    holder.eventaLight.invalidate();
                    holder.event_card_mainLayout.invalidate();
                    holder.checkpointText.invalidate();
                    holder.eventDate.invalidate();
                    Util.picassoLoadImageWithoutMeasurement(mContext, holder.eventIcon, url, R.mipmap.icon_notification);
                    //Util.picassoLoadIcon(mContext, holder.eventIcon, url, R.dimen.activity_icon_hgt, R.dimen.activity_icon_width, R.mipmap.icon_notification);
                }
                break;
            case 2:
                MyViewHolder2 adHolder = (MyViewHolder2) viewHolder;
                adHolder.eventadHeader.setText(adList.get(position - elistLocal.size()).getAdCardData().getAdCardHeader());
                adHolder.eventAdSubheader.setText(adList.get(position - elistLocal.size()).getAdCardData().getAdCardSubHeader());
                String cardBackgroundImageUrl = adList.get(position - elistLocal.size()).getAdCardData().getAdCardBaseUrl() + adList.get(position - elistLocal.size()).getAdCardData().getAdCardImagePath();
                String iconImageUrl = adList.get(position - elistLocal.size()).getActivityIconUrl();
                Util.picassoLoadIcon(mContext, adHolder.eventAdIcon, iconImageUrl, R.dimen.activity_icon_hgt, R.dimen.activity_icon_width, R.mipmap.icon_notification);
                //Util.picassoLoadIcon(mContext, adHolder.adCardImg, cardBackgroundImageUrl, R.dimen.ad_hgt, R.dimen.ad_width, R.drawable.img_adcard_raid_golgoroth);
                if (mContext != null) {
                    Picasso.with(mContext)
                            .load(cardBackgroundImageUrl)
                            .placeholder(null)
                            .fit().centerCrop()
                            .into(adHolder.adCardImg);
                }

                adHolder.event_adcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext != null && mContext instanceof ListActivityFragment) {
                            ((ListActivityFragment) mContext).showProgressBar();
                            ((ListActivityFragment) mContext).setAdCardPosition(adList.get(position - elistLocal.size()).getId());
                            RequestParams rp = new RequestParams();
                            rp.add("aType", adList.get(position - elistLocal.size()).getActivityType());
                            rp.add("includeTags", "true");
                            mManager.postGetActivityList(mContext, rp);

                            //tracking adcard click
                            Map<String, String> json = new HashMap<String, String>();
                            if (adList.get(position - elistLocal.size()).getId() != null && !adList.get(position - elistLocal.size()).getId().isEmpty()) {
                                json.put("activityId", adList.get(position - elistLocal.size()).getId().toString());
                                Util.postTracking(json, mContext, mManager, Constants.APP_ADCARD);
                            }
                            //mManager.postCreateEvent(adList.get(position-elistLocal.size()).getId(), user.getUserId(), adList.get(position-elistLocal.size()).getMinPlayer(), adList.get(position-elistLocal.size()).getMaxPlayer(), null, mContext);
                        }
                    }
                });
                break;
        }
    }

    private boolean ifUserIsInvited(ArrayList<PlayerData> playerData) {
        if (user != null && user.getUserId() != null) {
            if (playerData != null) {
                for (int i = 0; i < playerData.size(); i++) {
                    if (playerData.get(i) != null && playerData.get(i).getUserId().equalsIgnoreCase(user.getUserId())) {
                        if (playerData.get(i).getIsInvited()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void setCardViewLayoutParams(RelativeLayout event_card_mainLayout, int i) {
        int pix = Util.dpToPx(i, mContext);
        if (pix > 0) {
            event_card_mainLayout.getLayoutParams().height = pix;
            event_card_mainLayout.requestLayout();
        }
    }

    private void updateJoinButton(MyViewHolder holder, String status, boolean creatorIn, boolean creatorIsPlayer, boolean playerIsInvited) {
        switch (status) {
            case "new":
                if (creatorIsPlayer) {
                    holder.unjoinBtn.setVisibility(View.VISIBLE);
                    holder.joinBtn.setImageResource(R.drawable.btn_o_w_n_e_r);
                } else if (creatorIn) {
                    if (playerIsInvited) {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_i_n_v_i_t_e_d);
                    } else {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_g_o_i_n_g);
                    }
                } else {
                    holder.unjoinBtn.setVisibility(View.GONE);
                    holder.joinBtn.setImageResource(R.drawable.btn_j_o_i_n);
                }
                break;
            case "can_join":
                if (creatorIsPlayer) {
                    holder.unjoinBtn.setVisibility(View.VISIBLE);
                    holder.joinBtn.setImageResource(R.drawable.btn_o_w_n_e_r);
                } else if (creatorIn) {
                    if (playerIsInvited) {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_i_n_v_i_t_e_d);
                    } else {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_g_o_i_n_g);
                    }
                } else {
                    holder.unjoinBtn.setVisibility(View.GONE);
                    holder.joinBtn.setImageResource(R.drawable.btn_j_o_i_n);
                }
                break;
            case "full":
                if (creatorIsPlayer) {
                    holder.unjoinBtn.setVisibility(View.VISIBLE);
                    holder.joinBtn.setImageResource(R.drawable.btn_o_w_n_e_r);
                } else if (creatorIn) {
                    if (playerIsInvited) {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_i_n_v_i_t_e_d);
                    } else {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_r_e_a_d_y);
                    }
                } else {
                    holder.unjoinBtn.setVisibility(View.GONE);
                    holder.joinBtn.setImageResource(R.drawable.btn_f_u_l_l);
                }
                break;
            case "open":
                if (creatorIsPlayer) {
                    holder.unjoinBtn.setVisibility(View.VISIBLE);
                    holder.joinBtn.setImageResource(R.drawable.btn_o_w_n_e_r);
                } else if (creatorIn) {
                    if (playerIsInvited) {
                        holder.unjoinBtn.setVisibility(View.VISIBLE);
                        holder.joinBtn.setImageResource(R.drawable.btn_i_n_v_i_t_e_d);
                    } else {
                        if (playerIsInvited) {
                            holder.unjoinBtn.setVisibility(View.VISIBLE);
                            holder.joinBtn.setImageResource(R.drawable.btn_i_n_v_i_t_e_d);
                        } else {
                            holder.unjoinBtn.setVisibility(View.VISIBLE);
                            holder.joinBtn.setImageResource(R.drawable.btn_g_o_i_n_g);
                        }
                    }
                } else {
                    holder.unjoinBtn.setVisibility(View.GONE);
                    holder.joinBtn.setImageResource(R.drawable.btn_j_o_i_n);
                }
                break;
        }
    }

    private void uploadPlayerImg(MyViewHolder holder, String profileUrl, int player, int playersCount) {
        switch (player) {
            case 0:
                holder.playerProfileImage3.setVisibility(View.GONE);
                holder.playerProfileImage2.setVisibility(View.GONE);
                holder.playerCountImage3.setVisibility(View.GONE);
                Util.picassoLoadIcon(mContext, holder.playerProfileImage1, profileUrl, R.dimen.player_icon_hgt, R.dimen.player_icon_width, R.drawable.default_profile);
                break;
            case 1:
                holder.playerProfileImage2.setVisibility(View.VISIBLE);
                holder.playerCountImage3.setVisibility(View.GONE);
                Util.picassoLoadIcon(mContext, holder.playerProfileImage2, profileUrl, R.dimen.player_icon_hgt, R.dimen.player_icon_width, R.drawable.default_profile);
                break;
            default:
                holder.playerProfileImage3.setVisibility(View.VISIBLE);
                if (player == 2 && playersCount < 4) {
                    holder.playerCountImage3.setVisibility(View.GONE);
                    Util.picassoLoadIcon(mContext, holder.playerProfileImage3, profileUrl, R.dimen.player_icon_hgt, R.dimen.player_icon_width, R.drawable.default_profile);
                } else {
                    holder.playerProfileImage3.setImageResource(R.drawable.img_avatar_empty);
                    holder.playerCountImage3.setVisibility(View.VISIBLE);
                    int p = playersCount - 2;
                    holder.playerCountImage3.setText("+" + p);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (elistLocal != null && adList != null) {
            return this.elistLocal.size() + this.adList.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
