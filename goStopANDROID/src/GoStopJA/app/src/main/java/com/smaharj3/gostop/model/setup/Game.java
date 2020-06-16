/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.model.setup;

import com.smaharj3.gostop.model.players.Computer;
import com.smaharj3.gostop.model.players.Player;
import com.smaharj3.gostop.model.players.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 ************************************************************
 * Game.java
 * Main Game class to play the game goStop.
 *
 *
 * Member Variables:
 *       m_user User player of the game.
 *       m_userName User player name.
 *       m_computer Computer player of the game.
 *       m_round Round currently being played in the game.
 *       m_roundNum Number of rounds played in the game.
 *       m_userScore Total user score.
 *       m_compScore Total computer score.
 *
 * Created by Salil Maharjan on 03/31/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
 */
public class Game
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Variable to hold the User player
    private Player m_user;

    /// Variable to hold player name
    private String m_userName;

    /// Variable to hold the Computer player
    private Player m_computer;

    /// Variable to hold the current round
    private Round m_round;

    /// Variable to hold the number of rounds
    private int m_roundNum;

    /// Variable to hold user's total score
    private int m_userScore;

    /// Variable to hold computer's total score
    private int m_compScore;


    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * Game::Game.
     * Game class constructor.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public Game() {
        // Initializing game score
        m_compScore = 0;
        m_userScore = 0;

        // Initializing rounds
        m_roundNum = 0;

        // Initializing the players of the game. One computer and user.
        m_user = new User();
        m_computer = new Computer();

        // Initializing username as empty
        m_userName = "";

        // Initialize empty round
        m_round = null;
    }


    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * Game::getRoundNumber
     * Accessor method to get round number.
     * @return int Number of rounds in the game.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final int getRoundNumber() { return m_roundNum; }

    /**
     * Game::getUserScore
     * Accessor method to get User's total score.
     * @return int User's Total score.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final int getUserScore()
    {
        return m_userScore;
    }

    /**
     * Game::getCompScore
     * Accessor method to get Computer's total score.
     * @return int Computer's Total Score.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final int getCompScore()
    {
        return m_compScore;
    }

    /**
     * Game::getPlayerName
     * Accessor method to get player's name.
     * @return String Player's name.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final String getPlayerName()
    {
        return m_userName;
    }

    // *********************************************************
    // *********************** Mutators ************************
    // *********************************************************

    /**
     * Game::setPlayerName
     * Mutator method to set player's name.
     * @return void
     * @author Salil Maharjan
     * @date 04/06/20.
     */
    public final void setPlayerName(String a_name)
    {
        if(a_name.length() <= 1)
            m_userName = "User";
        else
            m_userName = a_name;
    }

    // *********************************************************
    // ******************** MAIN METHOD ************************
    // *********************************************************

    // *********************************************************
    // ******************** CLASS METHODS **********************
    // *********************************************************

    /**
     * Game::startNewRound
     * Initialize a new round of Go Stop.
     * @return Round The initialized round.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public Round startNewRound()
    {
        // Temp Variable to hold the player turn:
        short t_turn;
        // Temp Variable to hold the round:
        Round t_round;

        // Looping round construction in case of modulo suits.
        do {
            // Initializing a round of go stop
            t_round = new Round(m_user, m_computer, m_userName);
            // Get player turn according to game rule:
            t_turn = t_round.determineFirstPlayer();
            // Modulo Suit case
            if(t_turn == 3)
                System.out.println("Encountered a modulo suit.\nReshuffling deck and starting new round.");
        } while (t_turn==3);

        // Setting round.
        m_round = t_round;
        m_roundNum++;

        // Sorting the hand pile
        m_user.sortHandPile(m_round);
        m_computer.sortHandPile(m_round);

        return m_round;
    }

    /**
     * Game::getFirstPlayerDecision
     * Method to get first player decision.
     * @return String The logic behind first player decision.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public String getFirstPlayerDecision()
    {
        // Modulo suits are skipped by this point.
        short t_turn = m_round.determineFirstPlayer();

        // Initial round or a tie score:
        if(m_roundNum == 1 || m_userScore == m_compScore)
        {
            /*Correct player is already initialized by determineFirstPlayer*/
            m_round.setPlayerTurn(t_turn);

            if(t_turn == 1)
                return ( m_userName+ " has more higher rank cards.\nFirst player: " + m_userName );
            else
                return ("Computer has more higher rank cards.\nFirst player: COMPUTER");
        }
        // Player with higher score plays on subsequent rounds
        else {
            if(m_userScore < m_compScore) {
                m_round.setPlayerTurn((short) 2);
                return ("Computer has higher score than user.\n First player: COMPUTER");
            }
            else {
                m_round.setPlayerTurn((short) 1);
                return (m_userName +" has higher score than computer.\n First player: " + m_userName);
            }
        }
    }

    /**
     * Game::endOfRound
     * Method to update game after the end of a round.
     * @return void
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    public void endOfRound(Round a_round)
    {
        // Updating round values
        m_round = a_round;

        // Updating total game score:
        m_userScore += a_round.getUserScore();
        m_compScore += a_round.getComputerScore();
    }

    /**
     * Game::getEndMessage
     * Method to get end message of who won.
     * @return String End game message
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    public String getEndMessage()
    {
        String t_message = "";
        t_message += "****************************************************************\n";

        if(m_userScore>m_compScore)
            t_message += " YOU WIN THIS ROUND OF GO STOP! CONGRATULATIONS! \n";
        else if(m_userScore==m_compScore)
            t_message += " ------- THE GAME IS TIED -------- \n";
        else
            t_message += " COMPUTER WINS! BETTER LUCK NEXT TIME. \n";

        t_message += "****************************************************************\n";
        t_message += getGameStats();

        t_message += "\n\n\n---------  Do you want to play another round of Go Stop? -------\n\n";

        return t_message;
    }

    /**
     * Game::getGameStats
     * Method to get game statistics.
     * @return void
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    public String getGameStats()
    {
        String t_message = "";
        t_message += "Game Summary: \n";
        t_message += "On this round...\n";
        t_message = t_message + "User score: " + m_round.getUserScore() + "\n";
        t_message = t_message + "Computer score: " + m_round.getComputerScore() + "\n";
        t_message += "-----------------------------------------------------\n";
        t_message = t_message + "Total User score: " + m_userScore + "\n";
        t_message = t_message + "Total Computer score: " + m_compScore + "\n";
        t_message += "-----------------------------------------------------\n";
        return t_message;
    }

    /**
     * Game::loadFromFile
     * Method to load game from a text file
     * @param a_is InputStream saved game to load from
     * @return Round the loaded round
     * @author Salil Maharjan
     * @date 04/21/20.
     */
    public Round loadFromFile(InputStream a_is) throws IOException {
        // Buffer variable to store each line and line element.
        String t_buffer="";

        // Variables to hold previous game details:
        // Round number that was last played
        int t_roundNum = 0;
        // Computer and user score
        // Round score
        int t_compRoundScore = 0;
        int t_userRoundScore = 0;
        // Computer and user hand pile and capture pile
        Vector<Card> t_userHand = new Vector<>();
        Vector<Card> t_compHand = new Vector<>();
        Vector<Vector<Card>> t_userCapture = new Vector<>();
        Vector<Vector<Card>> t_compCapture = new Vector<>();
        // Game layout
        Vector<Vector<Card>> t_layout = new Vector<>();
        // Deck
        Vector<Card> t_deck = new Vector<>();
        // Player turn: 2 for computer, 1 for user
        short t_turn=0;


        BufferedReader reader = new BufferedReader(new InputStreamReader(a_is));
        String line = reader.readLine();
        while(line != null)
        {
            // Get Round information
            if(line.contains("Round"))
                t_roundNum = Integer.parseInt(line.split(":")[1].trim());

            // Get Computer player details
            if(line.contains("Computer") == true
                    && line.contains("Next Player") == false)
            {
                // Get score from next line
                line = reader.readLine();
                if(line.contains("Score"))
                    m_compScore = Integer.parseInt(line.split(":")[1].trim());

                // Get computer hand from next line
                line = reader.readLine();
                if(line.contains("Hand"))
                    t_buffer = line.split(":")[1].trim();
                // Parsing using space delimiter and updating computer hand:
                String[] t_cards = t_buffer.split(" ");
                for(String t_card: t_cards)
                    t_compHand.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));

                // Get computer capture pile from next line
                // Variable to hold stacked cards in pile
                line = reader.readLine();
                if(line.contains("Capture Pile"))
                    t_buffer = line.split(":")[1].trim();
                if(t_buffer.length()>1)
                {
                    // Parsing using space delimiter and updating computer capture:
                    t_cards = t_buffer.split(" ");
                    for(String t_card: t_cards) {
                        // Temp buffer to read stack of cards
                        Vector<Card> t_cardBuffer = new Vector<>();
                        t_cardBuffer.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));
                        // Pushing the buffer vector of cards and clearing it
                        t_compCapture.add(t_cardBuffer);
                    }
                }

            }

            // Get User player details
            if(line.contains("Human") == true
                    && line.contains("Next Player") == false)
            {
                // Get score from next line
                line = reader.readLine();
                if(line.contains("Score"))
                    m_userScore = Integer.parseInt(line.split(":")[1].trim());

                // Get user hand from next line
                line = reader.readLine();
                if(line.contains("Hand"))
                    t_buffer = line.split(":")[1].trim();
                // Parsing using space delimiter and updating user hand:
                String[] t_cards = t_buffer.split(" ");
                for(String t_card: t_cards)
                    t_userHand.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));

                // Get user capture pile from next line
                // Variable to hold stacked cards in pile
                line = reader.readLine();
                if(line.contains("Capture Pile"))
                    t_buffer = line.split(":")[1].trim();
                if(t_buffer.length()>1)
                {
                    // Parsing using space delimiter and updating user capture:
                    t_cards = t_buffer.split(" ");
                    for(String t_card: t_cards) {
                        // Temp buffer to read stack of cards
                        Vector<Card> t_cardBuffer = new Vector<>();
                        t_cardBuffer.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));
                        // Pushing the buffer vector of cards and clearing it
                        t_userCapture.add(t_cardBuffer);
                    }
                }

            }

            // Get layout
            if(line.contains("Layout"))
            {
                t_buffer = line.split(":")[1].trim();
                // Parsing using space delimiter and updating layout
                String[] t_cards = t_buffer.split(" ");
                for(String t_card: t_cards) {
                    // Temp buffer to read stack of cards
                    Vector<Card> t_cardBuffer = new Vector<>();
                    t_cardBuffer.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));
                    // Pushing the buffer vector of cards and clearing it
                    t_layout.add(t_cardBuffer);
                }
            }

            // Get stock pile, main deck:
            if(line.contains("Stock Pile"))
            {
                // Skip after :
                t_buffer = line.split(":")[1].trim();

                // Parsing using space delimiter and updating user hand:
                String[] t_cards = t_buffer.split(" ");
                for(String t_card: t_cards)
                    t_deck.add(new Card(asciiToRank(t_card), asciiToSuit(t_card)));
            }

            // Get player turn
            if(line.contains("Next Player"))
            {
                if(line.contains("Computer"))
                    t_turn = 2;
                else
                    t_turn = 1;
            }

            // Reading next line
            line = reader.readLine();
        }

        /*
         END OF READING FROM GAME CONFIG FILE.
         READY TO INITIALIZE GAME ACCORDING TO CONFIG FILE
         */
        // Checking if initialization completed:
        if(t_userHand.size()==0 && t_compHand.size()==0 && t_layout.size()==0)
        {
            System.err.println("Unable to load game from the provided file.");
            System.err.println("Please try a configuration file with the saved game. Thank you.");
            System.exit(1);
        }

        // CONSTRUCTING GAME:
        // Player reconstruction
        m_user = new User(t_userHand, t_userCapture, t_userRoundScore);
        m_computer = new Computer(t_compHand, t_compCapture, t_compRoundScore);
        // Round initialization
        m_roundNum = t_roundNum;

        // Initializing round class and starting the round:
        m_round = new Round(t_deck, t_layout, m_user, m_computer, "User");

        // Check if loaded game is a modulo suit:
        short t_temp = m_round.determineFirstPlayer();
        if(t_temp == 3)
        {
            System.err.println("The loaded game has a modulo suit.");
            System.err.println("Reshuffling deck and starting a new round.");
            return null;
        }

        // Arranging and sorting capture piles:
        m_user.arrangePile(m_round);
        m_computer.arrangePile(m_round);

        m_user.sortCapturePile(m_round);
        m_computer.sortCapturePile(m_round);
        // Sorting hand pile
        m_user.sortHandPile(m_round);
        m_computer.sortHandPile(m_round);

        // Set name and turn
        this.setPlayerName("User");
        m_round.setPlayerTurn(t_turn);

        return m_round;
    }

    /**
     * Game::saveGame
     * Method to save game to text file. Uses getSerialization method.
     * @return boolean If file was saved successfully.
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    public boolean saveGame(File a_file)
    {
        try {
            FileOutputStream t_writeFile = new FileOutputStream(a_file, false);
            t_writeFile.write(getSerialization().getBytes());
            t_writeFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Game::getSerialization
     * Method to serialize round for saving purposes.
     * @return String The serialization information.
     * @author Salil Maharjan
     * @date 04/22/20.
     */
    public String getSerialization()
    {
        // Get both player's hands and capture pile:
        Vector<Card> t_compHand = m_computer.getHandPile();
        Vector<Card> t_userHand = m_user.getHandPile();
        Vector<Vector<Card>> t_compCapture = m_computer.getCapturePile();
        Vector<Vector<Card>> t_userCapture = m_user.getCapturePile();
        Vector<Vector<Card>> t_layout = m_round.getLayout();


        String t_serialize = "";

        t_serialize += "Round: " + m_roundNum + "\n\n";

        // Computer stats
        t_serialize += "Computer:\n";
        t_serialize += "\tScore: " + m_compScore + "\n";
        t_serialize += "\tHand: ";
        for(int i = 0; i<t_compHand.size(); i++)
            t_serialize += m_round.getGUIchar(t_compHand.get(i).getCardRank(), t_compHand.get(i).getCardSuit()) + " ";
        t_serialize += "\n";
        t_serialize += "\tCapture Pile: ";
        for(int i = 0; i<t_compCapture.size(); i++)
        {
            for(int j = 0; j< t_compCapture.get(i).size(); j++)
            {
                t_serialize += m_round.getGUIchar(t_compCapture.get(i).get(j).getCardRank(), t_compCapture.get(i).get(j).getCardSuit()) + " ";
            }
        }
        t_serialize += "\n\n";

        // User stats
        t_serialize += "Human: \n";
        t_serialize += "\tScore: " + m_userScore + "\n";
        t_serialize += "\tHand: ";
        for(int i = 0; i<t_userHand.size(); i++)
            t_serialize += m_round.getGUIchar(t_userHand.get(i).getCardRank(), t_userHand.get(i).getCardSuit()) + " ";
        t_serialize += "\n";
        t_serialize+="\tCapture Pile: ";
        for(int i = 0; i<t_userCapture.size(); i++)
        {
            for(int j = 0; j< t_userCapture.get(i).size(); j++)
            {
                t_serialize += m_round.getGUIchar(t_userCapture.get(i).get(j).getCardRank(), t_userCapture.get(i).get(j).getCardSuit()) + " ";
            }
        }
        t_serialize += "\n\n";

        // Layout:
        t_serialize+="Layout: ";
        for(int i = 0; i<t_layout.size(); i++)
        {
            for(int j = 0; j< t_layout.get(i).size(); j++)
            {
                t_serialize += m_round.getGUIchar(t_layout.get(i).get(j).getCardRank(), t_layout.get(i).get(j).getCardSuit()) + " ";
            }
        }
        t_serialize += "\n";

        // Deck of cards from the top card:
        t_serialize += "\nStock Pile: ";
        Vector<Card> t_deck = m_round.getStockDeck().getDeck();
        for(int i = m_round.getStockDeck().topCardIndex(); i<t_deck.size(); i++)
            t_serialize += m_round.getGUIchar(t_deck.get(i).getCardRank(), t_deck.get(i).getCardSuit()) + " ";

        t_serialize += "\n";

        t_serialize += "\nNext Player: ";
        if(m_round.getPlayerTurn() == 2)
            t_serialize += "Computer\n";
        else
            t_serialize+="Human\n";

        return t_serialize;
    }

    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************

    /**
     * Game::asciiToRank
     * Method to get card rank from its ASCII representation.
     * @param a_string string ASCII representation of card
     * @return CardRank The rank of the card
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    private Card.CardRank asciiToRank(String a_string)
    {
        char t_rank = a_string.charAt(0);
        switch(t_rank)
        {
            case '1':
                return Card.CardRank.ACE;
            case '2':
                return Card.CardRank.TWO;
            case '3':
                return Card.CardRank.THREE;
            case '4':
                return Card.CardRank.FOUR;
            case '5':
                return Card.CardRank.FIVE;
            case '6':
                return Card.CardRank.SIX;
            case '7':
                return Card.CardRank.SEVEN;
            case '8':
                return Card.CardRank.EIGHT;
            case '9':
                return Card.CardRank.NINE;
            case 'X':
                return Card.CardRank.TEN;
            case 'J':
                return Card.CardRank.JACK;
            case 'Q':
                return Card.CardRank.QUEEN;
            case 'K':
                return Card.CardRank.KING;

        }
        System.err.println("Cannot determine Card Rank from the file. Method asciiToRank");
        System.exit(1);
        return null;
    }

    /**
     * Game::asciiToSuit
     * Method to get card suit from its ASCII representation.
     * @param a_string string ASCII representation of card
     * @return CardSuit The suit of the card
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    private Card.CardSuit asciiToSuit(String a_string)
    {
        char t_suit = a_string.charAt(1);

        switch(t_suit)
        {
            case 'H':
                return Card.CardSuit.HEARTS;
            case 'C':
                return Card.CardSuit.CLUBS;
            case 'S':
                return Card.CardSuit.SPADES;
            case 'D':
                return Card.CardSuit.DIAMONDS;
        }

        System.err.println("Cannot determine Card Suit from the file. Method asciiToSuit");
        System.exit(1);
        return null;
    }
}

// *********************************************************
// ********************* TRASH METHODS *********************
// *********************************************************
//
//    /**
//     * Game::getPlayingRound
//     * Accessor method to the current playing round.
//     * @return String Player's name.
//     * @author Salil Maharjan
//     * @date 03/31/20.
//     */
//    public final Round getPlayingRound()
//    {
//        return m_round;
//    }
//
//
//