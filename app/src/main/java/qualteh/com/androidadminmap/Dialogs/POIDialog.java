package qualteh.com.androidadminmap.Dialogs;



import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public class POIDialog extends AppCompatDialogFragment implements View.OnClickListener{

    private String titleString;
    private Fragment fragment;
    private POIDialogListener poiDialogListener;

    public interface POIDialogListener{
        void onEditButtonClicked();
        void onDeleteButtonClicked();
    }

    public void setPOIDialogListener(POIDialogListener listener){
        poiDialogListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.poi_dialog, container );
    }

    @Override
    public void onViewCreated ( View view, @Nullable Bundle savedInstanceState ) {

        TextView titleText = (TextView ) view.findViewById( R.id.poi_dialog_title );
        titleText.setMaxWidth( ( int ) (getResources().getDisplayMetrics().widthPixels * 0.5f) );
        titleText.setText( titleString );

        Button editButton = ( Button ) view.findViewById( R.id.button_edit_poi );
        Button deleteButton = ( Button ) view.findViewById( R.id.button_delete_poi );
        editButton.setOnClickListener( this );
        deleteButton.setOnClickListener( this );

        super.onViewCreated( view, savedInstanceState );
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ){
            case R.id.button_edit_poi:
                poiDialogListener.onEditButtonClicked();
                this.dismiss();
                break;
            case R.id.button_delete_poi:
                poiDialogListener.onDeleteButtonClicked();
                this.dismiss();
                break;
            default:
                break;
        }
    }


    public String getTitleString () {
        return titleString;
    }

    public void setTitleString ( String titleString ) {
        this.titleString = titleString;
    }
}
