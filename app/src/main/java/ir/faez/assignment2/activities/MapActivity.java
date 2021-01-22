package ir.faez.assignment2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import ir.faez.assignment2.R;
import ir.faez.assignment2.databinding.ActivityLocationBinding;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EXTRA_MESSAGE = "LATLNG";
    private static LatLng latLang;
    private ActivityLocationBinding binding;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        // initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // when permission granted
            getCurrentLocation();
        } else {
            // when permission denied
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }


    private void init() {
        latLang = null;
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        invokeListeners();
    }

    private void invokeListeners() {
        binding.saveLocationBtn.setOnClickListener(this);


    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        // init task location
        Task<android.location.Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(final android.location.Location location) {
                // when success
                if (location != null) {
                    // sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                            // init lat lng
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // create marker options
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");
                            // change marker icon
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_add_location_alt_orange_600_48dp));
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            //add marker on map
                            googleMap.addMarker(markerOptions);
                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                Marker marker;

                                @Override
                                public void onMapClick(LatLng latLng) {
                                    // for remove auto location detection marker
                                    googleMap.clear();

                                    if (marker != null) {
                                        marker.remove();
                                    }
                                    marker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.baseline_add_location_alt_orange_600_48dp)));

                                    // set class instance
                                    MapActivity.latLang = latLng;
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // when permission granted
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_location_btn:
                saveLocationBtn();
        }
    }

    // send lat lng data to signup activity
    private void saveLocationBtn() {

        if (latLang != null) {
            double lat = this.latLang.latitude;
            double lang = this.latLang.longitude;


            double res[] = new double[2];
            res[0] = lat;
            res[1] = lang;
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MESSAGE, res);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.selectSomewhereOnMap, Toast.LENGTH_SHORT).show();
        }
    }
}
