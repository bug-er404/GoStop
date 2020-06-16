/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#ifndef Computer_hpp
#define Computer_hpp

#include "Player.hpp"

class Computer : public Player
{
public:
    // Computer class constructor
    Computer();
    
    // Parameterized computer class constructor
    // used for initiating game from a config file.
    Computer(vector<Card*> a_hand, vector<vector<Card*>> a_capture, unsigned int a_score);
    
    // Computer class destructor
    ~Computer();
    
    // Overloaded method to play a turn for computer:
    bool play(Round* a_round);
    
protected:
private:
    
    // Method to process a computer move
    void makeMove(Round* a_round, bool a_help);
    
    // Method to put card on the layout and perform the required captures
    // Defined as H0, H1, H2 AND H2 in the game description
    void processMove(Round* a_round, Card* a_card, short a_matches);
    
    // Method to get move recommendation. Used as a strategy for computer player and move helper for user.
    int movePickHelp(Round* a_round);
    
    // Method to check layout for matching card faces:
    short checkLayout(Round* a_round, Card* a_card, bool a_print);
    
};

#endif /* Computer_hpp */


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Computer.hpp
*  Computer Class header file.
*  Class that implements computer player of the game.
*  Uses Player class.
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
