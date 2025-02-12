package com.example.whtsappstatussaver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.whtsappstatussaver.adapter.PagerAdapter;
import com.example.whtsappstatussaver.fragments.ImageFragment;
import com.example.whtsappstatussaver.fragments.SavedFragment;
import com.example.whtsappstatussaver.fragments.VideoFragment;
import com.example.whtsappstatussaver.util.Application;
import com.example.whtsappstatussaver.util.EPreferences;
import com.example.whtsappstatussaver.util.PermissionModelUtil;
import com.example.whtsappstatussaver.util.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ViewPager pager;
    
    public static TabLayout tbLayout;
    Toolbar tlbar;
    static LinearLayout lltLoader;

    static ImageFragment imgFragment;
    static VideoFragment videoFragment;
    static SavedFragment savedFragment;

    public static PagerAdapter pagerAdapter;

    private static final int PERMISSION_CODE_REQUEST = 201;
    public static final int MAIN_ACTIVITY_CODE_HOME = 11001;
    private static final int REQUEST_CODE_STORAGE_ACCESS = 1;


    private EPreferences ePreferences;
    Uri treeUri = null;
    boolean isFragmentLoaded = false;
    private ImageView iv_more;
    private ImageView iv_whatsapp;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_1);
        Permission.checkPermission(this);
        bindView();
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Application.setRunningActivity(this);
    }

    public Context mActContext = MainActivity.this;

    private void bindView() {
        tlbar = findViewById(R.id.mainToolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tbLayout = (TabLayout) findViewById(R.id.tbLayout);
        lltLoader = findViewById(R.id.lltLoader);
        iv_more=(ImageView)findViewById(R.id.iv_more);
        iv_whatsapp=(ImageView)findViewById(R.id.iv_whatsapp);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        iv_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(sendIntent, ""));
                startActivity(sendIntent);
            }
        });
    }

    private void init() {
        Permission.checkPermission(MainActivity.this);
        PermissionModelUtil.checkPermission(MainActivity.this);
        ePreferences = EPreferences.getInstance(this);
        setSupportActionBar(tlbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        getSupportActionBar().setElevation(0);
        if (Utils.checkP(MainActivity.this)) {
            loadData();
        } else {
            requestPermission();
        }
    }

    public void loadData() {
        if (Build.VERSION.SDK_INT >= 29) {
            dialog_whatsappFolder();
        } else {
            fatchFragment();
        }
    }

    private void dialog_whatsappFolder() {
        if (!TextUtils.isEmpty(ePreferences.getString(EPreferences.PREF_KEY_WHATSAPP_STORAGE_PERMISSION, ""))) {
            fatchFragment();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View inflate = View.inflate(this, R.layout.dialog_permission, null);
        ((TextView) inflate.findViewById(R.id.tvTitle)).setText(R.string.title_choose_whatsapp_folder);
        ((TextView) inflate.findViewById(R.id.tvMsg)).setText(R.string.message_whatsapp_folder_path);
        builder.setView(inflate);
        builder.setPositiveButton(R.string.button_open, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openStorageAccessFramework();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });

        AlertDialog create = builder.create();
        if (!isFinishing()) {
            create.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openStorageAccessFramework() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        DocumentFile file = DocumentFile.fromTreeUri(this, Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"));
        if (file != null) {
            Log.e(" INITIAL_URI", file.getUri().toString());
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, file.getUri());
        }
        startActivityForResult(intent, REQUEST_CODE_STORAGE_ACCESS);
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_STORAGE_ACCESS) {
            if (!(data == null || resultCode != -1 || data.getData() == null)) {
                treeUri = data.getData();
                Log.e(" openStorageAccess", treeUri.toString());
                if (treeUri.getPath().toLowerCase().contains("WhatsApp/Media/.Statuses".toLowerCase())) {
                    ePreferences.putString(EPreferences.PREF_KEY_WHATSAPP_STORAGE_PERMISSION, data.toString());

                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(treeUri, takeFlags);

                    Log.e("WStatus>onActResult", treeUri + "");
                    fatchFragment();

                } else {
                    dialog_whatsappFolder();
                    Toast.makeText(this, getString(R.string.incorrect_folder), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog_whatsappFolder();
            }
        }
    }

    public ActivityResultLauncher<IntentSenderRequest> launcher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(this, this.getString(R.string.deleteMessage_sucess), Toast.LENGTH_SHORT).show();
                    if (savedFragment != null) {
                        savedFragment.refresh();
                    }
                }
            });

    private void fatchFragment() {
        lltLoader.setVisibility(View.GONE);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/WhatsAppSaver_2022/" + Application.WHATSAPP_FOLDER);
        try{
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return;
                }
            }
        } catch (Exception e ){
            Toast.makeText(mActContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        imgFragment = new ImageFragment();
        videoFragment = new VideoFragment();
        savedFragment = new SavedFragment();

        pager.setOffscreenPageLimit(2);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);

        pagerAdapter.addTabs(getString(R.string.Photo), imgFragment);
        pagerAdapter.addTabs(getString(R.string.Video), videoFragment);

        pagerAdapter.addTabs(getString(R.string.save), savedFragment);

        pager.setAdapter(pagerAdapter);
        tbLayout.setupWithViewPager(pager);
        for (int i = 0; i < tbLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tbLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        tbLayout.setVisibility(View.VISIBLE);
        isFragmentLoaded = true;
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PermissionModelUtil.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:

                case PERMISSION_CODE_REQUEST:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), R.string.perm_granted, Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                    break;

                    case MAIN_ACTIVITY_CODE_HOME:
                        if (grantResults.length > 0) {
                            boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                            if (writeStorage) {
                                loadData();
                            } else {
                                if (!Utils.ratoinalCheckFragment(MainActivity.this)) {
                                    showMessageOKCancel("You need to allow access permissions", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                                } else {
                                    Utils.Frag_Req(MainActivity.this, MAIN_ACTIVITY_CODE_HOME);
                                }
                            }
                        }
                        break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.btn_ok, okListener)
                .setNegativeButton(R.string.cancel_btn, null)
                .create()
                .show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_CODE_REQUEST);
    }

    public static void refreshAll() {
        if (imgFragment != null) {
            imgFragment.refresh();
        }
        if (videoFragment != null) {
            videoFragment.refresh();
        }
        if (savedFragment != null) {
            savedFragment.refresh();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemid = menuItem.getItemId();

        if (itemid == R.id.menu_1) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,  getString(R.string.app_name) + " at here : "
                    + "https://play.google.com/store/apps/details?id="
                    + getPackageName());
            shareIntent.setType("text/plain");
            startActivity(shareIntent);

        } else if (itemid == R.id.menu_2) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (itemid == R.id.menu_4) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youngbreedtechnologies.blogspot.com/2022/03/status-saver-downloader.html")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}




