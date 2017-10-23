package com.nadoo.tacademy.eunbyul_nanu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kyoonho on 2017-06-11.
 */

public class ModifyItemMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener{

    GoogleMap map;

    String itemname,info;

    private LinearLayout mapinfo;
    private TextView mapinfo_title,mapinfo_subtitle;

    class TMapPOIAsyncTask extends AsyncTask<String, Void, TMapPOISearchResult> {
        public TMapPOIAsyncTask() {
            super();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected TMapPOISearchResult doInBackground(String... params) {
            String targetURL =  null;
            OkHttpClient httpClient = null;
            Response response;
            //TMap 포이정보를 가져오는 URL 및
            String URL_TMAP_POI =
                    "https://apis.skplanetx.com/tmap/pois?version=1" +
                            "&resCoordType=WGS84GEO&reqCoordType=WGS84GEO&searchKeyword=%s";
            TMapPOISearchResult poiSearchResult=null;
            try{
                targetURL = String.format(URL_TMAP_POI, URLEncoder.encode(params[0], "utf-8"));
                httpClient = new OkHttpClient.Builder()
                        .connectTimeout(5000, TimeUnit.MILLISECONDS)
                        .build();
                Request request = new Request.Builder().url(targetURL)
                        .header("Accept", "application/json")
                        .header("appKey", "5f907958-082d-3593-ac31-f23cb01efaa7")
                        .build();
                //SK developer key작성

                response =  httpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    poiSearchResult  = gson.fromJson(response.body().charStream(), TMapPOISearchResult.class);
                }else{
                    Log.e("test", "요청실패");
                }


            }catch(Exception e){
                Log.e("--------------", String.valueOf(e));
            }

            return poiSearchResult;
        }
        @Override
        protected void onPostExecute(TMapPOISearchResult poiResult) {
            super.onPostExecute(poiResult);
            //현재 어댑터에 마커가 있다면 지운다
            if(poiResult==null) {
                Toast.makeText(MyApplication.getmContext(), "검색 결과가 없습니다. 다시 검색해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                clearAllMarker();
                for (POIItem item : poiResult.searchPoiInfo.pois.poi) {
                    item.updatePOIData();
                    LatLng latLng = new LatLng(item.latitude, item.longitude);
                    //지도 및 어댑터에 마커 추가
                    addMarker(latLng, item);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    map.animateCamera(update);

                }
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_write_content_main);
        //구글 맵 지도를 꺼낸다.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.read_write_map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
       /* itemname = i.getExtras().getString("itemname");
        info = i.getExtras().getString("info");*/

        final AutoCompleteTextView search = (AutoCompleteTextView)findViewById(R.id.autocomplte_search);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String keyword = search.getText().toString();
                    new TMapPOIAsyncTask().execute(keyword);
                    Toast.makeText(getApplicationContext(),"검색시작",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        mapinfo_title = (TextView)findViewById(R.id.mapinfo_title);
        mapinfo_subtitle = (TextView)findViewById(R.id.mapinfo_contents);
    }
    private static final int RC_PERMISSION = 1;
    //포이 정보를 이용해 마커를 찾는다.
    Map<POIItem, Marker> markerResolver = new HashMap<>();
    //마커를 이용해 포이정보를 찾는다
    Map<Marker, POIItem> poiResolver = new HashMap<>();
    //마커 추가
    private void addMarker(LatLng position, POIItem data) {
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        // BitmapDescriptor icon = new BitmapDescriptor();
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        options.anchor(0.5f, 1);
        options.title(data.title);
        options.snippet(data.subtitle);
        options.draggable(true);

        Marker marker = map.addMarker(options);
        markerResolver.put(data, marker);
        poiResolver.put(marker, data);

    }
    private void clearAllMarker() {
        map.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        // map.setMyLocationEnabled(true); //현재 내 위치 활성화

        LatLng SEOUL = new LatLng(37.527089,127.028480);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 11.0f));
        map.getUiSettings().setZoomControlsEnabled(true); //줌컨트롤 설정
        map.getUiSettings().isMyLocationButtonEnabled();
        map.setOnCameraChangeListener(this); //뷰의 이동시키는
        map.setOnMarkerClickListener(this); //마커클릭 허용
        map.setOnInfoWindowClickListener(this); //인포윈도우 클릭허용


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted

                map.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {

            map.setMyLocationEnabled(true);
        }

    /*    if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION);

            }
            return;
        }*/

    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(ModifyItemMapActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode){
            case RC_PERMISSION : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }
    private static final String TAG = "MainActivity";
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.i(TAG, "camera change");
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        POIItem item = poiResolver.get(marker);
        mapinfo = (LinearLayout)findViewById(R.id.mapinfo_detail);
        mapinfo.setVisibility(View.VISIBLE);
        mapinfo_title.setText(item.title);
        mapinfo_subtitle.setText(item.subtitle);
        //Toast.makeText(this, "description : " + item.description, Toast.LENGTH_SHORT).show();
        marker.showInfoWindow();
        return true;
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        POIItem item = poiResolver.get(marker);
        Intent intent = new Intent(this, ModifyItem.class);
        intent.putExtra("title",item.title);
        intent.putExtra("subtitle",item.subtitle);
        /*intent.putExtra("itemname",itemname);
        intent.putExtra("info",info);*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(5,intent);
        finish();
        //marker.hideInfoWindow();

    }
}
