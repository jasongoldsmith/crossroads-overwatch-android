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

import java.util.ArrayList;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.EventData;

/**
 * Created by sharmha on 3/9/17.
 */
public class TutorialCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private View view;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView heroIcon;
        protected ImageView backgrnd;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            heroIcon = (ImageView) v.findViewById(R.id.hero);
            backgrnd = (ImageView) v.findViewById(R.id.top_background);
        }
    }

    public TutorialCardAdapter(MainActivity mainActivity) {
        context = mainActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tutorial_screen, null);
        return new TutorialCardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder holder = (TutorialCardAdapter.MyViewHolder) viewHolder;
        switch(position) {
            case 0:
                holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_01);
                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.join_btn_color));
                break;
            case 1:
                holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_02);
                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.bright_sky_blue));
                break;
            case 2:
                holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_03);
                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.green_tut));
                break;
            case 3:
                holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_04);
                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.join_btn_color));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
