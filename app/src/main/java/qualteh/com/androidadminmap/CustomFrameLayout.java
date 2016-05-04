package qualteh.com.androidadminmap;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class CustomFrameLayout extends FrameLayout {

    private long lastTouched = 0;
    private float mapX;
    private float mapY;


    public CustomFrameLayout ( Context context ) {
        super(context);
    }


    CatchTouchFrameLayoutListener onCatchTouchFrameLayoutListener;
    public interface CatchTouchFrameLayoutListener{
        void onTouchUp();
        void onTouchDown();
    }

    public CustomFrameLayout ( Context context, AttributeSet attrs ) {
        super( context, attrs );

    }

    public void setOnCatchTouchFrameLayoutListener(CatchTouchFrameLayoutListener listener){
        onCatchTouchFrameLayoutListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent ( MotionEvent ev ) {

        switch ( ev.getAction() ){
            case MotionEvent.ACTION_DOWN:
                mapX=ev.getX();
                mapY=ev.getY();
                onCatchTouchFrameLayoutListener.onTouchDown();
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs( ev.getX() - mapX) >2 ){
                    onCatchTouchFrameLayoutListener.onTouchUp();
                }



                break;
            default:
                break;
        }

        return super.dispatchTouchEvent( ev );
    }
}

