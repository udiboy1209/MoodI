package org.iitb.moodi.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.iitb.moodi.MainActivity;
import org.iitb.moodi.R;

public class GenreCard extends RelativeLayout{
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
}
