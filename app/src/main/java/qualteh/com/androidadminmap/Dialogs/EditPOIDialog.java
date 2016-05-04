package qualteh.com.androidadminmap.Dialogs;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public class EditPOIDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private String streetName;
    private String streetNumber;
    private String stairway;
    private boolean isPOI;

    @Override
    public void onClick ( View v ) {

    }

    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        return inflater.inflate ( R.layout.poi_dialog_edit, container );
    }

    @Override
    public void onViewCreated ( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        EditText nameEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_name );
        EditText numberEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_number );
        EditText stairwayEditText = ( EditText ) view.findViewById( R.id.edit_text_dialog_edit_stairway );
        Switch isPoiSwitch = (Switch) view.findViewById( R.id.switch_is_poi );
        nameEditText.setText( streetName );
        numberEditText.setText( streetNumber );
        stairwayEditText.setText( stairway );
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
