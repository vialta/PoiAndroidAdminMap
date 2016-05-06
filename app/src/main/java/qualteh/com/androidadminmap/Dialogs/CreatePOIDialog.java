package qualteh.com.androidadminmap.Dialogs;

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

import java.util.Scanner;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 03.05.2016.
 */
public class CreatePOIDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private String address;
    private String number="";
    private EditText nameEditText;
    private EditText numberEditText;
    private EditText stairwayEditText;
    private Switch isPoiSwitch;
    private CreatePOIDialogListener createPoiListener;

    public Switch getIsPoiSwitch () {
        return isPoiSwitch;
    }

    public void setIsPoiSwitch ( Switch isPoiSwitch ) {
        this.isPoiSwitch = isPoiSwitch;
    }

    public EditText getStairwayEditText () {
        return stairwayEditText;
    }

    public void setStairwayEditText ( EditText stairwayEditText ) {
        this.stairwayEditText = stairwayEditText;
    }

    public interface CreatePOIDialogListener{
        void onCreate();
    }

    public void setCreatePOIDialogListener(CreatePOIDialogListener listener){
        createPoiListener = listener;
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ){
            case R.id.button_create_poi:
                address = String.valueOf( nameEditText.getText() );
                number = String.valueOf( numberEditText.getText() );
                Log.d("Click","Click Button");
                createPoiListener.onCreate();
                this.dismiss();
               break;
            default:
                break;
        }
    }

    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.poi_dialog_create, container );    }

    @Override
    public void onViewCreated ( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        nameEditText = (EditText) view.findViewById( R.id.edit_text_dialog_create_name );
        numberEditText = (EditText) view.findViewById( R.id.edit_text_dialog_create_number );
        nameEditText.setText( address );
        numberEditText.setText( number );
        isPoiSwitch = ( Switch ) view.findViewById( R.id.switch_create_is_poi );
        stairwayEditText = (EditText) view.findViewById( R.id.edit_text_dialog_create_stairway );
        Button createButton = (Button) view.findViewById( R.id.button_create_poi );
        createButton.setOnClickListener( this );
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setAddressFromGeocoder(String address){
        String[] addressWords = address.split( " " );
        if(isInteger( addressWords[addressWords.length-1] )){
            number=addressWords[addressWords.length-1];
            addressWords[addressWords.length-1]="";
        }
        this.address="";
        for( int i=0; i<addressWords.length-1; i++){
            this.address += addressWords[i]+" ";
        }
        this.address+=addressWords[addressWords.length-1];
    }

    boolean isInteger(String s){
        return isInteger( s,10 );
    }

    boolean isInteger(String s, int radix){
        if(s.isEmpty()){
            return false;
        }
        for(int i=0;i<s.length();i++){
            if(i==0 && s.charAt( i ) == '-'){
                if(s.length() == 1){
                    return false;
                }
                else{
                    continue;
                }
            }
            if(Character.digit( s.charAt( i ), radix) < 0 ){
                return false;
            }
        }
        return true;
    }

}
