package com.joytouch.superlive.widget.stickylistheaders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.joytouch.superlive.R;
import com.joytouch.superlive.widget.Pullable;

import java.util.List;

/**
 * add expand/collapse functions like ExpandableListView
 * @author lsjwzh
 */
public class ExpandableStickyListHeadersListView extends StickyListHeadersListView implements
        Pullable {

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
    public interface IAnimationExecutor{
        public void executeAnim(View target, int animType);
    }

    public final static int ANIMATION_COLLAPSE = 1;
    public final static int ANIMATION_EXPAND = 0;

    ExpandableStickyListHeadersAdapter mExpandableStickyListHeadersAdapter;



    IAnimationExecutor mDefaultAnimExecutor = new IAnimationExecutor() {
        @Override
        public void executeAnim(View target, int animType) {
            if(animType==ANIMATION_EXPAND){
                target.setVisibility(VISIBLE);
            }else if(animType==ANIMATION_COLLAPSE){
                target.setVisibility(GONE);
            }
        }
    };


    public ExpandableStickyListHeadersListView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
        setEmptyView(view);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
        setEmptyView(view);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.empty,null);
        setEmptyView(view);
    }

    @Override
    public ExpandableStickyListHeadersAdapter getAdapter() {
        return mExpandableStickyListHeadersAdapter;
    }

    @Override
    public void setAdapter(StickyListHeadersAdapter adapter) {
        mExpandableStickyListHeadersAdapter = new ExpandableStickyListHeadersAdapter(adapter);
        super.setAdapter(mExpandableStickyListHeadersAdapter);
    }

    public View findViewByItemId(long itemId){
        return mExpandableStickyListHeadersAdapter.findViewByItemId(itemId);
    }

    public long findItemIdByView(View view){
        return mExpandableStickyListHeadersAdapter.findItemIdByView(view);
    }

    public void expand(long headerId) {
        if(!mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)){
            return;
        }
        mExpandableStickyListHeadersAdapter.expand(headerId);
        //find and expand views in group
        List<View> itemViews = mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews==null){
            return;
        }
        for (View view : itemViews) {
            animateView(view, ANIMATION_EXPAND);
        }
    }

    public void collapse(long headerId) {
        if(mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)){
            return;
        }
        mExpandableStickyListHeadersAdapter.collapse(headerId);
        //find and hide views with the same header
        List<View> itemViews = mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews==null){
            return;
        }
        for (View view : itemViews) {
            animateView(view, ANIMATION_COLLAPSE);
        }
    }

    public boolean isHeaderCollapsed(long headerId){
        return  mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId);
    }

    public void setAnimExecutor(IAnimationExecutor animExecutor) {
        this.mDefaultAnimExecutor = animExecutor;
    }

    /**
     * Performs either COLLAPSE or EXPAND animation on the target view
     *
     * @param target the view to animate
     * @param type   the animation type, either ExpandCollapseAnimation.COLLAPSE
     *               or ExpandCollapseAnimation.EXPAND
     */
    private void animateView(final View target, final int type) {
        if(ANIMATION_EXPAND==type&&target.getVisibility()==VISIBLE){
            return;
        }
        if(ANIMATION_COLLAPSE==type&&target.getVisibility()!=VISIBLE){
            return;
        }
        if(mDefaultAnimExecutor !=null){
            mDefaultAnimExecutor.executeAnim(target,type);
        }

    }

}
