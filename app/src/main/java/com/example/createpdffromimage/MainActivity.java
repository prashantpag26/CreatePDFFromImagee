package com.example.createpdffromimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.BucketHomeFragmentActivity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DEST = "storage/emulated/0/Podcasts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBroadcastForMediaChooser();
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewPDF();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDir();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTextNewPDF();
            }
        });
    }

    private void deleteDir() {
        File dir = new File(Environment.getExternalStorageDirectory() +
                File.separator + "PDF");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }

    private void generateNewPDF() {
        Intent intent = new Intent(MainActivity.this, BucketHomeFragmentActivity.class);
        startActivity(intent);
    }

    private void generateTextNewPDF() {
        Intent intent = new Intent(MainActivity.this, FirstPdf.class);
        startActivity(intent);
    }

    private void setBroadcastForMediaChooser() {
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
    }

    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG", "Broadcast received********************");
            try {
                sendPhotoMediaToContentResult(intent.getStringArrayListExtra("list"), false);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Method to send the Selected Media File (Photo captured from Camera or selected from
     * Gallery) for multiple images
     * to the ContentResult Screen and process further to Upload it to the AWS Server
     *
     * @param arrImagePath array of image path from gallery
     */
    private void sendPhotoMediaToContentResult(final ArrayList<String> arrImagePath, final boolean isCapturedAttachment) throws IOException, DocumentException {
        for (int i = 0; i < arrImagePath.size(); i++) {
            Log.d(TAG, "Image path ++++++++++++++++++++++++++++++++++++++++" + arrImagePath.get(i));
            createPDF(arrImagePath);
        }
    }

    private void createPDF(final ArrayList<String> arrImagePath) throws IOException, DocumentException {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "PDF");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Image img = Image.getInstance(arrImagePath.get(0));
            Document document = new Document(img);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory() +
                    File.separator + "PDF" + File.separator +currentDateTimeString + ".pdf"));
            document.open();
            for (String image : arrImagePath) {
                img = Image.getInstance(image);
                // Scale to new height and new width of image
                document.setPageSize(img);
                document.newPage();
                img.setAbsolutePosition(0, 0);
                document.add(img);
            }
            document.close();
        }
    }
}

