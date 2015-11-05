package org.iitb.moodi.ui.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by udiboy on 3/11/15.
 */
public class ResizeWidgetBehavior extends CoordinatorLayout.Behavior{
    public ResizeWidgetBehavior(Context c, AttributeSet s){
        super(c,s);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ToolbarWidgetLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        ((CoordinatorLayout.LayoutParams)child.getLayoutParams())
                .topMargin = dependency.getHeight();

        return false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d("ResizeWidgetBehavior", dxConsumed + "," + dyConsumed + "," + dxUnconsumed + "," + dyUnconsumed);

        ToolbarWidgetLayout dependency = (ToolbarWidgetLayout) coordinatorLayout.getDependencies(child).get(0);

        if(dyConsumed >0 || dyUnconsumed > 0){
            Log.d("ResizeWidgetBehavior","run collapse animation");
            //dependency.collapse();
        } else if (dyUnconsumed < 0) {
            Log.d("ResizeWidgetBehavior","run expand animation");
            //dependency.expand();
        }
    }
}
