package mirko.android.datetimepicker.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import mirko.android.datetimepicker.R;

/**
 * A {@link FrameLayout} that imposes a maximal width on itself and its children. If normal measurement results in a
 * dimension that is too high, then the subviews are remeasured with a height restriction.
 */
public class FrameLayoutWithMaxHeight extends FrameLayout {

    /**
     * Maximal height of the view.
     */
    private int m_maxHeight = Integer.MAX_VALUE;

    /**
     * Default constructor.
     *
     * @param context the context
     */
    public FrameLayoutWithMaxHeight(final Context context) {
        super(context);
    }

    /**
     * Constructor.
     *
     * @param context the context
     * @param attrs   the XML attributes
     */
    public FrameLayoutWithMaxHeight(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor.
     *
     * @param context  the context
     * @param attrs    XML attributes
     * @param defStyle default style.
     */
    public FrameLayoutWithMaxHeight(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameLayoutWithMaxHeight, defStyle, 0);
        final int maxSize = typedArray.getDimensionPixelSize(R.styleable.FrameLayoutWithMaxHeight_maxHeight, -1);
        if (maxSize > 0) {
            setMaxHeight(maxSize);
        }
        typedArray.recycle();
    }

    /**
     * Sets the maximal allowed height of the view.
     *
     * @param maxHeight the maximal height
     */
    public final void setMaxHeight(final int maxHeight) {
        m_maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the established height is to large, then remeasure with AT_MOST
        if (getMeasuredHeight() > m_maxHeight) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(m_maxHeight, MeasureSpec.AT_MOST));
        }
    }

}
