/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#ifndef Game_hpp
#define Game_hpp
#include "PrefixHeader.pch"
#include "Player.hpp"

// Forward declaration to avoid circular dependency
class Round;
class User;
class Computer;
class Player;

class Game
{
public:
    // Game class constructor
    Game();
    
    // Game class destructor
    ~Game(){delete[]m_user; delete[]m_computer;};
    
    // Member variable accessors:
    const unsigned int getRoundNumber(){return m_rounds;}
    const unsigned int getUserScore(){return m_userScore;}
    const unsigned int getCompScore(){return m_compScore;}
    
    // Method to start the game:
    void startGoStop();
    
    // Method to end the game:
    void endGoStop();
    
    // Method to display game statistics:
    void displayGameStats();
    
protected:
    
private:
    // Variable to hold the User player
    Player* m_user;
    
    // Variable to hold the Computer player
    Player* m_computer;
    
    // Variable to hold the number of rounds
    unsigned int m_rounds;
    
    // Variable to hold user's total score
    unsigned int m_userScore;
    
    // Variable to hold computer's total score
    unsigned int m_compScore;
    
    // Method to load game from a text file
    void loadFromFile();
    
    // Method to get card rank from its ASCII representation
    CardRank asciiToRank(string a_string);
    
    // Method to get card suit from its ASCII representation
    CardSuit asciiToSuit(string a_string);

    
    // Round is highly coupled with Game class
    friend class Round;
};

#endif /* Game_hpp */


/*
 ***************
 DOXYGEN HEADER
 ***************
 */

/**
*  Game.hpp
*  Game Class header file.
*  Class to implement a game of go stop.
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
