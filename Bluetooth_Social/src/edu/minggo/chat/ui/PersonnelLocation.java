package edu.minggo.chat.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;

import edu.minggo.chat.R;
import edu.minggo.chat.util.BaiMapGlobalApplication;

public class PersonnelLocation extends MapActivity{
	
	private MapView mMapView;
	
	private OverItemTs overitem;
	
	private GeoPoint geoPoint;
	
	private Button backButton;
	
	private BaiMapGlobalApplication app;
	
	public static double Lon;
	public static double Lat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personnel_location);
		
		Lon = Double.valueOf(this.getIntent().getStringExtra("Lon"));
		Lat = Double.valueOf(this.getIntent().getStringExtra("Lat"));
		
		geoPoint = new GeoPoint((int) (Lat * 1E6), (int) (Lon * 1E6));
		
		app = (BaiMapGlobalApplication)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BaiMapGlobalApplication.MyGeneralListener());
		}
		app.mBMapMan.start();
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView) findViewById(R.id.personnel_location_mapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDrawOverlayWhenZooming(true);
        
        mMapView.getController().setCenter(geoPoint);
        mMapView.getController().setZoom(18);
		
        
        Drawable mdDrawable = this.getResources().getDrawable(R.drawable.title_location_nor);
		mdDrawable.setBounds(0, 0, mdDrawable.getIntrinsicWidth(), mdDrawable.getIntrinsicHeight());

		overitem = new OverItemTs(mdDrawable, this, geoPoint,"");
		mMapView.getOverlays().add(overitem); //Ìí¼ÓItemizedOverlayÊµÀýµ½mMapView
        
        
        backButton = (Button)findViewById(R.id.personnel_location_backbtn);
        backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				app = (BaiMapGlobalApplication)PersonnelLocation.this.getApplication();
		        mMapView.destroyDrawingCache();
				app.mBMapMan.stop();
				
				PersonnelLocation.this.finish();
			}
		});
        
	}
	
	
	@Override
	protected void onPause() {
		BaiMapGlobalApplication app = (BaiMapGlobalApplication)this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		
		BaiMapGlobalApplication app = (BaiMapGlobalApplication)this.getApplication();
		app.mBMapMan.start();
		
		super.onResume();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			app = (BaiMapGlobalApplication)this.getApplication();
	        mMapView.destroyDrawingCache();
			app.mBMapMan.stop();
			
			PersonnelLocation.this.finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	
}


class OverItemTs extends ItemizedOverlay<OverlayItem>{
	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

	public OverItemTs(Drawable marker, Context context, GeoPoint pt, String title) {
		super(boundCenterBottom(marker));
		
		mGeoList.add(new OverlayItem(pt, title, null));

		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		return mGeoList.size();
	}

	@Override
	public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
		Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
		return false;
	}
}
