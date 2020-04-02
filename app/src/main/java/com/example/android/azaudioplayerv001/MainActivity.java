package com.example.android.azaudioplayerv001;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean isPlaying;
    MediaPlayer player;
    ImageView imageView2, imgPlay;
    TextView tvDuration, tvName;
    ArrayList<Audio> audioArrayList;
    private AudioManager mAudioManager;
    int i = 0;
    int result;

    AudioManager.OnAudioFocusChangeListener mOnAudioFoucsChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                player.pause();
                player.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                player.stop();
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                player.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                player.start();
            }
        }
    };


    // on completion instance

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
            getNext();
        }
    };


    //on Item Instance
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            i = position;
            //audio focus
            // get permission
            GetPermission(position);
        }


    };

    private void GetPermission(int position) {
        releaseMediaPlayer();
        result = mAudioManager.requestAudioFocus(mOnAudioFoucsChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player = MediaPlayer.create(MainActivity.this, audioArrayList.get(position).getmAudioResourse());
            player.start();
            isPlaying = true;
            imgPlay.setImageResource(R.drawable.ic_action_pause);
        }
        player.setOnCompletionListener(mCompletionListener);

        tvName.setText(audioArrayList.get(position).getmName());
        tvDuration.setText(formateMilliSeccond(player.getDuration()));

        imageView2.setImageResource(audioArrayList.get(position).mImageResource());
        imageView2.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set boolean to false cuz it is not playing yet!
        isPlaying = false;
        //find by id
        imageView2 = findViewById(R.id.imageView2);

        tvName = findViewById(R.id.tvName);
        tvDuration = findViewById(R.id.tvDuration);
        imgPlay = findViewById(R.id.player_play);

        audioArrayList = new ArrayList<>();
        add_Items_to_ArrayList(audioArrayList);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        final AudioAdapter audioAdapter = new AudioAdapter(this, audioArrayList);
        final ListView show_List = findViewById(R.id.listView);
        show_List.setAdapter(audioAdapter);

        show_List.setOnItemClickListener(onItemClickListener);

    }

    private void add_Items_to_ArrayList(ArrayList<Audio> audioArrayList) {
        audioArrayList.add(new Audio("red", R.raw.color_red));
        audioArrayList.add(new Audio("mustard yellow", R.raw.color_mustard_yellow));
        audioArrayList.add(new Audio("dusty yellow", R.raw.color_dusty_yellow));
        audioArrayList.add(new Audio("green",  R.raw.color_green));
        audioArrayList.add(new Audio("brown",  R.raw.color_brown));
        audioArrayList.add(new Audio("gray",  R.raw.color_gray));
        audioArrayList.add(new Audio("black",  R.raw.color_black));
        audioArrayList.add(new Audio("white", R.raw.color_white));
    }

    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
            isPlaying = false;

            imgPlay.setImageResource(R.drawable.ic_action_play);
        }
    }

    public void play_and_pause(View view) {
        if (player == null) {
            //play
            result = mAudioManager.requestAudioFocus(mOnAudioFoucsChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player = MediaPlayer.create(MainActivity.this, audioArrayList.get(i).getmAudioResourse());
            }
            player.setOnCompletionListener(mCompletionListener);
        }
        if (!isPlaying) {
            imgPlay.setImageResource(R.drawable.ic_action_pause);
            isPlaying = true;
            player.start();
            player.setOnCompletionListener(mCompletionListener);

            tvName.setText(audioArrayList.get(i).getmName());
            tvDuration.setText(formateMilliSeccond(player.getDuration()));

            imageView2.setImageResource(audioArrayList.get(i).mImageResource());
            imageView2.setVisibility(View.VISIBLE);
        } else if (isPlaying) {
            imgPlay.setImageResource(R.drawable.ic_action_play);
            isPlaying = false;
            player.pause();
        }

    }

    public void previous(View view) {
        if (i > 0 && i < audioArrayList.size()) {
            i = i - 1;
            GetPermission(i);
            Log.v("test", String.valueOf(i));
        } else if (i == 0) {
            i = audioArrayList.size() - 1;
            GetPermission(i);
            Log.v("test", String.valueOf(i));
        }
    }

    public void next(View view) {
        getNext();
    }

    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
            imgPlay.setImageResource(R.drawable.ic_action_play);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null && isPlaying) {
            player.start();
            imgPlay.setImageResource(R.drawable.ic_action_pause);
        }
    }

    private void getNext(){
        if (i >= 0 && i < audioArrayList.size() - 1) {
            i = i + 1;
            GetPermission(i);
            Log.v("test", String.valueOf(i));
        } else if (i == audioArrayList.size() - 1) {
            i = 0;
            GetPermission(i);
            Log.v("test", String.valueOf(i));
        }
    }
}