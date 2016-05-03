package com.meetic.marypopup.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meetic.marypopup.MaryPopup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupCenterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.cardView) View cardView;
    MaryPopup popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_center);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        popup = MaryPopup.with(this)
            .cancellable(true)
            .draggable(true)
            .scaleDownDragging(true)
            .fadeOutDragging(true)
            .center(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            .backgroundColor(Color.parseColor("#EFF4F5"));
    }

    @OnClick(R.id.cardView)
    public void onClickCardView() {
        popup
            .content(R.layout.popup_content)
            .from(cardView)
            .show();
    }

    @Override
    public void onBackPressed() {
        if(popup.isOpened()){
            popup.close(true);
        } else {
            super.onBackPressed();
        }
    }
}
