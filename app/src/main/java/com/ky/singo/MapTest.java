package com.ky.singo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ggungnae on 2017-05-30.
 */

public class MapTest extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //구글맵 테스트 코드 - 서울에 마커 찍기
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(37.555744, 126.970431)   // 위도, 경도
        ));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

        MarkerOptions marker = new MarkerOptions();
        marker .position(new LatLng(37.555744, 126.970431))
                .title("서울역")
                .snippet("Seoul Station");
        googleMap.addMarker(marker).showInfoWindow(); // 마커추가,화면에출력

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),
                        marker.getTitle() + " 클릭했음"
                        , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String text = "맵 클릭 이벤트 latitude ="
                        + latLng.latitude + ", longitude ="
                        + latLng.longitude;
                // 주소 구하기
                String firstAddress = "";
                List<Address> address;


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                try {
                    if (geocoder != null) {
                        //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                        //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                        address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                        if (address != null && address.size() > 0) {
                            // 주소 받아오기
                            String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                            firstAddress  = currentLocationAddress;

                        }
                    }

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), firstAddress, Toast.LENGTH_LONG).show();
            }
        });
    }

}

