/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/


#ifndef Player_hpp
#define Player_hpp
#include "PrefixHeader.pch"
#include "Card.hpp"
#include "Round.hpp"

class Player
{
public:
    // Player class constructor
    Player();
    
    // Parameterized player class constructor
    // used for initiating game from a config file.
    Player(vector<Card*> a_hand, vector<vector<Card*>> a_capture, unsigned int a_score);
   
    // Player class virtual destructor
    virtual ~Player();
    
    // Accessors:
    const vector<Card*> getHandPile(){return m_hand;}
    const vector<vector<Card*>> getCapturePile(){return m_capture;}
    const unsigned int getPlayerScore(){return this->m_score;}
    
    // Debug method: to print player cards:
    void printCards()
    {
        for(int i =0; i<m_hand.size();i++)
            cout<<"Rank: "<<m_hand[i]->getCardRank()<<" SUIT: "<<m_hand[i]->getCardSuit()<<endl;
    }
    
    // Method to deal a card to the player
    void handPlayerCard(Card* a_card){m_hand.push_back(a_card);}
    
    // Virtual method to play a turn:
    virtual bool play(Round* a_round) = 0;

    // Method to clear capture and hand pile and reset player score:
    void clearPile(){m_hand.clear(); m_capture.clear(); this->m_score = 0;}
    
    // Method to sort capture pile
    void sortCapturePile(Round* a_round);
    
    // Method to sort hand pile
    void sortHandPile(Round* a_round);
    
    // Method to sort pile and make appropriate stacks after loading it from config file
    void arrangePile(Round* a_round);
    
protected:
    // Player hand pile variable
    vector<Card*> m_hand;
    // Player capture pile variable
    vector<vector<Card*>> m_capture;
    // Player score variable:
    unsigned int m_score;
    
    // Method to make a player move
    virtual void makeMove(Round* a_round, bool a_help) = 0;
    
    // Method to check layout for matching card faces:
    virtual short checkLayout(Round* a_round, Card* a_card, bool a_print) = 0;
    
    // Virtual method to put card on the layout and process the selected move:
    virtual void processMove(Round* a_round, Card* a_card, short a_matches) = 0;
    
    // Method to capture stacks (stack pair and triple stacks) from the layout
    void captureStack(Round* a_round, int a_index);
    
    // Method to play stock pile:
    void playStockPile(Round* a_round, short a_matches, bool a_help);
    
    // Method to find and capture stacked pairs at the end of each player's turn
    void getStackedPairs(Round* a_round);
    
    // Method to get move recommendation. Used as a strategy for computer player and move helper for user.
    virtual int movePickHelp(Round* a_round) = 0;
    
private:
    
};

#endif /* Player_hpp */


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Player.hpp
*  Player Class header file.
*  Class that implements a Players for the game.
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
