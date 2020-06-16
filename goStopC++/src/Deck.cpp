/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/22/2020                                        *
************************************************************
*/

#include "Deck.hpp"
#include "Card.hpp"

/* *********************************************************************
Function Name: Deck
Purpose: Deck class constructor.
Parameters:
 a_deckNum short Number of decks to construct
Return Value: NA
Local Variables: NA
Algorithm: Constructs a sorted deck of all ranks and faces.
Assistance Received: NA
********************************************************************* */
Deck::Deck(int a_deckNum)
{
    // Initialize the top card as the first index of the array.
    m_topCard = 0;
    
    // Initializing the deck size.
    m_deckSize = a_deckNum*DECKSIZE;
    
    // Constructing a_deckNum number of sorted decks
    for(int j=0;j<a_deckNum;j++)
    {
        for(int i=0; i<DECKSIZE; i++)
            m_deck.push_back(new Card((CardRank)(i%13),(CardSuit)(i%4)));
          
    }
    
}

/* *********************************************************************
Function Name: Deck
Purpose: Parameterized deck class constructor to load deck from config file
Parameters:
 a_deck vector<Card*> The deck processed to load from config file.
Return Value: NA
Local Variables: NA
Algorithm: Constructs a deck object.
Assistance Received: NA
********************************************************************* */
Deck::Deck(vector<Card*> a_deck)
{
    // Initialize the top card as the first index of the array.
    m_topCard = 0;
    
    // Initializing the deck size.
    m_deckSize = (int)a_deck.size();
    
    // Initializing the deck
    m_deck = a_deck;
    
}

/* *********************************************************************
Function Name: ~Deck
Purpose: Deck class destructor.
Parameters:
Return Value: NA
Local Variables: NA
Algorithm: NA
Assistance Received: NA
********************************************************************* */
Deck::~Deck()
{
    for(int i = 0; i<m_deck.size(); i++)
    {
        delete []m_deck[i];
    }
}



/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Deck.cpp
*  Implementation of Deck.hpp
*
*
*  Created by Salil Maharjan on 1/22/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
