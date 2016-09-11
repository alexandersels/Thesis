package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.R;

/**
 * Created by Alexander on 11/10/2015.
 */
public abstract class LocalisationObjectView  extends LinearLayout {

    /**
     * Default constructor setting the view type to "draw"
     * @param context the context of the parent
     * @param attrs the attribute set
     * @param defStyle the default style to apply
     */
    public LocalisationObjectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloorPlanObjectView);
        a.recycle();
    }

    /**
     * Default constructor  setting the view type to "draw"
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public LocalisationObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloorPlanObjectView);
        a.recycle();
    }

    /**
     * Default constructor
     * @param context the context of the parent
     */
    public LocalisationObjectView(Context context) {
        super(context);
    }
}
