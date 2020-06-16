/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.smaharj3.gostop.R;
import com.smaharj3.gostop.model.setup.Game;
import com.smaharj3.gostop.model.setup.Round;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class MainGameActivity extends Activity
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /// Request code to get result from Round Activity.
    private static final int ROUND_REQUEST = 1;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// GoStop game object.
    private Game m_game;

    /// Current playing round object.
    private Round m_round;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * MainGameActivity::onCreate
     * Game Activity create method.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main_game);

    }


    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    /**
     * MainGameActivity::startGame
     * Method to start a new game. Onclick Listener.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    public void startGame(View view)
    {
        // Go Stop game.
        m_game = new Game();

        //ask for the tournament score
        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainGameActivity.this);
        final EditText v_playerName = new EditText(MainGameActivity.this);
        v_playerName.setInputType(InputType.TYPE_CLASS_TEXT);
        alertBoxBuilder.setCancelable(false)
                .setTitle("Please enter player name:")
                .setView(v_playerName)
                .setPositiveButton("Let's start! ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check for empty player name?
                        m_game.setPlayerName(v_playerName.getText().toString());
                        // Initialize round
                        m_round = m_game.startNewRound();
                        displayFirstPlayer();
                    }
                });
        alertBoxBuilder.show();
    }

    /**
     * MainGameActivity::loadGame
     * Method to load game. Onclick Listener.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    public void loadGame(View view)
    {
        // Go Stop game.
        m_game = new Game();

        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainGameActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("Select game to load");

        final File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        });
        String[] items = new String[files.length];
        for (int i = 0; i<files.length; ++i)
            items[i] = files[i].getName();

        alertBoxBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView t_listview = ((AlertDialog) dialog).getListView();
                String t_fileName = (String) t_listview.getAdapter().getItem(which);

                //de-serialize and create round;
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + t_fileName;
                try {
                    InputStream is = new FileInputStream(filePath);
                    m_round = m_game.loadFromFile(is);
                    startRoundActivity();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        alertBoxBuilder.show();
    }


    /**
     * MainGameActivity::onActivityResult
     * Listener when round activity finishes to get the results from the round activity.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    @Override
    protected void onActivityResult(int a_requestCode, int a_resultCode, Intent a_data)
    {
        // Updating the round
        m_round = (Round) a_data.getSerializableExtra("round");

        // End round and update scores
        m_game.endOfRound(m_round);

        // Display end of round messages
        endGameRound();

        // If the game is to be saved
        if(a_resultCode == RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(),"Saving game...", Toast.LENGTH_LONG).show();

            String t_filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/savefile.txt";
            if (m_game.saveGame(new File(t_filePath)))
            {
                Toast.makeText(getApplicationContext(),"Game saved!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    // *********************************************************
    // *********************** Mutators ************************
    // *********************************************************

    // *********************************************************
    // ******************** MAIN METHOD ************************
    // *********************************************************

    // *********************************************************
    // ******************** CLASS METHODS **********************
    // *********************************************************


    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************

    /**
     * MainGameActivity::displayFirstPlayer
     * Method to display first player decision and start the round.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void displayFirstPlayer()
    {
        // First player decision
        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainGameActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("First Player Decision");
        alertBoxBuilder.setMessage(m_game.getFirstPlayerDecision());
        alertBoxBuilder.setPositiveButton("Got it!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startRoundActivity();
            }
        });
        alertBoxBuilder.show();
    }

    /**
     * MainGameActivity::startRoundActivity
     * Method to start round activity.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void startRoundActivity()
    {
        Toast.makeText(getApplicationContext(),"Loading game...", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainRoundActivity.class);
        intent.putExtra("round", m_round);
        intent.putExtra("userScore", m_game.getUserScore());
        intent.putExtra("compScore", m_game.getCompScore());
        intent.putExtra("roundNumber", m_game.getRoundNumber());
        startActivityForResult(intent, ROUND_REQUEST);
    }

    /**
     * MainGameActivity::endGameRound
     * Method to display end messages and end round.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void endGameRound()
    {
        // Display game end statistics
        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainGameActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("End of round...");
        alertBoxBuilder.setMessage(m_game.getEndMessage());
        alertBoxBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Initialize new round
                m_round = m_game.startNewRound();
                displayFirstPlayer();
            }
        });
        alertBoxBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Thank you for playing Go Stop! ", Toast.LENGTH_LONG).show();
            }
        });

        alertBoxBuilder.show();
    }
}

