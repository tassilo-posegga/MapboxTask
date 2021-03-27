package eu.aggesop.mapboxtask

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.mapbox.mapboxsdk.maps.MapView

fun MapView.attachToLifeCycle(lifecycle: Lifecycle) {

    lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
        fun onStart(owner: LifecycleOwner) {
            this@attachToLifeCycle.onStart()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
        fun onResume(owner: LifecycleOwner) {
            this@attachToLifeCycle.onResume()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
        fun onPause(owner: LifecycleOwner) {
            this@attachToLifeCycle.onPause()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_STOP)
        fun onStop(owner: LifecycleOwner) {
            this@attachToLifeCycle.onStop()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            this@attachToLifeCycle.onDestroy()
        }
    })
}
