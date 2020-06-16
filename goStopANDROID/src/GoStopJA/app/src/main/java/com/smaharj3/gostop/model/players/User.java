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

import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

/**
 ************************************************************
 * User.java
 * User class to implement User player of the game. Inherits Player class.
 *
 * Member Variables:
 *
 * Created by Salil Maharjan on 03/31/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
 */
public class User extends Player
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * User::User.
     * User class constructor.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public User()
    {
        super();
    }

    /**
     * User::User.
     * Parameterized User class constructor used for initiating game from a config file.
     * @param a_hand vector<Card> cards in hand of the User
     * @param a_capture vector<vector<Card>> Cards in the capture pile of the User
     * @param a_score unsigned int Score from config file
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public User(Vector<Card> a_hand, Vector<Vector<Card>> a_capture, int a_score)
    {
        super(a_hand, a_capture, a_score);
    }


    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

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

    /**
     * User::movePickHelp.
     * Method to get move recommendation. Used as a move helper for user.
     * @param a_round Round Pointer to the current round.
     * @return String First index of string has the card index to play, and the rest has the help logic.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public String movePickHelp(Round a_round)
    {
        // Temp message string. First index of string has the index of the card.
        String t_message = "";

        // Temp string to hold the detail of the logic
        String t_logic = "";

        // Variable to hold card index that can create a stacked pair:
        int t_cardIdx = -1;

        // Variable to hold number of matches on the layout with picked card:
        int t_matches;

        // Map for index of card on user hand and number of matches on the layout (int, int)
        TreeMap<Integer, Integer> t_matchMap = new TreeMap<>();

        for(int i = 0 ; i<m_hand.size(); i++)
        {
            // Check layout for matches
            t_matches = Integer.parseInt(String.valueOf(checkLayout(a_round, m_hand.get(i), false).charAt(0)));

            // Return if a triple stack is found:
            if(t_matches >= 3)
            {
                t_message = "You can chose to play " + a_round.getCardInformation(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()) + " because it can capture three cards or a triple stack and give you a point. \n";
                System.out.print(t_message);
                t_logic = checkLayout(a_round, m_hand.get(i), true);
                t_message = String.valueOf(i) + t_message + "\n" + t_logic.substring(1);
                return t_message;
            }

            // Can create one stacked pair:
            else if (t_matches == 1 || t_matches == 2)
            {
                // Recording index and matches
                // (Even if the the card matches 2 cards, only a single stack can be created,
                // so we initialize the same capture value)
                t_matchMap.put(i, 1);

                // Checking capture pile for existing stacked pairs of the same face:
                for(int j = 0; j<m_capture.size(); j++)
                {
                    // If found, a point can be gained by capturing current pair.
                    // The card index is returned:
                    if(m_capture.get(j).get(0).getCardRank() == m_hand.get(i).getCardRank()
                            && (m_capture.get(j).size() + 2) == 4)
                    {
                        t_message = "You can chose to play " + a_round.getCardInformation(m_hand.get(i).getCardRank(), m_hand.get(i).getCardSuit()) + " because it can capture a stacked pair and build a captured pair to earn a point. \n";
                        System.out.print(t_message);
                        t_logic = checkLayout(a_round, m_hand.get(i), true);
                        t_message = String.valueOf(i) + t_message + "\n" + t_logic.substring(1);
                        return t_message;
                    }
                }
                // Recording index that can create a stacked pair
                t_cardIdx = i;
            }
            // No matches
            else
                t_matchMap.put(i, 0);
        }

        // If function reaches here, none of the cards on hand can give a point.
        // So a card that creates a stacked pair is returned.
        if(t_cardIdx != -1)
        {
            t_message = "You can chose to play "+ a_round.getCardInformation(m_hand.get(t_cardIdx).getCardRank(), m_hand.get(t_cardIdx).getCardSuit()) + " because it can build and capture a stacked pair. \n";
            System.out.print(t_message);
            t_logic = checkLayout(a_round, m_hand.get(t_cardIdx), true);
            t_message = String.valueOf(t_cardIdx) + t_message + "\n" + t_logic.substring(1);
            return t_message;
        }

        // If no stacked card can be created, a random card is picked:
        else
        {
            int t_rand = new Random().nextInt(m_hand.size());
            t_message = "You can chose to play " + a_round.getCardInformation(m_hand.get(t_rand).getCardRank(), m_hand.get(t_rand).getCardSuit()) + " because you have no card that matches a card on the layout.\n" +
                    "You can pick any card for this turn. \n";
            System.out.print(t_message);
            t_message = String.valueOf(t_rand) + t_message;
            return t_message;
        }
    }

    /**
     * User::playHandCard.
     * Overloaded method to play a turn for user.
     * @param a_cardIdx int Index of card to play
     * @param a_round Round The round being played in the game
     * @param a_help boolean If help needs to be displayed.
     * @return Vector<Integer> Vector of card matches when 2 matches are found, else null is returned. Computer always returns null.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public Vector<Integer> playHandCard(int a_cardIdx, Round a_round, boolean a_help)
    {
        // Checking layout for # of matches on the layout
        // and performing the move:
        m_handMatches = (short) Integer.parseInt(String.valueOf(checkLayout(a_round, m_hand.get(a_cardIdx), false).charAt(0)));
        Vector<Integer> t_matches = processMove(a_round, m_hand.get(a_cardIdx), m_handMatches);

        // When two matches are found
        if(t_matches != null)
            return t_matches;

        // Removing card from hand
        m_hand.removeElementAt(a_cardIdx);

        // Sorting piles
        sortHandPile(a_round);
        sortCapturePile(a_round);

        return null;
    }

    /**
     * User::processPickedLayoutCard
     * Method to make stacked pair on the chosen card on the layout by the user.
     * @param a_cardIdx int Index of card picked from the hand
     * @param a_cardLayout int Index of card picked on the layout to stack with.
     * @param a_round Round The round being played currently.
     * @param a_help boolean If help is requested by the user.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void processPickedLayoutCard(int a_cardIdx, int a_cardLayout, Round a_round, boolean a_help)
    {
        // Checking layout for # of matches on the layout
        // and performing the move:
        m_handMatches = (short) Integer.parseInt(String.valueOf(checkLayout(a_round, m_hand.get(a_cardIdx), false).charAt(0)));

        // Asserting H2 condition
        assert(m_handMatches==2);

        // Adding to layout
        a_round.addToLayout(m_hand.get(a_cardIdx), a_cardLayout);

        // Removing card from hand
        m_hand.removeElementAt(a_cardIdx);

        // Sorting piles
        sortHandPile(a_round);
        sortCapturePile(a_round);
    }

    /**
     * User::playStockPile.
     * Method to play stock pile after completing hand pile move and capture remaining stacked pairs.
     * Calls getStackedPairs function to capture stacked pairs from the turn.
     * @param a_round Round* Current round of the game being played.
     * @param a_help bool Flag for help mode for user and move details for computer player.
     * @return String The drawn card from the stockpile and the logic of the move help.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public String playStockPile(Round a_round, boolean a_help)
    {
        // Variable to hold number of matches on the layout with drawn card:
        short t_matches;

        // Temp string message variable
        String t_message;
        String t_temp;

        // Drawing card from the stock pile
        // THIS IS DONE IN THE ACTIVITY
        Card t_stockCard = a_round.drawStockCard();
        t_message = "You drew: " + a_round.getCardInformation(t_stockCard.getCardRank(), t_stockCard.getCardSuit())
                +" from the stock pile!\n";
        System.out.println(t_message);

        // Previous hand case was H0 or H3:
        if(m_handMatches == 0 || m_handMatches >=3)
        {
            // Checking layout for # of matches on the layout
            // and performing the move:
            t_temp = checkLayout(a_round, t_stockCard, a_help);
            t_matches = (short) Integer.parseInt(String.valueOf(t_temp.charAt(0)));
            t_message = t_message + "\n" + t_temp.substring(1);

            Vector<Integer> t_layoutMatches = processMove(a_round, t_stockCard, t_matches);

            // If two matches are found, user needs to make a pick
            if(t_layoutMatches != null)
            {
//                System.out.print(t_message);

                // Get matches in string format
                String temp="";
                for(int i = 0; i<t_layoutMatches.size(); i++)
                {
                    temp += t_layoutMatches.get(i).toString();
                    temp += " | ";
                }
                t_message = temp + t_message+ "\n";

                // Temporarily adding stock card to the end of the layout
                a_round.addToLayout(t_stockCard, a_round.getLayout().size());

                return t_message;
            }
            else
            {
                // Capture stacked pairs on the layout:
                getStackedPairs(a_round);

                // Return logic message
                t_message = t_message + "\n";

                // Sort capture pile
                sortCapturePile(a_round);

                return t_message;
            }

        }
        // Previous hand case was H1 or H2
        else
        {
            // Flag for checking if drawn card matches a stacked pair in the layout,
            // This results in no capture and a triple stack on the layout
            boolean a_stackMatch = false;

            // Get layout of the round:
            Vector<Vector<Card>> t_layout = a_round.getLayout();

            // Variable to hold the index of the stacked pair
            // that matches with the drawn stock card
            // Only initialized if a_stackMatch is true.
            int t_cardIdx = -1;

            // Check to see if drawn card matches stacked pair:
            for(int i = 0; i<t_layout.size(); i++)
            {
                if(t_layout.get(i).get(0).getCardRank() == t_stockCard.getCardRank()
                        && t_layout.get(i).size() == 2)
                {
                    a_stackMatch = true;
                    t_cardIdx = i;
                }
            }

            // Checking layout for # of matches on the layout:
            t_temp = checkLayout(a_round, t_stockCard, a_help);
            t_matches = (short) Integer.parseInt(String.valueOf(t_temp.charAt(0)));

            // Card does not match a stacked pair
            if(a_stackMatch == false)
            {
                // Process move normally
                Vector<Integer> t_layoutMatches = processMove(a_round, t_stockCard, t_matches);

                // If two matches are found, user needs to make a pick
                if(t_layoutMatches != null)
                {
//                    System.out.print(t_message);

                    // Get matches in string format
                    String temp="";
                    for(int i = 0; i<t_layoutMatches.size(); i++)
                    {
                        temp += t_layoutMatches.get(i).toString();
                        temp += " | ";
                    }
                    t_message = temp + t_message+ "\n" + t_temp.substring(1);

                    // Temporarily adding stock card to the end of the layout
                    a_round.addToLayout(t_stockCard, a_round.getLayout().size());

                    return t_message;
                }
                else
                {
                    // Capture stacked pairs from previous move
                    getStackedPairs(a_round);

                    // Return logic message
                    t_message = t_message + "\n" + t_temp.substring(1);

                    // Sort capture pile
                    sortCapturePile(a_round);

                    return t_message;
                }
            }
            // Card matches stacked pair:
            else
            {
                // If drawn card only matches stacked pair
                // and index of stacked pair is initialized:
                if(t_matches == 0 && t_cardIdx != -1)
                {
                    // A triple stack is created and no capture is made
                    a_round.addToLayout(t_stockCard, t_cardIdx);
//                    System.out.println(t_message);

                    // Get logic
                    String t_logic = checkLayout(a_round, t_stockCard, true);

                    t_message = t_message + "\n" + "TRIPLE STACK IS LEFT ON THE PILE! You were unable to make the capture...\n" + t_logic.substring(1);

                    // Sort capture pile
                    sortCapturePile(a_round);

                    return t_message;
                }
                else
                {
                    // Get logic
                    String t_logic = checkLayout(a_round, t_stockCard, true);

                    // There are other options to place the card:
                    // Process move normally
                    // Function processMove
                    Vector<Integer> t_layoutMatches = processMove(a_round, t_stockCard, t_matches);

                    // If two matches are found, user needs to make a pick
                    if(t_layoutMatches != null)
                    {
//                        System.out.print(t_message);

                        // Get matches in string format
                        String temp="";
                        for(int i = 0; i<t_layoutMatches.size(); i++)
                        {
                            temp += t_layoutMatches.get(i).toString();
                            temp += " | ";
                        }
                        t_message = temp + t_message+ "\n" + t_logic.substring(1);

                        // Temporarily adding stock card to the end of the layout
                        a_round.addToLayout(t_stockCard, a_round.getLayout().size());

                        return t_message;
                    }
                    else
                    {
                        // Capture stacked pairs from previous move
                        getStackedPairs(a_round);

                        // Sort capture pile
                        sortCapturePile(a_round);

                        // Return logic message
                        t_message = t_message + "\n" + t_logic.substring(1);
                        return t_message;
                    }
                }
            }
        }
    }

    /**
     * User::finishStockPlay.
     * Method to finish stock play. Used by user to capture remaining stacks after choosing a card to stack with from stockpile. Computer does not use it.
     * @param a_round Round Current round of the game being played.
     * @return void
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public void finishStockPlay(Round a_round)
    {
        // Capture stacked pairs from previous move
        getStackedPairs(a_round);

        // Sort capture pile
        sortCapturePile(a_round);
    }


    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************

    /**
     * User::checkLayout.
     * Method to check layout for matching card faces with a_card and return number of matches.
     * @param a_round Round Current round of the game being played.
     * @param a_card Card Card to check on the layout
     * @param a_print boolean Flag to print match statistics
     * @return String Index 0 has number of matches, rest has logic for selection.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    protected String checkLayout(Round a_round, Card a_card, boolean a_print)
    {
        // Temp message string
        String t_message = "";

        // Variable to count number of matches:
        short t_count = 0;

        // Get layout of the round:
        Vector<Vector<Card>> t_layout = a_round.getLayout();


        // Checking the layout for matches:
        if(a_print)
        {
            t_message = "Picked card: " + a_round.getCardInformation(a_card.getCardRank(), a_card.getCardSuit()) + "\n" +
                    "Recommended layout captures: \n";
        }
        for(int i = 0; i<t_layout.size(); i++)
        {
            for(int j = 0; j< t_layout.get(i).size(); j++)
            {
                // Not counting if it is a stacked pair:
                // Stack pairs on the layout are only possible on the stock pile draw and if the previous match case was
                // H1 or H2.
                // Stacked pairs are played differently. See function seeStockPile
                if (t_layout.get(i).size()==2)
                    continue;

                if(t_layout.get(i).get(j).getCardRank() == a_card.getCardRank())
                {
                    if(a_print)
                        t_message = t_message + a_round.getCardInformation(t_layout.get(i).get(j).getCardRank(), t_layout.get(i).get(j).getCardSuit()) +"\n";

                    t_count++;
                    // Triple stack notification
                    if(j==2 && a_print)
                        t_message = t_message + "[ A triple stack! ] \n";
                }
            }
        }

        if(t_count==0 && a_print)
            t_message = t_message + "NA \n";
        else
        {
            if(a_print)
                t_message += "\n";
        }

        if(a_print)
        {
            t_message = t_message + "Number of matches: " + t_count + "\n";
            System.out.print(t_message);
        }

        t_message = String.valueOf(t_count) + t_message;
        return t_message;
    }

    /**
     * User::processMove.
     * Method to put card on the layout and perform the required captures. Defined as H0, H1, H2 AND H2 in the game description
     * @param a_round Round Current round of the game being played.
     * @param a_card Card Card to check on the layout
     * @param a_matches short The number of matches of card a_card in the layout. Generated by method checkLayout.
     * @return Vector<Integer> Vector of card matches when 2 matches are found, else null is returned.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    protected Vector<Integer> processMove(Round a_round, Card a_card, short a_matches)
    {
        // H0 CASE
        // H0: card matches no card in the layout, the card is added to the layout.
        if (a_matches == 0)
        {
            a_round.addToLayout(a_card, a_round.getLayout().size());
            return null;
        }
        // H1 CASE
        // H1: card matches one card in the layout, the player creates a stack pair of the two cards and leaves it in the layout.
        else if (a_matches == 1)
        {
            // Get layout of the round:
            Vector<Vector<Card>> t_layout = a_round.getLayout();

            for(int i = 0; i<t_layout.size(); i++)
            {
                // Check for matching faces and ignoring stacked pairs:
                if(t_layout.get(i).get(0).getCardRank() == a_card.getCardRank() && t_layout.get(i).size() != 2)
                a_round.addToLayout(a_card, i);
            }

            return null;
        }
        // H2 CASE
        // H2: card matches two cards in the layout, the player picks one of the two cards and creates a stack pair with it and the card played from the hand, leaving the stack pair in the layout.
        else if (a_matches == 2)
        {
            // Variable to hold card placement index:
            int t_cardIdx = 999;

            // Get layout of the round:
            Vector<Vector<Card>> t_layout = (Vector<Vector<Card>>) a_round.getLayout().clone();
            // Map to hold index of matching cards on the layout
            TreeMap<Integer, Card> t_cardMatch = new TreeMap<>();
            // Vector of indices of matched cards:
            Vector<Integer> t_cardMatches = new Vector<>();

            for(int i = 0; i<t_layout.size(); i++)
            {
                // Check for matching faces and ignoring stacked pairs:
                if(t_layout.get(i).get(0).getCardRank() == a_card.getCardRank() && t_layout.get(i).size() != 2)
                    t_cardMatches.add(i);
            }
            return t_cardMatches;
        }
        // 3 or more, H3 CASE. (TRIPLE STACK IS PRIORITIZED AUTOMATICALLY)
        // H3: card matches three cards in the layout or triple stack, the player captures all four cards, i.e., adds them to their capture pile.
        else
        {
            // Get layout of the round:
            Vector<Vector<Card>> t_layout = a_round.getLayout();

            // Vector to hold indexes of matching cards in the layout
            Vector<Integer> t_cardIdx = new Vector<>();

            // Checking for matching faces
            for(int i = 0; i<t_layout.size(); i++)
            {
                // Checking for triple stack and capturing if found:
                if(t_layout.get(i).get(0).getCardRank() == a_card.getCardRank() && t_layout.get(i).size()==3)
                {
                    a_round.addToLayout(a_card, i);
                    captureStack(a_round, i);
                    a_round.removeFromLayout(i);
                    return null;
                }
                // Check for matching faces and ignoring stacked pairs:
                if(t_layout.get(i).get(0).getCardRank() == a_card.getCardRank() && t_layout.get(i).size() != 2)
                    t_cardIdx.add(i);
            }

            // If function reaches here, no triple stack was found.
            // But, there are 3 cards on the layout that has the same face.
            // Capturing all cards:
            if(t_cardIdx.size()>=3)
            {
                // Leave the card from your hand on the first card
                // then capture all 4 cards:
                a_round.addToLayout(a_card, t_cardIdx.get(0));
                // You can only capture 3 cards from the layout
                // Even if there are more.
                // The first 3 matching cards are captured.
                for(int i = 0; i<3; i++)
                    captureStack(a_round, t_cardIdx.get(i));
                // Removing the cards captured from the layout
                for(int i = 0; i<3; i++)
                {
                    // If i elements are removed, the index is less by i
                    if(i>0)
                        t_cardIdx.set(i, t_cardIdx.get(i)-i);
                    a_round.removeFromLayout(t_cardIdx.get(i));
                }
                // Updating score as the score increments three times on function call
                m_score -= 2;

            }
            return null;
        }
    }
}


// *********************************************************
// ******************** TRASH METHODS **********************
// *********************************************************

//
//    /**
//     * User::play.
//     * Overloaded method to play a turn for user.
//     * @param a_round Round The round being played currently.
//     * @return boolean 0 if the player wants to stop playing.
//     * @author Salil Maharjan
//     * @date 04/03/20.
//     */
//    public void play(Round a_round)
//    {
////        boolean t_helpMode = m_view.getHelpSwitchVal();
//
////        if(t_helpMode)
////        {
////            movePickHelp(a_round);
////        }
////        m_view.activatePlayerHand(this, t_helpMode);
//    }

//
//    /**
//     * User::makeStockMove.
//     * Method to process a user stockpile move.
//     * @param a_round Round Pointer to the current round.
//     * @return void
//     * @author Salil Maharjan
//     * @date 04/03/20.
//     */
//    public void makeStockMove(Round a_round)
//    {
//        // Sort player piles
//        sortHandPile(a_round);
//        sortCapturePile(a_round);
//
//        System.out.println("makeStockMove");
//        Vector<Vector<String>> temp = a_round.getCaptureStr((short)1);
//        for(int i = 0; i<temp.size(); i++)
//        {
//            for(int j=0; j<temp.get(i).size(); j++)
//                System.out.println(temp.get(i).get(j));
//        }
//
//        a_round.displayBoard((short) 1);
//
////        m_view.startNextTurn("End of turn. Continue to next player's turn.", (short) 2);
//
//    }