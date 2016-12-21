package com.joytouch.superlive.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.utils.FastBlur;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.MyListView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
/**
 * 主播的个人信息页面
 */

public class OtherAnchorActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener,View.OnClickListener{
    private TagFlowLayout tfl;
    private List<String> strs;
    private PullToRefreshLayout refresh;
    private MyListView listView;
    private PullableScrollView scrollView;
//    private List<OtherUserStatus> statuses;
    private CircleImageView iv_icon;
    private TextView tv_bg;
    private ImageView iv_finish;
    private RelativeLayout rl_living;//正在直播
    private TextView tv_attention,tv_fans;//关注和粉丝
    private RelativeLayout rl_attention;//点击关注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_anchor);
        initView();
    }

    private void initView() {
        getDate();
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        rl_living = (RelativeLayout) this.findViewById(R.id.rl_living);
        rl_living.setOnClickListener(this);
        tv_attention = (TextView) this.findViewById(R.id.tv_attention);
        tv_attention.setOnClickListener(this);
        tv_fans = (TextView) this.findViewById(R.id.tv_fans);
        tv_fans.setOnClickListener(this);
        rl_attention = (RelativeLayout) this.findViewById(R.id.rl_attention);
        rl_attention.setOnClickListener(this);
        scrollView = (PullableScrollView) this.findViewById(R.id.scrollView);
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        listView = (MyListView) this.findViewById(R.id.listView);
        iv_icon = (CircleImageView) this.findViewById(R.id.iv_icon);
        tv_bg = (TextView) this.findViewById(R.id.tv_bg);
        refresh.setOnRefreshListener(this);
        refresh.setCanPullDown(false);
//        listView.setAdapter(new OtherUserStatusAdapter(statuses,this));
        scrollView.smoothScrollBy(0, 0);
        tfl = (TagFlowLayout) this.findViewById(R.id.tfl);
        //主播风格
        tfl.setAdapter(new TagAdapter<String>(strs) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(OtherAnchorActivity.this).inflate(R.layout.item_other_auchor_biaoqian, null);
                tv.setText(s);
                return tv;
            }
        });
        ImageLoader.getInstance().displayImage("http://s.cjzhibo.net/Uploads/SuperBid/201603/be85481d345381ba743d35b67fb8d94c.png", iv_icon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                applyBlur(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    private void getDate() {
        strs = new ArrayList<>();
//        statuses = new ArrayList<>();
        strs.add("幽默感");
        strs.add("场控员");
        strs.add("专业");
        strs.add("幽默感");
        strs.add("场控员");
        strs.add("专业");
        for (int i=0;i<5;i++){
//            statuses.add(new OtherUserStatus());
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
    private void applyBlur(final Bitmap bm) {
        if (bm == null){
            return;
        }
        iv_icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_icon.getViewTreeObserver().removeOnPreDrawListener(this);
                iv_icon.buildDrawingCache();
                float w = (float) (tv_bg.getWidth() * 1.0 / bm.getWidth());
                float h = (float) (tv_bg.getHeight() * 1.0 / bm.getHeight());
                Matrix matrix = new Matrix();
                matrix.postScale(w, h); //长和宽放大缩小的比例
                Bitmap bmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                blur(bmp, tv_bg);
                return true;
            }
        });
    }
    //模糊处理方法
    private void blur(Bitmap bkg, View view) {
        float radius = 4;
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setBackground(new BitmapDrawable(this.getResources(), overlay));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_living://正在直播

                break;
            case R.id.tv_attention:
                //关注
                toActivity(AttentionAndFansActivity.class);
                break;
            case R.id.tv_fans:
                //粉丝
                toActivity(AttentionAndFansActivity.class);
                break;
            case R.id.rl_attention:
                //添加关注
                break;
        }
    }
}
