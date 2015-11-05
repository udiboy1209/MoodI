package org.iitb.moodi.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import org.iitb.moodi.R;
import org.iitb.moodi.ui.anim.ResizeAnimation;

public class ToolbarWidgetLayout extends LinearLayout implements View.OnTouchListener{
    private Toolbar mToolbar;
    private View mWidget;

    private int mWidgetHeight;

    private float prevY;

    public ToolbarWidgetLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public ToolbarWidgetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ToolbarWidgetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ToolbarWidgetLayout, defStyle, 0);

        setOrientation(LinearLayout.VERTICAL);
        setOnTouchListener(this);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        for(int i=0; i<getChildCount();i++) {
            if(getChildAt(i) instanceof Toolbar) {
                mToolbar = (Toolbar) getChildAt(i);

                if (i + 1 < getChildCount()) {
                    mWidget = getChildAt(i + 1);
                    i++;
                }
            } else {
                removeViewAt(i);
                i--;
            }
        }
    }

    public void setWidget(View v){
        if(mWidget!=null) removeView(mWidget);

        mWidget=v;

        if(mWidget!=null) {
            addView(mWidget);
;           mWidget.measure(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            mWidgetHeight=mWidget.getMeasuredHeight();
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void collapse(){
        ResizeAnimation a = new ResizeAnimation(mWidget, 1);
        a.setDuration(300);

        if(mWidget.getAnimation()==null){
            mWidget.startAnimation(a);
        } else if(mWidget.getAnimation().hasEnded()) {
            mWidget.startAnimation(a);
        }
    }

    public void expand(){
        ResizeAnimation a = new ResizeAnimation(mWidget, mWidgetHeight);
        a.setDuration(600);

        if(mWidget.getAnimation()==null){
            mWidget.startAnimation(a);
        } else if(mWidget.getAnimation().hasEnded()) {
            mWidget.startAnimation(a);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("ToolbarWidget", "onTouch");

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            prevY=event.getY();
            Log.d("ToolbarWidget", "prevY:"+prevY);
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(event.getY()-prevY > mToolbar.getHeight()/3){
                //Scroll Down
                expand();
            } else if(prevY-event.getY() > mToolbar.getHeight()/3){
                //Scroll Up
                collapse();
            }

            Log.d("ToolbarWidget", "currY:"+event.getY());
            return true;
        }
        return false;
    }
}
