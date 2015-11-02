package org.iitb.moodi.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.iitb.moodi.R;

public class ToolbarWidgetLayout extends LinearLayout {
    private Toolbar mToolbar;
    private View mWidget;

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
        if(v!=null) addView(v);

        mWidget=v;
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }
}
