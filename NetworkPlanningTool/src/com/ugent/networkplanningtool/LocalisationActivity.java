package com.ugent.networkplanningtool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;

import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.Location;
import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.io.ASyncIOTaskManager;
import com.ugent.networkplanningtool.io.OnAsyncTaskCompleteListener;
import com.ugent.networkplanningtool.io.img.SaveImageParams;
import com.ugent.networkplanningtool.io.img.SaveImageTask;
import com.ugent.networkplanningtool.io.xml.LoadFloorPlanTask;
import com.ugent.networkplanningtool.layout.LocalisationView;
import com.ugent.networkplanningtool.layout.components.MyScrollBar;
import com.ugent.networkplanningtool.layout.design.FingerprintView;
import com.ugent.networkplanningtool.layout.design.MeasureView;
import com.ugent.networkplanningtool.layout.design.LogView;
import com.ugent.networkplanningtool.localisation.Measure;
import com.ugent.networkplanningtool.localisation.RssiReceiver;
import com.ugent.networkplanningtool.localisation.listener.StepListener;
import com.ugent.networkplanningtool.localisation.tasks.ConvertLogTask;
import com.ugent.networkplanningtool.localisation.tasks.InitializePlanTask;
import com.ugent.networkplanningtool.localisation.tasks.LoadLogTask;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.model.LocalisationModel;
import com.ugent.networkplanningtool.model.StepModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import be.ugent.intec.wica.viterbi.Lokalisatie;

/**
 * Created by Alexander on 2/10/2015.
 * Class containing the functionality of the localisation view
 */
