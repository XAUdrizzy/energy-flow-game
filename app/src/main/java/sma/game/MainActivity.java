package sma.game;

import static sma.game.RegisterActivity.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import sma.game.model.*;
import sma.game.view.*;
import sma.game.R;
import tile.*;

public class MainActivity extends Activity implements OnTileTouchListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int LEVELS_FILE = R.raw.levels;
    private TextView levels,moves,msg;
    private TilePanel grid;
    private Button btn, btn2;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private Plant model;
    private String name;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_power_plant);
        levels = findViewById(R.id.levels);
        levels.setText("" +level);

        moves = findViewById(R.id.moves);


        msg = findViewById(R.id.msg);
        msg.setVisibility(View.INVISIBLE);

        grid = findViewById(R.id.grid);
        grid.setListener(this);

        btn = findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();
            }
        });

        btn2 = findViewById(R.id.btn2);
        btn2.setVisibility(View.INVISIBLE);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("TesteCRASHHHH");
            }
        });

        loadLevel(level);

    }

    public void updateScore(String user, int level, int moves){
        Map<String,Object> score = new HashMap<>();
        score.put("user", user);
        score.put("level", level);
        score.put("moves", moves);

        db.collection("scores")
                .add(score)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public boolean onClick(int xTile, int yTile) {
        model.touch(yTile,xTile);
        moves.setText("" +model.getMoves());
        listener.cellChanged(yTile,xTile,model.getCell(yTile,xTile));
        if(model.isCompleted()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    name = profile.getDisplayName();
                }
            }

            updateScore(name, level, model.getMoves());
            level++;
            loadLevel(level);
            levels.setText("" +level);
        }
        return true;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

    }

    @Override
    public void onDragCancel() {

    }

    private boolean loadLevel(int n) {
        Scanner in = null;
        try {
            in = new Scanner(getResources().openRawResource(LEVELS_FILE));
            model = new Loader(in).load(n);
            model.setListener( listener );
            grid.setSize(model.getWidth(),model.getHeight());
            for (int i = 0; i < model.getHeight(); i++) {
                for (int j = 0; j < model.getWidth(); j++) {
                    grid.setTile(j,i, CellTile.newInstance(model.getCell(i,j),this));
                }
            }
            return true;
        } catch (InputMismatchException e) {
            System.out.println("Error loading file \""+LEVELS_FILE+"\":\n"+e.getMessage());
            return false;
        } catch (Loader.LevelFormatException e) {
            System.out.println(e.getMessage()+" in file \""+LEVELS_FILE+"\"");
            System.out.println(" "+e.getLineNumber()+": "+e.getLine());
            return false;
        } finally {
            if (in!=null) in.close();
        }
    }

    private class ModelListener implements Plant.Listener {
        @Override
        public void cellChanged(int lin, int col, Cell cell) {
            grid.setTile(col,lin,fixedContextTiles(cell));
        }
    }

    private CellTile fixedContextTiles(Cell cell){
        return CellTile.newInstance(cell,this);
    }

    private ModelListener listener = new ModelListener();
}
