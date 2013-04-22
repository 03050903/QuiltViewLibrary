package com.jake.quiltview;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;
import com.jake.quiltview.R.styleable;

import java.util.ArrayList;


public class QuiltView extends FrameLayout implements OnGlobalLayoutListener {

	public QuiltViewBase quilt;
	public ViewGroup scroll;
	public int padding;
	public boolean isVertical = false;
    public int baseWidth, baseHeight;
	public ArrayList<View> views;
	
	public QuiltView(Context context,boolean isVertical) {
		super(context);
		this.isVertical = isVertical;
		setup();
	}
	
	public QuiltView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
			    R.styleable.QuiltView);
			 
		String orientation = a.getString(R.styleable.QuiltView_scrollOrientation);
		if(orientation != null){
			if(orientation.equals("vertical")){
				isVertical = true;
			} else {
				isVertical = false;
			}
		}
        padding = a.getInt(styleable.QuiltView_childPadding, 5);
        baseWidth = a.getInt(R.styleable.QuiltView_baseWidth, 0);
        baseHeight = a.getInt(R.styleable.QuiltView_baseHeight, 0);

		setup();
	}
	
	public void setup(){
		views = new ArrayList<View>();
		
		if(isVertical){
			scroll = new ScrollView(this.getContext());
		} else {
			scroll = new HorizontalScrollView(this.getContext());
		}
		quilt = new QuiltViewBase(getContext(), isVertical, baseWidth, baseHeight);
		scroll.addView(quilt);
		this.addView(scroll);
		
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
	
	public void addPatchImages(ArrayList<ImageView> images){
		
		for(ImageView image: images){
            addPatchImage(image);
		}
	}

    public void addPatchImage(ImageView image){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(params);

        LinearLayout wrapper = new LinearLayout(this.getContext());
        wrapper.setPadding(padding, padding, padding, padding);
        wrapper.addView(image);
        addPatchView(wrapper);
    }
	
	public void addPatchViews(ArrayList<View> views_a){
		for(View view: views_a){
            addPatchView(view);
		}
	}
	
	public void addPatchesOnLayout(){
		for(View view: views){
            addPatchView(view);
		}
	}

    public void addPatchView(View view){
        quilt.addPatch(view);
    }
	
	public void removeQuilt(View view){
		quilt.removeView(view);
	}
	
	public void setChildPadding(int padding){
		this.padding = padding;
	}
	
	public void refresh(){
		quilt.refresh();
	}
	
	public void setOrientation(boolean isVertical){
		this.isVertical = isVertical;
	}

	
	@Override
	public void onGlobalLayout() {
		//addPatchesOnLayout();
	}
}
