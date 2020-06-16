/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/22/2020                                        *
************************************************************
*/

#include "Card.hpp"

/* *********************************************************************
Function Name: Card
Purpose: Card class parameterized constructor.
Parameters:
 a_rank CardRank The rank of the card to construct.
 a_suit CardSuit The suit of the card to construct.
Return Value: NA
Local Variables: NA
Algorithm: Constructs a card object by initializing member variables.
Assistance Received: NA
********************************************************************* */
Card::Card(CardRank a_rank, CardSuit a_suit)
{
    m_suit = a_suit;
    m_rank = a_rank;
}

/* *********************************************************************
Function Name: ~Card
Purpose: Card class parameterized destructor.
Parameters:
Return Value: NA
Local Variables: NA
Algorithm: NA
Assistance Received: NA
********************************************************************* */
Card::~Card()
{
}


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Card.cpp
*  Implementation of Card.hpp
*
*  Created by Salil Maharjan on 1/22/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
