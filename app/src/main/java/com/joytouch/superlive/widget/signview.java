package com.joytouch.superlive.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 5/26 0026.
 */
public class signview extends FrameLayout {


    private  SharedPreferences sp;
    private Context context;
    private ImageView iv;
    private ImageView iv_head;
    private boolean isaign=true;

    public signview(Context context) {
        super(context);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initUI();
    }

    public signview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initUI();

    }
    View view;
    private void initUI() {
//将单个写法布局文件家在城view对象

        view = View.inflate(context, R.layout.ou_sign_item, this);
        iv=(ImageView)view.findViewById(R.id.iv);
        iv_head=(ImageView)view.findViewById(R.id.iv_head);
    }

    /**
     * 设置头像(需要修改)
     */
    public void setHeadPhoto(String img_url){
        ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+sp.getString(Preference.headPhoto,""), iv_head, ImageLoaderOption.optionsHeaderno);
    }

    int[] bac = new int[]{R.drawable.first_sign_0,R.drawable.two_sign_0,R.drawable.three_sign_0
            ,R.drawable.four_sign_0,R.drawable.five_sign_0,R.drawable.six_sign_0,R.drawable.baoxiang};

    int[] no_sign_bac = new int[]{R.drawable.first_sign_0,R.drawable.no_sign_1,R.drawable.no_sign_2,
            R.drawable.no_sign_3,R.drawable.no_sign_4,R.drawable.no_sign_5,R.drawable.baoxiang};
    /**
     * 设置背景图片
     */
    public void setBac(int id,boolean isaign){
        switch (id){
            case 0:
                    iv.setBackgroundResource(bac[0]);
                break;
            case 1:
                if (isaign){
                    iv.setBackgroundResource(bac[1]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[1]);
                }
                break;
            case 2:
                if (isaign){
                    iv.setBackgroundResource(bac[2]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[2]);
                }
                break;
            case 3:
                if (isaign){
                    iv.setBackgroundResource(bac[3]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[3]);
                }
                break;
            case 4:
                if (isaign){
                    iv.setBackgroundResource(bac[4]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[4]);
                }
                break;
            case 5:
                if (isaign){
                    iv.setBackgroundResource(bac[5]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[5]);
                }
                break;
            case 6:
                if (isaign){
                    iv.setBackgroundResource(bac[6]);
                }else{
                    iv.setBackgroundResource(no_sign_bac[6]);
                }
                break;
            case 7:
                    iv.setBackgroundResource(bac[7]);
                break;
        }
    }

}
