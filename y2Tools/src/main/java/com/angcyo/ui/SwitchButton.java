package com.angcyo.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.Scroller;

import com.angcyo.y2tools.R;

/**
 * 寮?鍏虫寜閽?
 */
public class SwitchButton extends CompoundButton {
    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private int buttonLeft;  //鎸夐挳鍦ㄧ敾甯冧笂鐨刋鍧愭爣
    private int buttonTop;  //鎸夐挳鍦ㄧ敾甯冧笂鐨刌鍧愭爣
    private int tempSlideX = 0; //X杞村綋鍓嶅潗鏍囷紝鐢ㄤ簬鍔ㄦ?佺粯鍒跺浘鐗囨樉绀哄潗鏍囷紝瀹炵幇婊戝姩鏁堟灉
    private int tempMinSlideX = 0;  //X杞存渶灏忓潗鏍囷紝鐢ㄤ簬闃叉寰?宸﹁竟婊戝姩鏃惰秴鍑鸿寖鍥?
    private int tempMaxSlideX = 0;  //X杞存渶澶у潗鏍囷紝鐢ㄤ簬闃叉寰?鍙宠竟婊戝姩鏃惰秴鍑鸿寖鍥?
    private int tempTotalSlideDistance;   //婊戝姩璺濈锛岀敤浜庤褰曟瘡娆℃粦鍔ㄧ殑璺濈锛屽湪婊戝姩缁撴潫鍚庢牴鎹窛绂诲垽鏂槸鍚﹀垏鎹㈢姸鎬佹垨鑰呭洖婊?
    private int duration = 200; //鍔ㄧ敾鎸佺画鏃堕棿
    private int touchMode; //瑙︽懜妯″紡锛岀敤鏉ュ湪澶勭悊婊戝姩浜嬩欢鐨勬椂鍊欏尯鍒嗘搷浣?
    private int touchSlop;
    private int withTextInterval = 16;   //鏂囧瓧鍜屾寜閽箣闂寸殑闂磋窛
    private float touchX;   //璁板綍涓婃瑙︽懜鍧愭爣锛岀敤浜庤绠楁粦鍔ㄨ窛绂?
    private float minChangeDistanceScale = 0.2f;   //鏈夋晥璺濈姣斾緥锛屼緥濡傛寜閽搴︿负100锛屾瘮渚嬩负0.3锛岄偅涔堝彧鏈夊綋婊戝姩璺濈澶т簬绛変簬(100*0.3)鎵嶄細鍒囨崲鐘舵?侊紝鍚﹀垯灏卞洖婊?
    private Paint paint;    //鐢荤瑪锛岀敤鏉ョ粯鍒堕伄缃╂晥鏋?
    private RectF buttonRectF;   //鎸夐挳鐨勪綅缃?
    private Drawable frameDrawable; //妗嗘灦灞傚浘鐗?
    private Drawable stateDrawable;    //鐘舵?佸浘鐗?
    private Drawable stateMaskDrawable;    //鐘舵?侀伄缃╁浘鐗?
    private Drawable sliderDrawable;    //婊戝潡鍥剧墖
    private SwitchScroller switchScroller;  //鍒囨崲婊氬姩鍣紝鐢ㄤ簬瀹炵幇骞虫粦婊氬姩鏁堟灉
    private PorterDuffXfermode porterDuffXfermode;//閬僵绫诲瀷

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * 鍒濆鍖?
     *
     * @param attrs 灞炴??
     */
    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER_VERTICAL);
        paint = new Paint();
        paint.setColor(Color.RED);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        switchScroller = new SwitchScroller(getContext(), new AccelerateDecelerateInterpolator());
        buttonRectF = new RectF();

        if (attrs != null && getContext() != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            if (typedArray != null) {
                withTextInterval = (int) typedArray.getDimension(R.styleable.SwitchButton_withTextInterval, 0.0f);
                setDrawables(
                        typedArray.getDrawable(R.styleable.SwitchButton_frameDrawable),
                        typedArray.getDrawable(R.styleable.SwitchButton_stateDrawable),
                        typedArray.getDrawable(R.styleable.SwitchButton_stateMaskDrawable),
                        typedArray.getDrawable(R.styleable.SwitchButton_sliderDrawable)
                );
                typedArray.recycle();
            }
        }

        ViewConfiguration config = ViewConfiguration.get(getContext());
        touchSlop = config.getScaledTouchSlop();
        setChecked(isChecked());
        setClickable(true); //璁剧疆鍏佽鐐瑰嚮锛屽綋鐢ㄦ埛鐐瑰嚮鍦ㄦ寜閽叾瀹冨尯鍩熺殑鏃跺?欏氨浼氬垏鎹㈢姸鎬?
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //璁＄畻瀹藉害
        int measureWidth;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.AT_MOST://濡傛灉widthSize鏄綋鍓嶈鍥惧彲浣跨敤鐨勬渶澶у搴?
                measureWidth = getCompoundPaddingLeft() + getCompoundPaddingRight();
                break;
            case MeasureSpec.EXACTLY://濡傛灉widthSize鏄綋鍓嶈鍥惧彲浣跨敤鐨勭粷瀵瑰搴?
                measureWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://濡傛灉widthSize瀵瑰綋鍓嶈鍥惧搴︾殑璁＄畻娌℃湁浠讳綍鍙傝?冩剰涔?
                measureWidth = getCompoundPaddingLeft() + getCompoundPaddingRight();
                break;
            default:
                measureWidth = getCompoundPaddingLeft() + getCompoundPaddingRight();
                break;
        }

        //璁＄畻楂樺害
        int measureHeight;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST://濡傛灉heightSize鏄綋鍓嶈鍥惧彲浣跨敤鐨勬渶澶у搴?
                measureHeight = (frameDrawable != null ? frameDrawable.getIntrinsicHeight() : 0) + getCompoundPaddingTop() + getCompoundPaddingBottom();
                break;
            case MeasureSpec.EXACTLY://濡傛灉heightSize鏄綋鍓嶈鍥惧彲浣跨敤鐨勭粷瀵瑰搴?
                measureHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://濡傛灉heightSize瀵瑰綋鍓嶈鍥惧搴︾殑璁＄畻娌℃湁浠讳綍鍙傝?冩剰涔?
                measureHeight = (frameDrawable != null ? frameDrawable.getIntrinsicHeight() : 0) + getCompoundPaddingTop() + getCompoundPaddingBottom();
                break;
            default:
                measureHeight = (frameDrawable != null ? frameDrawable.getIntrinsicHeight() : 0) + getCompoundPaddingTop() + getCompoundPaddingBottom();
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measureWidth < getMeasuredWidth()) {
            measureWidth = getMeasuredWidth();
        }

        if (measureHeight < getMeasuredHeight()) {
            measureHeight = getMeasuredHeight();
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Drawable[] drawables = getCompoundDrawables();
        int drawableRightWidth = 0;
        int drawableTopHeight = 0;
        int drawableBottomHeight = 0;
        if (drawables != null) {
            if (drawables.length > 1 && drawables[1] != null) {
                drawableTopHeight = drawables[1].getIntrinsicHeight() + getCompoundDrawablePadding();
            }
            if (drawables.length > 2 && drawables[2] != null) {
                drawableRightWidth = drawables[2].getIntrinsicWidth() + getCompoundDrawablePadding();
            }
            if (drawables.length > 3 && drawables[3] != null) {
                drawableBottomHeight = drawables[3].getIntrinsicHeight() + getCompoundDrawablePadding();
            }
        }

        buttonLeft = (getWidth() - (frameDrawable != null ? frameDrawable.getIntrinsicWidth() : 0) - getPaddingRight() - drawableRightWidth);
        buttonTop = (getHeight() - (frameDrawable != null ? frameDrawable.getIntrinsicHeight() : 0) + drawableTopHeight - drawableBottomHeight) / 2;
        buttonRectF.set(buttonLeft, buttonTop, buttonLeft + (frameDrawable != null ? frameDrawable.getIntrinsicWidth() : 0), buttonTop + (frameDrawable != null ? frameDrawable.getIntrinsicHeight() : 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 淇濆瓨鍥惧眰骞跺叏浣撳亸绉伙紝璁﹑addingTop鍜宲addingLeft鐢熸晥
        canvas.save();
        canvas.translate(buttonLeft, buttonTop);

        // 缁樺埗鐘舵?佸眰
        if (stateDrawable != null && stateMaskDrawable != null) {
            Bitmap stateBitmap = getBitmapFromDrawable(stateDrawable);
            if (stateMaskDrawable != null && stateBitmap != null && !stateBitmap.isRecycled()) {
                // 淇濆瓨骞跺垱寤轰竴涓柊鐨勯?忔槑灞傦紝濡傛灉涓嶈繖鏍峰仛鐨勮瘽锛岀敾鍑烘潵鐨勮儗鏅細鏄粦鐨?
                int src = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
                // 缁樺埗閬僵灞?
                stateMaskDrawable.draw(canvas);
                // 缁樺埗鐘舵?佸浘鐗囨寜骞跺簲鐢ㄩ伄缃╂晥鏋?
                paint.setXfermode(porterDuffXfermode);
                canvas.drawBitmap(stateBitmap, tempSlideX, 0, paint);
                paint.setXfermode(null);
                // 铻嶅悎鍥惧眰
                canvas.restoreToCount(src);
            }
        }

        // 缁樺埗妗嗘灦灞?
        if (frameDrawable != null) {
            frameDrawable.draw(canvas);
        }

        // 缁樺埗婊戝潡灞?
        if (sliderDrawable != null) {
            Bitmap sliderBitmap = getBitmapFromDrawable(sliderDrawable);
            if (sliderBitmap != null && !sliderBitmap.isRecycled()) {
                canvas.drawBitmap(sliderBitmap, tempSlideX, 0, paint);
            }
        }

        // 铻嶅悎鍥惧眰
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                // 濡傛灉鎸夐挳褰撳墠鍙敤骞朵笖鎸変笅浣嶇疆姝ｅソ鍦ㄦ寜閽箣鍐?
                if (isEnabled() && buttonRectF.contains(event.getX(), event.getY())) {
                    touchMode = TOUCH_MODE_DOWN;
                    tempTotalSlideDistance = 0; // 娓呯┖鎬绘粦鍔ㄨ窛绂?
                    touchX = event.getX();  // 璁板綍X杞村潗鏍?
                    setClickable(false);    // 褰撶敤鎴疯Е鎽稿湪鎸夐挳浣嶇疆鐨勬椂鍊欑鐢ㄧ偣鍑绘晥鏋滐紝杩欐牱鍋氱殑鐩殑鏄负浜嗕笉璁╄儗鏅湁鎸変笅鏁堟灉
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                switch (touchMode) {
                    case TOUCH_MODE_IDLE: {
                        break;
                    }
                    case TOUCH_MODE_DOWN: {
                        final float x = event.getX();
                        if (Math.abs(x - touchX) > touchSlop) {
                            touchMode = TOUCH_MODE_DRAGGING;
                            // 绂佸?肩埗View鎷︽埅瑙︽懜浜嬩欢
                            // 濡傛灉涓嶅姞杩欐浠ｇ爜鐨勮瘽锛屽綋琚玈crollView鍖呮嫭鐨勬椂鍊欙紝浣犱細鍙戠幇锛屽綋浣犲湪姝ゆ寜閽笂鎸変笅锛?
                            // 绱ф帴鐫?婊戝姩鐨勬椂鍊橲crollView浼氳窡鐫?婊戝姩锛岀劧鍚庢寜閽殑浜嬩欢灏变涪澶变簡锛岃繖浼氶?犳垚寰堥毦瀹屾垚婊戝姩鎿嶄綔
                            // 杩欐牱涓?鏉ョ敤鎴蜂細鎶撶媯鐨勶紝鍔犱笂杩欏彞璇濆憿ScrollView灏变笉浼氭粴鍔ㄤ簡
                            if (getParent() != null) {
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            touchX = x;
                            return true;
                        }
                        break;
                    }
                    case TOUCH_MODE_DRAGGING: {
                        float newTouchX = event.getX();
                        tempTotalSlideDistance += setSlideX(tempSlideX + ((int) (newTouchX - touchX)));    // 鏇存柊X杞村潗鏍囧苟璁板綍鎬绘粦鍔ㄨ窛绂?
                        touchX = newTouchX;
                        invalidate();
                        return true;
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                setClickable(true);

                //缁撳熬婊戝姩鎿嶄綔
                if (touchMode == TOUCH_MODE_DRAGGING) {// 杩欐槸婊戝姩鎿嶄綔
                    touchMode = TOUCH_MODE_IDLE;
                    // 濡傛灉婊戝姩璺濈澶т簬绛変簬鏈?灏忓垏鎹㈣窛绂诲氨鍒囨崲鐘舵?侊紝鍚﹀垯鍥炴粴
                    if (Math.abs(tempTotalSlideDistance) >= Math.abs(frameDrawable.getIntrinsicWidth() * minChangeDistanceScale)) {
                        toggle();   //鍒囨崲鐘舵??
                    } else {
                        switchScroller.startScroll(isChecked());
                    }
                } else if (touchMode == TOUCH_MODE_DOWN) { // 杩欐槸鎸夊湪鎸夐挳涓婄殑鍗曞嚮鎿嶄綔
                    touchMode = TOUCH_MODE_IDLE;
                    toggle();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE: {
                setClickable(true);
                if (touchMode == TOUCH_MODE_DRAGGING) {
                    touchMode = TOUCH_MODE_IDLE;
                    switchScroller.startScroll(isChecked()); //鍥炴粴
                } else {
                    touchMode = TOUCH_MODE_IDLE;
                }
                break;
            }
        }

        super.onTouchEvent(event);
        return isEnabled();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        if (frameDrawable != null) frameDrawable.setState(drawableState);  //鏇存柊妗嗘灦鍥剧墖鐨勭姸鎬?
        if (stateDrawable != null) stateDrawable.setState(drawableState); //鏇存柊鐘舵?佸浘鐗囩殑鐘舵??
        if (stateMaskDrawable != null)
            stateMaskDrawable.setState(drawableState); //鏇存柊鐘舵?侀伄缃╁浘鐗囩殑鐘舵??
        if (sliderDrawable != null) sliderDrawable.setState(drawableState); //鏇存柊婊戝潡鍥剧墖鐨勭姸鎬?
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == frameDrawable || who == stateDrawable || who == stateMaskDrawable || who == sliderDrawable;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void jumpDrawablesToCurrentState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.jumpDrawablesToCurrentState();
            if (frameDrawable != null) frameDrawable.jumpToCurrentState();
            if (stateDrawable != null) stateDrawable.jumpToCurrentState();
            if (stateMaskDrawable != null) stateMaskDrawable.jumpToCurrentState();
            if (sliderDrawable != null) sliderDrawable.jumpToCurrentState();
        }
    }

    @Override
    public void setChecked(boolean checked) {
        boolean changed = checked != isChecked();
        super.setChecked(checked);
        if (changed) {
            if (getWidth() > 0 && switchScroller != null) {   //濡傛灉宸茬粡缁樺埗瀹屾垚
                switchScroller.startScroll(checked);
            } else {
                setSlideX(isChecked() ? tempMinSlideX : tempMaxSlideX);  //鐩存帴淇敼X杞村潗鏍囷紝鍥犱负灏氭湭缁樺埗瀹屾垚鐨勬椂鍊欙紝鍔ㄧ敾鎵ц鏁堟灉涓嶇悊鎯筹紝鎵?浠ョ洿鎺ヤ慨鏀瑰潗鏍囷紝鑰屼笉鎵ц鍔ㄧ敾
            }
        }
    }

    @Override
    public int getCompoundPaddingRight() {
        //閲嶅啓姝ゆ柟娉曞疄鐜拌鏂囨湰鎻愬墠鎹㈣锛岄伩鍏嶅綋鏂囨湰杩囬暱鏃惰鎸夐挳缁欑洊浣?
        int padding = super.getCompoundPaddingRight() + (frameDrawable != null ? frameDrawable.getIntrinsicWidth() : 0);
        if (!TextUtils.isEmpty(getText())) {
            padding += withTextInterval;
        }
        return padding;
    }

    /**
     * 璁剧疆鍥剧墖
     *
     * @param frameBitmap       妗嗘灦鍥剧墖
     * @param stateDrawable     鐘舵?佸浘鐗?
     * @param stateMaskDrawable 鐘舵?侀伄缃╁浘鐗?
     * @param sliderDrawable    婊戝潡鍥剧墖
     */
    public void setDrawables(Drawable frameBitmap, Drawable stateDrawable, Drawable stateMaskDrawable, Drawable sliderDrawable) {
        if (frameBitmap == null || stateDrawable == null || stateMaskDrawable == null || sliderDrawable == null) {
            throw new IllegalArgumentException("ALL NULL");
        }

        this.frameDrawable = frameBitmap;
        this.stateDrawable = stateDrawable;
        this.stateMaskDrawable = stateMaskDrawable;
        this.sliderDrawable = sliderDrawable;

        float density = getResources().getDisplayMetrics().density;
        int width = (int) (60 * density);
        int height = (int) (20 * density);

        this.frameDrawable.setBounds(0, 0, this.frameDrawable.getIntrinsicWidth(), this.frameDrawable.getIntrinsicHeight());
//        this.frameDrawable.setBounds(0, 0, width, height);
        this.frameDrawable.setCallback(this);
        this.stateDrawable.setBounds(0, 0, this.stateDrawable.getIntrinsicWidth(), this.stateDrawable.getIntrinsicHeight());
//        this.stateDrawable.setBounds(0, 0, width, height);
        this.stateDrawable.setCallback(this);
        this.stateMaskDrawable.setBounds(0, 0, this.stateMaskDrawable.getIntrinsicWidth(), this.stateMaskDrawable.getIntrinsicHeight());
//        this.stateMaskDrawable.setBounds(0, 0, width, height);
        this.stateMaskDrawable.setCallback(this);
        this.sliderDrawable.setBounds(0, 0, this.sliderDrawable.getIntrinsicWidth(), this.sliderDrawable.getIntrinsicHeight());
//        this.sliderDrawable.setBounds(0, 0, width, height);
        this.sliderDrawable.setCallback(this);

        this.tempMinSlideX = (-1 * (stateDrawable.getIntrinsicWidth() - frameBitmap.getIntrinsicWidth()));  //鍒濆鍖朮杞存渶灏忓??
//        this.tempMinSlideX = (-1 * (width - frameBitmap.getIntrinsicWidth()));  //鍒濆鍖朮杞存渶灏忓??
        setSlideX(isChecked() ? tempMinSlideX : tempMaxSlideX);  //鏍规嵁閫変腑鐘舵?佸垵濮嬪寲榛樿鍧愭爣

        requestLayout();
    }

    /**
     * 璁剧疆鍥剧墖
     *
     * @param frameDrawableResId     妗嗘灦鍥剧墖ID
     * @param stateDrawableResId     鐘舵?佸浘鐗嘔D
     * @param stateMaskDrawableResId 鐘舵?侀伄缃╁浘鐗嘔D
     * @param sliderDrawableResId    婊戝潡鍥剧墖ID
     */
    public void setDrawableResIds(int frameDrawableResId, int stateDrawableResId, int stateMaskDrawableResId, int sliderDrawableResId) {
        if (getResources() != null) {
            setDrawables(
                    getResources().getDrawable(frameDrawableResId),
                    getResources().getDrawable(stateDrawableResId),
                    getResources().getDrawable(stateMaskDrawableResId),
                    getResources().getDrawable(sliderDrawableResId)
            );
        }
    }

    /**
     * 璁剧疆鍔ㄧ敾鎸佺画鏃堕棿
     *
     * @param duration 鍔ㄧ敾鎸佺画鏃堕棿
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 璁剧疆鏈夋晥璺濈姣斾緥
     *
     * @param minChangeDistanceScale 鏈夋晥璺濈姣斾緥锛屼緥濡傛寜閽搴︿负100锛屾瘮渚嬩负0.3锛岄偅涔堝彧鏈夊綋婊戝姩璺濈澶т簬绛変簬(100*0.3)鎵嶄細鍒囨崲鐘舵?侊紝鍚﹀垯灏卞洖婊?
     */
    public void setMinChangeDistanceScale(float minChangeDistanceScale) {
        this.minChangeDistanceScale = minChangeDistanceScale;
    }

    /**
     * 璁剧疆鎸夐挳鍜屾枃鏈箣闂寸殑闂磋窛
     *
     * @param withTextInterval 鎸夐挳鍜屾枃鏈箣闂寸殑闂磋窛锛屽綋鏈夋枃鏈殑鏃跺?欐鍙傛暟鎵嶈兘娲句笂鐢ㄥ満
     */
    public void setWithTextInterval(int withTextInterval) {
        this.withTextInterval = withTextInterval;
        requestLayout();
    }

    /**
     * 璁剧疆X杞村潗鏍?
     *
     * @param newSlideX 鏂扮殑X杞村潗鏍?
     * @return Xz杞村潗鏍囧鍔犵殑鍊硷紝渚嬪newSlideX绛変簬100锛屾棫鐨刋杞村潗鏍囦负49锛岄偅涔堣繑鍥炲?煎氨鏄?51
     */
    private int setSlideX(int newSlideX) {
        //闃叉婊戝姩瓒呭嚭鑼冨洿
        if (newSlideX < tempMinSlideX) newSlideX = tempMinSlideX;
        if (newSlideX > tempMaxSlideX) newSlideX = tempMaxSlideX;
        //璁＄畻鏈璺濈澧為噺
        int addDistance = newSlideX - tempSlideX;
        this.tempSlideX = newSlideX;
        return addDistance;
    }

    private static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof DrawableContainer) {
            return getBitmapFromDrawable(drawable.getCurrent());
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }
    }

    /**
     * 鍒囨崲婊氬姩鍣紝鐢ㄤ簬瀹炵幇婊氬姩鍔ㄧ敾
     */
    private class SwitchScroller implements Runnable {
        private Scroller scroller;

        public SwitchScroller(Context context, android.view.animation.Interpolator interpolator) {
            this.scroller = new Scroller(context, interpolator);
        }

        /**
         * 寮?濮嬫粴鍔?
         *
         * @param checked 鏄惁閫変腑
         */
        public void startScroll(boolean checked) {
            scroller.startScroll(tempSlideX, 0, (checked ? tempMinSlideX : tempMaxSlideX) - tempSlideX, 0, duration);
            post(this);
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                setSlideX(scroller.getCurrX());
                invalidate();
                post(this);
            }
        }
    }
}

