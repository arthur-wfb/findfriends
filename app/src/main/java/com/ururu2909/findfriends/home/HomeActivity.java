package com.ururu2909.findfriends.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.createroom.CreateRoomActivity;
import com.ururu2909.findfriends.login.LoginActivity;
import com.ururu2909.findfriends.room.RoomActivity;
import com.ururu2909.findfriends.util.SharedPreferencesManager;

public class HomeActivity extends AppCompatActivity implements HomeView {
    Button createRoomButton;
    Button joinRoomButton;
    Button joinMyRoomButton;
    Button copyRoomLinkButton;
    Button deleteRoomButton;
    ProgressBar progressBar;
    ImageView avatar;
    TextView login;
    TextView roomInfoNotLoadedAttention;
    TextView roomNameText;
    TextView roomName;
    TextView roomLinkText;
    TextView roomLink;
    HomePresenter presenter;
    SharedPreferences mSettings;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = SharedPreferencesManager.getInstance(this);
        presenter = new HomePresenter(this, new HomeInteractor(), mSettings);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        createRoomButton = findViewById(R.id.createRoomButton);
        joinRoomButton = findViewById(R.id.joinRoomButton);
        joinMyRoomButton = findViewById(R.id.joinMyRoomButton);
        deleteRoomButton = findViewById(R.id.deleteRoomButton);
        copyRoomLinkButton = findViewById(R.id.copyRoomLinkButton);
        progressBar = findViewById(R.id.progressBar);
        avatar = findViewById(R.id.avatar);
        login = findViewById(R.id.login);
        roomNameText = findViewById(R.id.roomNameText);
        roomName = findViewById(R.id.roomName);
        roomLinkText = findViewById(R.id.roomLinkText);
        roomLink = findViewById(R.id.roomLink);
        roomInfoNotLoadedAttention = findViewById(R.id.roomInfoNotLoadedAttention);
        avatar.setOnClickListener(r -> showChooseAvatarDialog());
        joinRoomButton.setOnClickListener(r -> showLinkRequestDialog());
        createRoomButton.setOnClickListener(r -> navigateToCreateRoom());
        deleteRoomButton.setOnClickListener(r -> presenter.deleteRoom());
        copyRoomLinkButton.setOnClickListener(r -> copyRoomLink());
    }

    @Override
    protected void onResume() {
        login.setText(mSettings.getString("login", ""));
        avatar.setImageResource(mSettings.getInt("avatar", R.drawable.ic_launcher_foreground));
        presenter.getRoomInfo();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            presenter.logout();
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
    public void setLogoutError() {
        Toast.makeText(this, "Logout error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setRoomEnteringError() {
        Toast.makeText(this, "Room entering error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setRoomInfoRetrievingError() {
        roomInfoNotLoadedAttention.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRoomDeletingError() {
        Toast.makeText(this, "Room deleting error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAvatarChangingError() {
        Toast.makeText(this, "Avatar changing error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setRoomInfo(String roomName, String roomLink) {
        createRoomButton.setVisibility(View.GONE);

        this.roomName.setVisibility(View.VISIBLE);
        this.roomLink.setVisibility(View.VISIBLE);
        roomNameText.setVisibility(View.VISIBLE);
        roomLinkText.setVisibility(View.VISIBLE);
        joinMyRoomButton.setVisibility(View.VISIBLE);
        copyRoomLinkButton.setVisibility(View.VISIBLE);
        deleteRoomButton.setVisibility(View.VISIBLE);

        this.roomName.setText(roomName);
        this.roomLink.setText(roomLink);
        joinMyRoomButton.setOnClickListener(r -> presenter.enterRoom(roomLink));
    }

    @Override
    public void showCreateRoomButon() {
        this.roomName.setVisibility(View.GONE);
        this.roomLink.setVisibility(View.GONE);
        roomNameText.setVisibility(View.GONE);
        roomLinkText.setVisibility(View.GONE);
        joinMyRoomButton.setVisibility(View.GONE);
        copyRoomLinkButton.setVisibility(View.GONE);
        deleteRoomButton.setVisibility(View.GONE);

        createRoomButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLinkRequestDialog() {
        DialogFragment linkRequestDialog = new LinkRequestDialog(link -> presenter.enterRoom(link));
        linkRequestDialog.show(getSupportFragmentManager(), "enter room");
    }

    @Override
    public void showChooseAvatarDialog() {
        DialogFragment avatarChooseDialog = new AvatarChooseDialog(avatar -> presenter.updateAvatar(avatar));
        avatarChooseDialog.show(getSupportFragmentManager(), "choose avatar");
    }

    @Override
    public void navigateToCreateRoom() {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToRoom() {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateAvatar(int avatar) {
        this.avatar.setImageResource(avatar);
    }

    void copyRoomLink() {
        ClipData clip = ClipData.newPlainText("link", roomLink.getText().toString());
        clipboardManager.setPrimaryClip(clip);
    }

    public interface LinkListener{
        void onLinkEntered(String link);
    }

    public interface AvatarListener{
        void onAvatarChoosed(int avatar);
    }
}
