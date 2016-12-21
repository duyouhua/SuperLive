package com.joytouch.superlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.MyGuessAdapter;
import com.joytouch.superlive.javabean.Guess;
import com.joytouch.superlive.javabean.MyGuess;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的竞猜列表
 */
public class MyGuessActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,ExpandableListView.OnChildClickListener{
    private ImageView iv_finish;
    private ImageView iv_right;
    private TextView tv_title;
    private PullToRefreshLayout refresh;
    private PullableExpandableListView elv_guess;
    private List<MyGuess> myGuesses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_guess);
        initView();
    }

    private void initView() {
        getDate();
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("竞猜记录");
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.drawable.money_white);
        iv_right.setOnClickListener(this);
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        elv_guess= (PullableExpandableListView) this.findViewById(R.id.elv_guess);
        elv_guess.setAdapter(new MyGuessAdapter(myGuesses, this));
        for (int i=0;i<myGuesses.size();i++){
            elv_guess.expandGroup(i);
        }
        elv_guess.setOnChildClickListener(this);
    }

    private void getDate() {
        myGuesses = new ArrayList<>();
        for (int i = 0;i<10;i++){
            MyGuess myGuess = new MyGuess();
            ArrayList<Guess> guesses = new ArrayList<>();
            for (int j=0;j<2;j++){
             guesses.add(new Guess());
            }
            myGuess.setGuesses(guesses);
            myGuesses.add(myGuess);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_right:
                toActivity(ChargeActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        toActivity(GuessDetailsActivity.class);
        return false;
    }
}
