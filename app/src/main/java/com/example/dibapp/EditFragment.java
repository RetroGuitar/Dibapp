package com.example.dibapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
        final View view = inflater.inflate(R.layout.fragment_edit, container, false);
        final EditText profilename = (EditText) view.findViewById(R.id.changenametext);
        final EditText changenumber = (EditText) view.findViewById(R.id.changeregnumbertext);
        final EditText oldpass = (EditText) view.findViewById(R.id.oldpwdtext);
        final EditText newpass = (EditText) view.findViewById(R.id.newpwdtext);
        Button save = (Button) view.findViewById(R.id.saveeditsbutton);
        Button change = (Button) view.findViewById(R.id.changepasswordbutton);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = user.getUid();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!profilename.getText().toString().isEmpty()){
                    Map<String, Object> hopperUpdates = new HashMap<>();
                    hopperUpdates.put("name", profilename.getText().toString());

                    db.updateChildren(hopperUpdates);
                    Toast.makeText(getContext(), "Name changed Successfully!", Toast.LENGTH_SHORT);
                }
                if(!changenumber.getText().toString().isEmpty()){
                    Map<String, Object> hopperUpdates = new HashMap<>();
                    hopperUpdates.put("matricola", changenumber.getText().toString());

                    db.updateChildren(hopperUpdates);
                    Toast.makeText(getContext(), "Number changed Successfully!", Toast.LENGTH_SHORT);
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldpassword, newpassword;
                oldpassword = oldpass.getText().toString();
                newpassword = newpass.getText().toString();

                if(oldpass.getText() == null){
                    Toast.makeText(getContext(),"Enter old password!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpass.length() < 6){
                    Toast.makeText(getContext(),"Password must be >= 6 characters!",Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),oldpassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Password Changed!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                                        startActivity(i);
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Password Not Changed!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(),"Please Enter The Old Password Correctly!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
