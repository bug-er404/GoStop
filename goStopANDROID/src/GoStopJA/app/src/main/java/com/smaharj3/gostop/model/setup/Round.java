/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.model.setup;

import com.smaharj3.gostop.model.players.Player;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.Vector;

/**
 ************************************************************
 * Round.java
 * Round class to play a round of the game goStop.
 * Implements Serializable so a round object can be passed as extra from main (GameActivity to RoundActivity).
 *
 *
 * Member Variables:
 *          m_layout Layout of the game
 *          m_stockpile Stock pile deck
 *          m_user User player
 *          m_comp Computer player
 *          m_handPlayFlag Flag to play from hand pile
 *          m_stockPlayFlag Flag to play from stock pile
 *          m_playerTurn Current player turn
 *          m_userName User player name
 *
 * Created by Salil Maharjan on 03/31/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
 */
public class Round implements Serializable
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Variable to hold the game round's layout pile.
    private Vector<Vector<Card>> m_layout = new Vector<>();

    /// Variable to hold the game round's stock pile that is "faced down"
    private Deck m_stockpile;

    /// User player
    private Player m_user;

    /// Computer player
    private Player m_comp;

    /// Flag for hand draw:
    private boolean m_handPlayFlag;

    /// Flag for stockpile draw:
    private boolean m_stockPlayFlag;

    /// Player turn indicator: 1 for user, 2 for computer.
    private short m_playerTurn;

    /// Variable to hold player name
    private String m_userName;


    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * Round::Round
     * Round class constructor.
     * @param a_user Player User player object
     * @param a_comp Player Computer player object
     * @param a_userName String User player name
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public Round(Player a_user, Player a_comp, String a_userName)
    {
        // Temp initializations
        m_handPlayFlag = false;
        m_stockPlayFlag = false;
        m_playerTurn = 99;

        m_userName = a_userName;

        m_layout = new Vector<Vector<Card>>();

        // Recording game pointer, players and main view
//        m_game = a_game;
        m_user = a_user;
        m_comp = a_comp;

        // Initializing the two decks of cards and shuffling them for the round
        m_stockpile = new Deck(2);
        m_stockpile.shuffleDeck();

        // Clearing players hands:
        m_user.clearPile();
        m_comp.clearPile();

        /*
         Dealing card as per the rules of the game:
             5 cards are dealt to the human player;
             5 cards are dealt to the computer player;
             4 cards are placed face up in the layout;
             5 cards are dealt to the human player;
             5 cards are dealt to the computer player;
             4 cards are placed face up in the layout;
        */
        dealCards(m_user, 5);
        dealCards(m_comp, 5);
        for(int i = 0; i<4; i++)
        {
            Vector<Card> t_layout = new Vector<>();
            t_layout.add(m_stockpile.getNewCard());
            m_layout.add(t_layout);
        }
        dealCards(m_user, 5);
        dealCards(m_comp, 5);
        for(int i = 0; i<4; i++)
        {
            Vector<Card> t_layout = new Vector<>();
            t_layout.add(m_stockpile.getNewCard());
            m_layout.add(t_layout);
        }
    }

    /**
     * Round::Round
     * Parameterized round class constructor to initiate round from config file
     * @param a_deck Vector<Card> The deck to load for the game from the config file
     * @param a_layout Vector<Vector<Card>> The layout from th config file to load
     * @param a_user Player User player object
     * @param a_comp Player Computer player object
     * @param a_userName String User player name
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public Round(Vector<Card> a_deck, Vector<Vector<Card>> a_layout, Player a_user, Player a_comp, String a_userName)
    {
        // Initializing the two decks of cards and shuffling them for the round
        m_stockpile = new Deck(a_deck);

        // Initializing layout
        m_layout = a_layout;

        // Initializing players
        m_user = a_user;
        m_comp = a_comp;

        m_userName = a_userName;
    }

    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * Round::getLayout
     * Accessor to get the layout.
     * @return Vector<Vector<Card>> The layout of the round
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public final Vector<Vector<Card>> getLayout(){ return m_layout; }

    /**
     * Round::getRoundEndStatus
     * Accessor to get round's end status when both players hands are empty.
     * @return boolean If the round has ended.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final boolean getRoundEndStatus()
    {
        if(m_user.getHandPile().size()==0 && m_comp.getHandPile().size()==0)
            return true;
        else
            return false;
    }

    /**
     * Round::getPlayerTurn
     * Accessor to get player turn
     * @return short The player turn. 1 for user, 2 for computer.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final short getPlayerTurn()
    {
        return m_playerTurn;
    }

    /**
     * Round::getUserScore
     * Accessor to get user score.
     * @return int The user's score
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final int getUserScore()
    {
        return m_user.getPlayerScore();
    }

    /**
     * Round::getComputerScore
     * Accessor to get computer score.
     * @return int The computers's score
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final int getComputerScore()
    {
        return m_comp.getPlayerScore();
    }

    /**
     * Round::getStockDeck
     * Accessor to get stockpile.
     * @return Deck the stockpile used in the game
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Deck getStockDeck()
    {
        return m_stockpile;
    }

    /**
     * Round::getPlayerName
     * Accessor method to get player's name.
     * @return String Player's name.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final String getPlayerName()
    {
        return m_userName;
    }

    /**
     * Round::getStockPlayFlag
     * Accessor method to get stock play flag.
     * @return boolean Bool if it is turn to play from stock pile.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final boolean getStockPlayFlag()
    {
        return m_stockPlayFlag;
    }

    /**
     * Round::getHandPlayFlag
     * Accessor method to get hand play flag.
     * @return boolean Bool if it is turn to play from hand pile.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final boolean getHandPlayFlag()
    {
        return m_handPlayFlag;
    }

    /**
     * Round::getHelpString
     * Method to get suggestion from the algorithm to display on the GUI.
     * @return String The help string. First index has index of suggested card, rest has help logic.
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public String getHelpString()
    {
        return getCurrentPlayer().movePickHelp(this);
    }


    // *********************************************************
    // *********************** Mutators ************************
    // *********************************************************

    /**
     * Round::setPlayerTurn
     * Mutator to set player turn
     * @param a_turn short 1 for user, 2 for computer.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final void setPlayerTurn(short a_turn)
    {
        assert (a_turn==1 || a_turn ==2);
        m_playerTurn = a_turn;
    }

    /**
     * Round::setHandPlayFlag
     * Mutator method to set hand play flag.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public final void setHandPlayFlag(boolean a_flag)
    {
        m_handPlayFlag = a_flag;
    }

    // *********************************************************
    // ******************** MAIN METHOD ************************
    // *********************************************************

    // *********************************************************
    // ******************** CLASS METHODS **********************
    // *********************************************************
    /**
     * Round::determineFirstPlayer
     * Method to check who will go first according the rule of the game and for modulo suit.
     * @return short
     *  (1) User goes first.
     *  (2) Computer goes first.
     *  (3) A modulo suit. (Same cards in hands of both players)
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    public short determineFirstPlayer()
    {
        // Variable to record ranks and occurences of each hand
        TreeMap<Integer, Integer> t_userHandMap = new TreeMap<>();
        TreeMap<Integer, Integer> t_compHandMap = new TreeMap<>();

        // Recording hands of each player in temp variables
        Vector<Card> t_userHand = m_user.getHandPile();
        Vector<Card> t_compHand = m_comp.getHandPile();

        // Variable to hold the highest ranking card:
        // 12 == Aces, 0 == Kings
        int t_minVal = 12;

        // Temp value holding variables
        int t_userCardVal;
        int t_compCardVal;

        // Recording card ranks in a flipped manner using respective hashmaps.
        int t_count;
        for(int i = 0; i<t_userHand.size(); i++)
        {
            // Recording rank value:
            t_userCardVal = 12 - Card.CardRank.valueOf(t_userHand.get(i).getCardRank().name()).ordinal();
            t_compCardVal = 12 - Card.CardRank.valueOf(t_compHand.get(i).getCardRank().name()).ordinal();

            // Updating hash map
            t_count = t_userHandMap.containsKey(t_userCardVal)? t_userHandMap.get(t_userCardVal):0;
            t_userHandMap.put(t_userCardVal, t_count+1);
            t_count = t_compHandMap.containsKey(t_compCardVal)? t_compHandMap.get(t_compCardVal):0;
            t_compHandMap.put(t_compCardVal, t_count+1);

            // Updating the highest rank card:
            if(t_userCardVal < t_minVal) t_minVal = t_userCardVal;
            if(t_compCardVal < t_minVal) t_minVal = t_compCardVal;
        }

        // Checking for modulo suit and determining first player
        // Checking cards from highest rank to lowest rank:
        for(int i = t_minVal; i<13; i++)
        {
            // If both hands don't have the card:
            if(t_compHandMap.containsKey(i) == false
                    && t_userHandMap.containsKey(i) == false)
                continue;

                // Check if computer has higher rank card and user does not:
                // Computer goes first
            else if(t_compHandMap.containsKey(i) == true
                    && t_userHandMap.containsKey(i) == false)
                return 2;


                // Check if user has higher rank card and computer does not:
                // User goes first
            else if(t_userHandMap.containsKey(i) == true
                    && t_compHandMap.containsKey(i) == false)
                return 1;


                // If both have the higher rank card:
            else
            {
                // Check who has more of the higher rank card:
                // User
                if(t_userHandMap.get(i) > t_compHandMap.get(i))
                return 1;
                // Computer
            else if(t_compHandMap.get(i) > t_userHandMap.get(i))
                return 2;

                // Equal number of higher rank cards
            else
                continue;
            }
        }
        // If function reaches here, there is a modulo suit:
        // System.out.println("Modulo suit! Reshuffling cards and restarting the round...");
        return 3;
    }


    /**
     * Round::playFirstTurn
     * Method to process first play from hand of a player.
     * @param a_card int The card selected by the player to play.
     * @param a_help boolean If user has help switch turned on.
     * @return Vector<Integer> Vector of card matches when 2 matches are found, else null is returned. Computer always returns null.
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public Vector<Integer> playFirstTurn(int a_card, boolean a_help)
    {
        Vector<Integer> t_matches = getCurrentPlayer().playHandCard(a_card, this, a_help);
        // If it is the end of the hand turn
        if (t_matches == null)
        {
            // Flag updated for next move
            m_stockPlayFlag = true;
        }
        return t_matches;
    }

    /**
     * Round::processLayoutChoice
     * Method to process first play from hand of a player.
     * @param a_card int The card selected by the player to play.
     * @param a_help boolean If user has help switch turned on.
     * @param a_layoutCard int The card user wants to stack with on the layout.
     * @return void
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public void processLayoutChoice(int a_card, boolean a_help, int a_layoutCard)
    {
        m_user.processPickedLayoutCard(a_card, a_layoutCard, this, a_help);
        m_stockPlayFlag = true;
    }

    /**
     * Round::playSecondTurn
     * Method to process second play from stockpile of a player.
     * @param a_help boolean If user has help switch turned on.
     * @return String The drawn card from the stockpile and the logic of the move help.
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public String playSecondTurn(boolean a_help) { return(getCurrentPlayer().playStockPile( this, a_help)); }

    /**
     * Round::processStockLayoutPlay
     * Method to process the layout choice after playing stock pile.
     * @param a_help boolean If user has help switch turned on.
     * @param a_layoutCard int The card user wants to stack with on the layout.
     * @return void
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public void processStockLayoutPlay(boolean a_help, int a_layoutCard)
    {
        // Stacking card and finishing the stock play
        addToLayout(m_layout.lastElement().get(0), a_layoutCard);
        removeFromLayout(m_layout.size()-1);

        m_user.finishStockPlay(this);
    }


    /**
     * Round::drawStockCard
     * Method to draw a new card from the deck
     * @return Card A new card draw from the stockpile.
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public Card drawStockCard()
    {
        // Play appropriate player hand
        if(m_stockPlayFlag == false)
            return null;

        // To disallow multiple draws after a valid draw:
        m_stockPlayFlag = false;

        return m_stockpile.getNewCard();
    }

    /**
     * Round::getNumberOfStacksBefore
     * Method to get number of stacks before the index t_idx. Used by GUI to highlight cards on layout.
     * @param t_idx int Index of card to count stacks till
     * @return int The number of stacks
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public int getNumberOfStacksBefore(int t_idx)
    {
        int t_count = 0;
        for(int i=0; i<t_idx; i++)
        {
            // Skip one card to highlight
            if(m_layout.get(i).size()==2)
                t_count += 1;
            // Skip two cards to highlight
            if(m_layout.get(i).size()==3)
                t_count += 2;
        }

        return t_count;
    }


    /**
     * Round::changeTurn
     * Mutator to change player turn.
     * @author Salil Maharjan
     * @date 04/05/20.
     */
    public final void changeTurn()
    {
        if (m_playerTurn == 1)
            m_playerTurn = 2;
        else
            m_playerTurn = 1;
    }

    /**
     * Round::getHandStr
     * Method to get player's hand pile in string format. 1 for user.
     * @return Vector<String> Player's hand pile in string format.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Vector<String> getHandStr(short a_player)
    {
        // Get Player capture pile
        Vector<Card> t_hand = new Vector<>();
        if(a_player==1)
            t_hand = m_user.getHandPile();
        else
            t_hand = m_comp.getHandPile();

        // Convert to string
        Vector<String> t_str = new Vector<>();
        for(int i = 0; i<t_hand.size(); i++)
        {
            t_str.add("card" + getGUIchar(t_hand.get(i).getCardRank(), t_hand.get(i).getCardSuit()).toLowerCase());
        }
        return t_str;
    }

    /**
     * Round::getCaptureStr
     * Method to get player's capture pile in string format. 1 for user.
     * @return Vector<Vector<String>> Player's capture pile in string format.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Vector<Vector<String>> getCaptureStr(short a_player)
    {
        // Get Player capture pile
        Vector<Vector<Card>> t_capture = new Vector<Vector<Card>>();
        if(a_player==1)
            t_capture = m_user.getCapturePile();
        else
            t_capture = m_comp.getCapturePile();

        // Convert to string
        Vector<Vector<String>> t_str = new Vector<>();
        for(int i = 0; i<t_capture.size(); i++)
        {
            Vector<String> t_temp= new Vector<>();
            for(int j = 0; j< t_capture.get(i).size(); j++)
            {
                t_temp.add("card" + getGUIchar(t_capture.get(i).get(j).getCardRank(), t_capture.get(i).get(j).getCardSuit()).toLowerCase());
            }
            t_str.add(t_temp);
        }
//        System.out.println("getCaptureStr" + t_str.size());
        return t_str;
    }

    /**
     * Round::getLayoutStr
     * Method to get layout in string format.
     * @return Vector<Vector<String>> Layout in string format.
     * @author Salil Maharjan
     * @date 04/03/20.
     */
    public final Vector<Vector<String>> getLayoutStr()
    {
        // Convert to string
        Vector<Vector<String>> t_str = new Vector<>();
        for(int i = 0; i<m_layout.size(); i++)
        {
            Vector<String> t_temp= new Vector<>();
            for(int j = 0; j< m_layout.get(i).size(); j++)
            {
                t_temp.add("card" + getGUIchar(m_layout.get(i).get(j).getCardRank(), m_layout.get(i).get(j).getCardSuit()).toLowerCase());
            }
            t_str.add(t_temp);
        }
//        System.out.println("getLayoutStri" + t_str.size());
        return t_str;
    }

    /**
     * Round::getGUIchar
     * Method to translate card value to GUI character representation.
     * @param a_rank CardRank Rank of the card
     * @param a_suit CardSuit Suit of the card
     * @return String The GUI character representation of the card value
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public String getGUIchar(Card.CardRank a_rank, Card.CardSuit a_suit)
    {
        // Variable to hold the character representation
        String t_code = "";

        switch(a_rank)
        {
            case ACE:
                t_code += "1";
                break;
            case TWO:
                t_code += "2";
                break;
            case THREE:
                t_code += "3";
                break;
            case FOUR:
                t_code += "4";
                break;
            case FIVE:
                t_code += "5";
                break;
            case SIX:
                t_code += "6";
                break;
            case SEVEN:
                t_code += "7";
                break;
            case EIGHT:
                t_code += "8";
                break;
            case NINE:
                t_code += "9";
                break;
            case TEN:
                t_code += "X";
                break;
            case JACK:
                t_code += "J";
                break;
            case QUEEN:
                t_code += "Q";
                break;
            case KING:
                t_code += "K";
                break;
        }

        switch(a_suit)
        {
            case HEARTS:
                t_code += "H";
                break;
            case CLUBS:
                t_code += "C";
                break;
            case SPADES:
                t_code += "S";
                break;
            case DIAMONDS:
                t_code += "D";
                break;
        }
        return t_code;
    }

    /**
     * Round::getCardInformation
     * Method to translate card value to detailed card description. Used for printing.
     * @param a_rank CardRank Rank of the card
     * @param a_suit CardSuit Suit of the card
     * @return String The Card Information as a string.
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public String getCardInformation(Card.CardRank a_rank, Card.CardSuit a_suit)
    {
        // Variable to hold the string representation
        String t_code = "";

        switch(a_rank)
        {
            case ACE:
                t_code += "ACE of ";
                break;
            case TWO:
                t_code += "2 of ";
                break;
            case THREE:
                t_code += "3 of ";
                break;
            case FOUR:
                t_code += "4 of ";
                break;
            case FIVE:
                t_code += "5 of ";
                break;
            case SIX:
                t_code += "6 of ";
                break;
            case SEVEN:
                t_code += "7 of ";
                break;
            case EIGHT:
                t_code += "8 of ";
                break;
            case NINE:
                t_code += "9 of ";
                break;
            case TEN:
                t_code += "X of ";
                break;
            case JACK:
                t_code += "JACK of ";
                break;
            case QUEEN:
                t_code += "QUEEN of ";
                break;
            case KING:
                t_code += "KING of ";
                break;
        }

        switch(a_suit)
        {
            case HEARTS:
                t_code += "Hearts";
                break;
            case CLUBS:
                t_code += "Clubs";
                break;
            case SPADES:
                t_code += "Spades";
                break;
            case DIAMONDS:
                t_code += "Diamonds";
                break;
        }

        return t_code;
    }

    /**
     * Round::addToLayout
     * Method to add cards to the layout on given index. Used by players.
     * @param a_card Card Pointer to the card to be added
     * @param a_index int Index position of where to place the card on the layout
     * @return void
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public void addToLayout(Card a_card, int a_index)
    {
        // Temp card vector
        Vector<Card> t_card = new Vector<>();

        // If it is a new card in the layout
        if(a_index == m_layout.size())
        {
            t_card.add(a_card);
            m_layout.add(t_card);
        }
        else
        {
            assert(a_index >= 0 && a_index<m_layout.size());
            m_layout.get(a_index).add(a_card);
        }
    }

    /**
     * Round::removeFromLayout
     * Method to remove cards to the layout. Used by players.
     * @param a_index int Index position of where to place the card on the layout
     * @return void
     * @author Salil Maharjan
     * @date 04/02/20.
     */
    public void removeFromLayout(int a_index)
    {
//        // DEBUGGING
//        System.out.println("RemovefromLayout1: " + a_index);
//        for(int i = 0; i<m_layout.size(); i++)
//        {
//            for(int j = 0; j<m_layout.get(i).size(); j++)
//                System.out.print(getGUIchar(m_layout.get(i).get(j).getCardRank(), m_layout.get(i).get(j).getCardSuit()) + " ");
//        }

        // m_layout.erase(m_layout.begin()+a_index);
        m_layout.remove(m_layout.elementAt(a_index));

//        // DEBUGGING
//        System.out.println("RemovefromLayout2: ");
//        for(int i = 0; i<m_layout.size(); i++)
//        {
//            for(int j = 0; j<m_layout.get(i).size(); j++)
//                System.out.print(getGUIchar(m_layout.get(i).get(j).getCardRank(), m_layout.get(i).get(j).getCardSuit()) + " ");
//        }

    }



    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************
    /**
     * Round::dealCards
     * Method to deal cards in the game.
     * @param a_player Player A player of the game. Can be the computer or user.
     * @param a_cards int Number of cards to deal to a_player.
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    private void dealCards(Player a_player, int a_cards)
    {
        for(int i = 0; i<a_cards; i++)
            a_player.handPlayerCard(m_stockpile.getNewCard());
    }

    /**
     * Round::getCurrentPlayer
     * Method to get current player.
     * @return Player The current player
     * @author Salil Maharjan
     * @date 03/31/20.
     */
    private Player getCurrentPlayer()
    {
        if(m_playerTurn==1)
            return m_user;
        if(m_playerTurn==2)
            return m_comp;
        else
        {
//            System.out.println("Cannot determine player turn: getCurrentPlayer");
            System.exit(1);
            return null;
        }
    }

}



