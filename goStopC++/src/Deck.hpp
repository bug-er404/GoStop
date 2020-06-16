/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/22/2020                                        *
************************************************************
*/

#pragma once
#ifndef Deck_hpp
#define Deck_hpp
#include "PrefixHeader.pch"
#include "Card.hpp"

class Deck
{
public:
    // Default deck class constructor
    Deck(int a_deckNum);
    
    // Parameterized deck class constructor to load deck from config file
    Deck(vector<Card*> a_deck);
    
    // Deck class destructor
    ~Deck();
    
    // Accessors:
    const vector<Card*> getDeck(){return m_deck;}
    const int topCardIndex(){return m_topCard;}
    
    // Accessors:
    // Method to view top card
    Card* viewTopCard(){return m_deck[m_topCard];}
    
    // Method to draw next card from the deck of cards.
    // Once the card is drawn, it cannot be put back into the deck.
    Card* getNewCard(){assert(this->m_topCard<this->m_deckSize); m_topCard++; return m_deck[m_topCard-1];}
    

    // Method to shuffle deck. Shuffles the vector of cards using vector random_shuffle function.
    void shuffleDeck(){srand((unsigned)time(0)); std::shuffle(std::begin(m_deck), std::end(m_deck),std::default_random_engine(rand()));}
    
protected:
private:
    // Constant variable to assign the deck size.
    const static short DECKSIZE = 52;
    
    // Variable to hold the deck of cards.
    vector<Card*> m_deck;
    
    // Variable that holds the index of the top card.
    int m_topCard;
    
    // Variable that holds the deck size
    int m_deckSize;
};

#endif /* Deck_hpp */


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Deck.hpp
*  Deck Class header file.
*  Class to create deck objects and implement deck behaviors.
*
*  Created by Salil Maharjan on 1/22/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
