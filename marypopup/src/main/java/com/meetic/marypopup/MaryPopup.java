package com.meetic.marypopup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meetic.dragueur.Direction;
import com.meetic.dragueur.DraggableView;
import com.meetic.dragueur.ExitViewAnimator;
import com.meetic.dragueur.ReturnOriginViewAnimator;
import com.meetic.marypopup.DurX.Listeners;

import java.lang.ref.WeakReference;

/**
 * Created by florentchampigny on 14/04/2016.
 */
public class MaryPopup implements View.OnClickListener {

    final Activity activity;
    final ViewGroup activityView;
    final View actionBarView;

    View viewOrigin;

    @Nullable
    View blackOverlay;
    int blackOverlayColor = Color.parseColor("#CC333333");

    @Nullable
    ViewGroup popupView;
    @Nullable
    ViewGroup popupViewContent;

    Integer popupBackgroundColor;

    float differenceScaleX;
    float differenceTranslationX;
    float differenceScaleY;
    float differenceTranslationY;

    View contentLayout;
    int height = -1;
    int width = -1;
    boolean cancellable;

    long openDuration = 200;
    long closeDuration = 200;

    boolean center = false;
    boolean draggable = false;
    boolean scaleDownDragging = false;
    boolean fadeOutDragging = false;
    boolean inlineMove = true;
    boolean shadow = true;
    boolean scaleDownCloseOnDrag = false;
    boolean scaleDownCloseOnClick = true;

    boolean handleClick = false;
    boolean isAnimating = false;

    MaryPopup(Activity activity) {
        this.activity = activity;
        this.activityView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.actionBarView = activityView.findViewById(R.id.action_bar_container);
    }

    public static MaryPopup with(Activity activity) {
        return new MaryPopup(activity);
    }

    public MaryPopup from(View viewOrigin) {
        this.viewOrigin = viewOrigin;
        return this;
    }

    public MaryPopup content(int contentLayoutId) {
        View contentView = LayoutInflater.from(activity).inflate(contentLayoutId, popupView, false);
        content(contentView);
        return this;
    }

    public MaryPopup content(View view) {
        this.contentLayout = view;
        return this;
    }

    public MaryPopup width(int width) {
        this.width = width;
        return this;
    }

    public MaryPopup height(int height) {
        this.height = height;
        return this;
    }

    public MaryPopup blackOverlayColor(int blackOverlayColor) {
        this.blackOverlayColor = blackOverlayColor;
        return this;
    }

    public MaryPopup backgroundColor(Integer popupBackgroundColor) {
        this.popupBackgroundColor = popupBackgroundColor;
        return this;
    }

    public MaryPopup cancellable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    public MaryPopup draggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public MaryPopup inlineMove(boolean inlineMove) {
        this.inlineMove = inlineMove;
        return this;
    }

    public MaryPopup fadeOutDragging(boolean fadeOutDragging) {
        this.fadeOutDragging = fadeOutDragging;
        return this;
    }

    public MaryPopup scaleDownDragging(boolean scaleDownDragging) {
        this.scaleDownDragging = scaleDownDragging;
        return this;
    }

    public MaryPopup openDuration(long duration) {
        this.openDuration = duration;
        return this;
    }

    public MaryPopup closeDuration(long duration) {
        this.closeDuration = duration;
        return this;
    }

    public MaryPopup shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public MaryPopup scaleDownCloseOnDrag(boolean scaleDownCloseOnDrag) {
        this.scaleDownCloseOnDrag = scaleDownCloseOnDrag;
        return this;
    }

    public MaryPopup scaleDownCloseOnClick(boolean scaleDownCloseOnClick) {
        this.scaleDownCloseOnClick = scaleDownCloseOnClick;
        return this;
    }

