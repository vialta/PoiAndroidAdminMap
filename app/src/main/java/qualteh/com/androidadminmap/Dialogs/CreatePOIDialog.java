package qualteh.com.androidadminmap.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qualteh.com.androidadminmap.R;

/**
 * Created by Virgil Tanase on 03.05.2016.
 */
public class CreatePOIDialog extends AppCompatDialogFragment implements View.OnClickListener {
    @Override
    public void onClick ( View v ) {

    }

    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.poi_dialog_create, container );    }

}
