package qualteh.com.androidadminmap.Dialogs;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.maps.model.Marker;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public class EditPOIDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private String streetName;
    private String streetNumber;
    private String stairway;
    private boolean isPOI;
    private EditText nameEditText ;
    private EditText numberEditText ;
    private EditText stairwayEditText ;
    private Switch isPoiSwitch ;

    private EditPoiDialogListener editPoiDialogListener;

    public void setEditPoiDialogListener ( EditPoiDialogListener editPoiDialogListener ) {
        this.editPoiDialogListener = editPoiDialogListener;
    }

    public interface EditPoiDialogListener{
        void onSaveEdit();
        void onChangePos();
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ){
            case R.id.button_edit_poi_save:
                streetName= String.valueOf( nameEditText.getText() );
                streetNumber= String.valueOf( numberEditText.getText() );
                stairway= String.valueOf( stairwayEditText.getText() );
                isPOI = isPoiSwitch.isChecked();
                editPoiDialogListener.onSaveEdit();
                this.dismiss();
                break;
            case R.id.button_edit_poi_change:
                editPoiDialogListener.onChangePos();
                this.dismiss();
                break;
            case R.id.button_create_poi:


                this.dismiss();
                break;
            default:
                break;
        }
    }

    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        return inflater.inflate ( R.layout.poi_dialog_edit, container );
    }

    @Override
    public void onViewCreated ( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        nameEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_name );
        numberEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_number );
        stairwayEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_stairway );
        isPoiSwitch = (Switch) view.findViewById( R.id.switch_is_poi );
        nameEditText.setText( streetName );
        numberEditText.setText( streetNumber );
        stairwayEditText.setText( stairway );

        Button changeButton = (Button) view.findViewById( R.id.button_edit_poi_change );
        Button saveButton = (Button ) view.findViewById( R.id.button_edit_poi_save );

        changeButton.setOnClickListener( this );
        saveButton.setOnClickListener( this );

    }

    public String getStreetName () {
        return streetName;
    }

    public void setStreetName ( String streetName ) {
        this.streetName = streetName;
    }

    public String getStreetNumber () {
        return streetNumber;
    }

    public void setStreetNumber ( String streetNumber ) {
        this.streetNumber = streetNumber;
    }

    public String getStairway () {
        return stairway;
    }

    public void setStairway ( String stairway ) {
        this.stairway = stairway;
    }

    public boolean isPOI () {
        return isPOI;
    }

    public void setPOI ( boolean POI ) {
        isPOI = POI;
    }


}
