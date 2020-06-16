/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smaharj3.gostop.R;
import com.smaharj3.gostop.model.players.Computer;
import com.smaharj3.gostop.model.players.Player;
import com.smaharj3.gostop.model.players.User;
import com.smaharj3.gostop.model.setup.Card;
import com.smaharj3.gostop.model.setup.Game;
import com.smaharj3.gostop.model.setup.Round;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class MainRoundActivity extends Activity
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /// Button value to open capture pile.
    public static String OPEN_CAPTURE;
    /// Button value to open hand pile.
    public static String OPEN_HAND;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Current game round object.
    private Round m_round;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    /// TextView to alert the first player of the game
    private TextView m_firstPlayerAlert;
    /// TextView for player scores
    private TextView m_userTitle;
    private TextView m_compTitle;

    /// TextView to show system log messages.
    private TextView m_systemLog;
    /// TextView to show game information
    private TextView m_gameInfo;
    /// User area
    private LinearLayout m_userArea;
    /// Computer area
    private LinearLayout m_compArea;
    /// Layout area
    private LinearLayout m_layoutArea;
    /// ImageButton for top card on stockpile
    private ImageButton m_topCard;
    /// ImageButton for stockpile
    private ImageButton m_stockPile;
    /// Help mode switch
    private Switch m_helpMode;
    /// Save game button
    private Button m_saveGame;
    /// User pile change button
    private Button m_userButton;
    /// Computer pile change button
    private Button m_compButton;
    /// Continue turn button
    private Button m_changeTurn;

    /// Main game info text view
    private TextView m_mainInfo;

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * MainRoundActivity::onCreate
     * Round Activity create method. Record view elements and start round.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main_round);
        Toast.makeText(getApplicationContext(),"Loading game...", Toast.LENGTH_LONG).show();

        // Get data from main game Activity:
        Intent intent = getIntent();
        m_round = (Round) intent.getSerializableExtra("round");
        TextView m_mainInfo = (TextView) findViewById(R.id.mainInfo);
        String t_infoText = "User Total: " +
                intent.getSerializableExtra("userScore").toString() +
                "\nComputer Total: " +
                intent.getSerializableExtra("compScore").toString() +
                "\nRound #"+
                intent.getSerializableExtra("roundNumber").toString();
        m_mainInfo.setText(t_infoText);


        // Get view elements:
        // For pop up messages
        m_firstPlayerAlert = (TextView) findViewById(R.id.firstPlayerAlertTextView);
        // Player information view
        m_userTitle = (TextView) findViewById(R.id.userTitle);
        m_compTitle = (TextView) findViewById(R.id.compTitle);
        // View to display system log
        m_systemLog = (TextView) findViewById(R.id.systemLog);
        // View to display game information
        m_gameInfo = (TextView) findViewById(R.id.gameInfo);

        // User area view
        m_userArea = (LinearLayout) findViewById(R.id.userArea);
        // User Pile change button
        m_userButton = (Button) findViewById(R.id.userButton);

        // Comp area view
        m_compArea = (LinearLayout) findViewById(R.id.compArea);
        // Comp pile change button
        m_compButton = (Button) findViewById(R.id.compButton);

        // Layout area view
        m_layoutArea = (LinearLayout) findViewById(R.id.layoutArea);
        // Top Card view
        m_topCard = (ImageButton) findViewById(R.id.stockTopCard);

        // Stockpile view
        m_stockPile = (ImageButton) findViewById(R.id.deck);
        Context context = m_stockPile.getContext();
        int id = context.getResources().getIdentifier("red_back", "drawable", context.getPackageName());
        (m_stockPile).setImageResource(id);

        // Help mode view
        m_helpMode = (Switch) findViewById(R.id.helpMode);
        // Change turn button
        m_changeTurn = (Button) findViewById(R.id.changeTurn);
        // Save game button
        m_saveGame = (Button) findViewById(R.id.saveButton);
        // Set on click listener

        // Get string resources
        OPEN_CAPTURE = getString(R.string.open_capture);
        OPEN_HAND = getString(R.string.open_hand);

        // Set output stream to textView element (m_systemLog)
        System.setOut(new PrintStream(new OutputStream()
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            @Override
            public void write(int oneByte) throws IOException {
                outputStream.write(oneByte);
                m_systemLog.setText(new String(outputStream.toByteArray()));
            }
        }));

        disableContinueButton();
        updateScreen();

        android.app.AlertDialog.Builder alertBoxBuilder = new android.app.AlertDialog.Builder(MainRoundActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("Game loaded");
        alertBoxBuilder.setMessage("Game had loaded successfully. Press continue to start.");
        alertBoxBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Start round
                startNextTurn(m_round.getPlayerTurn());
            }
        });
        alertBoxBuilder.show();

    }

    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    /**
     * MainRoundActivity::saveGame
     * Method to save game to file
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    public void saveGame(View view)
    {
        // Prompt to user for saving game.
        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainRoundActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("Save game...");
        alertBoxBuilder.setMessage("Do you want to save game? NOTE: This will save and close the current game.");
        alertBoxBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent();
                intent.putExtra("round", m_round);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        alertBoxBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertBoxBuilder.show();
    }

    /**
     * MainRoundActivity::toggleUserPile
     * Method to toggle User pile.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    public void toggleUserPile(View view)
    {
        // Open capture pile
        if (m_userButton.getText().toString() == OPEN_CAPTURE)
        {
            m_userButton.setText(OPEN_HAND);
            updateUserCapture();

        }
        // Open hand pile
        else
        {
            m_userButton.setText(OPEN_CAPTURE);
            updateUserHand();
            // Reactivating hand if it is turn to play hand
            if(m_round.getPlayerTurn() == 1 && m_round.getHandPlayFlag()==true)
                activatePlayerHand(getHelpSwitchVal());
        }
    }

    /**
     * MainRoundActivity::toggleComputerPile
     * Method to toggle Computer pile.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    public void toggleComputerPile(View view)
    {
        // Open capture pile
        if (m_compButton.getText().toString() == OPEN_CAPTURE)
        {
            m_compButton.setText(OPEN_HAND);
            updateCompCapture();
        }
        // Open hand pile
        else
        {
            m_compButton.setText(OPEN_CAPTURE);
            updateComputerHand();
            // Reactivating hand if it is turn to play hand
            if(m_round.getPlayerTurn() == 2 && m_round.getHandPlayFlag()==true)
                activateCompHand(true);
        }
    }

    /**
     * MainRoundActivity::onBackPressed
     * Method to disable back press.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("round", m_round);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * MainRoundActivity::getHelpSwitchVal
     * Method to get Help Mode switch value.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    final public boolean getHelpSwitchVal()
    {
        return m_helpMode.isChecked();
    }

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
     * MainRoundActivity::startNextTurn
     * Method to start round by playing decided player's turn.
     * @param a_turn short The current player turn.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void startNextTurn(short a_turn)
    {
        // Checking for end of the game
        if(m_round.getRoundEndStatus()) {
            endRound();
            return;
        }
        // Switch for turns
        switch(a_turn)
        {
            case 1:
                deactivateCompHand();
                m_round.setHandPlayFlag(true);
                activatePlayerHand(getHelpSwitchVal());
                break;
            case 2:
                deactivatePlayerHand();
                m_round.setHandPlayFlag(true);
                activateCompHand(true);
                break;
        }
    }

    /**
     * MainRoundActivity::endRound
     * Method to end round and start main game Activity.
     * @return void
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    private void endRound()
    {
        Toast.makeText(getApplicationContext(),"End of round...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("round", m_round);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    /**
     * MainRoundActivity::activatePlayerHand
     * Make player hand active for user turn
     * @param a_help boolean Help Mode switch value.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void activatePlayerHand(final boolean a_help)
    {
        // Making hand pile active
        if(m_userButton.getText().toString() == OPEN_HAND)
            toggleUserPile(m_userButton);

        m_userArea.setBackgroundColor(Color.GREEN);

        // Providing help if requested
        if(a_help)
            displayPlayerHelp(m_round.getHelpString(), (short) 1, true);

        // Set onclick listener for user cards and capture pile button
        for (int i = 0; i < m_userArea.getChildCount(); i++)
        {
            View v = m_userArea.getChildAt(i);
            // Card Button
            if (v instanceof ImageButton)
            {
                final int finalI = i;
                ((ImageButton) v).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Play card at finalI
//                        m_user.playCard(finalI, m_round, a_help);
                        Vector<Integer> t_matches = m_round.playFirstTurn(finalI, a_help);
                        // Decision about which card to stack on the layout when 2 choices are present:
                        if(t_matches != null)
                        {
                            getLayoutStackChoice(t_matches, finalI, a_help);
                        }
                        else {
                            deactivatePlayerHand();
                            m_round.setHandPlayFlag(false);
                            updateScreen();
                            activateStockPile();
                        }
                    }
                });
            }
        }
    }

    /**
     * MainRoundActivity::getLayoutStackChoice
     * Method to get card choice from user to pick which card on the layout to stack the drawn card with.
     * @param a_help boolean Help Mode switch value.
     * @param a_card int drawn card from the user's hand pile
     * @param a_matches Vector<Integer> The matches found on the layout with hand card.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void getLayoutStackChoice(final Vector<Integer> a_matches, final int a_card, final boolean a_help)
    {
        Toast.makeText(getApplicationContext(),"There are multiple matches on the layout. Pick a highlighted card you want to create a stack pair with!", Toast.LENGTH_LONG).show();

        // Highlight recommended cards
        for(int i = 0; i<a_matches.size(); i++)
        {
            assert (a_matches.get(i) < m_layoutArea.getChildCount());
            m_layoutArea.getChildAt(a_matches.get(i)).setBackgroundColor(Color.RED);
            final int finalI = i;
            m_layoutArea.getChildAt(a_matches.get(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    m_round.processLayoutChoice(a_card, a_help, a_matches.get(finalI));
                    deactivatePlayerHand();
                    m_round.setHandPlayFlag(false);
                    updateScreen();
                    activateStockPile();
                }
            });

        }
    }

    /**
     * MainRoundActivity::activateCompHand
     * Make computer hand active for computer turn
     * @param a_help boolean Help Mode switch value.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void activateCompHand(final boolean a_help)
    {
        // Making computer hand active
        if(m_compButton.getText().toString() == OPEN_HAND)
            toggleComputerPile(m_compButton);

        m_compArea.setBackgroundColor(Color.GREEN);

        // Set onclick listener for user cards and capture pile button
        for (int i = 0; i < m_compArea.getChildCount(); i++)
        {
            View v = m_compArea.getChildAt(i);
            // Card Button
            if (v instanceof ImageButton)
            {
                ((ImageButton) v).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Show computer play logic and get index of card selected.
                        int t_idx = displayPlayerHelp(m_round.getHelpString(), (short) 2, true);

                        // a_player.playCard(99, m_round, a_help);
                        // Play hand pile and update GUI
                        m_round.playFirstTurn(t_idx, a_help);
                        m_round.setHandPlayFlag(false);
                        deactivateCompHand();
                        updateScreen();
                        activateStockPile();
                    }
                });
            }
        }
    }

    /**
     * MainRoundActivity::displayPlayerHelp
     * Display player help logic and highlights recommended card. Parses out index of the card picked by the helper and returns it.
     * @param a_message String The help logic from computer.
     * @param a_player short The current player turn.
     * @param a_highlight boolean Help Mode switch value.
     * @return int Index of card recommended by the logic algorithm.
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private int displayPlayerHelp(String a_message, short a_player, boolean a_highlight)
    {
        // Highlight recommended card:
        int t_idx =  Integer.parseInt(String.valueOf(a_message.charAt(0)));
        if(a_highlight)
        {
            if(a_player == 1)
            {
                assert (t_idx < m_userArea.getChildCount());
                m_userArea.getChildAt(t_idx).setBackgroundColor(Color.RED);
            }
            else
            {
                assert (t_idx < m_compArea.getChildCount());
                m_compArea.getChildAt(t_idx).setBackgroundColor(Color.RED);
            }
        }

        // Display logic
        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainRoundActivity.this);
        alertBoxBuilder.setCancelable(true);
        alertBoxBuilder.setTitle("Go Stop Helper");
        alertBoxBuilder.setMessage(a_message.substring(1));
        alertBoxBuilder.setPositiveButton("Got it!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertBoxBuilder.show();

        return t_idx;
    }

    /**
     * MainRoundActivity::deactivatePlayerHand
     * Make player hand inactive.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void deactivatePlayerHand()
    {
        // Making player hand active
        if(m_userButton.getText().toString() == OPEN_HAND)
            toggleUserPile(m_userButton);

        m_userArea.setBackgroundColor(0x00000000);
        // Set onclick listener for user cards and capture pile button
        for (int i = 0; i < m_userArea.getChildCount(); i++)
        {
            View v = m_userArea.getChildAt(i);
            // Card Button
            if (v instanceof ImageButton)
            {
                ((ImageButton) v).setOnClickListener(null);
            }
        }
    }

    /**
     * MainRoundActivity::deactivateCompHand
     * Make computer hand inactive.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void deactivateCompHand()
    {
        // Making computer hand active
        if(m_compButton.getText().toString() == OPEN_HAND)
            toggleUserPile(m_userButton);

        m_compArea.setBackgroundColor(0x00000000);
        // Set onclick listener for user cards and capture pile button
        for (int i = 0; i < m_compArea.getChildCount(); i++)
        {
            View v = m_compArea.getChildAt(i);
            // Card Button
            if (v instanceof ImageButton)
            {
                ((ImageButton) v).setOnClickListener(null);
            }
        }
    }

    /**
     * MainRoundActivity::activateStockPile
     * Activate stockpile button.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void activateStockPile()
    {
        m_stockPile.setBackgroundColor(Color.GREEN);

        m_stockPile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_playStatus = m_round.playSecondTurn(true);

                // Checking if stock play is completed.
                // (If 'Y' or 'C' is not the first character, user has to make a choice between cards)
                if(t_playStatus.charAt(0)!='Y' && t_playStatus.charAt(0)!='C')
                {
                    stockLayoutChoice(t_playStatus);
                }
                else
                {
                    displayPlayerHelp("9"+t_playStatus, (short) 1, false);
                    updateScreen();
                    deactivateStockPile();
                    activateContinueButton();
                }
            }
        });
    }

    /**
     * MainRoundActivity::stockLayoutChoice
     * Method to get card choice from user to pick which card on the layout to stack card from stockpile with.
     * @param a_playInfo String that has the layout choices and recommendation logic.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void stockLayoutChoice(String a_playInfo)
    {
        Toast.makeText(getApplicationContext(),"There are multiple matches on the layout. Pick a highlighted card you want to create a stack pair with!", Toast.LENGTH_LONG).show();

        // Parse out matches from string
        String[] t_info = a_playInfo.split("\\|");

        // Asserting multiple options and parsing out message from card indexes
        assert (t_info.length==3);
        final Vector<Integer> t_matches = new Vector<>();
        for (String t_temp : t_info){
            // Displaying logic message
            if(t_temp.length()>3)
                displayPlayerHelp("9"+t_temp, (short) 1, false);
            else
                t_matches.add(Integer.parseInt(t_temp.trim()));
        }

        // Highlight recommended cards
        for(int i = 0; i<t_matches.size(); i++)
        {
            assert (t_matches.get(i) < m_layoutArea.getChildCount());
            // Skipping stacked pairs and getting the appropriate card to highlight
            int t_index = t_matches.get(i) + m_round.getNumberOfStacksBefore(t_matches.get(i));

            m_layoutArea.getChildAt(t_index).setBackgroundColor(Color.RED);
            final int finalI = i;
            m_layoutArea.getChildAt(t_index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    m_round.processStockLayoutPlay(getHelpSwitchVal(), t_matches.get(finalI));
                    updateScreen();
                    deactivateStockPile();
                    activateContinueButton();
                }
            });
        }
    }

    /**
     * MainRoundActivity::deactivateStockPile
     * Deactivate stockpile button.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void deactivateStockPile()
    {
        m_stockPile.setBackgroundColor(0x00000000);
        m_stockPile.setOnClickListener(null);
    }

    /**
     * MainRoundActivity::activateContinueButton
     * Method to enable continue button and switch turns:
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void activateContinueButton()
    {
        m_changeTurn.setBackgroundColor(Color.GREEN);

        m_changeTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                m_round.changeTurn();
                updateScreen();
                startNextTurn(m_round.getPlayerTurn());
                disableContinueButton();
            }
        });
    }

    /**
     * MainRoundActivity::disableContinueButton
     * Method to disable continue button.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void disableContinueButton()
    {
        m_changeTurn.setBackgroundColor(0x00000000);
        m_changeTurn.setOnClickListener(null);
    }

    /**
     * MainRoundActivity::updateScreen
     * Method to update the board screen.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateScreen()
    {
        // Update game info.
        updateGameInfo();
        // Update player headlines and score.
        updateUserInfo();
        updateCompInfo();

        // Update user hand
        updateUserHand();
        // Update user capture
        updateUserCapture();
        // Update computer hand
        updateComputerHand();
        // Update computer capture
        updateCompCapture();

        // Update layout
        updateLayout();

        // Update stockpile
        updateTopCard();
    }

    /**
     * MainRoundActivity::updateGameInfo
     * Method to update the game information.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateGameInfo()
    {
        // Get player turn and update view.
        String t_turn;
        if(m_round.getPlayerTurn() == 1)
            t_turn = "User";
        else
            t_turn = "Computer";

        m_gameInfo.setText(t_turn +" Turn");
    }

    /**
     * MainRoundActivity::updateUserInfo
     *  Method to update user score
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateUserInfo()
    {
        m_userTitle.setText(m_round.getPlayerName() +"   |     Score: " + m_round.getUserScore());
    }

    /**
     * MainRoundActivity::updateCompInfo
     * Method to update computer score
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateCompInfo()
    {
        m_compTitle.setText("Computer   |     Score: " + m_round.getComputerScore());
    }

    /**
     * MainRoundActivity::updateUserHand
     * Method to update user hand.
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateUserHand()
    {
        Vector<String> t_cards = m_round.getHandStr((short) 1);
        // Check if hand pile is open for update, else do not update:
        if(m_userButton.getText().toString() == OPEN_CAPTURE)
        {
            // Clear view and set parameters:
            m_userArea.removeAllViews();
            LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayoutParam.gravity = Gravity.CENTER;
            linearLayoutParam.setMargins(0,2,0,2);

            for(int i = 0; i<t_cards.size(); i++)
            {
                ImageButton t_imageButton = new ImageButton(this);
                t_imageButton.setLayoutParams(linearLayoutParam);

                Context context = t_imageButton.getContext();
                int id = context.getResources().getIdentifier(t_cards.get(i), "drawable", context.getPackageName());
                t_imageButton.setImageResource(id);
                t_imageButton.setAdjustViewBounds(true);
                t_imageButton.setPadding(9,9,9,9);
                t_imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                m_userArea.addView(t_imageButton);
            }
        }
    }

    /**
     * MainRoundActivity::updateComputerHand
     * Method to update computer hand
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateComputerHand()
    {
        Vector<String> t_cards = m_round.getHandStr((short) 2);
        // Check if hand pile is open for update, else do not update:
        if(m_compButton.getText().toString() == OPEN_CAPTURE)
        {
            // Clear view and set parameters:
            m_compArea.removeAllViews();
            LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayoutParam.gravity = Gravity.CENTER;
            linearLayoutParam.setMargins(0,2,0,2);

            for(int i = 0; i<t_cards.size(); i++)
            {
                ImageButton t_imageButton = new ImageButton(this);
                t_imageButton.setLayoutParams(linearLayoutParam);

                Context context = t_imageButton.getContext();
                int id = context.getResources().getIdentifier(t_cards.get(i), "drawable", context.getPackageName());
                t_imageButton.setImageResource(id);
                t_imageButton.setAdjustViewBounds(true);
                t_imageButton.setPadding(9,9,9,9);
                t_imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                m_compArea.addView(t_imageButton);
            }
        }
    }

    /**
     * MainRoundActivity::updateUserCapture
     * Method to update user capture pile
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateUserCapture()
    {
        Vector<Vector<String>> t_cards = m_round.getCaptureStr((short) 1);
        // Check if capture pile is open for update, else do not update:
        if(m_userButton.getText().toString() == OPEN_HAND)
        {
//            System.out.println("updateUserCapture");

            // Clear view and set parameters:
            m_userArea.removeAllViews();
            LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayoutParam.gravity = Gravity.CENTER;
            linearLayoutParam.setMargins(0,2,0,2);

            for(int i = 0; i<t_cards.size(); i++)
            {
                for(int j = 0; j<t_cards.get(i).size(); j++)
                {
//                    System.out.println(t_cards.get(i).size());

                    ImageButton t_imageButton = new ImageButton(this);
                    t_imageButton.setLayoutParams(linearLayoutParam);

                    Context context = t_imageButton.getContext();
                    int id = context.getResources().getIdentifier(t_cards.get(i).get(j), "drawable", context.getPackageName());
                    t_imageButton.setImageResource(id);
                    t_imageButton.setAdjustViewBounds(true);
                    t_imageButton.setPadding(9,9,9,9);
                    t_imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    // Highlight card if point earned
                    if(t_cards.get(i).size()==4)
                        t_imageButton.setBackgroundColor(Color.rgb(255, 165, 0));

//                    System.out.println(t_cards.get(i).get(j));
                    m_userArea.addView(t_imageButton);
                }
            }
        }

    }

    /**
     * MainRoundActivity::updateCompCapture
     * Method to update computer capture pile
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateCompCapture()
    {
        Vector<Vector<String>> t_cards = m_round.getCaptureStr((short) 2);
        // Check if capture pile is open for update, else do not update:
        if(m_compButton.getText().toString() == OPEN_HAND)
        {
//            System.out.println("updateCompCapture");
//            System.out.println(t_cards.size());


            // Clear view and set parameters:
            m_compArea.removeAllViews();
            LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayoutParam.gravity = Gravity.CENTER;
            linearLayoutParam.setMargins(0,2,0,2);

            for(int i = 0; i<t_cards.size(); i++)
            {
                for(int j = 0; j<t_cards.get(i).size(); j++)
                {
//                    System.out.println(t_cards.get(i).size());

                    ImageButton t_imageButton = new ImageButton(this);
                    t_imageButton.setLayoutParams(linearLayoutParam);

                    Context context = t_imageButton.getContext();
                    int id = context.getResources().getIdentifier(t_cards.get(i).get(j), "drawable", context.getPackageName());
                    t_imageButton.setImageResource(id);
                    t_imageButton.setAdjustViewBounds(true);
                    t_imageButton.setPadding(9,9,9,9);
                    t_imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    // Highlight card if stacked
                    if(t_cards.get(i).size()==4)
                        t_imageButton.setBackgroundColor(Color.rgb(255, 165, 0));

//                    System.out.println(t_cards.get(i).get(j));
                    m_compArea.addView(t_imageButton);
                }
            }
        }
    }

    /**
     * MainRoundActivity::updateLayout
     * Method to update layout
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateLayout()
    {
        Vector<Vector<String>> t_cards = m_round.getLayoutStr();
        m_layoutArea.removeAllViews();
        LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayoutParam.gravity = Gravity.CENTER;
        linearLayoutParam.setMargins(0,2,0,2);

        for(int i = 0; i<t_cards.size(); i++)
        {
            for(int j = 0; j<t_cards.get(i).size(); j++)
            {
                ImageButton t_imageButton = new ImageButton(this);
                t_imageButton.setLayoutParams(linearLayoutParam);

                Context context = t_imageButton.getContext();
                int id = context.getResources().getIdentifier(t_cards.get(i).get(j), "drawable", context.getPackageName());
                t_imageButton.setImageResource(id);
                t_imageButton.setAdjustViewBounds(true);
                t_imageButton.setPadding(9,9,9,9);
                t_imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                // Highlight card if stacked
                if(t_cards.get(i).size()>1)
                    t_imageButton.setBackgroundColor(Color.rgb(255, 165, 0));

                m_layoutArea.addView(t_imageButton);
            }
        }
    }

    /**
     * MainRoundActivity::updateTopCard
     * Method to update top card on stockpile
     * @return void
     * @author Salil Maharjan
     * @date 04/23/20.
     */
    private void updateTopCard()
    {
        String t_card = "card" + m_round.getGUIchar(m_round.getStockDeck().viewTopCard().getCardRank(), m_round.getStockDeck().viewTopCard().getCardSuit()).toLowerCase();
        View v = m_topCard;
        if (v instanceof ImageButton)
        {
            Context context = v.getContext();
            int id = context.getResources().getIdentifier(t_card, "drawable", context.getPackageName());
            ((ImageButton) v).setImageResource(id);
        }

    }
}



