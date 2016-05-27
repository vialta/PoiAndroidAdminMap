package qualteh.com.androidadminmap.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public class EditPOIDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private long markerId;
    private String streetName;
    private String streetNumber;
    private String stairway;
    private EditText nameEditText ;
    private EditText numberEditText ;
    private EditText stairwayEditText ;
    private Spinner typeSpinner;

    private EditPoiDialogListener editPoiDialogListener;

    public void setEditPoiDialogListener ( EditPoiDialogListener editPoiDialogListener ) {
        this.editPoiDialogListener = editPoiDialogListener;
    }

    public long getMarkerId () {
        return markerId;
    }

    public void setMarkerId ( long id ) {
        this.markerId = id;
    }

    public interface EditPoiDialogListener{
        void onSaveEdit();
        void onChangePos(EditPOIDialog editPOIDialog);
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ){
            case R.id.button_edit_poi_save:
                streetName= String.valueOf( nameEditText.getText() );
                streetNumber= String.valueOf( numberEditText.getText() );
                stairway= String.valueOf( stairwayEditText.getText() );

                editPoiDialogListener.onSaveEdit();
                this.dismiss();
                break;
            case R.id.button_edit_poi_change:
                editPoiDialogListener.onChangePos(this);
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
        typeSpinner = (Spinner) view.findViewById( R.id.spinner_is_poi );
        nameEditText.setText( streetName );
        numberEditText.setText( streetNumber );
        stairwayEditText.setText( stairway );

        int minWidth = ( int ) (getResources().getDisplayMetrics().widthPixels * 0.4);
        nameEditText.setMinimumWidth( minWidth );
        numberEditText.setMinimumWidth( minWidth );
        stairwayEditText.setMinimumWidth( minWidth );

        Button changeButton = (Button) view.findViewById( R.id.button_edit_poi_change );
        Button saveButton = (Button ) view.findViewById( R.id.button_edit_poi_save );

        changeButton.setOnClickListener( this );
        saveButton.setOnClickListener( this );

        List<String> spinnerArray = new ArrayList<>(  );
        spinnerArray.add( "POI" );
        spinnerArray.add( "Casa" );
        spinnerArray.add( "Bloc" );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this.getContext(),android.R.layout.simple_spinner_item, spinnerArray );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        typeSpinner.setAdapter( adapter );

        typeSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected ( AdapterView<?> parent, View view, int position, long id ) {
                switch ( typeSpinner.getSelectedItem().toString() ){
                    case "POI":
                        numberEditText.setEnabled( false );
                        stairwayEditText.setEnabled( false );
                        break;
                    case "Casa":
                        numberEditText.setEnabled( true );
                        stairwayEditText.setEnabled( false );
                        break;
                    case "Bloc":
                        numberEditText.setEnabled( true );
                        stairwayEditText.setEnabled( true );
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected ( AdapterView<?> parent ) {

            }
        } );
        typeSpinner.setSelection( 0 );
        if( !numberEditText.getText().toString().equals( "" )){
            typeSpinner.setSelection(1);
        }
        if(!stairwayEditText.getText().toString().equals( "" )){
            typeSpinner.setSelection( 2 );
        }
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

    public Spinner getSpinner() {return typeSpinner;}

}