// *********************************************************
// ******************** TRASH METHODS **********************
// *********************************************************

//
//    /**
//     * Round::displayBoard
//     * Method to display the updated board
//     * @param a_turn Player turn. 1 for user, 2 for computer.
//     * @return void
//     * @author Salil Maharjan
//     * @date 04/02/20.
//     */
//    public void displayBoard(short a_turn)
//    {
//        // Get player turn and update view.
//        String t_turn;
//        if(a_turn == 1)
//            t_turn = "User";
//        else
//            t_turn = "Computer";
//
//        String t_gameInfo = "Round: " + m_game.getRoundNumber() + "\n"
//                + "Turn: " + t_turn;
//
////        m_view.updateGameInfo(t_gameInfo);
////        System.out.println(t_gameInfo);
////
////        // Update player headlines and score.
////        String t_userInfo = "| User Player |     Score: " + m_user.getPlayerScore();
////        String t_compInfo = "| Computer Player |     Score: " + m_comp.getPlayerScore();
////        m_view.updateUserInfo(t_userInfo);
////        m_view.updateCompInfo(t_compInfo);
////
////        // Update user hand
////        m_view.updateUserHand();
////        // Update user capture
////        m_view.updateUserCapture();
////
////        // Update computer hand
////        m_view.updateComputerHand();
////        // Update computer capture
////        m_view.updateCompCapture();
////
////        // Update layout
////        Vector<Vector<String>> t_layout = new Vector<>();
////        for(int i = 0; i<m_layout.size(); i++)
////        {
////            Vector<String> t_temp= new Vector<>();
////            for(int j = 0; j< m_layout.get(i).size(); j++)
////            {
////                t_temp.add("card" + getGUIchar(m_layout.get(i).get(j).getCardRank(), m_layout.get(i).get(j).getCardSuit()).toLowerCase());
////            }
////            t_layout.add(t_temp);
////        }
////        m_view.updateLayout(t_layout);
////
////        // Update stockpile
////        m_view.updateTopCard("card" + getGUIchar(m_stockpile.viewTopCard().getCardRank(), m_stockpile.viewTopCard().getCardSuit()).toLowerCase());
//
//
//    }