package com.example.smmusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stlye, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.settings)
        {
            Intent intent= new Intent( PlaySong.this, Settings.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.about)
        {
            Intent intent= new Intent(PlaySong.this, About_Us.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.playlist)
        {
            Intent intent= new Intent( PlaySong.this, MainActivity.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.contactUs)
        {
            Intent intent= new Intent( PlaySong.this, Contact_Us.class);
            startActivity(intent);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    TextView textView;
    ImageView play, previous, next;
    ArrayList songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView= findViewById(R.id.textView);
        play= findViewById(R.id.play);
        previous= findViewById(R.id.previous);
        next= findViewById(R.id.next);
        seekBar= findViewById(R.id.seekBar);
        Intent intent= getIntent();
        Bundle bundle= intent.getExtras();
        songs= (ArrayList) bundle.getParcelableArrayList("songsList");
        textContent= intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position= intent.getIntExtra("position", 0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer= MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek= new Thread()
        {
            @Override
            public void run()
            {
                int currentPosition= 0;
                try
                    {
                        while (currentPosition<mediaPlayer.getDuration())
                        {
                            currentPosition= mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentPosition);
                            sleep(800);
                        }
                    }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying())
            {
                play.setImageResource(R.drawable.play);
                mediaPlayer.pause();
            }
            else {
                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();
            }
        });

        previous.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(position!=0)
            {
                position= position-1;
            }
            else {
                position= songs.size() -1;
            }
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer= MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            textContent= songs.get(position).getClass().getName().toString();
            textView.setText(textContent);
        });

        next.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(position!=songs.size() -1)
            {
                position= position+1;
            }
            else {
                position= 0;
            }
            Uri uri12 = Uri.parse(songs.get(position).toString());
            mediaPlayer= MediaPlayer.create(getApplicationContext(), uri12);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            textContent= songs.get(position).getClass().getName().toString();
            textView.setText(textContent);
        });



    }
}