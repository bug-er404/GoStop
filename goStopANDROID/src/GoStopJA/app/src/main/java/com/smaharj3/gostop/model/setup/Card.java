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

/**
 ************************************************************
 * Card.java
 * Card class to create Card objects. Implements behavior of a card object.
 *
 * Member Variables:
 *       m_rank Total game score of computer player
 *       m_suit Total game score of user player
 *
 * Created by Salil Maharjan on 03/29/20.
 * Copyright © 2020 Salil Maharjan. All rights reserved.
 ************************************************************
*/
public class Card implements Serializable
{
    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /// CARD SUIT TYPES
    public enum CardSuit
    {
        HEARTS,
        CLUBS,
        SPADES,
        DIAMONDS
    };

    /// CARD RANKS ENUMERATED
    public enum CardRank
    {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    };

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /// Variable that holds the rank of the card
    private CardRank m_rank;

    /// Variable that holds the suit of the card
    private CardSuit m_suit;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ********************* Constructors **********************
    // *********************************************************

    /**
     * Card::Card.
     * Card class parameterized constructor.
     * Constructs a card object by initializing member variables.
     * @param a_rank CardRank The rank of the card to construct.
     * @param a_suit CardSuit The suit of the card to construct.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public Card(CardRank a_rank, CardSuit a_suit)
    {
        m_suit = a_suit;
        m_rank = a_rank;
    }

    // *********************************************************
    // ******************** Event Handlers *********************
    // *********************************************************

    // *********************************************************
    // *********************** Selectors ***********************
    // *********************************************************

    /**
     * Card::getCardRank.
     * Card rank accessor.
     * Accessor to get the card rank of a card object.
     * @return CardRank The rank of the card object.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public final CardRank getCardRank(){ return m_rank; }

    /**
     * Card::getCardSuit.
     * Card suit accessor.
     * Accessor to get the card suit of a card object.
     * @return CardRank The suit of the card object.
     * @author Salil Maharjan
     * @date 03/29/20.
     */
    public final CardSuit getCardSuit(){ return m_suit; }

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
}
