package com.yuvi.hamroui.audio;

public interface PlayerAdapter {

    void loadMedia(String fileName);

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();

    void initializeProgressCallback();

    void seekTo(int position);
}