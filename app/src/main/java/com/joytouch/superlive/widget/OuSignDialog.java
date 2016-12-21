package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 5/26 0026.
 */
public class OuSignDialog extends Dialog {
    private  int sign_num;
    private GridView grsign;
    List<Map<String, Object>> list;
    private List<signview> viewlist=new ArrayList<>();
    private List<LinearLayout> linearlist=new ArrayList<>();
    private List<ImageView> imagelist=new ArrayList<>();
    private signview my_view_0;
    private signview my_view_1;
    private signview my_view_2;
    private signview my_view_3,my_view_4,my_view_5,my_view_6,my_view_7,my_view_8,my_view_9,my_view_10,my_view_11,my_view_12,my_view_13,my_view_14,
    my_view_15,my_view_16,my_view_17,my_view_18,my_view_19,my_view_20,my_view_21,my_view_22,my_view_23,my_view_24,my_view_25,my_view_26,my_view_27,my_view_28,my_view_29,my_view_30;

    private LinearLayout img_0,img_1,img_2,img_3,img_4,img_5,img_6,img_7,img_8,img_9,img_10,img_11,img_12,img_13,img_14,img_15,img_16,img_17,img_18,img_19
            ,img_20,img_21,img_22,img_23,img_24,img_25,img_26,img_27,img_28,img_29,img_30;

    private ImageView head_img_0,head_img_1,head_img_2,head_img_3,head_img_4,head_img_5,head_img_6,head_img_7,head_img_8,head_img_9,head_img_10,head_img_11
            ,head_img_12,head_img_13,head_img_14,head_img_15,head_img_16,head_img_17,head_img_18,head_img_19,head_img_20,head_img_21,head_img_22,head_img_23,head_img_24,head_img_25
            ,head_img_26,head_img_27,head_img_28,head_img_29,head_img_30;
    private boolean isOk=false;
    private SharedPreferences sp;

    public OuSignDialog(Context context,int sign_num) {
        super(context, R.style.Dialog_bocop);

        this.sign_num=sign_num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getContext().getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
//        setContentView(R.layout.sign_ou);
        setContentView(R.layout.ssss);
        list = new ArrayList<Map<String,Object>>();
        Map<String, Object> title = new HashMap<String, Object>();
        list.add(title);
        initView();

    }