public class LocalisationActivity extends Activity implements Observer, OnTouchListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = LocalisationActivity.class.getName();
    private static LocalisationActivity mContext;

    //For phone
    public static String path = "/sdcard/Download/";

    //For emulator
    //public static String path = "/storage/emulated/0/Download/";

    private WifiManager mgr;

    private static File standardPlan = new File(path + "/xml/ZuiderpoortSensor2extra.xml");

    private ViewFlipper mainFlip;

    private View mainActive;

    private LocalisationView localisationView;

    private TextView locationText;
    private TextView coordinateText;

    private ZoomControls zoomControls;

    private DrawingModel drawingModel;
    private FloorPlanModel floorPlanModel;
    private LocalisationModel localisationModel;
    private StepModel stepModel;

    private MyScrollBar hScrollBar;
    private MyScrollBar vScrollBar;

    private ASyncIOTaskManager taskManager;

    private Lokalisatie lokalisatie;

    private Dialog dialog;

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private StepListener stepListener;

    private RssiReceiver receiver;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = this;

        setContentView(R.layout.localisation_main);

        stepModel = StepModel.INSTANCE;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        stepListener = new StepListener(stepModel);
        sensorManager.registerListener(stepListener,stepCounter, sensorManager.SENSOR_DELAY_NORMAL);

        floorPlanModel = FloorPlanModel.LOCALISATION_INSTANCE;

        locationText = (TextView) findViewById(R.id.locationText);
        coordinateText = (TextView) findViewById(R.id.coordinatesTextView);

        localisationView = (LocalisationView) findViewById(R.id.localisationView);
        drawingModel = new DrawingModel(localisationView.getWidth(), localisationView.getHeight(), floorPlanModel);

        localisationModel = LocalisationModel.INSTANCE;
        localisationModel.reset();
        localisationModel.initializeLocalisationMode();

        hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);

        localisationView.setModel(drawingModel);

        mainFlip = (ViewFlipper) findViewById(R.id.testFlipper);
        mainActive = findViewById(R.id.locationButton);

        hScrollBar.setModel(drawingModel);
        vScrollBar.setModel(drawingModel);

        zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingModel.zoomIn();
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingModel.zoomOut();
            }
        });

        drawingModel.addObserver(this);
        floorPlanModel.addObserver(this);
        localisationModel.addObserver(this);
        stepModel.addObserver(this);

        localisationView.setOnTouchListener(this);

        lokalisatie = new Lokalisatie();
        localisationModel.setAlgorithm(lokalisatie);

        taskManager = new ASyncIOTaskManager(this);

        initializePlan();
        openFloorPlan(standardPlan);

        mgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiver = new RssiReceiver(mgr);

        Spinner spinner = (Spinner) findViewById(R.id.NavigateTo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navigate_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void update(Observable arg0, Object arg1) {
        zoomControls.setIsZoomInEnabled(!drawingModel.isZoomInMaxed());
        zoomControls.setIsZoomOutEnabled(!drawingModel.isZoomOutMaxed());
        if (localisationModel.getMarkedLocation() == null) {
            locationText.setText("");
            coordinateText.setText("");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == localisationView) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (LocalisationModel.INSTANCE.drawMarkedLocationAllowed()) {
                        locationText.setText((int) event.getX(0) + ":" + (int) event.getY(0));
                        Point point = drawingModel.getActualTouchLocation(event.getX(0), event.getY(0));
                        int x = point.x + (int) (6000 / drawingModel.getPixelsPerInterval());
                        int y = point.y - (int) (6000 / drawingModel.getPixelsPerInterval());
                        LocalisationModel.INSTANCE.setMarkedLocation(new Location(LocationType.MARKED, x, y));
                        coordinateText.setText(x + ":" + y);
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public void locate(View view) throws IOException {
        if (mgr.isWifiEnabled()) {
            registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mgr.startScan();
            stepModel.stopMeasuring();
        } else {
            Toast.makeText(this, "Enable WiFi to start localisation", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call the fingerprint function of the application
     * @param view the pressed button
     * @throws IOException when fingerprint file can't be accessed
     */
    public void fingerprint(View view) throws IOException {
        if (localisationModel.getMarkedLocation() == null) {
            Toast.makeText(this, "Select location to start fingerprint", Toast.LENGTH_SHORT).show();
        } else if (!mgr.isWifiEnabled()) {
            Toast.makeText(this, "Enable WiFi to start fingerprinting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fingerprinting started", Toast.LENGTH_SHORT).show();
            registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mgr.startScan();
        }
    }

    /**
     * Call the measure function of the application
     * @param view the pressed button
     * @throws IOException when the selected file can't be accessed
     */
    public void measure(View view) throws IOException {
        if (localisationModel.getMarkedLocation() == null) {
            Toast.makeText(this, "Select location to start measure", Toast.LENGTH_SHORT).show();
        } else if (localisationModel.getMeasureFileName().equals("")) {
            Toast.makeText(this, "Enter name of the file to start measure", Toast.LENGTH_SHORT).show();
        } else if (!mgr.isWifiEnabled()) {
            Toast.makeText(this, "Enable WiFi to start measure", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Measure started", Toast.LENGTH_SHORT).show();
            registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mgr.startScan();
        }
    }

    /**
     * Loads the standard floorplan
     */
    private void initializePlan() {
        taskManager.executeTask(new InitializePlanTask(), path, "Initializing plan...", new OnAsyncTaskCompleteListener<Boolean>() {
            @Override
            public void onTaskCompleteSuccess(Boolean result) {
                System.out.println("Plan initialized");
            }

            @Override
            public void onTaskFailed(Exception cause) {

            }
        }, false);
    }

    /**
     * Return the localisation instance
     * @return instance of the localisation activity
     */
    public static LocalisationActivity getInstance() {
        return mContext;
    }

    /**
     * Open a new floorplan and display it on the application
     * @param file the selected plan to be opened
     */
    private void openFloorPlan(File file) {
        taskManager.executeTask(new LoadFloorPlanTask(), file, "Loading " + file.getName() + " ...", new OnAsyncTaskCompleteListener<FloorPlan>() {
            @Override
            public void onTaskCompleteSuccess(FloorPlan result) {
                floorPlanModel.setFloorPlan(result);
                drawingModel.setPixelsPerInterval(12.5f);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(LocalisationActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    /**
     * Open the logs of a measure file to display the measures to the user
     * @param file the selected measure file
     */
    private void openLogs(File file) {
        taskManager.executeTask(new LoadLogTask(), file, "Loading" + file.getName() + " ...", new OnAsyncTaskCompleteListener<List<Measure>>() {
            @Override
            public void onTaskCompleteSuccess(List<Measure> result) {
                final Dialog d = new Dialog(LocalisationActivity.this);
                d.setContentView(new LogView(LocalisationActivity.getInstance(), result));
                d.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                displayNewDialog(d);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(LocalisationActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    /**
     * Convert the measure file to a format that can be copy pasted in excel
     * @param file the selected file
     */
    private void convertLogs(File file) {
        taskManager.executeTask(new ConvertLogTask(), file, "Converting log...", new OnAsyncTaskCompleteListener<File>() {
            @Override
            public void onTaskCompleteSuccess(File result) {
                System.out.println("File converted");
            }

            @Override
            public void onTaskFailed(Exception cause) {

            }
        }, false);
    }

    /**
     * Open a floor plan file to be loaded in the application.
     *
     * @param v the clicked button
     */
    public void handleOpenFileClick(View v) {
        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                // can not be reached
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                Log.d("DEBUG", file.getAbsolutePath());
                source.dismiss();
                openFloorPlan(file);
            }
        });
        dialog.setFilter(".*xml|.*XML");
        dialog.setShowOnlySelectable(true);
        dialog.setTitle("Select file to open");
        displayNewDialog(dialog);
    }

    /**
     * Open the required dialogue to choose the file to be converted
     * @param v the pressed button
     */
    public void handleConvert(View v) {

        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                // can not be reached
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                convertLogs(file);
                Log.d("DEBUG", file.getAbsolutePath());
                source.dismiss();
            }
        });
        dialog.loadFolder(path + "/logs");
        dialog.setFilter(".*xml|.*XML");
        dialog.setShowOnlySelectable(true);
        dialog.setTitle("Select file to open");
        displayNewDialog(dialog);

    }

    /**
     * Open the required dialogue to choose the log to be displayed
     * @param v the pressed button
     */
    public void handleOpenLog(View v) {

        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                // can not be reached
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                Log.d("DEBUG", file.getAbsolutePath());
                source.dismiss();
                openLogs(file);
            }
        });
        dialog.loadFolder(path + "/logs");
        dialog.setFilter(".*xml|.*XML");
        dialog.setShowOnlySelectable(true);
        dialog.setTitle("Select file to open");
        displayNewDialog(dialog);

    }

    /**
     * Take a screenshot of the localisation view and save it to a given destination
     * @param v the pressed button
     */
    public void handleScreenshot(View v) {
        localisationView.invalidate();
        localisationView.destroyDrawingCache();
        localisationView.setDrawingCacheEnabled(false);
        localisationView.setDrawingCacheEnabled(true);
        localisationView.buildDrawingCache();
        final Bitmap bm = localisationView.getDrawingCache();

        final Dialog d = new Dialog(this);
        d.setTitle(R.string.saveScreenshot);
        d.setContentView(R.layout.save_name);
        d.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                source.dismiss();
                if (!name.toLowerCase().endsWith(".png")) {
                    name += ".png";
                }
                File file = new File(folder, name);
                performTask(file);
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                source.dismiss();
                performTask(file);
            }

            private void performTask(File file) {
                SaveImageParams params = new SaveImageParams(bm, file);
                taskManager.executeTask(new SaveImageTask(), params, "saving...", new OnAsyncTaskCompleteListener<File>() {
                    @Override
                    public void onTaskCompleteSuccess(File result) {
                        Toast.makeText(LocalisationActivity.this, "Saved successful to " + result.getName(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTaskFailed(Exception cause) {
                        Log.e(TAG, cause.getMessage(), cause);
                        Toast.makeText(LocalisationActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, true);
            }
        });
        dialog.setFilter(".*png|.*PNG");
        dialog.setShowOnlySelectable(true);
        dialog.setCanCreateFiles(true);
        dialog.setTitle("Select file to save to or create a new one");
        displayNewDialog(dialog);
    }

    /**
     * Show a new file dialogue
     * @param d the dialogue
     */
    private void displayNewDialog(Dialog d) {
        dismissDialog();
        dialog = d;
        dialog.show();
    }

    /**
     * Hide the dialogue
     */
    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * Switches between the available views when a button is pressed.
     * @param view the pressed button
     */
    public void flip(View view) {
        mainActive.setEnabled(true);
        mainActive = view;
        view.setEnabled(false);

        Object o = view.getTag();
        if (o.equals("fingerprint")) {
            localisationModel.initializeFingerprintMode();
        } else if (o.equals("localisation")) {
            localisationModel.initializeLocalisationMode();
        } else if (o.equals("measure")) {
            localisationModel.initializeMeasureMode();
        }
        for (int i = 0; i < mainFlip.getChildCount(); i++) {
            if (mainFlip.getChildAt(i).getTag().equals(o)) {
                if (o.equals("measure")) {
                    MeasureView measure = (MeasureView) mainFlip.getChildAt(i);
                    measure.loadText();
                } else if (o.equals("fingerprint")) {
                    FingerprintView fingerprint = (FingerprintView) mainFlip.getChildAt(i);
                    fingerprint.updateCheckBox();
                }
                mainFlip.setDisplayedChild(i);
                localisationModel.refreshView();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(stepListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(stepListener, stepCounter, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        localisationModel.setNavigateTo(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("Niks jong");
    }
}
