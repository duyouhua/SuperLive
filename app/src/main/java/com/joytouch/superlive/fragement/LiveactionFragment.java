package com.joytouch.superlive.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.ReviewTimeAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ReviewTime;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/11.
 * 赛况
 */
public class LiveactionFragment extends BaseFragment {
    private View view;
    private CircleImageView iv_home_team;//主队头像
    private CircleImageView iv_visiting_team;//客队头像
    private TextView tv_home_name;//主队名字
    private TextView tv_visiting_name;//客队名字
    private TextView tv_score;//比分
    private TextView tv_time;//时间
    private ListView listView;//赛况节点
    private WebView webView;//赛况数据
    private List<ReviewTime> times;
    private String match_id;
    private String result;
    private LinearLayout rl;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match_id = getArguments().getString("matchid");
        result=getArguments().getString("result");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_liveaction, null);
        initView();
        return view;
    }

    private void initView() {

        iv_home_team = (CircleImageView) view.findViewById(R.id.iv_home_team);
        iv_visiting_team = (CircleImageView) view.findViewById(R.id.iv_visiting_team);
        tv_home_name = (TextView) view.findViewById(R.id.tv_home_name);
        tv_visiting_name = (TextView) view.findViewById(R.id.tv_visiting_name);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new ReviewTimeAdapter(times,getActivity()));
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        rl = (LinearLayout) view.findViewById(R.id.rl_score);
       getDate();

    }
    public void getDate(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version",Preference.version)
                .add("match_id", match_id)
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.LiveAction, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("赛况::", json);
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray teaminfo = object.optJSONArray("team_info");
                    if (teaminfo != null) {
                        if (teaminfo.length() == 2) {
                            rl.setVisibility(View.VISIBLE);
                        } else {
                            rl.setVisibility(View.GONE);
                        }
                        for (int n = 0; n < teaminfo.length(); n++) {
                            JSONObject object2 = teaminfo.optJSONObject(n);
                            if (n == 0) {
                                tv_home_name.setText(object2.optString("competitor_name"));
                                ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + object2.optString("competitor_image"), iv_home_team, ImageLoaderOption.optionsteamLogo);
                            } else {
                                tv_visiting_name.setText(object2.optString("competitor_name"));
                                ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + object2.optString("competitor_image"), iv_visiting_team, ImageLoaderOption.optionsteamLogo);
                            }
                        }

                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    url = object.getString("h5_url");
                    webView.loadUrl(url);
                    if (!TextUtils.isEmpty(result)) {
                        tv_score.setText(result.split(",")[0] + "-" + result.split(",")[1]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setScore(String score,String state){
        Log.e("赛况", score + "__" + state);
        tv_score.setText(score);
        tv_time.setText(state);
        webView.loadUrl(url);

    }
    public void refresh(){
        webView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}
