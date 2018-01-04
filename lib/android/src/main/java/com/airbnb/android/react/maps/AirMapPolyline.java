package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirMapPolyline extends AirMapFeature {

  private PolylineOptions polylineOptions;
  private Polyline polyline;

  private List<LatLng> coordinates;
  private int color;
  private float width;
  private boolean geodesic;
  private float zIndex;

  // TODO: To add as props (lineDashLength, lineGapLength)
  private int PATTERN_DASH_LENGTH_PX = 20;
  private int PATTERN_GAP_LENGTH_PX = 20;
  
  // TODO: Add as optional prop
  private PatternItem DOT = new Dot();
  private PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
  private PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

  // Create a stroke pattern of a gap followed by a dash.
  private List<PatternItem> PATTERN_POLYGON = Arrays.asList(GAP, DASH);

  public AirMapPolyline(Context context) {
    super(context);
  }

  public void setCoordinates(ReadableArray coordinates) {
    this.coordinates = new ArrayList<>(coordinates.size());
    for (int i = 0; i < coordinates.size(); i++) {
      ReadableMap coordinate = coordinates.getMap(i);
      this.coordinates.add(i,
          new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
    }
    if (polyline != null) {
      polyline.setPoints(this.coordinates);
    }
  }

  public void setColor(int color) {
    this.color = color;
    if (polyline != null) {
      polyline.setColor(color);
    }
  }

  public void setWidth(float width) {
    this.width = width;
    if (polyline != null) {
      polyline.setWidth(width);
    }
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;
    if (polyline != null) {
      polyline.setZIndex(zIndex);
    }
  }

  public void setGeodesic(boolean geodesic) {
    this.geodesic = geodesic;
    if (polyline != null) {
      polyline.setGeodesic(geodesic);
    }
  }

  public PolylineOptions getPolylineOptions() {
    if (polylineOptions == null) {
      polylineOptions = createPolylineOptions();
    }
    return polylineOptions;
  }

  private PolylineOptions createPolylineOptions() {
    PolylineOptions options = new PolylineOptions();
    options.addAll(coordinates);
    options.color(color);
    options.width(width);
    options.geodesic(geodesic);
    options.zIndex(zIndex);
    // TODO: Should connect to react
    // this currently forces the map to use pattern
    // on all polylines
    options.pattern(PATTERN_POLYGON);
    return options;
  }

  @Override
  public Object getFeature() {
    return polyline;
  }

  @Override
  public void addToMap(GoogleMap map) {
    polyline = map.addPolyline(getPolylineOptions());
    polyline.setClickable(true);
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    polyline.remove();
  }
}
