package co.crossroadsapp.overwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class TutorialActivity extends AppCompatActivity implements Observer{

    private ControlManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mManager = ControlManager.getmInstance();
        launchTutorial();
    }

    private void launchTutorial() {
        setContentView(R.layout.activity_tutorial);

        final RecyclerView horizontal_tutorial_view = (RecyclerView) findViewById(R.id.horizontal_tutorial_recycler_view);

        TextView doneWithTutorial = (TextView) findViewById(R.id.done_tutorial);
        doneWithTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.postTutorialDone(TutorialActivity.this);
            }
        });
        ImageView skip = (ImageView) findViewById(R.id.skip_btn);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.postTutorialDone(TutorialActivity.this);
            }
        });
//        horizontalList = new ArrayList<EventData>();
//        if (mManager.getEventListCurrent() != null) {
//            horizontalList = mManager.getEventListCurrent();
//        }
        TutorialCardAdapter horizontalTutorialAdapter = new TutorialCardAdapter(this, mManager);
        CenterZoomLayoutManager horizontalLayoutManagaerTutorial
                = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_tutorial_view.setLayoutManager(horizontalLayoutManagaerTutorial);
        horizontal_tutorial_view.setAdapter(horizontalTutorialAdapter);

//        final int[] count = {0};
//        //swipe gestures for recyclerview
//        horizontal_tutorial_view.setOnTouchListener(new OnSwipeTouchListener(this){
//            @Override
//            public void onSwipeDown() {
//                Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
//                horizontal_tutorial_view.scrollToPosition(count[0]++);
//            }
//
//            @Override
//            public void onSwipeUp() {
//                Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
//                horizontal_tutorial_view.scrollToPosition(count[0]--);
//            }
//        });
    }

    @Override
    public void update(Observable observable, Object o) {
        finish();
    }
}
