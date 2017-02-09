package co.crossroadsapp.overwatch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;

import java.util.ArrayList;

public class BlankFragment extends Fragment {

    private View view;
    private UserData user;
    private ControlManager mManager;
    private ListActivityFragment mContext;
    private ArrayList<EventData> currentEventList;
    private ArrayList<EventData> fragmentCurrentEventList;
    private ArrayList<EventData> fragmentupcomingEventList;
    EventCardAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<ActivityData> currentAdList;

    public BlankFragment() {
    }

    @SuppressLint("ValidFragment")
    public BlankFragment(ListActivityFragment act, ArrayList<EventData> eData, ArrayList<ActivityData> adData) {
        // Required empty public constructor
        mContext = act;
        currentEventList = new ArrayList<EventData>();
        currentAdList = new ArrayList<ActivityData>();
        currentEventList = eData;
        if(adData!=null) {
            currentAdList = adData;
        } else {
            //adData.clear();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = ControlManager.getmInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.event_view);
        rv.setHasFixedSize(true);

        adapter = new EventCardAdapter(currentEventList, currentAdList, mContext, mManager, Constants.EVENT_FEED);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    void refreshItems() {
        //Util.updateCurrEventOnTime(currentEventList.get(0).getLaunchDate());
        // Load items
        // ...
        mManager.getEventList(mContext);

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);

        // Load complete
//        onItemsLoadComplete();
    }

    public boolean checkCurrentList() {
        if (currentEventList.size()==0){
            return true;
        }
        return false;
    }

    public void updateCurrListAdapter(ArrayList<EventData> eData, ArrayList<ActivityData> adActivityData){
        currentEventList = eData;
        currentAdList = new ArrayList<ActivityData>();
        if (adActivityData!=null) {
            currentAdList = adActivityData;
        }
        if (adapter!=null){
            adapter.elistLocal.clear();
            adapter.adList.clear();
            adapter.addItem(currentEventList, currentAdList);
            adapter.notifyDataSetChanged();
        }
    }

}