package com.example.smmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
            Intent intent= new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.about)
        {
            Intent intent= new Intent(MainActivity.this, About_Us.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.playlist)
        {
            Intent intent= new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return  true;
        }
        else if(id == R.id.contactUs)
        {
            Intent intent= new Intent(MainActivity.this, Contact_Us.class);
            startActivity(intent);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listview;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview= findViewById(R.id.listview1);
        Dexter.withContext(getApplicationContext())
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    //    Toast.makeText(MainActivity.this, "permission granted...", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String [] items = new String[mySongs.size()];
                        for( int i=0; i<mySongs.size(); i++)
                        {
                            items[i]= mySongs.get(i).getName().replace(".mp3", "");
                        }
                        ArrayAdapter<String> adapter= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener((adapterView, view, position, id) -> {
                            Intent intent= new Intent(MainActivity.this, PlaySong.class);
                            String currentSong = listview.getItemAtPosition(position).toString();
                            intent.putExtra("songsList", mySongs);
                            intent.putExtra("currentSong", currentSong);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();


    }
    public ArrayList<File> fetchSongs(File file)
    {
        ArrayList<File> arraylist = new ArrayList<>();
        File [] songs= file.listFiles();
        if(songs != null) {
            for(File myFile: songs)
            {
                if(!myFile.isHidden() && myFile.isDirectory())
                {
                    arraylist.addAll(fetchSongs(myFile));
                }
                else
                {
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith("."))
                    {
                        arraylist.add(myFile);
                    }
                }
            }
        }
        return arraylist;
    }
}