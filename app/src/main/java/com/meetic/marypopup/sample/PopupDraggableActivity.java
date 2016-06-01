package com.meetic.marypopup.sample;

import butterknife.ButterKnife;

public class PopupDraggableActivity extends PopupActivity {

    @Override
    protected void setUpPopup() {
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        popup = createMaryPopup()
            .draggable(true);
    }
}
