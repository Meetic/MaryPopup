package com.meetic.marypopup.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.popup)
    public void launchPopup() {
        startActivity(new Intent(this, PopupActivity.class));
    }

    @OnClick(R.id.popupDraggable)
    public void launchPopupDraggable() {
        startActivity(new Intent(this, PopupDraggableActivity.class));
    }

    @OnClick(R.id.popupNotDraggable)
    public void launchPopupNotDraggable() {
        startActivity(new Intent(this, PopupNotDraggableActivity.class));
    }

    @OnClick(R.id.popupScaleDownDraggable)
    public void launchPopupScaleDownDraggable() {
        startActivity(new Intent(this, PopupScaleDownDraggableActivity.class));
    }

    @OnClick(R.id.popupFadeOutDraggable)
    public void launchPopupFadeOutDraggable() {
        startActivity(new Intent(this, PopupFadeOutDraggableActivity.class));
    }

    @OnClick(R.id.popupCenter)
    public void launchPopupCenter() {
        startActivity(new Intent(this, PopupCenterActivity.class));
    }

    @OnClick(R.id.popupImage)
    public void launchPopupImage() {
        startActivity(new Intent(this, PopupImageActivity.class));
    }

}
