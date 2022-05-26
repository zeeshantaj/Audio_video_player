package com.example.audiovideoplayerforyvideo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ListView listView;
    String[] items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        listView = view.findViewById(R.id.listViewSongs);

        displaySongs();

        return view;
    }

    // work 1
    public ArrayList<File> findSong(File file){

        ArrayList arrayList = new ArrayList();
        File [] files = file.listFiles();
        if (files != null){
            for (File singleFile : files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(findSong(singleFile));
                }else{
                    if (singleFile.getName().endsWith(".mp3")){
                        arrayList.add(singleFile);
                    }
                    else if (singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                    else if (singleFile.getName().endsWith(".m4a")){
                        arrayList.add(singleFile);
                    }
                    else if (singleFile.getName().endsWith(".mpeg")){
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }
    //work 2
    void displaySongs(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];

        for (int i = 0; i < mySongs.size(); i++){

            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            //items[i] = mySongs.get(i).getName();
        }
       //  ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
       //  listView.setAdapter(myAdapter);

        // work 4

        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) listView.getItemAtPosition(i);
                startActivity(new Intent(getActivity(),PlayerAcitvity.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songsName",songName)
                        .putExtra("pos",i));
            }
        });
    }
    // work 3
    class customAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View myView = getLayoutInflater().inflate(R.layout.list_view, null);
            TextView textView = myView.findViewById(R.id.textSong);
            textView.setSelected(true);
            textView.setText(items[i]);
            return myView;
        }
    }
}



