    private void initView() {
        my_view_0=(signview)findViewById(R.id.my_view_0);
        my_view_1=(signview)findViewById(R.id.my_view_1);
        my_view_2=(signview)findViewById(R.id.my_view_2);
        my_view_3=(signview)findViewById(R.id.my_view_3);
        my_view_4=(signview)findViewById(R.id.my_view_4);
        my_view_5=(signview)findViewById(R.id.my_view_5);
        my_view_6=(signview)findViewById(R.id.my_view_6);
        my_view_7=(signview)findViewById(R.id.my_view_7);
        my_view_8=(signview)findViewById(R.id.my_view_8);
        my_view_9=(signview)findViewById(R.id.my_view_9);
        my_view_10=(signview)findViewById(R.id.my_view_10);
        my_view_11=(signview)findViewById(R.id.my_view_11);
        my_view_12=(signview)findViewById(R.id.my_view_12);
        my_view_13=(signview)findViewById(R.id.my_view_13);
        my_view_14=(signview)findViewById(R.id.my_view_14);
        my_view_15=(signview)findViewById(R.id.my_view_15);
        my_view_16=(signview)findViewById(R.id.my_view_16);
        my_view_17=(signview)findViewById(R.id.my_view_17);
        my_view_18=(signview)findViewById(R.id.my_view_18);
        my_view_19=(signview)findViewById(R.id.my_view_19);
        my_view_20=(signview)findViewById(R.id.my_view_20);
        my_view_21=(signview)findViewById(R.id.my_view_21);
        my_view_22=(signview)findViewById(R.id.my_view_22);
        my_view_23=(signview)findViewById(R.id.my_view_23);
        my_view_24=(signview)findViewById(R.id.my_view_24);
        my_view_25=(signview)findViewById(R.id.my_view_25);
        my_view_26=(signview)findViewById(R.id.my_view_26);
        my_view_27=(signview)findViewById(R.id.my_view_27);
        my_view_28=(signview)findViewById(R.id.my_view_28);
        my_view_29=(signview)findViewById(R.id.my_view_29);
        my_view_30=(signview)findViewById(R.id.my_view_30);

        head_img_0=(ImageView)findViewById(R.id.head_img_0);
        img_0=(LinearLayout)findViewById(R.id.img_0);
        head_img_1=(ImageView)findViewById(R.id.head_img_1);
        img_1=(LinearLayout)findViewById(R.id.img_1);
        head_img_2=(ImageView)findViewById(R.id.head_img_2);
        img_2=(LinearLayout)findViewById(R.id.img_2);
        head_img_3=(ImageView)findViewById(R.id.head_img_3);
        img_3=(LinearLayout)findViewById(R.id.img_3);
        head_img_4=(ImageView)findViewById(R.id.head_img_4);
        img_4=(LinearLayout)findViewById(R.id.img_4);
        head_img_5=(ImageView)findViewById(R.id.head_img_5);
        img_5=(LinearLayout)findViewById(R.id.img_5);
        head_img_6=(ImageView)findViewById(R.id.head_img_6);
        img_6=(LinearLayout)findViewById(R.id.img_6);
        head_img_7=(ImageView)findViewById(R.id.head_img_7);
        img_7=(LinearLayout)findViewById(R.id.img_7);
        head_img_8=(ImageView)findViewById(R.id.head_img_8);
        img_8=(LinearLayout)findViewById(R.id.img_8);
        head_img_9=(ImageView)findViewById(R.id.head_img_9);
        img_9=(LinearLayout)findViewById(R.id.img_9);
        head_img_10=(ImageView)findViewById(R.id.head_img_10);
        img_10=(LinearLayout)findViewById(R.id.img_10);
        head_img_11=(ImageView)findViewById(R.id.head_img_11);
        img_11=(LinearLayout)findViewById(R.id.img_11);
        head_img_12=(ImageView)findViewById(R.id.head_img_12);
        img_12=(LinearLayout)findViewById(R.id.img_12);
        head_img_13=(ImageView)findViewById(R.id.head_img_13);
        img_13=(LinearLayout)findViewById(R.id.img_13);
        head_img_14=(ImageView)findViewById(R.id.head_img_14);
        img_14=(LinearLayout)findViewById(R.id.img_14);
        head_img_15=(ImageView)findViewById(R.id.head_img_15);
        img_15=(LinearLayout)findViewById(R.id.img_15);
        head_img_16=(ImageView)findViewById(R.id.head_img_16);
        img_16=(LinearLayout)findViewById(R.id.img_16);
        head_img_17=(ImageView)findViewById(R.id.head_img_17);
        img_17=(LinearLayout)findViewById(R.id.img_17);
        head_img_18=(ImageView)findViewById(R.id.head_img_18);
        img_18=(LinearLayout)findViewById(R.id.img_18);
        head_img_19=(ImageView)findViewById(R.id.head_img_19);
        img_19=(LinearLayout)findViewById(R.id.img_19);
        head_img_20=(ImageView)findViewById(R.id.head_img_20);
        img_20=(LinearLayout)findViewById(R.id.img_20);
        head_img_21=(ImageView)findViewById(R.id.head_img_21);
        img_21=(LinearLayout)findViewById(R.id.img_21);
        head_img_22=(ImageView)findViewById(R.id.head_img_22);
        img_22=(LinearLayout)findViewById(R.id.img_22);
        head_img_23=(ImageView)findViewById(R.id.head_img_23);
        img_23=(LinearLayout)findViewById(R.id.img_23);
        head_img_24=(ImageView)findViewById(R.id.head_img_24);
        img_24=(LinearLayout)findViewById(R.id.img_24);
        head_img_25=(ImageView)findViewById(R.id.head_img_25);
        img_25=(LinearLayout)findViewById(R.id.img_25);
        head_img_26=(ImageView)findViewById(R.id.head_img_26);
        img_26=(LinearLayout)findViewById(R.id.img_26);
        head_img_27=(ImageView)findViewById(R.id.head_img_27);
        img_27=(LinearLayout)findViewById(R.id.img_27);
        head_img_28=(ImageView)findViewById(R.id.head_img_28);
        img_28=(LinearLayout)findViewById(R.id.img_28);
        head_img_29=(ImageView)findViewById(R.id.head_img_29);
        img_29=(LinearLayout)findViewById(R.id.img_29);
        head_img_30=(ImageView)findViewById(R.id.head_img_30);
        img_30=(LinearLayout)findViewById(R.id.img_30);

        imagelist.add(head_img_0);
        imagelist.add(head_img_1);
        imagelist.add(head_img_2);
        imagelist.add(head_img_3);
        imagelist.add(head_img_4);
        imagelist.add(head_img_5);
        imagelist.add(head_img_6);

        linearlist.add(img_0);
        linearlist.add(img_1);
        linearlist.add(img_2);
        linearlist.add(img_3);
        linearlist.add(img_4);
        linearlist.add(img_5);
        linearlist.add(img_6);

        imagelist.add(head_img_13);
        imagelist.add(head_img_12);
        imagelist.add(head_img_11);
        imagelist.add(head_img_10);
        imagelist.add(head_img_9);
        imagelist.add(head_img_8);
        imagelist.add(head_img_7);

        linearlist.add(img_13);
        linearlist.add(img_12);
        linearlist.add(img_11);
        linearlist.add(img_10);
        linearlist.add(img_9);
        linearlist.add(img_8);
        linearlist.add(img_7);

        imagelist.add(head_img_14);
        imagelist.add(head_img_15);
        imagelist.add(head_img_16);
        imagelist.add(head_img_17);
        imagelist.add(head_img_18);
        imagelist.add(head_img_19);
        imagelist.add(head_img_20);

        linearlist.add(img_14);
        linearlist.add(img_15);
        linearlist.add(img_16);
        linearlist.add(img_17);
        linearlist.add(img_18);
        linearlist.add(img_19);
        linearlist.add(img_20);

        imagelist.add(head_img_27);
        imagelist.add(head_img_26);
        imagelist.add(head_img_25);
        imagelist.add(head_img_24);
        imagelist.add(head_img_23);
        imagelist.add(head_img_22);
        imagelist.add(head_img_21);

        linearlist.add(img_27);
        linearlist.add(img_26);
        linearlist.add(img_25);
        linearlist.add(img_24);
        linearlist.add(img_23);
        linearlist.add(img_22);
        linearlist.add(img_21);

        imagelist.add(head_img_28);
        imagelist.add(head_img_29);
        imagelist.add(head_img_30);

        linearlist.add(img_28);
        linearlist.add(img_29);
        linearlist.add(img_30);

        viewlist.add(my_view_0);
        viewlist.add(my_view_1);
        viewlist.add(my_view_2);
        viewlist.add(my_view_3);
        viewlist.add(my_view_4);
        viewlist.add(my_view_5);
        viewlist.add(my_view_6);

        viewlist.add(my_view_13);
        viewlist.add(my_view_12);
        viewlist.add(my_view_11);
        viewlist.add(my_view_10);
        viewlist.add(my_view_9);
        viewlist.add(my_view_8);
        viewlist.add(my_view_7);

        viewlist.add(my_view_14);
        viewlist.add(my_view_15);
        viewlist.add(my_view_16);
        viewlist.add(my_view_17);
        viewlist.add(my_view_18);
        viewlist.add(my_view_19);
        viewlist.add(my_view_20);

        viewlist.add(my_view_27);
        viewlist.add(my_view_26);
        viewlist.add(my_view_25);
        viewlist.add(my_view_24);
        viewlist.add(my_view_23);
        viewlist.add(my_view_22);
        viewlist.add(my_view_21);

        viewlist.add(my_view_28);
        viewlist.add(my_view_29);
        viewlist.add(my_view_30);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        linearlist.get(sign_num - 1).setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" +sp, imagelist.get(sign_num-1), ImageLoaderOption.optionsHeaderno);
//        imagelist.get(sign_num-1).setBackgroundResource(R.drawable.gold);
        for (int k=0;k<viewlist.size();k++){
            if (k<sign_num){
                if (k==0){//第一天签到
                    isOk=true;
                    viewlist.get(k).setBac(0,isOk);
                }else if (k==6){
                    isOk=true;
                    viewlist.get(k).setBac(2,isOk);
                }else if(k==7){
                    isOk=true;
                    viewlist.get(k).setBac(3,isOk);
                }else if (k==13){
                    isOk=true;
                    viewlist.get(k).setBac(4,isOk);
                }else if (k==14){
                    isOk=true;
                    viewlist.get(k).setBac(5,isOk);
                }else if (k==20){
                    isOk=true;
                    viewlist.get(k).setBac(2,isOk);
                }else if (k==21){
                    isOk=true;
                    viewlist.get(k).setBac(3,isOk);
                }else if (k==27){
                    isOk=true;
                    viewlist.get(k).setBac(4,isOk);
                }else if (k==28){
                    isOk=true;
                    viewlist.get(k).setBac(5,isOk);
                }else if (k==30){
                    isOk=true;
                    viewlist.get(k).setBac(6,isOk);
                }else{
                    isOk=true;
                    viewlist.get(k).setBac(1,isOk);
                }
            }else{
                isOk=false;
                if (k==0){//第一天签到
                    viewlist.get(k).setBac(0,isOk);
                }else if (k==6){
                    viewlist.get(k).setBac(2,isOk);
                }else if(k==7){
                    viewlist.get(k).setBac(3,isOk);
                }else if (k==13){
                    viewlist.get(k).setBac(4,isOk);
                }else if (k==14){
                    viewlist.get(k).setBac(5,isOk);
                }else if (k==20){
                    viewlist.get(k).setBac(2,isOk);
                }else if (k==21){
                    viewlist.get(k).setBac(3,isOk);
                }else if (k==27){
                    viewlist.get(k).setBac(4,isOk);
                }else if (k==28){
                    viewlist.get(k).setBac(5,isOk);
                }else if (k==30){
                    viewlist.get(k).setBac(6,isOk);
                }else{
                    viewlist.get(k).setBac(1,isOk);
                }
            }

        }

//        grsign=(GridView)findViewById(R.id.grsign);
//        grsign.setAdapter(new  SiagnAadapte(list,getContext()));
    }
//    private class SiagnAadapte extends BaseAdapter {
//        private View iv;
//
//        public SiagnAadapte(List list, Context mainActivity) {
//        }
//
//        @Override
//        public int getCount() {
//            return 31;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView == null){
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ou_sign_item, null);
//                iv=convertView.findViewById(R.id.iv);
//                if (position==0){
//                    iv.setBackgroundResource(R.drawable.first_sign_0);
//                }else if (position>0 && position<6){
//                    iv.setBackgroundResource(R.drawable.two_sign_0);
//                }else if (position==6){
//                    iv.setBackgroundResource(R.drawable.three_sign_0);
//                }else if (position==7){
//                    iv.setBackgroundResource(R.drawable.five_sign_0);
//                }else if(position>7 && position<13){
//                    iv.setBackgroundResource(R.drawable.two_sign_0);
//                }else if (position==13){
//                    iv.setBackgroundResource(R.drawable.four_sign_0);
//                }else if (position==14){
//                    iv.setBackgroundResource(R.drawable.six_sign_0);
//                }else if (position>14 && position<20){
//                    iv.setBackgroundResource(R.drawable.two_sign_0);
//                }else if (position==20){
//                    iv.setBackgroundResource(R.drawable.three_sign_0);
//                }else if (position==21){
//                    iv.setBackgroundResource(R.drawable.five_sign_0);
//                }else if (position>21  && position<27){
//                    iv.setBackgroundResource(R.drawable.two_sign_0);
//                }else if (position==27){
//                    iv.setBackgroundResource(R.drawable.four_sign_0);
//                }else if (position==28){
//                    iv.setBackgroundResource(R.drawable.six_sign_0);
//                }else if (position==30){
//                    iv.setBackgroundResource(R.drawable.baoxiang);
//                }else {
//                    iv.setBackgroundResource(R.drawable.two_sign_0);
//                }
//            }
//            return convertView;
//        }
//    }
}
