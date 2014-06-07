package edu.minggo.chat.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class MyScrollListView extends ListView {
	@SuppressWarnings("unused")
	private Context context;
	private boolean outBound = false;
	private int distance;
	private int firstOut;
	private static final String TAG = "test";

	public MyScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MyScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MyScrollListView(Context context) {
		super(context);
		this.context = context;
	}

	GestureDetector gestureDetector = new GestureDetector(
			new OnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {

					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {

				}

				/**
				 * 手势滑动的时候触发
				 */
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					Log.d(TAG, "ENTER onscroll");
					int firstPos = getFirstVisiblePosition();
					int lastPos = getLastVisiblePosition();
					int itemCount = getCount();
					if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {
						scrollTo(0, 0);
						return false;
					}
					View firstView = getChildAt(firstPos);
					if (!outBound) {
						firstOut = (int) e2.getRawY();
					}
					if (firstView != null) {
						// Record the length of each slide
						distance = firstOut - (int) e2.getRawY();
						scrollTo(0, distance / 2);
						return true;
					}
					return true;
				}

				@Override
				public void onLongPress(MotionEvent e) {

				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {

					return false;
				}

				@Override
				public boolean onDown(MotionEvent e) {

					return false;
				}
			});

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int act = event.getAction();
		if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL)
				&& outBound) {
			outBound = false;
			// scroll back
		}
		if (!gestureDetector.onTouchEvent(event)) {
			outBound = false;
		} else {
			outBound = true;
		}
		Rect rect = new Rect();
		getLocalVisibleRect(rect);
		TranslateAnimation am = new TranslateAnimation(0, 0, -rect.top, 0);
		am.setDuration(3000);
		startAnimation(am);
		scrollTo(0, 0);
		return super.dispatchTouchEvent(event);
	}
}