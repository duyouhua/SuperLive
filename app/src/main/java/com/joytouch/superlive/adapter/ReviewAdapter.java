package com.joytouch.superlive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ReviewList;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by lzx on 2016/4/10.
 */
public class ReviewAdapter extends AppBaseAdapter<ReviewList> {
    public ReviewAdapter(List<ReviewList> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_review,null);
            holder = new ViewHolder();
            holder.ll_normal = (LinearLayout) convertView.findViewById(R.id.ll_normal);
            holder.tv_home_name = (TextView) convertView.findViewById(R.id.tv_home_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_visiting_name = (TextView) convertView.findViewById(R.id.tv_visiting_name);
            holder.iv_home_team = (CircleImageView) convertView.findViewById(R.id.iv_home_team);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            holder.iv_visiting_team = (CircleImageView) convertView.findViewById(R.id.iv_visiting_team);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
            holder.rl_news = (RelativeLayout) convertView.findViewById(R.id.rl_news);
            holder.iv_news_icon = (RoundedImageView)convertView.findViewById(R.id.iv_news_icon);
            holder.tv_news_name = (TextView) convertView.findViewById(R.id.tv_news_name);
            holder.ll_game = (LinearLayout) convertView.findViewById(R.id.ll_game);
            holder.tv_game_time = (TextView) convertView.findViewById(R.id.tv_game_time);
            holder.iv_game_icon = (CircleImageView) convertView.findViewById(R.id.iv_game_icon);
            holder.tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
            holder.tv_game_against = (TextView) convertView.findViewById(R.id.tv_game_against);
            holder.tv_game_type = (TextView) convertView.findViewById(R.id.tv_game_type);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (("1").equals(list.get(position).getBase_info().getMatch_mode())){
        //两队对阵以及比分
            holder.ll_normal.setVisibility(View.VISIBLE);
            if (list.get(position).getTeam_info().size()>1){
                holder.tv_home_name.setText(list.get(position).getTeam_info().get(0).getCompetitor_name());
                holder.tv_visiting_name.setText(list.get(position).getTeam_info().get(1).getCompetitor_name());
                Log.e("photourl",Preference.photourl + "100x100/" + list.get(position).getTeam_info().get(0).getCompetitor_image());
                ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + list.get(position).getTeam_info().get(0).getCompetitor_image(), holder.iv_home_team, ImageLoaderOption.optionsteamLogo);
                ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + list.get(position).getTeam_info().get(1).getCompetitor_image(), holder.iv_visiting_team, ImageLoaderOption.optionsteamLogo);
            }else {
                holder.tv_home_name.setText("");
                holder.tv_visiting_name.setText("");
                ImageLoader.getInstance().displayImage("", holder.iv_home_team, ImageLoaderOption.optionsteamLogo);
                ImageLoader.getInstance().displayImage("", holder.iv_visiting_team, ImageLoaderOption.optionsteamLogo);
            }
            holder.tv_time.setText(list.get(position).getBase_info().getTime());
            //比分
            String[] scores = list.get(position).getBase_info().getResult().split(",");
            if (scores.length>1){
                if (scores[0].length()>2||scores[1].length()>2){
                    holder.tv_score.setTextSize(16);
                }else {
                    holder.tv_score.setTextSize(20);
                }
                holder.tv_score.setText(scores[0]+" — "+scores[1]);
            }else {
                holder.tv_score.setText("");
            }
            holder.tv_type.setText(list.get(position).getBase_info().getCat_league());
            if ("0".equals(list.get(position).getBase_info().getReply_flag())){
                holder.iv_play.setVisibility(View.GONE);
            }else {
                holder.iv_play.setVisibility(View.VISIBLE);
            }
        }else {
            holder.ll_normal.setVisibility(View.GONE);
        }
        if (("2").equals(list.get(position).getBase_info().getMatch_mode())){
            holder.ll_game.setVisibility(View.VISIBLE);
            if ("".equals(list.get(position).getBase_info().getMatch_name1())){
                holder.tv_game_name.setVisibility(View.GONE);
            }else {
                holder.tv_game_name.setVisibility(View.VISIBLE);
                holder.tv_game_name.setText(list.get(position).getBase_info().getMatch_name1());
            }
            holder.tv_game_against.setText(list.get(position).getBase_info().getMatch_name2());
            holder.tv_game_time.setText(list.get(position).getBase_info().getTime());
            holder.tv_type.setText(list.get(position).getBase_info().getCat_league());
            ImageLoader.getInstance().displayImage(Preference.photourl+ "100x100/"+list.get(position).getBase_info().getLeague_image(),holder.iv_game_icon,ImageLoaderOption.optionsteamLogo);
        }else {
            holder.ll_game.setVisibility(View.GONE);
        }
        if (("3").equals(list.get(position).getBase_info().getMatch_mode())){
            holder.rl_news.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(Preference.photourl+ "orig/"+list.get(position).getBase_info().getLeague_image(), holder.iv_news_icon,ImageLoaderOption.optionsBaner);
            holder.tv_news_name.setText(list.get(position).getBase_info().getMatch_name1());
            final ViewHolder finalHolder = holder;
            ImageLoader.getInstance().displayImage(Preference.photourl+ "orig/"+list.get(position).getBase_info().getLeague_image(), holder.iv_news_icon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                float w = (float) (finalHolder.iv_news_icon.getWidth() * 1.0 / bitmap.getWidth());
                float h = (float) (finalHolder.iv_news_icon.getHeight() * 1.0 / bitmap.getHeight());
                Matrix matrix = new Matrix();
                matrix.postScale(w, h); //长和宽放大缩小的比例
                Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                finalHolder.iv_news_icon.setImageBitmap(bmp);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        }else {
            holder.rl_news.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        LinearLayout ll_normal;//有对阵情况的布局
        TextView tv_home_name;//主队名
        TextView tv_time;//比赛时间
        TextView tv_visiting_name;//客队名
        CircleImageView iv_home_team;//主队logo
        TextView tv_score;//主队分
        CircleImageView iv_visiting_team;//客队logo
        TextView tv_type;//联赛名
        ImageView iv_play;//播放按钮
        RelativeLayout rl_news;//新闻类布局
        RoundedImageView iv_news_icon;//新闻logo
        TextView tv_news_name;//新闻名称
        LinearLayout ll_game;//没有对阵情况
        TextView tv_game_time;//比赛时间
        CircleImageView iv_game_icon;//赛事logo
        TextView tv_game_name;//赛事名称
        TextView tv_game_against;//对阵双方
        TextView tv_game_type;//赛事类型
    }
}
