package com.meetic.marypopup.sample;

import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PopupCenterActivity extends PopupActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void setUpPopup() {
        setContentView(R.layout.activity_popup_center);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        popup = createMaryPopup()
                .draggable(true)
                .scaleDownDragging(true)
                .fadeOutDragging(true)
                .center(true);
    }
}
