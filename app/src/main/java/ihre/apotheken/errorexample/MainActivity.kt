package ihre.apotheken.errorexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private fun setCurrentFragment(newFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            // If below setTransition method is removed, the app functions properly
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.flutter_frame, newFragment)
            .commit()
    }

    private fun setNavigationView() {
        setCurrentFragment(Fragment())
        val app = applicationContext as App
        val bundlePath = FlutterInjector.instance().flutterLoader().findAppBundlePath()
        val mainEntrypoint = DartExecutor.DartEntrypoint(bundlePath, "main")
        val flutterEngine = app.engines.createAndRunEngine(this, mainEntrypoint)
        FlutterEngineCache.getInstance().put("main", flutterEngine)
        val flutterFragment = FlutterFragment.withCachedEngine("main").build<FlutterFragment>()
        val navigationBar = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_menu_0 -> setCurrentFragment(Fragment())
                R.id.bottom_nav_menu_1 -> setCurrentFragment(flutterFragment)
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationView()
    }
}