// *********************************************************
// ******************** TRASH METHODS **********************
// *********************************************************

//    // Method to pass turn
//    public void switchTurns(View view)
//    {
//
//    }

//    // Method to draw new card
//    public void drawNewCard(View view)
//    {
//        Card t_card = m_round.drawStockCard();
//        if(t_card==null)
//            Toast.makeText(MainRoundActivity.this, "Invalid action: Please play your hand before drawing from stockpile.", Toast.LENGTH_LONG).show();
//        else {
//            Toast.makeText(MainRoundActivity.this, "You drew: " + m_round.getCardInformation(t_card.getCardRank(), t_card.getCardSuit())
//                    + " from the stock pile!", Toast.LENGTH_LONG).show();
//            c();
//        }
//    }

//
//    @Override
//    public void askUser(String a_message)
//    {
//        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainRoundActivity.this);
//        alertBoxBuilder.setTitle("GoStop");
//        alertBoxBuilder.setMessage(a_message);
//        // No background clicks
//        alertBoxBuilder.setCancelable(false);
//
//        alertBoxBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                System.out.println("No");
//                dialog.cancel();
////                m_game.startGoStop();
//            }
//        });
//        alertBoxBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                System.out.println("Yes");
////                m_firstPlayerAlert.setVisibility(View.VISIBLE);
//                dialog.cancel();
////                m_game.endGoStop();
//            }
//        });
//
//        alertBoxBuilder.show();
//    }

//    // Method to process stockpile move and finish turn
//    @Override
//    public void finishPlayerTurn(String a_message)
//    {
//        AlertDialog.Builder alertBoxBuilder = new AlertDialog.Builder(MainRoundActivity.this);
//        alertBoxBuilder.setCancelable(true);
//        alertBoxBuilder.setTitle("Console message");
//        alertBoxBuilder.setMessage(a_message);
//        // No background clicks
//        alertBoxBuilder.setCancelable(false);
//        alertBoxBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.cancel();
//                // Start playing the round:
////                a_player.makeStockMove(m_round);
////                if(m_turn==1)
////                    m_user.makeStockMove(m_round);
////                else
////                    m_comp.makeStockMove(m_round);
//            }
//        });
//        alertBoxBuilder.show();
//    }