package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.crossroadsapp.overwatch.data.ConfigData;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by sharmha on 3/9/17.
 */
public class TutorialCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private final ControlManager cManager;
    private final ConfigData backgroundScreenData;
    private final int screenWidth;
    private final int screenHgt;
    private final int screenHgtBgd;
    private final int temp;
    private final int screenHgtBtmBgd;
    private final int customizedWidth;
    private View view;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView skip;
        private ImageView heroIcon;
        protected ImageView backgrnd;
        protected ImageView bottomBackground;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            heroIcon = (ImageView) v.findViewById(R.id.hero);
            backgrnd = (ImageView) v.findViewById(R.id.top_background);
            bottomBackground = (ImageView) v.findViewById(R.id.bottom_background);

            skip = (ImageView) v.findViewById(R.id.skip_btn);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof MainActivity) {
                        ((MainActivity)context).performSkip();
                    } else if(context instanceof TutorialActivity) {
                        ((TutorialActivity)context).performSkip();
                    }
                }
            });
        }
    }

    public TutorialCardAdapter(Activity mainActivity, ControlManager mManager) {
        context = mainActivity;
        cManager = mManager;
        backgroundScreenData = cManager.getmConfigData();

        Display display = mainActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = mainActivity.getResources().getDisplayMetrics().density;
        //float dpHeight = outMetrics.heightPixels / density;
        screenWidth = Math.round(outMetrics.widthPixels);
        screenHgt = Util.dpToPx(428, context);
        screenHgtBgd = Util.dpToPx(270, context);
        screenHgtBtmBgd = Util.dpToPx(170, context);
        temp = Util.dpToPx(95, context);
        customizedWidth = screenWidth-temp;
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
        System.out.print("Hardik tutorial cards " + position);
        if(backgroundScreenData!=null && backgroundScreenData.getOnBoardingScreens()!=null && backgroundScreenData.getOnBoardingScreens().getRequired()!=null) {
            if(position == backgroundScreenData.getOnBoardingScreens().getRequired().size()-1) {
                holder.skip.setVisibility(View.VISIBLE);
            } else {
                holder.skip.setVisibility(View.GONE);
            }
        }
        if(backgroundScreenData!=null && backgroundScreenData.getOnBoardingScreens()!=null && backgroundScreenData.getOnBoardingScreens().getRequired()!=null) {
            if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position)!=null) {
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getHeroImageUrl()!=null) {
                    Util.picassoLoadIconPX(context, holder.heroIcon, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getHeroImageUrl(), screenHgt, screenWidth);
                    //Util.picassoLoadImageWithoutMeasurement(context, holder.heroIcon, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getHeroImageUrl(), 199);
                }
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getBackgroundImageUrl()!=null) {
                    Util.picassoLoadIconPX(context, holder.backgrnd, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getBackgroundImageUrl(), screenHgtBgd, customizedWidth);
                    //Util.picassoLoadImageWithoutMeasurement(context, holder.backgrnd, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getBackgroundImageUrl(), 199);
                }
                if(backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getTextImageUrl()!=null) {
                    Util.picassoLoadIconPX(context, holder.bottomBackground, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getTextImageUrl(), screenHgtBtmBgd, customizedWidth);
                    //Util.picassoLoadImageWithoutMeasurement(context, holder.bottomBackground, backgroundScreenData.getOnBoardingScreens().getRequired().get(position).getTextImageUrl(), 199);
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
