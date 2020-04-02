package com.example.android.azaudioplayerv001;

public class Audio {
    private String mName;
    private int mAudioResourse , mImageResource;

    public Audio(String mName, int mAudioResourse) {
        this.mName = mName;
        this.mAudioResourse = mAudioResourse;
        mImageResource = R.drawable.no_image;
    }

    public Audio(String mName, int mAudioResourse , int mImageResource) {
        this.mName = mName;
        this.mAudioResourse = mAudioResourse;
        this.mImageResource = mImageResource;
    }

    public String getmName() {
        return mName;
    }

    public int getmAudioResourse() {
        return mAudioResourse;
    }

    public int mImageResource() {
        return mImageResource;
    }

}
