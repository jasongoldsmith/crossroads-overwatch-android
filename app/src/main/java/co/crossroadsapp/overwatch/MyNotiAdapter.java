package co.crossroadsapp.overwatch;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sharmha on 7/25/16.
 */
public class MyNotiAdapter extends RecyclerView.Adapter<MyNotiAdapter.MyViewHolder>{
    private ArrayList<String> elistNoti;
    private View view;

    public MyNotiAdapter(ArrayList<String> list) {
        elistNoti = list;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout event_card_mainLayout;
        private CardView noti_card;
        protected TextView noti_text;
        protected TextView noti_text1;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            event_card_mainLayout = (RelativeLayout) v.findViewById(R.id.notification_bar_base);
            noti_card = (CardView) v.findViewById(R.id.base_test_card);
            noti_text = (TextView) v.findViewById(R.id.noti_toptext);
            noti_text1 = (TextView) v.findViewById(R.id.noti_text);
        }
    }

    @Override
    public MyNotiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_notification_card, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyNotiAdapter.MyViewHolder holder, int position) {
        holder.noti_text.setText(elistNoti.get(position));
    }

    @Override
    public int getItemCount() {
        return this.elistNoti.size();
    }
}
