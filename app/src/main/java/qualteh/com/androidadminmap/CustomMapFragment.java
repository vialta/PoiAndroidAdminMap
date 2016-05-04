package qualteh.com.androidadminmap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class CustomMapFragment extends SupportMapFragment{

    private View mOriginalView;
    private CustomFrameLayout mTouchView;

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        mOriginalView = super.onCreateView( inflater, container, savedInstanceState );

        mTouchView = new CustomFrameLayout( getActivity() );
        mTouchView.addView( mOriginalView );

        return mTouchView;
    }

    public View getView(){
        return mOriginalView;
    }



}