package com.meetic.marypopup.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.meetic.poppers.Poppers;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupScaleDownDraggableActivity extends AppCompatActivity {

    @Bind(R.id.cardView) View cardView;
    Poppers poppers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        poppers = Poppers.with(this)
            .cancellable(true)
            .draggable(true)
            .scaleDownDragging(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            .backgroundColor(Color.parseColor("#EFF4F5"));
    }

    @OnClick(R.id.cardView)
    public void onClickCardView() {
        poppers
            .content(R.layout.popup_content)
            .from(cardView)
            .show();
    }

    @Override
    public void onBackPressed() {
        if(poppers.isOpened()){
            poppers.close(true);
        } else {
            super.onBackPressed();
        }
    }
}
