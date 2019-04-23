package com.shmglickman.firestoreexampleproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference noteBookRef = db.collection("Notebook");

    private DocumentReference noteRef = db.document("Notebook/My First Note");
    // Or Can Write This
    // private DocumentReference noteRef = db.collection("Notebook").document("My First Note");

//    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            //        noteListener = noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, e.toString());
//                    return;
//                }
//
//                if (documentSnapshot.exists()) {
//
////                    When we are working without an object only with HashMap
////                    String title = documentSnapshot.getString(KEY_TITLE);
////                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
////
////                    textViewData.setText("Title: " + title + "\n" + "Description" + description);
//
////                  When we are working with an object Name Note constructor
//                    Note note = documentSnapshot.toObject(Note.class);
//
//                    String title = note.getTitle();
//                    String description = note.getDescription();
//
//                    textViewData.setText("Title: " + title + "\n" + "Description" + description);
//                } else {
//                    textViewData.setText("");
//                }
//            }
//        }

        noteBookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String  documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    int priority = note.getPriority();

                    data += "ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description
                            + "\nPriority: " + priority + "\n\n";

                    //noteBookRef.document(documentId).delete();
                }

                textViewData.setText(data);
            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        noteListener.remove();
//    }

    public void saveNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

//      When we are working without an object only with HashMap
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//

//      When we are working with an object Name Note constructor
        Note note = new Note(title, description, priority);


        //Can Replace With this 2 Lines
        //db.document("Notebook/My First Note")
        //db.collection("Notebook").document("My First Note").set(note)
        //db.collection("Notebook").document("My First Note").set(note)
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    public void updateDescription(View view) {
        String description = editTextDescription.getText().toString();

        //-->Map<String, Object> note = new HashMap<>();
        //-->note.put(KEY_DESCRIPTION, description);

        //-->noteRef.set(note, SetOptions.merge());

        // The command line below after deleting the document does not produce a new document.
        //noteRef.update(note);
        // OR
        noteRef.update(KEY_DESCRIPTION, description);
    }

    public void deleteDescription(View view) {
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, FieldValue.delete());

        //noteRef.update(note);
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View view) {
        noteRef.delete();
    }

    public void loadNote(View view) {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

//                            When we are working without an object only with HashMap
//                            String title = documentSnapshot.getString(KEY_TITLE);
//                            String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            //Map<String, Object> note = documentSnapshot.getData();

//                          When we are working with an object Name Note constructor
                            Note note = documentSnapshot.toObject(Note.class);

                            note.setDocumentId(documentSnapshot.getId());

                            String data = "";

                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();

                            int priority = note.getPriority();

                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\nDescription: " + description
                                    + "\nPriority: " + priority + "\n\n";
                            textViewData.setText(data);
                            //textViewData.setText("Title: " + title + "\n" + "Description" + description);
                        } else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

//      When we are working without an object only with HashMap
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//

//      When we are working with an object Name Note constructor
        Note note = new Note(title, description, priority);

        // noteBookRef.add(note);
        //Can Replace With this 2 Lines
        //db.document("Notebook/My First Note")
        //db.collection("Notebook").document("My First Note").set(note)
        //db.collection("Notebook").document("My First Note").set(note)
        // noteRef.set(note)
        noteBookRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    public void loadNotes(View view) {
        // For Read and Load All Multiply Notes
        noteBookRef.whereGreaterThanOrEqualTo("priority", 2)
                .orderBy("priority", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            int priority = note.getPriority();

                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\nDescription: " + description
                                    + "\nPriority: " + priority + "\n\n";
                        }

                        textViewData.setText(data);
                    }
                });
    }


}
