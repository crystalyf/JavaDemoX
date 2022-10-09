package com.change.javaandroidtry.view.dragchoose.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.change.javaandroidtry.R;
import com.change.javaandroidtry.view.dragchoose.adapter.DragAdapter;
import com.google.firebase.database.annotations.NotNull;

public class DragChessView extends FrameLayout {
    private GestureDetector detector;
    /**
     * 点击拖动
     */
    public static final int DRAG_WHEN_TOUCH = 0;
    /**
     * 长按拖动
     */
    public static final int DRAG_BY_LONG_CLICK = 1;

    private int mDragMode = DRAG_WHEN_TOUCH;
    private boolean hasSendDragMsg = false;
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x123) {// 启动拖拽模式
                isDraggable = true;
                // 保存当前拖拽的index和value
                mDragTop.setCurrentDragPosition(msg.arg1);
                mDragTop.setCurrentDragValue(mDragTop.getTargetDataByIndex(mDragTop.getCurrentDragPosition()));
                // 根据点击的位置生成该位置上的view镜像
                copyView(mDragTop);
                mDragTop.removeTargetData(mDragTop.getCurrentDragPosition());
                hasSendDragMsg = false;
            }
            return false;
        }
    });

    private boolean isDraggable = true;
    private float[] lastLocation = null;
    private View mCopyView;
    private OnTouchListener l;
    private int mTouchArea = 0;
    private View dragSlider;
    private Point mMovePoint; // 记录移动走向，上到下，还是下到上 （记录初始长按拖拽的位置）


    //是否能添加view到右边的布局。 默认是true
    private boolean canAddViewWhenDragChange = true;
    /**
     * 手势监听器,滚动和单击
     */
    private final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /*
             * 缓慢滑动，手指滑动屏幕的过程中执行
             */
            if (hasSendDragMsg) {
                hasSendDragMsg = false;
                handler.removeMessages(0x123);
            }
            if (isDraggable && mCopyView != null) {
                if (lastLocation == null && e1 != null) {
                    lastLocation = new float[]{e1.getRawX(), e1.getRawY()};
                }
                if (lastLocation == null)
                    lastLocation = new float[]{0, 0};
                distanceX = lastLocation[0] - e2.getRawX();
                distanceY = lastLocation[1] - e2.getRawY();
                lastLocation[0] = e2.getRawX();
                lastLocation[1] = e2.getRawY();

                mCopyView.setX(mCopyView.getX() - distanceX);
                mCopyView.setY(mCopyView.getY() - distanceY);
                //刷新重绘当前的View
                mCopyView.invalidate();
                if (isDragInTop()) {
                    if (canAddViewWhenDragChange) {// 保证移动过程中，数据只有一次的添加
                        canAddViewWhenDragChange = false;
                        if (hideView != null)
                            hideView.setVisibility(VISIBLE);
                    }

                } else {
                    //从top区域拖拽到top之外的情况
                    if (isDragFromTop()) {
                        if (canAddViewWhenDragChange) {
                            mDragTop.removeSwapView();
                            canAddViewWhenDragChange = false;
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            /* 响应长按拖拽 */
            if (mDragMode == DRAG_BY_LONG_CLICK) {
                // 通知父控件不拦截我的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                // 根据点击的位置生成该位置上的view镜像
                int position = eventToPosition(e);
                Log.v("logStr:", "isCanDragMove");
                if (isCanDragMove(mDragTop, position)) {
                    Message msg = handler.obtainMessage(0x123, position, (int) e.getX(), (int) e.getY());
                    // show press本身大概需要170毫秒
                    handler.sendMessageDelayed(msg, dragLongPressTime - 170);
                    Log.v("logStr:", "sendMessageDelayed:" + position);
                    //记录初始长按拖拽的位置
                    mMovePoint = new Point((int) e.getX(), (int) e.getY());
                    hasSendDragMsg = true;
                }
            }
        }
    };

    private boolean isTouchInTop(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        return isTouchInTop(y, x);
    }

    /**
     * 根据拖拽触摸的y坐标，判断是否拖拽的是Top部分的布局
     *
     * @param y y坐标
     * @param x x坐标
     * @return bool
     */
    private boolean isTouchInTop(float y, float x) {
        return (y > mDragTop.getY() && y < (mDragTop.getY() + mDragTop.getHeight())) && (x > mDragTop.getX() && x < (mDragTop.getX() + mDragTop.getWidth()));
    }

    private boolean isCanDragMove(DragView dragView, int position) {
        return position >= dragView.getHeadDragPosition() && position < dragView.getGridChildCount() - dragView.getFootDragPosition();
    }

    private FrameLayout mDragFrame;
    //bottom 区域的列表view
    private RecyclerView mRvEnd;
    //top 区域的列表view
    private DragView mDragTop;
    //被拖拽的item在点击的时候，隐藏掉列表中的本体item。（拖拽的是个副本view）
    private View hideView;
    private long dragLongPressTime = 600;

    public DragChessView(@NonNull Context context) {
        this(context, null);
    }

    public DragChessView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DragChessView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化布局
     *
     * @param attrs attrs
     */
    private void init(AttributeSet attrs) {
        Context context = getContext();
        detector = new GestureDetector(context, simpleOnGestureListener);
        detector.setIsLongpressEnabled(false);
        mDragFrame = new FrameLayout(context);
        dragSlider = LayoutInflater.from(context).inflate(R.layout.layout_drag_chess, this, false);
        mDragTop = dragSlider.findViewById(R.id.drag_top);
        mRvEnd = dragSlider.findViewById(R.id.rv_right);
        addView(dragSlider, -1, -1);
        addView(mDragFrame, -1, -1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (l != null) {
            l.onTouch(this, ev);
        }
        if (!isViewInitDone()) {
            return false;
        }

        if (isDraggable) {
            handleScrollAndCreMirror(ev);
        } else {
            // 交给子控件自己处理
            dispatchEvent(mDragTop, ev);
        }

        // 处理拖动
        detector.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            lastLocation = null;
            if (hasSendDragMsg) {
                hasSendDragMsg = false;
                handler.removeMessages(0x123);
            }
        }
        return true;
    }

    private void dispatchEvent(DragView dragView, MotionEvent ev) {
        dragView.dispatchEvent(ev);
    }

    /**
     * 如果是在top部分的列表内拖拽
     *
     * @return bool
     */
    private boolean isDragInTop() {
        if (mCopyView == null)
            return false;
        //拖拽的copyView的纵坐标 < mDragTop列表的高度范围 && 拖拽的copyView的x坐标 < mDragTop列表的宽度范围
        return ((mCopyView.getY() < (mDragTop.getY() + mDragTop.getHeight())) && (mCopyView.getX() < (mDragTop.getX() + mDragTop.getWidth())));
    }

    /**
     * 从top区域拖拽到top之外的区域
     *
     * @return bool
     */
    private boolean isDragFromTop() {
        if (mMovePoint != null && mDragTop != null) {
            if ((mMovePoint.x > mDragTop.getX() && mMovePoint.x < (mDragTop.getX() + mDragTop.getWidth())) && (mMovePoint.y > mDragTop.getY() && mMovePoint.y < (mDragTop.getY() + mDragTop.getHeight()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否拖拽到了右布局区域
     *
     * @return bool
     */
    private boolean isDragInRightArea() {
        if (mDragTop == null || mRvEnd == null) {
            return true;
        }
        return (((mRvEnd.getX() < mCopyView.getX()) && (mCopyView.getX() < (mRvEnd.getX() + mRvEnd.getWidth()))) && ((mRvEnd.getY() < mCopyView.getY()) && (mCopyView.getY() < (mRvEnd.getY() + mRvEnd.getHeight()))));
    }

    /**
     * Description :拦截所有事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 处理自动滚屏,和单击生成镜像
     */
    private void handleScrollAndCreMirror(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 通知父控件不拦截我的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                // 根据点击的位置生成该位置上的view镜像
                int position = eventToPosition(ev);
                makeCopyView(mDragTop, position);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);// 通知父控件不拦截我的事件
                // 内容太多时,移动到边缘会自动滚动
                decodeScrollArea(mDragTop, ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (hideView != null) {
                    hideView.setVisibility(View.VISIBLE);
                }
                mDragFrame.removeAllViews();
                updateUI(mDragTop, ev);
                mCopyView = null;
                //抬起时，恢复addView的flag
                canAddViewWhenDragChange = true;
                mDragTop.clearCurrentDragInfo();
                // 放手时取消拖动排序模式
                if (mDragMode == DRAG_BY_LONG_CLICK) {
                    isDraggable = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * todo： 判断并显示刷新界面逻辑 （左布局是否恢复拖拽的item，右布局是否需要新增item）
     *
     * @param dragView 左边布局
     * @param ev       touch motionEvent
     */
    private void updateUI(DragView dragView, MotionEvent ev) {
        //当前松手之前，有拖拽的item
        if (mDragTop.getCurrentDragPosition() != -1) {
            //如果没拖到右边的区域，那么将拖拽的item恢复
            if (!isDragInRightArea()) {
                mDragTop.addDataByIndex(mDragTop.getCurrentDragPosition(), mDragTop.getCurrentDragValue());
                dragView.getAdapter().notifyDataSetChanged();
            }else {
                //todo: 拖拽到右布局的逻辑
            }
        }
        // 停止滚动
        if (dragView.isCanScroll()) {
            int scrollStates2 = dragView.decodeTouchArea(ev);
            if (scrollStates2 != 0) {
                dragView.onTouchAreaChange(0);
                mTouchArea = 0;
            }
        }
    }

    private void decodeScrollArea(DragView dragView, MotionEvent ev) {
        if (dragView.isCanScroll()) {
            int touchArea = dragView.decodeTouchArea(ev);
            if (touchArea != mTouchArea) {
                dragView.onTouchAreaChange(touchArea);
                mTouchArea = touchArea;
            }
        }
    }

    private void makeCopyView(DragView dragView, int position) {
        if (position >= dragView.getHeadDragPosition() && position < dragView.getGridChildCount() - dragView.getFootDragPosition()) {
            dragView.setCurrentDragPosition(position);
            copyView(dragView);
        }
    }

    /**
     * 得到事件触发点, 摸到的是哪一个item
     */
    public int eventToPosition(MotionEvent ev) {
        if (ev != null) {
            if (isTouchInTop(ev))
                return mDragTop.eventToPosition(ev);
        }
        return 0;
    }


    /**
     * 复制一个镜像,并添加到透明层 (拖动的item就是复制出来的)
     */
    private void copyView(DragView dragView) {
        // TODO: 2018/4/2 创建可移动的 item
        hideView = dragView.getGridChildAt(dragView.getCurrentDragPosition());
        int realPosition = dragView.getGridChildPos(hideView);
        DragAdapter adapter = dragView.getAdapter();
        if (!adapter.isUseCopyView()) {
            mCopyView = adapter.getView(realPosition, mCopyView, mDragFrame);
        } else {
            mCopyView = adapter.copyView(realPosition, mCopyView, mDragFrame);
        }
        hideView.setVisibility(View.INVISIBLE);
        if (mCopyView.getParent() == null)
            mDragFrame.addView(mCopyView, dragView.getmColWidth(), dragView.getmColHeight());

        int[] l1 = new int[2];
        int[] l2 = new int[2];
        hideView.getLocationOnScreen(l1);
        mDragFrame.getLocationOnScreen(l2);
        mCopyView.setX(l1[0] - l2[0]);
        mCopyView.setY(l1[1] - l2[1]);

        mCopyView.setScaleX(1.2f);
        mCopyView.setScaleY(1.2f);
    }

    public void setDragModel(int mode) {
        this.mDragMode = mode;
        isDraggable = mode == DRAG_WHEN_TOUCH;
    }

    public boolean isViewInitDone() {
        boolean result = true;
        if (mDragTop.getVisibility() == VISIBLE)
            result &= mDragTop.isViewInitDone();
        return result;
    }

    public void setTopAdapter(@NotNull DragAdapter adapter) {
        mDragTop.setAdapter(adapter);
    }

    /**
     * 拖拽时改变item的下标index （因为拖拽的时候列表内部item不变，所以这个函数在英检的功能里用不上）
     *
     * @param dragView dragView
     * @param to to
     */
    private void dragChangePosition(DragView dragView, int to) {
        if (to != dragView.getCurrentDragPosition() && isCanDragMove(dragView, to)) {
            dragView.onDragPositionChange(dragView.getCurrentDragPosition(), to);
        }
    }
}