package com.example.android.azaudioplayerv001;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AudioAdapter extends ArrayAdapter<Audio> {
    private MediaPlayer mediaPlayer;

    public AudioAdapter(@NonNull Context context, ArrayList<Audio> audioArrayList) {
        super(context, 0, audioArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }

        Audio currentPersonPosition = getItem(position);

        TextView tvName = listItemView.findViewById(R.id.tvName);
        tvName.setText(currentPersonPosition.getmName());

        return listItemView;
    }


}
