/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#ifndef Round_hpp
#define Round_hpp

#include "PrefixHeader.pch"
#include "Card.hpp"
#include "Deck.hpp"

class Player;
class Game;

class Round
{
public:
    // Round class constructor
    Round(Game* a_game);
    
    // Parameterized round class constructor to initiate round from config file
    Round(Game* a_game, vector<Card*> a_deck, vector<vector<Card*>> a_layout);
    
    // Round class destructor
    ~Round();
    
    // Accessors:
    const vector<vector<Card*>> getLayout(){return m_layout;}
    
    // Method to determine the first player and to check for modulo suit (Same cards in hands of both players)
    // 1 for User, 2 for Computer, 3 for Modulo suit.
    short determineFirstPlayer();
    
    // Method to display the board
    void displayBoard();
    
    // Method to start the round of the game:
    void startRound(short a_turn);
    
    // Method to translate card value to GUI character representation
    string getGUIchar(CardRank a_rank, CardSuit a_suit);
    // Method to translate card value to detailed card description.
    string getCardInformation(CardRank a_rank, CardSuit a_suit);
    
    // Method to add cards to the layout. Used by players.
    void addToLayout(Card* a_card, int a_index);
    
    // Method to remove cards to the layout. Used by players.
    void removeFromLayout(int a_index);
    
    // Method to draw a new card from the deck
    Card* drawStockCard(){return m_stockpile->getNewCard();}
    
    // Method to save game:
    void saveGame(bool a_player);
    
protected:
private:
    // Pointer to the main game:
    Game* m_game;
    
    // Variable to hold the game round's layout pile.
    vector<vector<Card*>> m_layout;
    
    // Variable to hold the game round's stock pile that is "faced down"
    Deck* m_stockpile;
    
    // Method to initially deal cards to a player from a deck 
    void dealCards(Player* a_player, int a_cards);
    
    
    
};

#endif /* Round_hpp */


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Round.hpp
*  Round Class header file.
*  Class to process a round of go stop game.
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
