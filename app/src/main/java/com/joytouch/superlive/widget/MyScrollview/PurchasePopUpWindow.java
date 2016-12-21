package com.joytouch.superlive.widget.MyScrollview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.joytouch.superlive.R;


public class PurchasePopUpWindow extends PopupWindow {
	TextView btn_submit;
	View detail_customization;
	ViewPager vp_details;

	public PurchasePopUpWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		detail_customization = inflater.inflate(R.layout.purchase_popup_window,
				null);

		btn_submit = (TextView) detail_customization
				.findViewById(R.id.btn_submit);

		vp_details = (ViewPager) detail_customization
				.findViewById(R.id.vp_details);

		detail_customization.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		this.setContentView(detail_customization);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(00000000);
		this.setBackgroundDrawable(dw);

		btn_submit.setOnClickListener(itemsOnClick);

	}


	public View getCustomView() {
		return detail_customization;
	}

	public void setDiss(){
		dismiss();
	}
}
