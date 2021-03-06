package com.xiaomai.shopping.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {

	public SquareRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareRelativeLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int height = widthSize;
		int heightMode = MeasureSpec.getMode(widthMeasureSpec);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
