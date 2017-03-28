package github.ishaan.buttonprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ishaan on 13/3/17.
 */

public class ButtonProgressBar extends View {

    private Paint mTextPaint, mBackgroundPaint, mProgressPaint;
    private String mInitialText = "DOWNLOAD";
    private int mTextSize;
    private int mTextColor, mBackgroundColor, mProgressColor;
    private int mCornerRadius = 10;
    private Bitmap mTickBitmap;
    public int indeterminateState = -1;
    private final int topX = 0, topY = 0, MIN = 0, MAX = 100;
    private float mProgress = MIN;
    private Type mLoaderType = Type.DETERMINATE;
    private int mProgressIncrementFactor = 2;
    private int mCounterFactor = 0, mMaxCounterFactor = INDETERMINATE_ANIMATION_VALUES.length - 1;
    private final static int STATE_RESET = -1, STATE_START = 1, STATE_STOP = 0, API_LEVEL_LOLLIPOP = 21;
    private static final int DEFAULT_BGCOLOR = Color.parseColor("#0E8DD4"),
            DEFAULT_PROGCOLOR = Color.parseColor("#0399E5");

    public ButtonProgressBar(Context context) {
        super(context);
    }

    public ButtonProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Initialize view params to user values if attrs != null
     * else provide default values.
     * @param context - view context
     * @param attrs - attribute set provided by user in layout xml file
     */
    public void init(Context context, AttributeSet attrs) {
        mTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_default);
        TypedArray attributeArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ButtonProgressBar, 0, 0);
        mTickBitmap = getBitmapFromVectorDrawable(context, R.drawable.ic_done);
        try {
            mBackgroundColor = attributeArray.getColor(R.styleable.ButtonProgressBar_bgColor, DEFAULT_BGCOLOR);
            mProgressColor = attributeArray.getColor(R.styleable.ButtonProgressBar_progColor, DEFAULT_PROGCOLOR);
            mTextColor = attributeArray.getColor(R.styleable.ButtonProgressBar_textColor, Color.WHITE);
            mTextSize = attributeArray.getDimensionPixelSize(R.styleable.ButtonProgressBar_textSize, mTextSize);
            if (attributeArray.getString(R.styleable.ButtonProgressBar_text) != null)
                mInitialText = attributeArray.getString(R.styleable.ButtonProgressBar_text);
            int type = attributeArray.getInt(R.styleable.ButtonProgressBar_type, 0);
            switch (type) {
                case 0:
                    mLoaderType = Type.DETERMINATE;
                    break;
                case 1:
                    mLoaderType = Type.INDETERMINATE;
                    break;
            }
        } finally {
            attributeArray.recycle();
        }
        backgroundPaint();
        progressPaint();
        textPaint();
    }

    public void backgroundPaint() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void progressPaint() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void textPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setBackgroundColor(int bgColor) {
        mBackgroundColor = bgColor;
        mBackgroundPaint.setColor(bgColor);
        invalidate();
    }

    public void setProgressColor(int progColor) {
        mProgressColor = progColor;
        mProgressPaint.setColor(progColor);
        invalidate();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(textColor);
        invalidate();
    }

    public void setTextSize(int size) {
        mTextSize = size;
        mTextPaint.setTextSize(size);
        invalidate();
    }

    /**
     * Get mTickBitmap image from vector drawable defined in xml
     * @param context - context to load drawable from resources
     * @param drawableId - drawable id of the vector drawable
     * @return - mTickBitmap image
     */
    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < API_LEVEL_LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(topX, topY, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height, width;
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = specHeight;
                break;
            default:
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                height = getResources().getDimensionPixelSize(R.dimen.default_height);
                break;
        }

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = specWidth;
                break;
            default:
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                width = getResources().getDimensionPixelSize(R.dimen.default_width);
                break;
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Set Loader type to determinate or indeterminate
     * @param mLoaderType
     */
    public void setLoaderType(Type mLoaderType) {
        this.mLoaderType = mLoaderType;
    }

    public Type getLoaderType() {
        return this.mLoaderType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLoaderType == Type.DETERMINATE) {
            drawDeterminateProgress(canvas);
        } else {
            drawIndeterminateProgress(canvas);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        switch (indeterminateState) {
            case STATE_RESET:
                onDrawInit(canvas);
                break;
            case STATE_START:
                updateProgress();
                onDrawProgress(canvas);
                invalidate();
                break;
            case STATE_STOP:
                onDrawFinished(canvas);
                break;
        }
    }

    /**
     * Update mProgress value based on static pre-calculated value and incrementFactor
     */
    public void updateProgress() {
        if (mProgressIncrementFactor == 4) {
            mProgressIncrementFactor = 1;
        }
        if (mCounterFactor >= mMaxCounterFactor) {
            mCounterFactor = 1;
        }
        mProgress = INDETERMINATE_ANIMATION_VALUES[mCounterFactor] * MAX;
        if (mProgress <= 1) {
            mProgress = MIN;
        }
        mCounterFactor += mProgressIncrementFactor;
        mProgressIncrementFactor++;
    }

    private void drawDeterminateProgress(Canvas canvas) {
        if (mProgress == MIN) {
            onDrawInit(canvas);
        } else if (mProgress > MIN && mProgress < MAX) {
            onDrawProgress(canvas);
        } else {
            onDrawFinished(canvas);
        }
    }

    /**
     * draw initial state of mProgress view when view is either created or in reset state.
     * @param canvas - view canvas object
     */
    public void onDrawInit(Canvas canvas) {
        RectF bgRectf = new RectF(topX, topY, canvas.getWidth(), canvas.getHeight());
        canvas.drawRoundRect(bgRectf, mCornerRadius, mCornerRadius, mBackgroundPaint);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mInitialText, topX, mInitialText.length(), bounds);
        int xPos = (canvas.getWidth() - bounds.width())/ 2;
        int textAdjust = (int) (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        int yPos = ((canvas.getHeight() / 2) - textAdjust);
        canvas.drawText(mInitialText, xPos, yPos, mTextPaint);
    }

    /**
     * Draw mProgress on view based on mLoaderType.
     * @param canvas - view canvas object
     */
    public void onDrawProgress(Canvas canvas) {
        RectF bgRectf = new RectF(topX, topY, canvas.getWidth(), canvas.getHeight());
        canvas.drawRoundRect(bgRectf, mCornerRadius, mCornerRadius, mBackgroundPaint);
        float progressPoint = (((float) canvas.getWidth()/ MAX) * mProgress);
        RectF progRect = new RectF(topX, topY, progressPoint, canvas.getHeight());
        canvas.drawRoundRect(progRect, mCornerRadius, mCornerRadius, mProgressPaint);
    }

    /**
     * Draw final state of mProgress view when mProgress is completed.
     * @param canvas - view canvas object
     */
    public void onDrawFinished(Canvas canvas) {
        RectF bgRectf = new RectF(topX, topY, canvas.getWidth(), canvas.getHeight());
        canvas.drawRoundRect(bgRectf, mCornerRadius, mCornerRadius, mBackgroundPaint);
        RectF progRect = new RectF(topX, topY, canvas.getWidth(), canvas.getHeight());
        canvas.drawRoundRect(progRect, mCornerRadius, mCornerRadius, mProgressPaint);
        if (mTickBitmap != null) {
            int centerX = (canvas.getWidth() - mTickBitmap.getWidth()) / 2;
            int centerY = (canvas.getHeight() - mTickBitmap.getHeight()) / 2;
            canvas.drawBitmap(mTickBitmap, centerX, centerY, null);
        }
    }

    /**
     * if view.Type == DETERMINATE then set mProgress to argument and invalidate() view
     * @param currentProgress - int mLoaderType mProgress indeterminateState varying between mMinProgress and mMaxProgress.
     */
    public void setProgress(int currentProgress) {
        if (mLoaderType == Type.DETERMINATE) {
            mProgress = currentProgress;
            invalidate();
        }
    }

    /**
     * set indeterminateState to 1 then invalidate() view
     */
    public void startLoader() {
        indeterminateState = STATE_START;
        invalidate();
    }

    /**
     * set indeterminateState to 0 then invalidate() view
     */
    public void stopLoader() {
        indeterminateState = STATE_STOP;
        invalidate();
    }

    /**
     * Reset State back to initialState
     * if view.Type == INDETERMINATE then set indeterminateState to -1
     * else set currentProgress to 0
     * finally invalidate() view
     */
    public void reset() {
        if (mLoaderType == Type.INDETERMINATE) {
            indeterminateState = STATE_RESET;
        }
        mProgress = MIN;
        invalidate();
    }

    /**
     * Enum for loader mLoaderType { determinate or indeterminate }
     */
    public enum Type {
        DETERMINATE,
        INDETERMINATE
    }

    /**
     * Pre-calculated values [0.0000f - 1.0000f]
     * Used from FastOutSlowInInterpolator, array of values to provide smooth indeterminate loading animation
     */
    private static final float[] INDETERMINATE_ANIMATION_VALUES = new float[] {
            0.0000f, 0.0001f, 0.0002f, 0.0005f, 0.0009f, 0.0014f, 0.0020f,
            0.0027f, 0.0036f, 0.0046f, 0.0058f, 0.0071f, 0.0085f, 0.0101f,
            0.0118f, 0.0137f, 0.0158f, 0.0180f, 0.0205f, 0.0231f, 0.0259f,
            0.0289f, 0.0321f, 0.0355f, 0.0391f, 0.0430f, 0.0471f, 0.0514f,
            0.0560f, 0.0608f, 0.0660f, 0.0714f, 0.0771f, 0.0830f, 0.0893f,
            0.0959f, 0.1029f, 0.1101f, 0.1177f, 0.1257f, 0.1339f, 0.1426f,
            0.1516f, 0.1610f, 0.1707f, 0.1808f, 0.1913f, 0.2021f, 0.2133f,
            0.2248f, 0.2366f, 0.2487f, 0.2611f, 0.2738f, 0.2867f, 0.2998f,
            0.3131f, 0.3265f, 0.3400f, 0.3536f, 0.3673f, 0.3810f, 0.3946f,
            0.4082f, 0.4217f, 0.4352f, 0.4485f, 0.4616f, 0.4746f, 0.4874f,
            0.5000f, 0.5124f, 0.5246f, 0.5365f, 0.5482f, 0.5597f, 0.5710f,
            0.5820f, 0.5928f, 0.6033f, 0.6136f, 0.6237f, 0.6335f, 0.6431f,
            0.6525f, 0.6616f, 0.6706f, 0.6793f, 0.6878f, 0.6961f, 0.7043f,
            0.7122f, 0.7199f, 0.7275f, 0.7349f, 0.7421f, 0.7491f, 0.7559f,
            0.7626f, 0.7692f, 0.7756f, 0.7818f, 0.7879f, 0.7938f, 0.7996f,
            0.8053f, 0.8108f, 0.8162f, 0.8215f, 0.8266f, 0.8317f, 0.8366f,
            0.8414f, 0.8461f, 0.8507f, 0.8551f, 0.8595f, 0.8638f, 0.8679f,
            0.8720f, 0.8760f, 0.8798f, 0.8836f, 0.8873f, 0.8909f, 0.8945f,
            0.8979f, 0.9013f, 0.9046f, 0.9078f, 0.9109f, 0.9139f, 0.9169f,
            0.9198f, 0.9227f, 0.9254f, 0.9281f, 0.9307f, 0.9333f, 0.9358f,
            0.9382f, 0.9406f, 0.9429f, 0.9452f, 0.9474f, 0.9495f, 0.9516f,
            0.9536f, 0.9556f, 0.9575f, 0.9594f, 0.9612f, 0.9629f, 0.9646f,
            0.9663f, 0.9679f, 0.9695f, 0.9710f, 0.9725f, 0.9739f, 0.9753f,
            0.9766f, 0.9779f, 0.9791f, 0.9803f, 0.9815f, 0.9826f, 0.9837f,
            0.9848f, 0.9858f, 0.9867f, 0.9877f, 0.9885f, 0.9894f, 0.9902f,
            0.9910f, 0.9917f, 0.9924f, 0.9931f, 0.9937f, 0.9944f, 0.9949f,
            0.9955f, 0.9960f, 0.9964f, 0.9969f, 0.9973f, 0.9977f, 0.9980f,
            0.9984f, 0.9986f, 0.9989f, 0.9991f, 0.9993f, 0.9995f, 0.9997f,
            0.9998f, 0.9999f, 0.9999f, 1.0000f, 1.0000f
    };
}
