package qualteh.com.androidadminmap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import qualteh.com.androidadminmap.API.IAdminMap;
import qualteh.com.androidadminmap.Dialogs.CreatePOIDialog;
import qualteh.com.androidadminmap.Dialogs.EditPOIDialog;
import qualteh.com.androidadminmap.Dialogs.POIDialog;
import qualteh.com.androidadminmap.Model.Address;
import qualteh.com.androidadminmap.Model.AdminMapModel;
import qualteh.com.androidadminmap.Model.JsonModel;
import qualteh.com.androidadminmap.Model.Poi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class POIActivity extends FragmentActivity implements OnMapReadyCallback, TouchWrapperCallback{


    private static int TEMP_ID = 3005;

    private Activity thisActivity;
    private EditText editTextSearch;
    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geoAutoComplete;
    private ImageView geoAutoCompleteClear;
    private CustomFrameLayout customFrameLayout;
    private AdminMapModel mAdminMapModel;
    private GoogleMap mMap;
    private ArrayList<Marker> addressMarkerList;
    private ArrayList<Marker> poiMarkerList;
    LocationManager locationManager;
    LatLng myLocation;
    String provider;
    private Marker editingMarker;
    private Button changePositionButton;
    private GeoSearchResult mSearchResult;

    public static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION;
    public static int MY_PERMISSIONS_REQUEST_FINE_LOCATION;
    private boolean isChangingPosition;
    private EditPOIDialog tmpEditPOIDialog;


    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_poi );

        thisActivity = this;

        geoAutoCompleteClear = (ImageView) findViewById(R.id.geo_autocomplete_clear);

        geoAutoComplete = (DelayAutoCompleteTextView) findViewById(R.id.edit_text_location_search);
        geoAutoComplete.setThreshold(THRESHOLD);
        geoAutoComplete.setAdapter(new GeoAutoCompleteAdapter(this)); // 'this' is Activity instance

        geoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mSearchResult = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geoAutoComplete.setText(mSearchResult.getAddress());
            }
        });

        geoAutoComplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0)
                {
                    geoAutoCompleteClear.setVisibility(View.VISIBLE);
                }
                else
                {
                    geoAutoCompleteClear.setVisibility(View.GONE);
                }
            }
        });

        geoAutoCompleteClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                geoAutoComplete.setText("");
            }
        });

        addressMarkerList = new ArrayList<>(  );
        poiMarkerList = new ArrayList<>(  );

        customFrameLayout = (CustomFrameLayout ) findViewById( R.id.map );
        customFrameLayout.setOnCatchTouchFrameLayoutListener( new CustomFrameLayout.CatchTouchFrameLayoutListener() {
            @Override
            public void onTouchUp () {
                if(!isChangingPosition) {
                    serverCall( mMap.getCameraPosition().target );
                }
            }

            @Override
            public void onTouchDown () {
            }
        } );

        changePositionButton = ( Button ) findViewById( R.id.button_save_poi_position );
        changePositionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                editingMarker.setPosition( mMap.getCameraPosition().target );
                changePositionButton.setVisibility( View.INVISIBLE );
                isChangingPosition=false;
                FragmentManager fm = getSupportFragmentManager();
                tmpEditPOIDialog.show( fm, "" );
            }
        } );

        Button searchButton = (Button) findViewById( R.id.button_map_search );
        searchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                String address = geoAutoComplete.getText().toString();
                Geocoder geoCoder = new Geocoder( getApplicationContext(), Locale.getDefault() );
                if(!address.equals( "" )){
                    try
                    {
                        List<android.location.Address> addresses = geoCoder.getFromLocationName(address, 5);
                        if (addresses.size() > 0)
                        {
                            Double lat = (double) (addresses.get(0).getLatitude());
                            Double lon = (double) (addresses.get(0).getLongitude());

                            Log.d("lat-long", "" + lat + "......." + lon);
                            final LatLng searchedPoint = new LatLng(lat, lon);
                            // Move the camera instantly to hamburg with a zoom of 15.

                            mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( searchedPoint, 17f ) );

                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } );

        ImageView centralImage = ( ImageView ) findViewById( R.id.centerMarker );
        centralImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                FragmentManager fm = getSupportFragmentManager();
                final CreatePOIDialog createPOIDialog = new CreatePOIDialog();
                Geocoder geocoder = new Geocoder( getApplicationContext(), Locale.getDefault() );
                try {
                    List<android.location.Address> addressList = geocoder.getFromLocation( mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude,1 );
                    if(addressList!=null && addressList.size()>0){
                        createPOIDialog.setAddressFromGeocoder( addressList.get( 0 ).getAddressLine( 0 ) );
                    }
                } catch ( IOException e ) {
                    e.printStackTrace();
                }

                createPOIDialog.setCreatePOIDialogListener( new CreatePOIDialog.CreatePOIDialogListener() {
                    @Override
                    public void onCreate () {

                        AlertDialog newAlertDialog = new AlertDialog.Builder( thisActivity )
                                .setTitle( "Salveaza" )
                                .setMessage( "Doriti sa creati adresa?" )
                                .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick ( DialogInterface dialog, int which ) {
                                        Marker tmpMarker;
                                        Log.d("Test",createPOIDialog.getTypeSpinner().getSelectedItem().toString());
                                        switch ( createPOIDialog.getTypeSpinner().getSelectedItem().toString()){
                                            case "POI":
                                                tmpMarker =  mMap.addMarker( new MarkerOptions().icon( BitmapDescriptorFactory.fromResource( R.drawable.map_circle_blue50 ) ).position(new LatLng( mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude ) ).title( createPOIDialog.getAddress() ) );
                                                poiMarkerList.add( tmpMarker );
                                                Poi newPoi = new Poi();
                                                newPoi.setLat( mMap.getCameraPosition().target.latitude );
                                                newPoi.setLng( mMap.getCameraPosition().target.longitude );
                                                newPoi.setDist( 0.0 );
                                                newPoi.setName( createPOIDialog.getAddress() );
                                                mAdminMapModel.getData().getPois().add( newPoi );

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                        .addConverterFactory( GsonConverterFactory.create() )
                                                        .build();
                                                IAdminMap api = retrofit.create( IAdminMap.class );
                                                JsonModel jsonModel = new JsonModel(0, "poi", "Timişoara", "Timişoara", "Timiş", tmpMarker.getPosition().latitude, tmpMarker.getPosition().longitude, newPoi.getName(), 0, "", 0, "", "", ""   );
                                                Call<JsonModel> createPoiCall = api.createMarker( jsonModel);
                                                createPoiCall.enqueue( new Callback<JsonModel>() {
                                                    @Override
                                                    public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                        Log.d("Response",response.message()+" "+call.toString());
                                                    }

                                                    @Override
                                                    public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                        Log.d("Failure",call.toString()+" "+t.getMessage());
                                                    }
                                                } );

                                                //TODO REFRESH MAP
                                                serverCall( mMap.getCameraPosition().target );
                                                break;
                                            case "Bloc":
                                                tmpMarker = mMap.addMarker( new MarkerOptions().icon( BitmapDescriptorFactory.fromResource( R.drawable.poi_50 ) ).position(new LatLng( mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude ) ).title( createPOIDialog.getAddress()+" "+createPOIDialog.getNumber() ) );
                                                addressMarkerList.add( tmpMarker );

                                                Address newAddress = new Address();
                                                newAddress.setLat( mMap.getCameraPosition().target.latitude );
                                                newAddress.setLng( mMap.getCameraPosition().target.longitude );
                                                newAddress.setDist( 0.0 );
                                                newAddress.setName( createPOIDialog.getAddress() );
                                                newAddress.setNo( createPOIDialog.getNumber() );
                                                newAddress.setSc( String.valueOf( createPOIDialog.getStairwayEditText().getText() ) );
                                                mAdminMapModel.getData().getAddresses().add( newAddress );

                                                //TODO ADDRESS CREATE CALL

                                                retrofit = new Retrofit.Builder()
                                                        .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                        .addConverterFactory( GsonConverterFactory.create() )
                                                        .build();
                                                api = retrofit.create( IAdminMap.class );
                                                jsonModel = new JsonModel(0, "scara", "Timişoara", "Timişoara", "Timiş", tmpMarker.getPosition().latitude, tmpMarker.getPosition().longitude, newAddress.getName(), 0, "", 0, newAddress.getNo(), newAddress.getNo(), ""   );
                                                createPoiCall = api.createMarker( jsonModel);
                                                createPoiCall.enqueue( new Callback<JsonModel>() {
                                                    @Override
                                                    public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                        Log.d("Response",response.message()+" "+call.toString());
                                                    }

                                                    @Override
                                                    public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                        Log.d("Failure",call.toString()+" "+t.getMessage());
                                                    }
                                                } );

                                                //TODO REFRESH MAP
                                                serverCall( mMap.getCameraPosition().target );
                                                break;
                                            case "Casa":
                                                tmpMarker = mMap.addMarker( new MarkerOptions().icon( BitmapDescriptorFactory.fromResource( R.drawable.poi_50 ) ).position(new LatLng( mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude ) ).title( createPOIDialog.getAddress()+" "+createPOIDialog.getNumber() ) );
                                                addressMarkerList.add( tmpMarker );

                                                newAddress = new Address();
                                                newAddress.setLat( mMap.getCameraPosition().target.latitude );
                                                newAddress.setLng( mMap.getCameraPosition().target.longitude );
                                                newAddress.setDist( 0.0 );
                                                newAddress.setName( createPOIDialog.getAddress() );
                                                newAddress.setNo( createPOIDialog.getNumber() );
                                                newAddress.setSc( String.valueOf( createPOIDialog.getStairwayEditText().getText() ) );
                                                mAdminMapModel.getData().getAddresses().add( newAddress );

                                                //TODO ADDRESS CREATE CALL

                                                retrofit = new Retrofit.Builder()
                                                        .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                        .addConverterFactory( GsonConverterFactory.create() )
                                                        .build();
                                                api = retrofit.create( IAdminMap.class );
                                                jsonModel = new JsonModel(0, "casa", "Timişoara", "Timişoara", "Timiş", tmpMarker.getPosition().latitude, tmpMarker.getPosition().longitude, newAddress.getName(), 0, "", 0, newAddress.getNo(), newAddress.getNo(), newAddress.getSc()   );
                                                createPoiCall = api.createMarker( jsonModel);
                                                createPoiCall.enqueue( new Callback<JsonModel>() {
                                                    @Override
                                                    public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                        Log.d("Response",response.message()+" "+call.toString());
                                                    }

                                                    @Override
                                                    public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                        Log.d("Failure",call.toString()+" "+t.getMessage());
                                                    }
                                                } );

                                                // TODO REFRESH MAP
                                                serverCall( mMap.getCameraPosition().target );
                                                break;
                                            default:
                                                break;
                                        }
                                        dialog.dismiss();
                                    }
                                } )
                                .setNegativeButton( "Nu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick ( DialogInterface dialog, int which ) {
                                        dialog.dismiss();
                                    }
                                } )
                                .create();
                        newAlertDialog.show();
                    }
                } );
                createPOIDialog.show( fm,"" );
            }
        } );

        CustomMapFragment customMapFragment = (CustomMapFragment ) getSupportFragmentManager().findFragmentById( R.id.map );
        customMapFragment.getMapAsync( this );

        final Switch mapSwitch = (Switch) findViewById( R.id.switch_map_type );
        mapSwitch.setChecked( false );
        mapSwitch.setTextOn ( "Hybrid" );
        mapSwitch.setTextOff( "Normal" );
        mapSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged ( CompoundButton buttonView, boolean isChecked ) {
                if(isChecked){
                    mMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
                }
                else{
                    mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
                }
            }
        } );

        if( Build.VERSION.SDK_INT >=23 &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_COARSE_LOCATION  );
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION  );
            return;
        }

        locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );

        provider = locationManager.getBestProvider( new Criteria(), false );
        Location location = locationManager.getLastKnownLocation( provider );

        if (location == null) {
            myLocation = new LatLng(  45.7256, 21.2406 );
        }
        else{
            myLocation = new LatLng( location.getLatitude(), location.getLongitude() );
        }
    }

    public void mapClear (){
        mMap.clear();
        addressMarkerList.clear();
        poiMarkerList.clear();
    }

    public void mapDraw(){

        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick ( final Marker marker ) {
                FragmentManager fm = getSupportFragmentManager();
                POIDialog poiDialog = new POIDialog();
                poiDialog.setPOIDialogListener( new POIDialog.POIDialogListener() {
                    @Override
                    public void onEditButtonClicked () {
                        int markerIndex;
                        FragmentManager fm = getSupportFragmentManager();
                        final EditPOIDialog editPOIDialog = new EditPOIDialog();

                        editPOIDialog.setEditPoiDialogListener( new EditPOIDialog.EditPoiDialogListener() {
                            @Override
                            public void onSaveEdit () {

                                AlertDialog newAlertDialog = new AlertDialog.Builder( thisActivity )
                                        .setTitle( "Salveaza" )
                                        .setMessage( "Doriti sa salvati modificarile?" )
                                        .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick ( DialogInterface dialog, int which ) {

                                                Spinner selectionSpinner = editPOIDialog.getSpinner();
                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                        .addConverterFactory( GsonConverterFactory.create() )
                                                        .build();
                                                IAdminMap api = retrofit.create( IAdminMap.class );

                                                switch ( selectionSpinner.getSelectedItemPosition() ){
                                                    case 0:
                                                        Log.d("Save","POI");
                                                        JsonModel jsonModel = new JsonModel(editPOIDialog.getMarkerId(), "poi", "Timişoara", "Timişoara", "Timiş", marker.getPosition().latitude, marker.getPosition().longitude, editPOIDialog.getStreetName(), 1, "", 0, "","",""   );
                                                        Call<JsonModel> createPoiCall = api.createMarker( jsonModel);
                                                        createPoiCall.enqueue( new Callback<JsonModel>() {
                                                            @Override
                                                            public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                                Log.d("Response",response.message()+" "+call.toString());
                                                            }

                                                            @Override
                                                            public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                                Log.d("Failure",call.toString()+" "+t.getMessage());
                                                            }
                                                        } );
                                                        break;
                                                    case 1:
                                                        Log.d("Save","Casa");
                                                        jsonModel = new JsonModel(editPOIDialog.getMarkerId(), "casa", "Timişoara", "Timişoara", "Timiş", marker.getPosition().latitude, marker.getPosition().longitude, editPOIDialog.getStreetName(), 1, "", 0, editPOIDialog.getStreetNumber(),editPOIDialog.getStreetNumber(),""   );
                                                        createPoiCall = api.createMarker( jsonModel);
                                                        createPoiCall.enqueue( new Callback<JsonModel>() {
                                                            @Override
                                                            public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                                Log.d("Response",response.message()+" "+call.toString());
                                                            }

                                                            @Override
                                                            public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                                Log.d("Failure",call.toString()+" "+t.getMessage());
                                                            }
                                                        } );
                                                        break;
                                                    case 2:
                                                        Log.d("Save","Bloc");
                                                        jsonModel = new JsonModel(editPOIDialog.getMarkerId(), "scara", "Timişoara", "Timişoara", "Timiş", marker.getPosition().latitude, marker.getPosition().longitude, editPOIDialog.getStreetName(), 1, "", 0, editPOIDialog.getStreetNumber(),editPOIDialog.getStreetNumber(), editPOIDialog.getStairway()   );
                                                        createPoiCall = api.createMarker( jsonModel);
                                                        createPoiCall.enqueue( new Callback<JsonModel>() {
                                                            @Override
                                                            public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                                Log.d("Response",response.message()+" "+call.toString());
                                                            }

                                                            @Override
                                                            public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                                Log.d("Failure",call.toString()+" "+t.getMessage());
                                                            }
                                                        } );
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                dialog.cancel();
                                            }
                                        } )
                                        .setNegativeButton( "Nu", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick ( DialogInterface dialog, int which ) {
                                                dialog.cancel();
                                            }
                                        } )
                                        .create();
                                newAlertDialog.show();


                            }

                            @Override
                            public void onChangePos (EditPOIDialog editPOIDialog1) {
                                tmpEditPOIDialog = editPOIDialog1;
                                editingMarker=marker;
                                isChangingPosition=true;
                                changePositionButton.setVisibility( View.VISIBLE );
                            }
                        } );

                        if(addressMarkerList.contains( marker )){
                            markerIndex = addressMarkerList.indexOf( marker );
                            editPOIDialog.setMarkerId( mAdminMapModel.getData().getAddresses().get( markerIndex ).getId() );
                            editPOIDialog.setStreetName( mAdminMapModel.getData().getAddresses().get( markerIndex ).getName() );
                            editPOIDialog.setStreetNumber( mAdminMapModel.getData().getAddresses().get( markerIndex ).getNo() );
                            editPOIDialog.setStairway( mAdminMapModel.getData().getAddresses().get( markerIndex ).getSc() );
                            editPOIDialog.show( fm, "" );
                        }
                        else{
                            markerIndex = poiMarkerList.indexOf( marker );
                            editPOIDialog.setMarkerId( mAdminMapModel.getData().getPois().get( markerIndex ).getId() );
                            editPOIDialog.setStreetName( mAdminMapModel.getData().getPois().get( markerIndex ).getName() );
                            editPOIDialog.show( fm, "" );
                        }
                    }
                    @Override
                    public void onDeleteButtonClicked () {
                        AlertDialog newAlertDialog = new AlertDialog.Builder( thisActivity )
                                .setTitle( "Salveaza" )
                                .setMessage( "Doriti sa stergeti punctul?" )
                                .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick ( DialogInterface dialog, int which ) {

                                        if(addressMarkerList.contains( marker )){

                                            int markerIndex = addressMarkerList.indexOf( marker );
                                            long markerId = mAdminMapModel.getData().getAddresses().get( markerIndex ).getId();
                                            mAdminMapModel.getData().getAddresses().remove( markerIndex );
                                            addressMarkerList.remove( marker );

                                            //TODO CALL DELETION

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                    .addConverterFactory( GsonConverterFactory.create() )
                                                    .build();
                                            IAdminMap api = retrofit.create( IAdminMap.class );
                                            JsonModel jsonModel = new JsonModel(markerId, "poi", "Timişoara", "Timişoara", "Timiş", marker.getPosition().latitude, marker.getPosition().longitude,"", 0, "", 1, "","",""   );
                                            Call<JsonModel> createPoiCall = api.createMarker( jsonModel);
                                            createPoiCall.enqueue( new Callback<JsonModel>() {
                                                @Override
                                                public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                    Log.d("Response",response.message()+" "+call.toString());
                                                }

                                                @Override
                                                public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                    Log.d("Failure",call.toString()+" "+t.getMessage());
                                                }
                                            } );
                                            marker.remove();
                                        }
                                        else{
                                            int markerIndex = poiMarkerList.indexOf( marker );
                                            long markerId = mAdminMapModel.getData().getPois().get( markerIndex ).getId();
                                            mAdminMapModel.getData().getPois().remove( markerIndex );
                                            poiMarkerList.remove( marker );

                                            //TODO CALL DELETION
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                                                    .addConverterFactory( GsonConverterFactory.create() )
                                                    .build();
                                            IAdminMap api = retrofit.create( IAdminMap.class );
                                            JsonModel jsonModel = new JsonModel(markerId, "poi", "Timişoara", "Timişoara", "Timiş", marker.getPosition().latitude, marker.getPosition().longitude,"", 0, "", 1, "","",""   );
                                            Call<JsonModel> createPoiCall = api.createMarker( jsonModel);
                                            createPoiCall.enqueue( new Callback<JsonModel>() {
                                                @Override
                                                public void onResponse ( Call<JsonModel> call, Response<JsonModel> response ) {
                                                    Log.d("Response",response.message()+" "+call.toString());
                                                }

                                                @Override
                                                public void onFailure ( Call<JsonModel> call, Throwable t ) {
                                                    Log.d("Failure",call.toString()+" "+t.getMessage());
                                                }
                                            } );

                                            marker.remove();
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton( "Nu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick ( DialogInterface dialog, int which ) {
                                        dialog.dismiss();
                                    }
                                } )
                                .create();
                        newAlertDialog.show();

                    }
                } );
                poiDialog.setTitleString( marker.getTitle() );
                poiDialog.show( fm, "" );
                return false;
            }
        } );

        for(final Address a : mAdminMapModel.getData().getAddresses()) {
            Marker tmpMarker = mMap.addMarker( new MarkerOptions().icon( BitmapDescriptorFactory.fromResource( R.drawable.poi_50 ) ).position(new LatLng( a.getLat(),a.getLng() ) ).title( a.getName()+" "+a.getNo() ) );
            addressMarkerList.add( tmpMarker );

        }
        for(final Poi p : mAdminMapModel.getData().getPois()) {
            Marker tmpMarker =  mMap.addMarker( new MarkerOptions().icon( BitmapDescriptorFactory.fromResource( R.drawable.map_circle_blue50 ) ).position(new LatLng( p.getLat(),p.getLng() ) ).title( p.getName() ) );
            poiMarkerList.add( tmpMarker );
        }
    }

    public void reverseGeocodeAddress(){
        TextView reverseGeoText = (TextView) findViewById( R.id.reverse_geo_text );
        Geocoder geocoder = new Geocoder( getApplicationContext(), Locale.getDefault() );
        try {
            List<android.location.Address> addressList = geocoder.getFromLocation( mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude,1 );
            if(addressList!=null && addressList.size()>0){
                reverseGeoText.setText( addressList.get( 0 ).getAddressLine( 0 ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void serverCall ( LatLng position){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://api.tudo.ro:"+TEMP_ID )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        IAdminMap api = retrofit.create( IAdminMap.class );

        Call<AdminMapModel> adminMapModelCall = api.getMapData( position.longitude, position.latitude );
        adminMapModelCall.enqueue( new Callback<AdminMapModel>() {
            @Override
            public void onResponse ( Call<AdminMapModel> call, Response<AdminMapModel> response ) {
                Log.d("Call",call.request().url().toString() );
                mAdminMapModel = response.body();
                mapClear();
                mapDraw();
                reverseGeocodeAddress();
                Log.d("Response",response.message()+" "+response.body().getData().getAddresses().size());
            }

            @Override
            public void onFailure ( Call<AdminMapModel> call, Throwable t ) {
                Log.d("Failure",call.toString()+" "+t.getMessage());
            }
        } );
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady ( GoogleMap googleMap ) {
        mMap = googleMap;
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( myLocation, 17f ) );
        serverCall( myLocation );

    }

    @Override
    public void touchWrapperCall () {

    }
}
