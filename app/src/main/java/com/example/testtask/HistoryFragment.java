package com.example.testtask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testtask.helpers.Change;
import com.example.testtask.helpers.Changes;
import com.example.testtask.table.history.PropertyAdapterHistory;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ListView listViewHistory;
    private float historicX;
    private float historicY;
    private float DELTA = 100;
    private ArrayList<Change> rentalProperties = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private int index = 0;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        addTable();

        return view;
    }

    public void addTable() {
        Changes changes = new Changes();
        changes.readFromDB();

        for (int i = changes.getCount() - 1; i >= 0; i--) {
            rentalProperties.add(changes.getItemAt(i));
        }

        ArrayAdapter<Change> adapter = new PropertyAdapterHistory(view.getContext(), 0, rentalProperties, R.layout.rowlayout_history);
        listViewHistory = (ListView)view.findViewById(R.id.listViewHistory);
        listViewHistory.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
