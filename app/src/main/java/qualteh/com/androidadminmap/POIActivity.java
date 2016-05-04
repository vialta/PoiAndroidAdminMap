package qualteh.com.androidadminmap;

import android.Manifest;
import android.content.Context;
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
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.CompoundButton;
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
import qualteh.com.androidadminmap.Dialogs.EditPOIDialog;
import qualteh.com.androidadminmap.Dialogs.POIDialog;
import qualteh.com.androidadminmap.Model.Address;
import qualteh.com.androidadminmap.Model.AdminMapModel;
import qualteh.com.androidadminmap.Model.Poi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class POIActivity extends FragmentActivity implements OnMapReadyCallback, TouchWrapperCallback{


    private static int TEMP_ID = 3005;

    private CustomFrameLayout customFrameLayout;
    private AdminMapModel mAdminMapModel;
    private GoogleMap mMap;
    private ArrayList<Marker> addressMarkerList;
    private ArrayList<Marker> poiMarkerList;
    LocationManager locationManager;
    LatLng myLocation;
    String provider;

    public static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION;
    public static int MY_PERMISSIONS_REQUEST_FINE_LOCATION;


    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_poi );

        addressMarkerList = new ArrayList<>(  );
        poiMarkerList = new ArrayList<>(  );

        customFrameLayout = (CustomFrameLayout ) findViewById( R.id.map );
        customFrameLayout.setOnCatchTouchFrameLayoutListener( new CustomFrameLayout.CatchTouchFrameLayoutListener() {
            @Override
            public void onTouchUp () {
                serverCall( mMap.getCameraPosition().target );
            }

            @Override
            public void onTouchDown () {
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
                        if(addressMarkerList.contains( marker )){
                            markerIndex = addressMarkerList.indexOf( marker );
                            FragmentManager fm = getSupportFragmentManager();
                            EditPOIDialog editPOIDialog = new EditPOIDialog();
                            editPOIDialog.setStreetName( mAdminMapModel.getData().getAddresses().get( markerIndex ).getName() );
                            editPOIDialog.setStreetNumber( mAdminMapModel.getData().getAddresses().get( markerIndex ).getNo() );
                            editPOIDialog.setStairway( mAdminMapModel.getData().getAddresses().get( markerIndex ).getSc() );
                            editPOIDialog.setPOI( false );
                            editPOIDialog.show( fm, "" );
                        }
                        else{
                            markerIndex = poiMarkerList.indexOf( marker );
                            FragmentManager fm = getSupportFragmentManager();
                            EditPOIDialog editPOIDialog = new EditPOIDialog();
                            editPOIDialog.setStreetName( mAdminMapModel.getData().getPois().get( markerIndex ).getName() );
                            editPOIDialog.setPOI( true );
                            editPOIDialog.show( fm, "" );
                        }
                    }
                    @Override
                    public void onDeleteButtonClicked () {
                        if(addressMarkerList.contains( marker )){
                            int markerIndex = addressMarkerList.indexOf( marker );
                            mAdminMapModel.getData().getAddresses().remove( markerIndex );
                            addressMarkerList.remove( marker );
                            marker.remove();
                        }
                        else{
                            int markerIndex = poiMarkerList.indexOf( marker );
                            mAdminMapModel.getData().getPois().remove( markerIndex );
                            poiMarkerList.remove( marker );
                            marker.remove();
                        }
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

        Call<AdminMapModel> adminMapModelCall = api.callVersion( position.longitude, position.latitude );
        adminMapModelCall.enqueue( new Callback<AdminMapModel>() {
            @Override
            public void onResponse ( Call<AdminMapModel> call, Response<AdminMapModel> response ) {
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
