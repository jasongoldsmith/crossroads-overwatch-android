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
import co.crossroadsapp.overwatch.data.ConfigData;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by sharmha on 3/9/17.
 */
public class TutorialCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private final ControlManager cManager;
    private final ConfigData backgroundScreenData;
    private View view;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView heroIcon;
        protected ImageView backgrnd;
        protected ImageView bottomBackground;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            heroIcon = (ImageView) v.findViewById(R.id.hero);
            backgrnd = (ImageView) v.findViewById(R.id.top_background);
            bottomBackground = (ImageView) v.findViewById(R.id.bottom_background);
        }
    }

    public TutorialCardAdapter(Activity mainActivity, ControlManager mManager) {
        context = mainActivity;
        cManager = mManager;
        backgroundScreenData = cManager.getmConfigData();
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

        if(backgroundScreenData!=null && backgroundScreenData.getOnBoardingScreens()!=null && backgroundScreenData.getOnBoardingScreens().getRequired()!=null) {
            if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position)!=null) {
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getBackgroundImageUrl()!=null) {
                    Util.picassoLoadImageWithoutMeasurement(context, holder.backgrnd, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getBackgroundImageUrl(), 199);
                }
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getHeroImageUrl()!=null) {
                    Util.picassoLoadImageWithoutMeasurement(context, holder.heroIcon, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getHeroImageUrl(), 199);
                }
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getTextImageUrl()!=null) {
                    Util.picassoLoadImageWithoutMeasurement(context, holder.bottomBackground, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getTextImageUrl(), 199);
                }
            }
        }
//        switch(position) {
//            case 0:
//                //holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_01);
//                //holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.join_btn_color));
//                break;
//            case 1:
//                //holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_02);
//                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.bright_sky_blue));
//                break;
//            case 2:
//                //holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_03);
//                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.green_tut));
//                break;
//            case 3:
//                //holder.heroIcon.setImageResource(R.drawable.img_h_e_r_o_04);
//                holder.backgrnd.setBackgroundColor(context.getResources().getColor(R.color.join_btn_color));
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        if(backgroundScreenData!=null && backgroundScreenData.getOnBoardingScreens()!=null && backgroundScreenData.getOnBoardingScreens().getRequired()!=null) {
            return backgroundScreenData.getOnBoardingScreens().getRequired().size();
        }
        return 0;
    }
}
