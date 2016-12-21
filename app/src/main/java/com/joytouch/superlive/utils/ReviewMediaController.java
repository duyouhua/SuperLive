package com.joytouch.superlive.utils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.pili.pldroid.player.IMediaController;

/**
 * Created by sks on 2016/4/23.
 */
public class ReviewMediaController extends FrameLayout implements IMediaController {
    public ReviewMediaController(Context context) {
        super(context);
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {

    }

    @Override
    public void show() {

    }

    @Override
    public void show(int i) {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public void setAnchorView(View view) {

    }
}
