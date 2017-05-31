/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.learnncode.mediachooser.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.MediaChooserConstants;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.fragment.ImageFragment;

public class HomeFragmentActivity extends Activity implements ImageFragment.OnImageSelectedListener {
    private final Handler handler = new Handler();
    private TextView headerBarTitle;
    private ImageView headerBarBack;
    private TextView headerBarDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_media_chooser);
        MediaChooserConstants.SELECTED_MEDIA_COUNT = 0;

        headerBarTitle = (TextView) findViewById(R.id.titleTextViewFromMediaChooserHeaderBar);
        headerBarBack = (ImageView) findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
        headerBarDone = (TextView) findViewById(R.id.doneTextViewViewFromMediaChooserHeaderView);

        headerBarDone.setVisibility(View.VISIBLE);

        headerBarBack.setOnClickListener(clickListener);
        headerBarDone.setOnClickListener(clickListener);

        ImageFragment imageFragment = new ImageFragment();

        if (getIntent() != null && (getIntent().getBooleanExtra("isFromBucket", false))) {
            if (getIntent().getBooleanExtra("image", false)) {
                headerBarTitle.setText(getIntent().getStringExtra("name"));
                Bundle bundle = new Bundle();
                bundle.putString("name", getIntent().getStringExtra("name"));
                imageFragment.setArguments(bundle);
            }
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.imgGallaryContainer, imageFragment, "tab1");
        fragmentTransaction.show(imageFragment);
        fragmentTransaction.commit();
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == headerBarDone) {
                ImageFragment imageFragment = (ImageFragment) getFragmentManager().findFragmentByTag("tab1");

                if (imageFragment != null) {

                    if (imageFragment != null) {
                        if (imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList().size() > 0) {
                            BucketHomeFragmentActivity.mBucketHomeFragmentActivity.finish();
                            Intent imageIntent = new Intent();
                            imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
                            imageIntent.putStringArrayListExtra("list", imageFragment.getSelectedImageList());
                            sendBroadcast(imageIntent);
                        }
                    }
                    finish();
                } else {
                    Toast.makeText(HomeFragmentActivity.this, getString(R.string.plaese_select_file), Toast.LENGTH_SHORT).show();
                }
            } else if (view == headerBarBack) {
                MediaChooserConstants.SELECTED_MEDIA_COUNT = 0;
                finish();
            }
        }
    };

    @Override
    public void onImageSelected(int count) {
        Log.e("TAGGG",count+"");
    }
}
