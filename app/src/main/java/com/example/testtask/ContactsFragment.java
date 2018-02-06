package com.example.testtask;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testtask.table.contact.PropertyContacts;
import com.example.testtask.table.contact.PropertyAdapterContacts;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ListView listViewContacts;
    private float historicX;
    private float historicY;
    private float DELTA = 100;

    private ArrayList<PropertyContacts> rentalProperties = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private int index = 0;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        addTable();
        setOnClickListener();


        return view;
    }

    public void addTable() {
        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                        ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        getActivity().startManagingCursor(cursor);

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                rentalProperties.add(new PropertyContacts(cursor.getString(1), cursor.getString(2)));
            }
        }
        ArrayAdapter<PropertyContacts> adapter = new PropertyAdapterContacts(view.getContext(), 0, rentalProperties, R.layout.rowlayout);
        listViewContacts = (ListView)view.findViewById(R.id.listViewContacts);
        listViewContacts.setAdapter(adapter);
    }

    public void setOnClickListener() {
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position,
                                    long id) {
                callPhone(position);
            }
        });
    }

    public void callPhone(int index) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + rentalProperties.get(index).getNumber()));
        startActivity(intent);
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
