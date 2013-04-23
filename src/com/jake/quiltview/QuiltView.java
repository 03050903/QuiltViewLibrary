package com.jake.quiltview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;

import java.util.ArrayList;


public class QuiltView extends FrameLayout implements OnGlobalLayoutListener {

    public QuiltViewBase quilt;
    public ViewGroup scroll;
    public int padding;
    public boolean isVertical = false;
    public int baseWidth, baseHeight;
    public ArrayList<View> views;
    private Adapter adapter;
    private boolean isScrollable;

    public QuiltView(Context context, boolean isVertical, boolean isScrollable) {
        super(context);
        this.isVertical = isVertical;
        this.isScrollable = isScrollable;
        setup();
    }

    public QuiltView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.QuiltView);

        String orientation = a.getString(R.styleable.QuiltView_scrollOrientation);
        if (orientation != null) {
            if (orientation.equals("vertical")) {
                isVertical = true;
            } else {
                isVertical = false;
            }
        }
        padding = a.getInt(R.styleable.QuiltView_childPadding, 5);
        baseWidth = a.getInt(R.styleable.QuiltView_baseWidth, 0);
        baseHeight = a.getInt(R.styleable.QuiltView_baseHeight, 0);
        isScrollable = a.getBoolean(R.styleable.QuiltView_scrollable, true);

        setup();
    }

    public void setup() {
        views = new ArrayList<View>();

        quilt = new QuiltViewBase(getContext(), isVertical, baseWidth, baseHeight);

        if (isScrollable) {
            if (isVertical) {
                scroll = new ScrollView(this.getContext());
            } else {
                scroll = new HorizontalScrollView(this.getContext());
            }
            scroll.addView(quilt);
            this.addView(scroll);
        } else {
            addView(quilt);
        }

    }

    public void setBaseWidth(int baseWidth) {
        this.baseWidth = baseWidth;
        quilt.baseWidth = baseWidth;
        setup();
    }

    public void setBaseHeight(int baseHeight) {
        this.baseHeight = baseHeight;
        quilt.baseHeight = baseHeight;
        setup();
    }

    private DataSetObserver adapterObserver = new DataSetObserver() {
        public void onChanged() {
            super.onChanged();
            onDataChanged();
        }

        public void onInvalidated() {
            super.onInvalidated();
            onDataChanged();
        }

        public void onDataChanged() {
            setViewsFromAdapter(adapter);
        }
    };

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        adapter.registerDataSetObserver(adapterObserver);
        setViewsFromAdapter(adapter);
    }

    private void setViewsFromAdapter(Adapter adapter) {
        this.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            quilt.addPatch(adapter.getView(i, null, quilt));
        }
    }

    public void addPatchImages(ArrayList<ImageView> images) {

        for (ImageView image : images) {
            addPatchImage(image);
        }
    }

    public void addPatchImage(ImageView image) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(params);

        if (padding != 0) {
            LinearLayout wrapper = new LinearLayout(this.getContext());
            wrapper.setPadding(padding, padding, padding, padding);
            wrapper.addView(image);
            addPatchView(wrapper);
        } else {
            addPatchView(image);
        }
    }

    public void addPatchViews(ArrayList<View> views_a) {
        for (View view : views_a) {
            addPatchView(view);
        }
    }

    public void addPatchesOnLayout() {
        for (View view : views) {
            addPatchView(view);
        }
    }

    public void addPatchView(View view) {
        quilt.addPatch(view);
    }

    public void removeQuilt(View view) {
        quilt.removeView(view);
    }

    public void setChildPadding(int padding) {
        this.padding = padding;
    }

    public void refresh() {
        quilt.refresh();
    }

    public void setOrientation(boolean isVertical) {
        this.isVertical = isVertical;
    }


    @Override
    public void onGlobalLayout() {
        //addPatchesOnLayout();
    }
}
