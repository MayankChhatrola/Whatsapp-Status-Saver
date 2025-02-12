package com.example.whtsappstatussaver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.bumptech.glide.Glide;
import com.example.whtsappstatussaver.fragments.SavedFragment;
import com.example.whtsappstatussaver.model.ImageModel;
import com.example.whtsappstatussaver.util.AndroidXI;
import com.example.whtsappstatussaver.util.Application;
import com.example.whtsappstatussaver.util.HelperMethods;
import com.example.whtsappstatussaver.util.Log;

import java.io.File;
import java.util.ArrayList;

public class ViewDataActivity extends AppCompatActivity {

    Toolbar toolbarvideoplayer;
    HelperMethods modelhelps;
    int position = 0;
    File file;
    Uri uri;
    ImageView imgDelete,  imgShareStatus;
    TextView txtDelete;
    private boolean isVideoDownloaded = false;
    private Uri videoDeletePath;
    Intent videointent;
    private boolean isFromCreation = false;
    //    RecyclerView recyclerViewVideo;
    public static ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    ImageView imgBack;
    boolean isFromHome = false;
    VideoView videoPlayer;
    ImageView imgPlayPause, imgPhoto;
    RelativeLayout rltVideoView, rltImgView;
    MediaController waMediaController;
    File directory;
    ImageView iv_imgdelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_data);


        bindView();
        init();
        addListener();

        /*if (adView == null && fbadView == null) {

        } else {

            if (new PrefManager(ViewDataActivity.this).getString(IConstant.AddType).equals("google_ad")) {
                adaptivebannerAd();

            } else {
                loadAdB();

            }
        }*/
    }
   /* private void loadAdB() {

        fbadView = new com.facebook.ads.AdView(this, new PrefManager(getApplicationContext()).getString(IConstant.FBBannerAd), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container1);
        adContainer.addView(fbadView);
        fbadView.loadAd();

    }

    public void adaptivebannerAd() {
        adContainerView = findViewById(R.id.adaptivebanner);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(new PrefManager(getApplicationContext()).getString(IConstant.GoogleBannerAd));
        adContainerView.addView(adView);
        loadBanner();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void loadBanner() {

        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }*/

    private void bindView() {
        toolbarvideoplayer = findViewById(R.id.toolbarvideoplayer);
        imgDelete = findViewById(R.id.imgDelete);
        txtDelete = findViewById(R.id.txtDelete);

        imgShareStatus = findViewById(R.id.imgShareStatus);
//        recyclerViewVideo = findViewById(R.id.recyclerViewVideo);
        imgBack = findViewById(R.id.imgBack);

        videoPlayer = findViewById(R.id.videoPlayer);
        imgPlayPause = findViewById(R.id.imgPlayPause);
        imgPhoto = findViewById(R.id.imgPhoto);
        rltVideoView = findViewById(R.id.rltVideoView);
        rltImgView = findViewById(R.id.rltImgView);
        iv_imgdelete = findViewById(R.id.iv_imgdelete);
    }

    private void init() {

        modelhelps = new HelperMethods(ViewDataActivity.this);
        setSupportActionBar(toolbarvideoplayer);
        toolbarvideoplayer.bringToFront();

        try {
            videointent = getIntent();
            if (videointent != null) {
                if (videointent.hasExtra("isFromHome")) {
                    isFromHome = true;
                }

                file = new File(videointent.getExtras().getString("pos"));
                uri = Uri.parse(videointent.getExtras().getString("uri"));
                this.position = videointent.getExtras().getInt("position");
                imageModelArrayList.get(position).getsOfflinePath();

                if (videointent.hasExtra("isDownload")) {
                    isVideoDownloaded = true;
                    isFromCreation = true;
                } else {
                    checkDownloaded();
                }
            }

            if (file.getAbsolutePath().endsWith(".mp4") || file.getAbsolutePath().endsWith("mkv")
                    || file.getAbsolutePath().toLowerCase().endsWith("mov")
                    || file.getAbsolutePath().toLowerCase().endsWith("gif")
                    || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                    || file.getAbsolutePath().toLowerCase().endsWith("avi")
                    || file.getAbsolutePath().toLowerCase().endsWith("flv")) {
            }

//            recyclerViewVideo.setLayoutManager(new LinearLayoutManager(WhtsappVideoPlayerActivity.this, RecyclerView.HORIZONTAL, false));
            PagerSnapHelper snapHelper = new PagerSnapHelper();
//            snapHelper.attachToRecyclerView(recyclerViewVideo);
//            recyclerViewVideo.setAdapter(new AdapterWhtsappPlayVideo(WhtsappVideoPlayerActivity.this, waImageModelArrayList));
//            recyclerViewVideo.scrollToPosition(position);


//            recyclerViewVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerViewVideo.getLayoutManager());
//                    position = linearLayoutManager.findFirstVisibleItemPosition();
            if (position != -1) {
                file = new File(imageModelArrayList.get(position).getsPath());
                uri = imageModelArrayList.get(position).getContentUri();
                if (getIntent() != null) {
                    if (getIntent().hasExtra("isDownload")) {
                        isVideoDownloaded = true;
                        Toast.makeText(ViewDataActivity.this, "Download Sucessfully", Toast.LENGTH_SHORT).show();

                    } else {
                        checkDownloaded();
                    }
                }
            }

            if (file.getAbsolutePath().endsWith(".mp4") || file.getAbsolutePath().endsWith("mkv")
                    || file.getAbsolutePath().toLowerCase().endsWith("mov")
                    || file.getAbsolutePath().toLowerCase().endsWith("gif")
                    || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                    || file.getAbsolutePath().toLowerCase().endsWith("avi")
                    || file.getAbsolutePath().toLowerCase().endsWith("flv")) {

            } else {

            }
//
//                }
//            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {

        if (file.getAbsolutePath().toLowerCase().endsWith("mp4")
                || file.getAbsolutePath().toLowerCase().endsWith("mkv")
                || file.getAbsolutePath().toLowerCase().endsWith("mov")
                || file.getAbsolutePath().toLowerCase().endsWith("gif")
                || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                || file.getAbsolutePath().toLowerCase().endsWith("avi")
                || file.getAbsolutePath().toLowerCase().endsWith("flv")) {
            rltVideoView.setVisibility(View.VISIBLE);
            rltImgView.setVisibility(View.GONE);

            waMediaController = new MediaController(ViewDataActivity.this, false);
            waMediaController.setAnchorView(videoPlayer);

            videoPlayer.setMediaController(null);
            videoPlayer.setVideoURI(uri);
            videoPlayer.requestFocus();
            videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    android.util.Log.e("WhatIsIt", "onPrepared");
                    int videoWidth = mp.getVideoWidth();
                    int videoHeight = mp.getVideoHeight();
                    float videoProportion = (float) videoWidth / (float) videoHeight;
                    int screenWidth = rltVideoView.getMeasuredWidth();
                    int screenHeight = rltVideoView.getMeasuredHeight();
                    float screenProportion = (float) screenWidth / (float) screenHeight;
                    ViewGroup.LayoutParams lp = videoPlayer.getLayoutParams();

                    if (videoProportion > screenProportion) {
                        lp.width = screenWidth;
                        lp.height = (int)((float) screenWidth / videoProportion);
                    } else {
                        lp.width = (int) (videoProportion * (float) screenHeight);
                        lp.height = screenHeight;
                    }
                    videoPlayer.setLayoutParams(lp);
                    videoPlayer.start();
                    imgPlayPause.setImageResource(R.drawable.vdo_pause);
                }
            });


            videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (videoPlayer != null) {
                        imgPlayPause.setVisibility(View.VISIBLE);
                        imgPlayPause.setImageResource(R.drawable.vdo_play);
                    }
                }
            });

            videoPlayer.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (imgPlayPause.getVisibility() == View.GONE) {
                        imgPlayPause.setVisibility(View.VISIBLE);
                    } else {
                        imgPlayPause.setVisibility(View.GONE);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgPlayPause.setVisibility(View.GONE);
                        }
                    }, 5000);
                    return false;
                }
            });

            imgPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoPlayer.isPlaying()) {
                        android.util.Log.e("WhatIsIt", "Image");
                        videoPlayer.pause();
                        imgPlayPause.setImageResource(R.drawable.vdo_play);
                    } else {
                        android.util.Log.e("WhatIsIt", "start");
                        videoPlayer.start();
                        imgPlayPause.setImageResource(R.drawable.vdo_pause);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgPlayPause.setVisibility(View.GONE);
                        }
                    }, 5000);
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgPlayPause.setVisibility(View.GONE);
                }
            }, 5000);
        } else {

            android.util.Log.e("WhatIsIt", "Image");
//            if(waLastViewHolder != null){
//                waLastViewvideoPlayer.pause();
//            }
            if (videoPlayer != null) {
                android.util.Log.e("WhatIsIt", "not null");
                videoPlayer.pause();
                //player = null;
            }
//            waLastViewHolder = null;
            rltVideoView.setVisibility(View.GONE);
            rltImgView.setVisibility(View.VISIBLE);
            Glide.with(this).load(uri).into(imgPhoto);

        }


        //save
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isVideoDownloaded) {
                        boolean isVideo = false;
                        if (file.getAbsolutePath().toLowerCase().endsWith("mp4")
                                || file.getAbsolutePath().toLowerCase().endsWith("mkv")
                                || file.getAbsolutePath().toLowerCase().endsWith("mov")
                                || file.getAbsolutePath().toLowerCase().endsWith("gif")
                                || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                                || file.getAbsolutePath().toLowerCase().endsWith("avi")
                                || file.getAbsolutePath().toLowerCase().endsWith("flv")) {
                            isVideo = true;
                        } else {
                            isVideo = false;
                        }
                        File newFile = new File(imageModelArrayList.get(position).getsOfflinePath());
                        Uri newUri = new HelperMethods(ViewDataActivity.this).transfer(new File((String) file.getAbsolutePath()), isVideo, uri, newFile.getName());
                        Log.w("WhtsappVideoPlayer", "new Uri : " + newUri.toString());
                        //imgDelete.setImageResource(R.drawable.ic_delete);
                       // imgDelete.setTag(R.drawable.ic_delete);
                        videoDeletePath = newUri;
                        isVideoDownloaded = true;
                        imageModelArrayList.get(position).setContentUri(videoDeletePath);
                        String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppSaver_2022/" + Application.WHATSAPP_FOLDER;
                        directory = new File(path);
                        //File[] files = directory.listFiles();
                        String delete = directory.getPath();
                        new PrefManager(ViewDataActivity.this).setString(IConstant.deleteuri, delete);

                        //txtDelete.setText(getResources().getString(R.string.del_btn));
                    }/* else {

                        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
                        Uri demo = Uri.parse(sh.getString("image", ""));
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ViewDataActivity.this, R.style.AppThemes);
                        builder.setTitle(R.string.deletetitle);
                        builder.setMessage(getResources().getString(
                                R.string.deleteMessage_vdo));
                        builder.setPositiveButton(getString(R.string.delete_btn),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            if (isFromCreation) {
                                                digStash(demo);
                                            } else {
                                                digStash(demo);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        builder.setNegativeButton(getString(R.string.cancel_btn), null);
                        builder.show();
                    }*/

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        //delete
        iv_imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    /*if (!isVideoDownloaded) {
                        boolean isVideo = false;
                        if (file.getAbsolutePath().toLowerCase().endsWith("mp4")
                                || file.getAbsolutePath().toLowerCase().endsWith("mkv")
                                || file.getAbsolutePath().toLowerCase().endsWith("mov")
                                || file.getAbsolutePath().toLowerCase().endsWith("gif")
                                || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                                || file.getAbsolutePath().toLowerCase().endsWith("avi")
                                || file.getAbsolutePath().toLowerCase().endsWith("flv")) {
                            isVideo = true;
                        } else {
                            isVideo = false;
                        }
                        File newFile = new File(imageModelArrayList.get(position).getsOfflinePath());
                        Uri newUri = new HelperMethods(ViewDataActivity.this).transfer(new File((String) file.getAbsolutePath()), isVideo, uri, newFile.getName());
                        Toast.makeText(ViewDataActivity.this, newUri.toString(), Toast.LENGTH_SHORT).show();
                        Log.w("WhtsappVideoPlayer", "new Uri : " + newUri.toString());
                        imgDelete.setImageResource(R.drawable.ic_delete);
                        imgDelete.setTag(R.drawable.ic_delete);
                        videoDeletePath = newUri;
                        isVideoDownloaded = true;
                        imageModelArrayList.get(position).setContentUri(newUri);
                        String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppSaver_2022/" + Application.WHATSAPP_FOLDER;
                        File directory = new File(path);
                        File[] files = directory.listFiles();

                        String delete = directory.getPath();

                        *//*SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        // Creating an Editor object to edit(write to the file)
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        // Storing the key and its value as the data fetched from imageview
                        myEdit.putString("image",delete);
                        myEdit.commit();*//*
                        txtDelete.setText(getResources().getString(R.string.del_btn));
                    } else {*/
                    Uri nndd = Uri.parse(new PrefManager(ViewDataActivity.this).getString(IConstant.deleteuri));

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDataActivity.this, R.style.AppThemes);
                        builder.setTitle(R.string.deletetitle);
                        builder.setMessage(getResources().getString(R.string.deleteMessage_vdo));
                        builder.setPositiveButton(getString(R.string.delete_btn),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            if (isFromCreation) {
                                                digStash(nndd);
                                            } else {
                                                digStash(nndd);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        builder.setNegativeButton(getString(R.string.cancel_btn), null);
                        builder.show();
                    /* } */
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        imgShareStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                if (file.getAbsolutePath().endsWith(".mp4") || file.getAbsolutePath().endsWith("mkv")
                        || file.getAbsolutePath().toLowerCase().endsWith("mov")
                        || file.getAbsolutePath().toLowerCase().endsWith("gif")
                        || file.getAbsolutePath().toLowerCase().endsWith("3gp")
                        || file.getAbsolutePath().toLowerCase().endsWith("avi")
                        || file.getAbsolutePath().toLowerCase().endsWith("flv")) {

                    shareIntent.setType("video/*");

                } else {
                    shareIntent.setType("image/*");
                }

                shareIntent.setType("image/*");
                /*shareIntent.putExtra("android.intent.extra.TEXT", getString(R.string.share_text)
                        + " https://play.google.com/store/apps/details?id="
                        + getPackageName());*/
                shareIntent.putExtra("android.intent.extra.STREAM", imageModelArrayList.get(position).getContentUri());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_img)));
            }
        });
    }


    private void checkDownloaded() {

        Log.w("WhtsappVideoPlayer", "path : " + new File(imageModelArrayList.get(position).getsOfflinePath()).exists());
        File file = new File(imageModelArrayList.get(position).getsOfflinePath());
        boolean bool = false;
        if (!bool) {
            imgDelete.setImageResource(R.drawable.whtsapp_download);
            txtDelete.setText(getResources().getString(R.string.down_btn));
            imgDelete.setTag(R.drawable.whtsapp_download);
            isVideoDownloaded = false;
        } else {
            videoDeletePath = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider",
                    new File(imageModelArrayList.get(position).getsOfflinePath()));
            Log.i("videoPath", videoDeletePath + "");
            isVideoDownloaded = true;

            imgDelete.setImageResource(R.drawable.ic_delete);
            txtDelete.setText(getResources().getString(R.string.del_btn));
            imgDelete.setTag(R.drawable.ic_delete);
        }
    }

    public ActivityResultLauncher<IntentSenderRequest> launcher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),

            result -> {
                Log.e("ActivityResult", "call");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.e("ActivityResult", "result OK");
                    Toast.makeText(this, this.getString(R.string.deleteMessage_sucess), Toast.LENGTH_SHORT).show();
                    if (!isFromHome) {
                        Log.e("ActivityResult", "!isFromHome");
                        Intent intent = new Intent();
                        intent.putExtra("pos", this.position);
                        setResult(-1, intent);
                        finish();
                    } else {
                        onBackPressed();
                    }

                    Log.e("ActivityResult", "notifyDataSetChanged");
                    try {
                        if (SavedFragment.adapterSaved != null)
                            SavedFragment.adapterSaved.notifyDataSetChanged();

//                        if (WhtsappImageFragment.adapterWhtsappImage != null)
//                            WhtsappImageFragment.adapterWhtsappImage.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


    public void digStash(@NonNull Uri uri) {
        Log.e("digStash", uri.toString());
        //Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
        boolean isDelete = AndroidXI.getInstance().with(this).deleteWhatsapp(launcher, uri);
        if (isDelete) {
            //Toast.makeText(this, this.getString(R.string.deleteMessage_sucess), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewDataActivity.this, MainActivity.class));
            finish();
            if (!isFromHome) {
                Intent intent = new Intent();
                intent.putExtra("pos", this.position);
                setResult(-1, intent);
                MediaScannerConnection.scanFile(ViewDataActivity.this,
                        new String[]{file.getAbsolutePath()},
                        new String[]{"image/*", "video/*"}, null);
                MainActivity.refreshAll();
//                finish();
            } else {
                onBackPressed();
            }
            try {
                if (SavedFragment.adapterSaved != null)
                    SavedFragment.adapterSaved.notifyDataSetChanged();

//                if (WhtsappImageFragment.adapterWhtsappImage != null)
//                    WhtsappImageFragment.adapterWhtsappImage.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    public void sendBackData() {
        if (this.file.exists()) {
            this.file.delete();
        }
        Intent intent = new Intent();
        intent.putExtra("pos", this.position);
        setResult(-1, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (player!=null) {
//            this.player.pause();
//            ivPlayPause.setImageResource(R.drawable.vdo_play);
//        }
    }


//    @Override
//    public void onClickVideoFrame(EasyVideoPlayer easyVideoPlayer) {
//        if (this.menu.isMenuButtonHidden()) {
//            this.menu.showMenuButton(true);
//            easyVideoPlayer.hideControls();
//            return;
//        }
//        this.menu.hideMenuButton(true);
//        easyVideoPlayer.showControls();
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}