/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.model.players;

import com.smaharj3.gostop.model.setup.Card;
import com.smaharj3.gostop.model.setup.Round;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 ************************************************************
 * Player.java
 * Player class to implement behavior of a player in the game of Go Stop.
 *
 *
 * Member Variables:
 *      m_hand Player hand pile
 *      m_capture Player capture pile
 *      m_score Player score
 *      m_handMatches Hand card matches on layout
 *
 * Created by Salil Maharjan on 03/31/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
 */
abstract public class Player implements Serializable
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Hand pile of a player
    protected Vector<Card> m_hand;

    /// Capture pile of a player
    protected Vector<Vector<Card>> m_capture;

    /// Player score variable:
    protected int m_score;

    /// Variable to hold hand match case
    protected short m_handMatches;


    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************
    /**
     * Player::Player
     * Player class constructor.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public Player()
    {
        m_hand = new Vector<>();
        m_capture = new Vector<Vector<Card>>();
        m_score = 0;
    }

    /**
     * Player::Player
     * Parameterized player class constructor used for initiating game from a config file.
     * @param a_hand vector<Card*> cards in hand of the player
     * @param a_capture vector<vector<Card*>> Cards in the capture pile of the player
     * @param a_score unsigned int Score from config file
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public Player(Vector<Card> a_hand, Vector<Vector<Card>> a_capture, int a_score)
    {
        m_hand = (Vector<Card>) a_hand.clone();
        m_capture = (Vector<Vector<Card>>) a_capture.clone();
        m_score = a_score;
    }

    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * Player::getHandPile.
     * Accessor to get player's hand pile.
     * @return Vector<Card> Player's hand pile.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Vector<Card> getHandPile() { return this.m_hand; }

    /**
     * Player::getCapturePile.
     * Accessor to get player's capture pile.
     * @return Vector<Vector<Card>> Player's capture pile.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Vector<Vector<Card>> getCapturePile() { return this.m_capture; }

    /**
     * Player::getPlayerScore.
     * Accessor to get player's round score.
     * @return int Player's round score.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final int getPlayerScore() { return this.m_score; }

    // *********************************************************
    // *********************** Mutators ************************
    // *********************************************************

    // *********************************************************
    // ******************** MAIN METHOD ************************
    // *********************************************************

    // *********************************************************
    // ******************** CLASS METHODS **********************
    // *********************************************************

    /**
     * Player::handPlayerCard.
     * Method to deal a card to the player
     * @param a_card Card The card to hand player
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void handPlayerCard(Card a_card) { m_hand.add(a_card); }

    /**
     * Player::clearPile.
     * Method to clear capture and hand pile and reset player score.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void clearPile()
    {
        try
        {
            this.m_hand.removeAllElements();
            this.m_capture.removeAllElements();
        } catch (NullPointerException e)
        {}

        this.m_score = 0;
    }

    /**
     * Player::sortHandPile.
     * Method to sort player hand pile.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void sortHandPile(Round a_round)
    {
        // Map to hold the pile faces
        TreeMap<Character, Integer> t_pileMap = new TreeMap<>();

        // Temp vector to hold hand pile
        Vector<Card> t_temp = new Vector<>();

        // Recording pile faces, map sorts the face value
        int t_count;
        char t_char;
        for(int i = 0; i<m_hand.size(); i++)
        {
            // Renaming for 10 and King face so that the map has them in the card face order:
            if(a_round.getGUIchar(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()).charAt(0) == 'X')
            {
                t_count = t_pileMap.containsKey('A')? t_pileMap.get('A'):0;
                t_pileMap.put('A', t_count+1);
            }
        else if(a_round.getGUIchar(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()).charAt(0) == 'K')
            {
                t_count = t_pileMap.containsKey('Z')? t_pileMap.get('Z'):0;
                t_pileMap.put('Z', t_count+1);
            }
        else
            {
                t_char = a_round.getGUIchar(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()).charAt(0);
                t_count = t_pileMap.containsKey(t_char)? t_pileMap.get(t_char):0;
                t_pileMap.put(t_char, t_count+1);
            }
        }

        // Variable to hold the face to compare
        char t_comp;
        // Iterating through the sorted faces
        for (Map.Entry<Character, Integer> entry : t_pileMap.entrySet())
        {
            t_comp = entry.getKey();
            // Reverting face names
            if(t_comp=='A')
                t_comp = 'X';
            if(t_comp=='Z')
                t_comp = 'K';

            // Finding matching face from the capture pile
            for(int i = 0; i<m_hand.size(); i++)
            {
                // If it matches the face, we push the card from the pile
                if(a_round.getGUIchar(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()).charAt(0) == t_comp)
                {
                    t_temp.add(m_hand.get(i));
                }
            }

        }
        // Reassigning the new sorted pile
        m_hand.clear();
        m_hand = t_temp;
    }

    /**
     * Player::sortCapturePile.
     * Method to sort player capture pile.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void sortCapturePile(Round a_round)
    {
//        //DEBUGGING
//        for(int n=0; n<m_capture.size(); n++)
//        {
//            for(int m=0; m<m_capture.get(n).size(); m++)
//            {
//                System.out.print("sortCapturePile1: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//            }
//        }

        // Map to hold the pile faces
        TreeMap<Character,Integer> t_pileMap = new TreeMap<>();

        // Temp capture pile variable to hold the sorted pile
        Vector<Vector<Card>> t_sortedPile = new Vector<>();

        // Recording pile faces, map sorts the face value
        int t_count;
        char t_char;
        for(int i = 0; i<m_capture.size(); i++)
        {
            // Renaming for 10 and King face so that the map has them in the card face order:
            if(a_round.getGUIchar(m_capture.get(i).get(0).getCardRank(), m_capture.get(i).get(0).getCardSuit()).charAt(0) == 'X')
            {
                t_count = t_pileMap.containsKey('A')? t_pileMap.get('A'):0;
                t_pileMap.put('A', t_count+1);
            }
            else if(a_round.getGUIchar(m_capture.get(i).get(0).getCardRank(), m_capture.get(i).get(0).getCardSuit()).charAt(0) == 'K')
            {
                t_count = t_pileMap.containsKey('Z')? t_pileMap.get('Z'):0;
                t_pileMap.put('Z', t_count+1);
            }
            else
            {
                t_char = a_round.getGUIchar(m_capture.get(i).get(0).getCardRank(), m_capture.get(i).get(0).getCardSuit()).charAt(0);
                t_count = t_pileMap.containsKey(t_char)? t_pileMap.get(t_char):0;
                t_pileMap.put(t_char, t_count+1);
            }
        }

        // Variable to hold the face to compare
        char t_comp;
        // Iterating through the sorted faces
        for (Map.Entry<Character, Integer> entry : t_pileMap.entrySet())
        {
//            // DEBUGGING
//            System.out.println("t_pileMap: "+ entry.getKey() + " " + entry.getValue());
            // Temp vector to hold stacked piles
            Vector<Card> t_temp = new Vector<>();

            t_comp = entry.getKey();
            // Reverting face names
            if(t_comp=='A')
                t_comp = 'X';
            if(t_comp=='Z')
                t_comp = 'K';

            // Finding matching face from the capture pile
            for(int i = 0; i<m_capture.size(); i++)
            {
                // If it matches the face, we push the card from the pile
                if(a_round.getGUIchar(m_capture.get(i).get(0).getCardRank(), m_capture.get(i).get(0).getCardSuit()).charAt(0) == t_comp)
                {
                    for(int j = 0; j< m_capture.get(i).size(); j++)
                        t_temp.add(m_capture.get(i).get(j));
                    t_sortedPile.add(t_temp);
                    t_temp = new Vector<>();
                }
            }
        }

//        // DEBUGGING
//        for(int n=0; n<t_sortedPile.size(); n++)
//        {
//            for(int m=0; m<t_sortedPile.get(n).size(); m++)
//            {
//                System.out.print("tsortedPile: " + a_round.getGUIchar(t_sortedPile.get(n).get(m).getCardRank(), t_sortedPile.get(n).get(m).getCardSuit()) + " ");
//            }
//        }

        // Reassigning the new sorted pile
        m_capture.clear();
        m_capture = (Vector<Vector<Card>>) t_sortedPile.clone();

//        //DEBUGGING
//        for(int n=0; n<m_capture.size(); n++)
//        {
//            for(int m=0; m<m_capture.get(n).size(); m++)
//            {
//                System.out.print("sortCapturePile2: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//            }
//        }
    }

    /**
     * Player::arrangePile.
     * Method to sort capture pile and make appropriate stacks when loading from config file.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void arrangePile(Round a_round)
    {
        // Temp capture pile variable to hold the new pile
        Vector<Vector<Card>> t_sortedPile = new Vector<>();

        // Iterating through capture file to find stack matches
        for(int i=0; i<m_capture.size();i++)
        {
            // Temp vector to hold stacked piles
            Vector<Card> t_temp = new Vector<>();

            if(m_capture.get(i).isEmpty())
                continue;

            for(int j=i; j<m_capture.size();j++)
            {
                if(m_capture.get(j).isEmpty())
                    continue;

                // Pushing first card
                if(j==i)
                {
                    t_temp.add(m_capture.get(i).get(0));
                    m_capture.get(i).clear();
                    continue;
                }

                // When a set of 4 is created it is pushed to the main temp stockpile
                // and the buffer is cleared for reuse
                if(t_temp.size()==4)
                {
                    t_sortedPile.add(t_temp);
                    m_score++;
                    t_temp = new Vector<>();
                    t_temp.add(m_capture.get(j).get(0));
                    m_capture.get(j).clear();
                    continue;
                }

                // Pushing cards with same faces to make sets
                if((a_round.getGUIchar(t_temp.get(0).getCardRank(), t_temp.get(0).getCardSuit())).charAt(0)
                   == (a_round.getGUIchar(m_capture.get(j).get(0).getCardRank(), m_capture.get(j).get(0).getCardSuit())).charAt(0))
                {
                    // Check if a set of <4 can be created
                    if(m_capture.get(j).size()+t_temp.size()<=4)
                    {
                        t_temp.add(m_capture.get(j).get(0));
                        m_capture.get(j).clear();
                    }
                    // Else we need push and clear the card buffer
                    else
                    {
                        t_sortedPile.add(t_temp);
                        t_temp.clear();
                        t_temp.add(m_capture.get(j).get(0));
                        m_capture.get(j).clear();
                    }
                }
            }
            // Pushing found stacks
            if(!t_temp.isEmpty())
                t_sortedPile.add(t_temp);
        }

        // Reassigning the new sorted pile
        m_capture.clear();
        m_capture = t_sortedPile;
    }

    // *********************************************************
    // ****************** ABSTRACT METHODS *********************
    // *********************************************************

    /**
     * Player::movePickHelp.
     * Method to get move recommendation. Used as a strategy for computer player and move helper for user.
     * @param a_round Round The round being played in the game
     * @return String First index of string has the card index to play, and the rest has the help logic.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract public String movePickHelp(Round a_round);

    /**
     * Player::playHandCard.
     * Virtual method to play a turn.
     * @param a_cardIdx int Index of card to play
     * @param a_round Round The round being played in the game
     * @param a_help boolean If help needs to be displayed.
     * @return Vector<Integer> Vector of card matches when 2 matches are found, else null is returned. Computer always returns null.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    // Function responding to click event to play user hand
    public abstract Vector<Integer> playHandCard(int a_cardIdx, Round a_round, boolean a_help);

    /**
     * Player::checkLayout.
     * Method to check layout for matching card faces.
     * @param a_round Round The round being played in the game
     * @param a_card Card Check layout for this card
     * @param a_print boolean If help needs to be displayed.
     * @return String Index 0 has number of matches, rest has logic for selection.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract protected String checkLayout(Round a_round, Card a_card, boolean a_print);

    /**
     * Player::processMove.
     * Virtual method to put card on the layout and process the selected move:
     * @param a_round Round The round being played in the game
     * @param a_card Card Check layout for this card
     * @param a_matches short Number of matches in the selected move.
     * @return Vector<Integer> Vector of card matches when 2 matches are found, else null is returned. Computer always returns null.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract protected Vector<Integer> processMove(Round a_round, Card a_card, short a_matches);

    /**
     * Player::processPickedLayoutCard
     * Method to make stacked pair on the chosen card on the layout by the user. Empty implementation for computer.
     * @param a_cardIdx int Index of card picked from the hand
     * @param a_cardLayout int Index of card picked on the layout to stack with.
     * @param a_round Round The round being played currently.
     * @param a_help boolean If help is requested by the user.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract public void processPickedLayoutCard(int a_cardIdx, int a_cardLayout, Round a_round, boolean a_help);

    /**
     * Player::playStockPile.
     * Method to play stock pile after completing hand pile move and capture remaining stacked pairs.
     * Calls getStackedPairs function to capture stacked pairs from the turn.
     * @param a_round Round Current round of the game being played.
     * @param a_help bool Flag for help mode for user and move details for computer player.
     * @return String The drawn card from the stockpile and the logic of the move help.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract public String playStockPile(Round a_round, boolean a_help);


    /**
     * Player::finishStockPlay.
     * Method to finish stock play. Used by user to capture remaining stacks after choosing a card to stack with from stockpile. Computer does not use it.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    abstract public void finishStockPlay(Round a_round);


    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************

    /**
     * Player::captureStack.
     * Method to capture stacks (triple stacks) from the layout
     * @param a_round Round Current round of the game being played.
     * @param a_index int Index of the card on the layout to capture
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    protected void captureStack(Round a_round, int a_index)
    {
//        //DEBUGGING
//        for(int n=0; n<m_capture.size(); n++)
//        {
//            for(int m=0; m<m_capture.get(n).size(); m++)
//            {
//                System.out.print("captureStack1: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//            }
//        }

        // Get layout of the round:
        Vector<Vector<Card>> t_layout = (Vector<Vector<Card>>) a_round.getLayout().clone();
        // Variable to hold captured cards
        Vector<Card> t_capture = new Vector<>();

        // Getting all layout cards:
        for(int i = 0; i< t_layout.get(a_index).size(); i++) {
            t_capture.add(t_layout.get(a_index).get(i));
        }

        // Check for existing stacked pairs that don't have 4 cards
        for(int i = 0; i<m_capture.size(); i++)
        {
            if(m_capture.get(i).get(0).getCardRank() == t_capture.get(0).getCardRank() && ((m_capture.get(i).size()+t_capture.size()) <= 4))
            {
                for(int j = 0; j<t_capture.size();j++) {
                    m_capture.get(i).add(t_capture.get(j));
                }

                // Updating score
                m_score++;

//                //DEBUGGING
//                for(int n=0; n<m_capture.size(); n++)
//                {
//                    for(int m=0; m<m_capture.get(n).size(); m++)
//                    {
//                        System.out.print("CaptureStack2: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//                    }
//                }

                return;
            }
        }

        // No existing pair, so we add a new capture pile
        m_capture.add(t_capture);
        m_score++;

//        //DEBUGGING
//        for(int n=0; n<m_capture.size(); n++)
//        {
//            for(int m=0; m<(m_capture.get(n)).size(); m++)
//            {
//                System.out.print("CaptureStack3: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//            }
//        }
    }

    /**
     * Player::getStackedPairs.
     * Method to find and capture stacked pairs at the end of each player's turn.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    // Method to find and capture stacked pairs at the end of each player's turn
    protected void getStackedPairs(Round a_round)
    {
        // Get layout of the round:
        Vector<Vector<Card>> t_layout = (Vector<Vector<Card>>) a_round.getLayout().clone();
        // Boolean to hold if the stack has been added
        boolean t_added = false;
        // Variable to hold number of cards removed.
        // Used to update index because:
        // If i elements are removed, the index is less by i
        // on the class member variable
        int t_actions = 0;

//        //DEBUGGING
//        System.out.print("BEFORE: " );
//        for(int n=0; n<t_layout.size(); n++)
//        {
//            for(int m=0; m<t_layout.get(n).size(); m++)
//            {
//                System.out.print(a_round.getGUIchar(t_layout.get(n).get(m).getCardRank(), t_layout.get(n).get(m).getCardSuit()) + " ");
//            }
//        }

        // Checking layout for stacked cards:
        for(int i = 0; i<t_layout.size(); i++)
        {
            // Stacked pair:
            if(t_layout.get(i).size() == 2)
            {
//                System.out.println("HERE"+i);

                // Check for existing stacked pairs that don't have 4 cards in capture pile
                for(int j = 0; j<m_capture.size(); j++)
                {
                    // Matching face:
                    if(m_capture.get(j).get(0).getCardRank() == t_layout.get(i).get(0).getCardRank())
                    {
                        // Checking if stacked pairs together make 4.
                        if((m_capture.get(j).size()+ t_layout.get(i).size()) <= 4)
                        {
                            // Pushing stacked pair to existing pair, making it a complete stack
                            for(int k = 0; k< t_layout.get(i).size(); k++) {
                                m_capture.get(j).add(t_layout.get(i).get(k));
                            }
//                            //DEBUGGING
//                            for(int n=0; n<m_capture.size(); n++)
//                            {
//                                for(int m=0; m<m_capture.get(n).size(); m++)
//                                {
//                                    System.out.print("GetStackedPairs: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//                                }
//                            }
                            // Setting flag
                            t_added = true;
                            // Updating score
                            m_score++;
                        }
                    }
                }
                // Checking flag and adding stack to capture if it has not been added
                if(t_added==false) {
                    this.m_capture.add(t_layout.get(i));
//                    //DEBUGGING
//                    for(int n=0; n<m_capture.size(); n++)
//                    {
//                        for(int m=0; m<m_capture.get(n).size(); m++)
//                        {
//                            System.out.print("GetStackedPairs: " + a_round.getGUIchar(m_capture.get(n).get(m).getCardRank(), m_capture.get(n).get(m).getCardSuit()) + " ");
//                        }
//                    }
                }


                // Removing from layout and updating action counter:
                // This works because we are iterating through the layout
                // in increasing order of indices
                a_round.removeFromLayout(i-t_actions);
                t_actions++;
            }

            // Resetting flag
            t_added=false;
        }

        t_layout = a_round.getLayout();
//        //DEBUGGING
//        System.out.print("AFTER: " );
//        for(int n=0; n<t_layout.size(); n++)
//        {
//            for(int m=0; m<t_layout.get(n).size(); m++)
//            {
//                System.out.print(a_round.getGUIchar(t_layout.get(n).get(m).getCardRank(), t_layout.get(n).get(m).getCardSuit()) + " ");
//            }
//        }
    }
}




// *********************************************************
// ******************** TRASH METHODS **********************
// *********************************************************

//    /**
//     * Player::play.
//     * Virtual method to play a turn.
//     * @param a_round Round The round being played in the game
//     * @return boolean If the play was successfully made.
//     * @author Salil Maharjan
//     * @date 04/03/20.
//     */
//    abstract public void play(Round a_round);


//    /**
//     * Player::drawStockPile.
//     * Interface method to draw from stockpile
//     * @param a_round Round Pointer to the current round.
//     * @param a_help bool Flag for help mode or not.
//     * @param a_matches short The number of matches made when playing from hand
//     * @return boolean If the play was successfully made.
//     * @author Salil Maharjan
//     * @date 04/03/20.
//     */
//    public void drawStockPile(Round a_round, boolean a_help, short a_matches)
//    {
//        System.out.println("drawStockPile: " + a_matches);
//        this.playStockPile(a_round, a_matches, a_help);
////        m_view.deactivateStockPile();
//    }

//    /**
//     * Player::makeStockMove.
//     * Virtual method to play the stockpile move.
//     * @param a_round Round Pointer to the current round.
//     * @return boolean If the play was successfully made.
//     * @author Salil Maharjan
//     * @date 04/03/20.
//     */
//    abstract public void makeStockMove(Round a_round);