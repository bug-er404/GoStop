/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/22/2020                                        *
************************************************************
*/


#pragma once
#ifndef Card_hpp
#define Card_hpp
#include "PrefixHeader.pch"

// CARD SUIT TYPES
enum CardSuit
{
    HEARTS,
    CLUBS,
    SPADES,
    DIAMONDS
};

// CARD RANKS ENUMERATED
enum CardRank
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

class Card
{
public:    
    // Card class parameterized constructor
    Card(CardRank a_rank, CardSuit a_suit);
    
    // Card class destructor
    ~Card();
    
    // Accessors:
    const CardRank getCardRank(){return m_rank;};
    const CardSuit getCardSuit(){return m_suit;};
    
protected:
private:
    // Variable that holds the rank of the card
    CardRank m_rank;
    // Variable that holds the suit of the card
    CardSuit m_suit;
    
};

#endif /* Card_hpp */


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Card.hpp
*  Card Class header file.
*  Class to create card objects.
*
*  Created by Salil Maharjan on 1/22/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
