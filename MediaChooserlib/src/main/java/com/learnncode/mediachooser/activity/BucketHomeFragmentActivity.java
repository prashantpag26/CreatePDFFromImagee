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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnncode.mediachooser.MediaChooserConstants;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.fragment.BucketImageFragment;

public class BucketHomeFragmentActivity extends Activity {

    private TextView headerBarDone;
    private ImageView headerBarBack;
    public static Activity mBucketHomeFragmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_media_chooser);

        mBucketHomeFragmentActivity = this;

        headerBarBack = (ImageView) findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
        headerBarDone = (TextView) findViewById(R.id.doneTextViewViewFromMediaChooserHeaderView);

        headerBarDone.setVisibility(View.GONE);

        headerBarBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaChooserConstants.SELECTED_MEDIA_COUNT = 0;
                finish();
            }
        });

        MediaChooserConstants.SELECTED_MEDIA_COUNT = 0;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        BucketImageFragment newImageFragment = new BucketImageFragment();
        fragmentTransaction.add(R.id.imgGallaryContainer, newImageFragment, "tab1");
        fragmentTransaction.commit();
    }
}