    public void show() {
        if (blackOverlay == null) {
            {
                handleClick = false;
                blackOverlay = new View(activity);
                blackOverlay.setBackgroundColor(blackOverlayColor);
                activityView.addView(blackOverlay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                DurX.putOn(blackOverlay)
                    .animate()
                    .alpha(0f, 1f);
                blackOverlay.setOnClickListener(this);
            }
            {
                if (draggable) {
                    popupView = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.popup_layout_draggable, activityView, false);
                } else {
                    popupView = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.popup_layout, activityView, false);
                }
                if (popupView != null) {
                    if (width >= 0) {
                        ViewGroup.LayoutParams layoutParams = popupView.getLayoutParams();
                        layoutParams.width = width;
                        popupView.setLayoutParams(layoutParams);
                    }
                    if (height >= 0) {
                        ViewGroup.LayoutParams layoutParams = popupView.getLayoutParams();
                        layoutParams.height = height;
                        popupView.setLayoutParams(layoutParams);
                    }

                    if(popupView instanceof DraggableView) {
                        DraggableView draggableView = (DraggableView) popupView;

                        draggableView.setDraggable(false);
                        draggableView.setInlineMove(inlineMove);
                        draggableView.setVertical(true);
                        draggableView.setListenVelocity(false);
                        draggableView.setMaxDragPercentageY(0.35f);

                        if (scaleDownCloseOnDrag) {
                            draggableView.setViewAnimator(new ReturnOriginViewAnimator() {
                                @Override
                                public boolean animateExit(@NonNull DraggableView draggableView, Direction direction, int duration) {
                                    close(true);
                                    return true;
                                }
                            });
                        } else {
                            draggableView.setViewAnimator(new ExitViewAnimator() {
                            });
                        }
                        draggableView.setDragListener(new DraggableViewListener(this));
                    }
                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    popupViewContent = (ViewGroup) popupView.findViewById(R.id.content);
                    if (!shadow) {
                        popupView.setBackgroundColor(popupBackgroundColor);
                    } else {
                        if (popupBackgroundColor != null && popupViewContent != null) {
                            popupViewContent.setBackgroundColor(popupBackgroundColor);
                        }
                        ViewCompat.setElevation(popupView, 6);
                    }
                    if (contentLayout != null && popupViewContent != null) {
                        if (contentLayout.getParent() != null) {
                            ((ViewGroup) contentLayout.getParent()).removeView(contentLayout);
                        }
                        popupViewContent.addView(contentLayout);
                    }

                    DurX.putOn(popupView)
                        .pivotX(0f)
                        .pivotY(0f)
                        .invisible()
                        .waitForSize(new Listeners.Size() {
                            @Override
                            public void onSize(DurX durX) {
                                if (viewOrigin != null) {
                                    differenceScaleX = viewOrigin.getWidth() * 1.0f / popupView.getWidth();
                                    differenceScaleY = viewOrigin.getHeight() * 1.0f / popupView.getHeight();

                                    float translationX;
                                    float translationY;

                                    if (center) {
                                        differenceTranslationX = getX(viewOrigin);
                                        differenceTranslationY = getY(viewOrigin);
                                        translationX = activityView.getWidth() / 2 - popupView.getWidth() / 2;
                                        translationY = activityView.getHeight() / 2 - popupView.getHeight() / 2;
                                    } else {
                                        differenceTranslationX = getX(viewOrigin) - getX(popupView);
                                        differenceTranslationY = getY(viewOrigin) - getY(popupView);
                                        translationX = getX(viewOrigin) - (popupView.getWidth() - viewOrigin.getWidth()) / 2f;
                                        translationY = getY(viewOrigin) - getStatusBarHeight();
                                    }

                                    DurX.putOn(popupView)
                                        .translationX(differenceTranslationX)
                                        .translationY(differenceTranslationY)
                                        .visible()

                                        .animate()
                                        .scaleX(differenceScaleX, 1f)
                                        .scaleY(differenceScaleY, 1f)
                                        .translationX(differenceTranslationX, translationX)
                                        .translationY(differenceTranslationY, translationY)
                                        .duration(openDuration)
                                        .end(new Listeners.End() {
                                            @Override
                                            public void onEnd() {
                                                if (popupView instanceof DraggableView) {
                                                    DraggableView draggableView = (DraggableView) popupView;
                                                    draggableView.initOriginalViewPositions();
                                                    draggableView.setDraggable(draggable);
                                                }
                                                blackOverlay.setClickable(true);
                                                handleClick = true;
                                            }
                                        })
                                        .pullOut()

                                        .andPutOn(popupViewContent)
                                        .visible()
                                        .animate()
                                        .startDelay(openDuration - 100)
                                        .alpha(0f, 1f);
                                }
                            }
                        });
                    activityView.addView(popupView);
                }
            }
        }
    }

    public boolean canClose() {
        return blackOverlay != null;
    }

    public boolean isOpened() {
        return blackOverlay != null;
    }

    public boolean close(final boolean withScaleDown) {
        if (blackOverlay != null) {

            handleClick = false;

            final Listeners.End clearListener = new Listeners.End() {
                @Override
                public void onEnd() {
                    activityView.removeView(blackOverlay);
                    blackOverlay = null;
                    activityView.removeView(popupView);
                    popupView = null;
                    isAnimating = false;
                }
            };

            isAnimating = true;
            if (withScaleDown) {

                float scaleX = viewOrigin.getWidth() * 1.0f / (popupView.getWidth() * ViewCompat.getScaleX(popupView));
                float scaleY = viewOrigin.getHeight() * 1.0f / (popupView.getHeight() * ViewCompat.getScaleY(popupView));

                float translationX;
                float translationY;

                if (center) {
                    translationX = 0;
                    translationY = 0;
                } else {
                    translationX = ViewCompat.getTranslationX(popupView);
                    translationY = ViewCompat.getTranslationY(popupView) - getStatusBarHeight();
                }

                float xViewOrigin = getX(viewOrigin);
                float yViewOrigin = getY(viewOrigin);

                float xPopupView = getX(popupView);
                float yPopupView = getY(popupView) - getStatusBarHeight();

                float tx = 0;
                if (xViewOrigin < xPopupView) {
                    tx = xPopupView - xViewOrigin;
                } else { // xViewOrigin > xPopupView
                    tx = xViewOrigin - xPopupView;
                }

                if (center) {
                    tx *= (1f - scaleX);
                }
                translationX += tx;

                float ty = 0;
                if (yViewOrigin < yPopupView) {
                    ty = yPopupView - yViewOrigin;
                } else { // yViewOrigin > yPopupView
                    ty = yViewOrigin - yPopupView;
                }

                if (center) {
                    ty *= (1f - scaleY);
                }
                translationY += ty;

                DurX.putOn(popupViewContent)
                    .animate()
                    .alpha(0f)
                    .duration(closeDuration)

                    .andAnimate(popupView)
                    .scaleX(scaleX)
                    .scaleY(scaleY)
                    .alpha(0f)
                    .translationX(translationX)
                    .translationY(translationY)
                    .duration(closeDuration)

                    .andAnimate(blackOverlay)
                    .alpha(0f)
                    .duration(closeDuration)
                    .end(clearListener)
                ;
            } else {
                DurX.putOn(blackOverlay)
                    .animate()
                    .alpha(0)
                    .duration(closeDuration)

                    .thenAnimate(popupViewContent)
                    .alpha(0)
                    .duration(closeDuration)
                    .end(clearListener);
            }

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (cancellable && handleClick) {
            close(scaleDownCloseOnClick);
        }
    }

    public MaryPopup center(boolean center) {
        this.center = center;
        return this;
    }

    public float getStatusBarHeight() {
        return getY(activityView);
    }

    float getY(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        float y = rect.top;
        if (y <= 0) {
            y = ViewCompat.getY(view);
        }
        return y;
    }

    float getX(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        float x = rect.left;
        if (x <= 0) {
            x = ViewCompat.getX(view);
        }
        return x;
    }

    static class DraggableViewListener extends DraggableView.DraggableViewListenerAdapter {

        WeakReference<MaryPopup> reference;

        public DraggableViewListener(MaryPopup popup) {
            this.reference = new WeakReference<>(popup);
        }

        @Override
        public void onDrag(DraggableView draggableView, float percentX, float percentY) {
            super.onDrag(draggableView, percentX, percentY);

            MaryPopup popup = reference.get();
            if (popup != null && !popup.isAnimating) {
                float percent = 1f - Math.abs(percentY);

                if (popup.fadeOutDragging) {
                    DurX.putOn(popup.popupView)
                        .alpha(percent);
                }

                if (popup.scaleDownDragging) {
                    float scale = Math.max(0.75f, percent);
                    DurX.putOn(popup.popupView)
                        .pivotX(0.5f)
                        .scale(scale);
                }
            }
        }

        @Override
        public void onDraggedStarted(DraggableView draggableView, Direction direction) {
            MaryPopup popup = reference.get();
            if (popup != null) {
                popup.close(false);
            }
        }
    }
}
