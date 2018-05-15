package com.ausichenko.lifecycles.step4

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log

class BoundLocationManager {

    fun bindLocationListenerIn(context: Context,
                                      locationListener: LocationListener,
                                      lifecycleOwner: LifecycleOwner) {
        BoundLocationListener(context, locationListener, lifecycleOwner)
    }

    @SuppressWarnings("MissingPermission")
    class BoundLocationListener(private val context: Context,
                                private val locationListener: LocationListener,
                                lifecycleOwner: LifecycleOwner) : LifecycleObserver {

        private var locationManager: LocationManager? = null

        init {
            //TODO: Add lifecycle observer
        }

        //TODO: Call this on resume
        fun addLocationListener() {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0f ,locationListener)
            Log.d("BoundLocationMgr", "Listener added")

            val lastLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                locationListener.onLocationChanged(lastLocation)
            }
        }

        //TODO: Call this on pause
        fun removeLocationListener() {
            locationManager?.removeUpdates(locationListener)
            locationManager = null
            Log.d("BoundLocationMgr", "Listener removed")
        }
    }
}