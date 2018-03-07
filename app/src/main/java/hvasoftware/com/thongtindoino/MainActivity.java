package hvasoftware.com.thongtindoino;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // uploadData();


        getData();

    }

    private void uploadData() {
        CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_USER);
        Map<String, Object> user = new HashMap<>();
        user.put("objectID", Utils.getRandomUUID());
        user.put("documentId", "Admin");
        user.put("account", "Admin");
        user.put("password", "12345678");
        user.put("displayName", "Doãn Chí Bình");
        user.put("createAt", Utils.getCurrentDateTime());
        user.put("updateAt", Utils.getCurrentDateTime());
        user.put("role", Constant.ROLE_ADMIN);
        collectionReference.document("admin").set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.wtf(TAG, "==============================> UPLOAD DONE");
            }
        });
    }


    private void getData() {
        final List<User> userList = new ArrayList<>();
        firebaseFirestore.collection(Constant.COLLECTION_USER)
                // .whereEqualTo()
                //  .orderBy()
                // .limit()
                //.startAt()
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                        Log.wtf(TAG, "===========================> NAME: " + user.getDisplayName());
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
