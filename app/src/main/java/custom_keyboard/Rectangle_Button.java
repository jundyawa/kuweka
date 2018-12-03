package custom_keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class Rectangle_Button extends android.support.v7.widget.AppCompatButton {

    public Rectangle_Button(Context context) {
        super(context);
    }

    public Rectangle_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Rectangle_Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size,size/2); // make it rectangle

    }
}
