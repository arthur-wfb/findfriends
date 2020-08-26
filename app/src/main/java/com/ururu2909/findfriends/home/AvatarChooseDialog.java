package com.ururu2909.findfriends.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.adapters.AvatarsAdapter;

public class AvatarChooseDialog extends DialogFragment {

    private HomeActivity.AvatarListener callback;
    private Integer[] avatars = {R.drawable.dog, R.drawable.cat, R.drawable.frog, R.drawable.rat};

    AvatarChooseDialog(HomeActivity.AvatarListener callback){
        super();
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_avatar_choose, container);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        AvatarsAdapter avatarsAdapter = new AvatarsAdapter(this.getActivity(), avatars, callback);
        recyclerView.setAdapter(avatarsAdapter);
        this.getDialog().setTitle("Choose avatar");
        return view;
    }
}
