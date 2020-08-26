package com.ururu2909.findfriends.createroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ururu2909.findfriends.util.Constants;
import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.room.RoomActivity;
import com.ururu2909.findfriends.util.SharedPreferencesManager;

public class CreateRoomActivity extends AppCompatActivity implements CreateRoomView {
    EditText roomName;
    ProgressBar progressBar;
    SharedPreferences mSettings;
    CreateRoomPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        roomName = findViewById(R.id.roomName);
        progressBar = findViewById(R.id.progressBar);

        mSettings = SharedPreferencesManager.getInstance(this);
        presenter = new CreateRoomPresenter(this, new CreateRoomInteractor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_room_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_create) {
            presenter.createRoom(
                    mSettings.getString("token", ""),
                    roomName.getText().toString());
        }
        return true;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setError() {
        Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToRoom() {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }
}
