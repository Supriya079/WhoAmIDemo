package com.supriya.whoami.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.supriya.whoami.R;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.LocationEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PatientHomeLocation extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Marker patientLocationMarker;
    Circle patientLocationAccuracyCircle;
    Location userLocation; // current patient location
    double tapLatitude;  // home latitude
    double tapLongitude; // home longitude
    DataDAO dataDAO;
    // Declare a variable to keep track of markers
    private List<Marker> markersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_location);

        dataDAO = WhoAmIDB.getInstance(getApplicationContext()).dataDAO();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FloatingActionButton fabDirection =  findViewById(R.id.fabDirections);
        fabDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDirections();
            }
        });

    }

    private void openDirections() {
        // Get the coordinates of the desired location (tapLatitude and tapLongitude)
        // Create a URI for the location coordinates
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tapLatitude + "," + tapLongitude);

        // Create an intent with the ACTION_VIEW action and the URI
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Set the package to Google Maps
        mapIntent.setPackage("com.google.android.apps.maps");

        // Verify that Google Maps is installed before launching the intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity with the intent
            startActivity(mapIntent);
        }
    }

    // updates the patient's current location marker
    private void setPatientLocationMarker(Location location) {
        Toast.makeText(getApplicationContext(), "NoOOO Markers", Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        userLocation = location; // save patient current location to a variable
        System.out.println("User Latitude: " + userLocation.getLatitude());
        System.out.println("User Longitude: " + userLocation.getLongitude());
        if (patientLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            patientLocationMarker = mMap.addMarker(markerOptions);
            markersList.add(patientLocationMarker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else {
            //use the previously created marker
            patientLocationMarker.setPosition(latLng);
            patientLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }

        if (patientLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            patientLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            patientLocationAccuracyCircle.setCenter(latLng);
            patientLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
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

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////            enableUserLocation();
////            zoomToUserLocation();
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                //We can show user a dialog why this permission is necessary
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
//            }
//
//        }
//        try {
//            List<Address> addresses = geocoder.getFromLocationName("london", 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                LatLng london = new LatLng(address.getLatitude(), address.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(london)
//                        .title(address.getLocality());
//                mMap.addMarker(markerOptions);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, 16));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Set up click listener for map
//        // set home location
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            // set marker on home location
//            public void onMapClick(LatLng latLng) {
//                // Clear previous circle
//                mMap.clear();
//
//                // Add marker at selected location
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Home Location"));
//                tapLatitude = latLng.latitude; // home latitude
//                tapLongitude = latLng.longitude; // home longitude
//                System.out.println("Home Location: ");
//                System.out.println("Latitude: "+tapLatitude);
//                System.out.println("Longitude: "+tapLongitude);
//                // Add circle around selected location with specified radius
//                CircleOptions circleOptions = new CircleOptions()
//                        .center(latLng)
//                        .radius(500)
//                        .strokeWidth(2)
//                        .strokeColor(Color.RED)
//                        .fillColor(Color.argb(70, 255, 0, 0));
//                mMap.addCircle(circleOptions);
//
//                // Show bottom sheet dialog to enter place name
//                showPlaceNameDialog(latLng);
//            }
//        });
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Check if a location is stored in the database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<LocationEntity> storedLocations = dataDAO.getAllLocations();
                if (!storedLocations.isEmpty()) {
                    final LocationEntity storedLocation = storedLocations.get(0); // Assuming only one location is stored
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            // Set the map with the saved location
                            LatLng savedLatLng = new LatLng(storedLocation.getLatitude(), storedLocation.getLongitude());
                            tapLatitude = storedLocation.getLatitude();
                            tapLongitude = storedLocation.getLongitude();
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(savedLatLng)
                                    .title("Home Location");
                            Marker marker = mMap.addMarker(markerOptions);
                            markersList.add(marker);

                            //mMap.addMarker(new MarkerOptions().position(savedLatLng).title("Home Location"));

                            markersList.add(marker);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 16));
                            // Add circle around saved location with specified radius
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(savedLatLng)
                                    .radius(500)
                                    .strokeWidth(2)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.argb(70, 255, 0, 0));
                            mMap.addCircle(circleOptions);

                            // Show bottom sheet dialog to enter place name
                            // showPlaceNameDialog(savedLatLng);

                            // Remove the patient's current location marker if it exists
                            if (patientLocationMarker != null) {
                                patientLocationMarker.remove();
                                patientLocationMarker = null;
                            }
                            // Remove the accuracy circle if it exists
                            if (patientLocationAccuracyCircle != null) {
                                patientLocationAccuracyCircle.remove();
                                patientLocationAccuracyCircle = null;
                            }


                        }
                    });
                }
                else {
                    // No location stored in the database, proceed with the default behavior

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Set up click listener for map
                            // set home location
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                // set marker on home location
                                public void onMapClick(LatLng latLng) {
                                    // Clear previous circle
                                    mMap.clear();

                                    // Add marker at selected location

                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(latLng)
                                            .title("Home Location");
                                    Marker marker = mMap.addMarker(markerOptions);
                                    markersList.add(marker);

                                    //mMap.addMarker(new MarkerOptions().position(latLng).title("Home Location"));


                                    tapLatitude = latLng.latitude; // home latitude
                                    tapLongitude = latLng.longitude; // home longitude
                                    System.out.println("Home Location: ");
                                    System.out.println("Latitude: "+tapLatitude);
                                    System.out.println("Longitude: "+tapLongitude);
                                    // Add circle around selected location with specified radius
                                    CircleOptions circleOptions = new CircleOptions()
                                            .center(latLng)
                                            .radius(500)
                                            .strokeWidth(2)
                                            .strokeColor(Color.RED)
                                            .fillColor(Color.argb(70, 255, 0, 0));
                                    mMap.addCircle(circleOptions);

                                    // Show bottom sheet dialog to enter place name
                                    showPlaceNameDialog(latLng);

                                    // Remove the patient's current location marker if it exists
//                                    if (patientLocationMarker != null) {
//                                        patientLocationMarker.remove();
//                                        patientLocationMarker = null;
//                                    }
                                    // Remove the accuracy circle if it exists
//                                    if (patientLocationAccuracyCircle != null) {
//                                        patientLocationAccuracyCircle.remove();
//                                        patientLocationAccuracyCircle = null;
//                                    }

                                }
                            });

                        }
                    });

                }
            }
        });
    }


    private void showPlaceNameDialog(final LatLng latLng) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_place_name, null);
        final TextInputEditText editTextPlaceName = bottomSheetView.findViewById(R.id.editTextPlaceName);
        Button buttonSave = bottomSheetView.findViewById(R.id.buttonSave);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(bottomSheetView);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeName = editTextPlaceName.getText().toString().trim();
                if (!placeName.isEmpty()) {
                    // Save the location to Room database
                    saveLocationToDatabase(placeName, latLng.latitude, latLng.longitude);

                    // Dismiss the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(PatientHomeLocation.this, "Please enter a place name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void saveLocationToDatabase(final String placeName, final double latitude, final double longitude) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LocationEntity location = new LocationEntity(placeName, latitude, longitude);

                // Delete previously stored location
                List<LocationEntity> storedLocations = dataDAO.getAllLocations();
                if (!storedLocations.isEmpty()) {
                    LocationEntity storedLocation = storedLocations.get(0); // Assuming only one location is stored
                    dataDAO.deleteLocation(storedLocation);
                }

                // Insert new location
                dataDAO.insertLocation(location);
            }
        });
    }


//    private void saveLocationToDatabase(final String placeName, final double latitude, final double longitude) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                LocationEntity location = new LocationEntity(placeName, latitude, longitude);
////                WhoAmIDB locationDatabase = Room.databaseBuilder(getApplicationContext(), WhoAmIDB.class, "locations.db").build();
////                DataDAO locationDao = locationDatabase.dataDAO();
//
//                // Delete previously stored location
//                List<LocationEntity> storedLocations = dataDAO.getAllLocations();
//                if (!storedLocations.isEmpty()) {
//                    LocationEntity storedLocation = storedLocations.get(0); // Assuming only one location is stored
//                    dataDAO.deleteLocation(storedLocation);
//                }
//
//                // Insert new location
//                dataDAO.insertLocation(location);
//            }
//        });
//    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("LastL:   ", "onLocationResult: " + locationResult.getLastLocation());
            setPatientLocationMarker(locationResult.getLastLocation());
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}