/*
 ************************************************************
 * Name:  Salil Maharjan                                    *
 * Project:  3 goStop JAVA                                  *
 * Class: CMPS366 Organization of Programming Languages     *
 * Date:  03/29/2020                                        *
 ************************************************************
 */

package com.smaharj3.gostop.model.setup;

import java.io.Serializable;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import com.smaharj3.gostop.model.setup.Card;

/**
 ************************************************************
 * Deck.java
 * Deck class to create a deck of cards. Implements behavior of a deck object. Uses Card class to create cards.
 *
 * Member Variables:
 *       m_deck Vector of cards to record the deck of cards.
 *       m_topCard Index of the top card in the deck.
 *       m_deckSize Total deck size. Dependent on how many decks are used in the game.
 *
 * Created by Salil Maharjan on 03/29/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
*/
public class Deck implements Serializable
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /// Constant variable to assign the deck size.
    private final static short DECKSIZE = 52;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Variable to hold the deck of cards.
    private Vector<Card> m_deck = new Vector<>();

    /// Variable that holds the index of the top card.
    private int m_topCard;

    /// Variable that holds the deck size
    private int m_deckSize;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * Deck::Deck.
     * Default deck class constructor.
     * @param a_deckNum int Number of decks to create.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public Deck(int a_deckNum)
    {
        // Initialize the top card as the first index of the array.
        m_topCard = 0;

        // Initializing the deck size.
        m_deckSize = a_deckNum*DECKSIZE;

        // Constructing a_deckNum number of sorted decks
        for(int j=0;j<a_deckNum;j++)
        {
            for(int i=0; i<DECKSIZE; i++)
            {
                m_deck.add(new Card (Card.CardRank.values()[i % 13], Card.CardSuit.values()[i % 4]));
            }
        }
    }

    /**
     * Deck::Deck.
     * Parameterized deck class constructor to load deck from config file.
     * @param a_deck Vector<Card> The deck loaded from config file.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public Deck(Vector<Card> a_deck)
    {
        // Initialize the top card as the first index of the array.
        m_topCard = 0;

        // Initializing the deck size.
        m_deckSize = (int)a_deck.size();

        // Initializing the deck
        m_deck = a_deck;
    }

    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * Deck::getDeck. Method to get deck.
     * Accessor method to get the deck.
     * @return Vector<Card> The deck object.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public final Vector<Card> getDeck(){ return m_deck; }

    /**
     * Deck::topCardIndex.
     * Accessor method to get index of top card.
     * @return int The index of the top card.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public final int topCardIndex(){ return m_topCard; }

    /**
     * Deck::viewTopCard.
     * Accessor method to view top card
     * @return int The index of the top card.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public final Card viewTopCard(){ return m_deck.get(m_topCard); }

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
     * Deck::getNewCard. Method to get new card from the deck.
     * Method to draw next card from the deck of cards. (Once the card is drawn, it cannot be put back into the deck.)
     * @return Card The top card of the deck.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public Card getNewCard()
    {
        assert(this.m_topCard<this.m_deckSize);
        m_topCard++;
        return m_deck.get(m_topCard-1);
    }

    /**
     * Deck::shuffleDeck.
     * Method to shuffle the deck.
     * @return void
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public void shuffleDeck() { Collections.shuffle(m_deck, new Random(System.currentTimeMillis())); }

    // *********************************************************
    // ****************** UTILITY METHODS **********************
    // *********************************************************
}
