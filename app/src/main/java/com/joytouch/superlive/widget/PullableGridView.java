package com.joytouch.superlive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.joytouch.superlive.R;

public class PullableGridView extends GridView implements Pullable
{

	public PullableGridView(Context context)
	{
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
		setEmptyView(view);
	}

	public PullableGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
		setEmptyView(view);
	}

	public PullableGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
		setEmptyView(view);
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}

}
