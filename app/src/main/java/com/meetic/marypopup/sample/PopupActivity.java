package com.meetic.marypopup.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.meetic.marypopup.MaryPopup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupActivity extends AppCompatActivity {

    @Bind(R.id.cardView) View cardView;
    MaryPopup popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        popup = MaryPopup.with(this)
            .cancellable(true)
            .draggable(true)
            .scaleDownDragging(true)
            .fadeOutDragging(true)
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
