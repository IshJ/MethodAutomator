package com.example.methodautomator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Map<Integer, String> viewMap = new HashMap<>();
    Map<String, Integer> inverseViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int loopCount = 20;

        String pkgName = "org.woheller69.weather";

        //        Initiating the sidechannel
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName,
                pkgName + ".activities.SplashActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        viewMap = new HashMap<>();

        viewMap.put(8, ".activities.RadiusSearchActivity"
        );
        viewMap.put(7, ".activities.ManageLocationsActivity"
        );
        viewMap.put(4, ".activities.RainViewerActivity"
        );
        viewMap.put(3, ".activities.RadiusSearchResultActivity"
        );

        viewMap.keySet().forEach(k-> inverseViewMap.put(viewMap.get(k), k));


        SortedSet<Integer> keys = new TreeSet<>(viewMap.keySet());
        List<String> views = keys.stream().map(viewMap::get).collect(Collectors.toList());


        List<ActivityRunner> runners = new ArrayList<>();
        views.stream().forEach(view -> runners.add(new ActivityRunner(view, pkgName)));

        List<SequentialActivityRunner> sequentialRunners = new ArrayList<>();
        views.stream().forEach(view -> sequentialRunners.add(new SequentialActivityRunner(view, pkgName)));

        for (int i = 0; i < loopCount; i++) {

//            parallelRun(runners);
            sequentialRun(sequentialRunners);
        }
        Log.d("###", "Done!");
    }

    public static void parallelRun(List<ActivityRunner> runners) {
        for (int j = 0; j < runners.size(); j++) {
            Handler handler = new Handler();
            handler.postDelayed(runners.get(j), 5000);
        }
    }

    public static void sequentialRun(List<SequentialActivityRunner> runners) {
        for (int j = 0; j < runners.size(); j++) {
            runners.get(j).run();
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis()-startTime<1000
            ){}
        }
    }



    class ActivityRunner implements Runnable {
        private final String view;
        private final String pkgName;

        public ActivityRunner(String view, String pkgName) {
            this.view = view;
            this.pkgName = pkgName;
        }

        public void run() {
            finish();
            Log.d("###", view);
            overridePendingTransition(0, 0);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(pkgName,
                    pkgName + view));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    class SequentialActivityRunner {
        private final String view;
        private final String pkgName;

        public SequentialActivityRunner(String view, String pkgName) {
            this.view = view;
            this.pkgName = pkgName;
        }

        public void run() {
            finish();
//            Log.d("###", view);
            Log.d("weather:AddressScan2",  "#"+inverseViewMap.get(view)+"#0#1");
            overridePendingTransition(0, 0);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(pkgName,
                    pkgName + view));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
    }

}
//
//        viewMap.put(5, ".activities.AboutActivity"
//                );
//
//                viewMap.put(6, ".activities.ForecastCityActivity"
//                );