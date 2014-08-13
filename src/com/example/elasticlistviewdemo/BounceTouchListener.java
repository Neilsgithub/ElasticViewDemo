package com.example.elasticlistviewdemo;

import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

public class BounceTouchListener implements OnTouchListener ,OnGestureListener{
	private float y;
	private Rect normal = new Rect();
	private boolean animationFinish = true;
	private View v;
	private GestureDetector detector;
	private final int moveOutVelocity = 2000;
	private boolean moveOut;
	
	public BounceTouchListener(Context context){
		super();
		detector = new GestureDetector(context, this);
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(velocityY < -moveOutVelocity){
			moveOut = true;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.v = v;
		detector.onTouchEvent(event);
		commOnTouchEvent(event);
		return true;
	}

	public void commOnTouchEvent(MotionEvent ev) {
		if (animationFinish) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				y = ev.getY();
				break;
			case MotionEvent.ACTION_UP:
				y = 0;
				if(!moveOut){
					if (isNeedAnimation()) {
						animation();
					}
				}else{
					moveOutAnim();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				final float preY = y == 0 ? ev.getY() : y;
				float nowY = ev.getY();
				int deltaY = (int) (preY - nowY);
				
				y = nowY;
				// 当滚动到最上或者最下时就不会再滚动，这时移动布局
				if(deltaY > 0){
					if (normal.isEmpty()) {
						// 保存正常的布局位置
						normal.set(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
					}
					// 移动布局
					v.layout(v.getLeft(), v.getTop() - deltaY, v.getRight(), v.getBottom() - deltaY );
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void moveOutAnim(){
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -(v.getHeight() + v.getTop()));
		ta.setInterpolator(new AccelerateInterpolator());
		ta.setDuration(500);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				animationFinish = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.clearAnimation();
				v.setVisibility(View.GONE);
				normal.setEmpty();
				animationFinish = true;
			}
		});
		v.startAnimation(ta);
	}

	// 开启动画移动
	public void animation() {
		System.out.println(">>" + (normal.top - v.getTop()));
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, normal.top - v.getTop());
		ta.setInterpolator(new BounceInterpolator());
		ta.setDuration(500);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				animationFinish = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.clearAnimation();
				// 设置回到正常的布局位置
				v.layout(normal.left, normal.top, normal.right, normal.bottom);
				normal.setEmpty();
				animationFinish = true;
			}
		});
		v.startAnimation(ta);
	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

}
