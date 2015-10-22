package org.iitb.moodi.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iitb.moodi.ui.activity.MainActivity;
import org.iitb.moodi.R;

public class GenreCard extends LinearLayout {
    MainActivity mContext;

    TextView mTitle;
    ImageView mColor;

    public GenreCard(Context context){
        super(context);

        init(context);
    }

    public GenreCard(Context context, AttributeSet attrs){
        super(context,attrs);

        init(context);
    }

    public GenreCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        if (context instanceof MainActivity)
            mContext = (MainActivity) context;

        LayoutInflater inflater = mContext.getLayoutInflater();
        View root = inflater.inflate(R.layout.view_genre_card, this, true);

        mTitle = (TextView) root.findViewById(R.id.genre_card_title);
        mColor= (ImageView) root.findViewById(R.id.genre_card_color);
    }

    public void setTitle(String text){
        mTitle.setText(text);
    }

    public void setColor(int color){
        mColor.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthDesc = MeasureSpec.getMode(widthMeasureSpec);
        int heightDesc = MeasureSpec.getMode(heightMeasureSpec);
        int size = 0;
        if (widthDesc == MeasureSpec.UNSPECIFIED
                && heightDesc == MeasureSpec.UNSPECIFIED) {
            size = getContext().getResources().getDimensionPixelSize(R.dimen.genre_card_size); // Use your own default size, for example 125dp
        } else if ((widthDesc == MeasureSpec.UNSPECIFIED || heightDesc == MeasureSpec.UNSPECIFIED)
                && !(widthDesc == MeasureSpec.UNSPECIFIED && heightDesc == MeasureSpec.UNSPECIFIED)) {
            //Only one of the dimensions has been specified so we choose the dimension that has a value (in the case of unspecified, the value assigned is 0)
            size = width > height ? width : height;
        } else {
            //In all other cases both dimensions have been specified so we choose the smaller of the two
            //size = width > height ? height : width;
            size = width;
        }
        setMeasuredDimension(size, size);

        Log.d("GenreCard", "on Measure : "+size);
    }
}
