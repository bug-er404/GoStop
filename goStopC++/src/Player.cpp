/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#include "Player.hpp"
#include <unistd.h>

/* *********************************************************************
Function Name: Player
Purpose: Player class constructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Player object by initializing member variables.
Assistance Received: NA
********************************************************************* */
Player::Player()
{
    m_score = 0;
}

/* *********************************************************************
Function Name: Player
Purpose: Parameterized player class constructor used for initiating game from a config file.
Parameters:
 a_hand vector<Card*> cards in hand of the player
 a_capture vector<vector<Card*>> Cards in the capture pile of the player
 a_score unsigned int Score from config file
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Player object by initializing member variables.
Assistance Received: NA
********************************************************************* */
Player::Player(vector<Card*> a_hand, vector<vector<Card*>> a_capture, unsigned int a_score)
{
    m_hand = a_hand;
    m_capture = a_capture;
    m_score = a_score;
}

/* *********************************************************************
Function Name: captureStack
Purpose:  Method to capture stacks (triple stacks) from the layout
Parameters:
 a_round Round* Current round of the game being played.
 a_index int Index of the card on the layout to capture
Return Value: NA
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
 t_capture vector<Card*> Variable to hold captured cards which is later used to push to the capture pile of vector<vector<Card*>>
Algorithm:
 Captures stack from a_index. Checks player's capture pile to see it can put the stacked pair to a previous stack pair,
 that makes a pair of 4 cards which will give the player a point. If no such instance is found, it simply adds the captured
 stack to the capture pile.
Assistance Received: NA
********************************************************************* */
void Player::captureStack(Round* a_round, int a_index)
{
    // Get layout of the round:
    vector<vector<Card*>> t_layout = a_round->getLayout();
    // Variable to hold captured cards
    vector<Card*> t_capture;
    
    // Getting all layout cards:
    for(int i = 0; i<t_layout[a_index].size(); i++)
        t_capture.push_back(t_layout[a_index][i]);
        
    // Check for existing stacked pairs that don't have 4 cards
    for(int i = 0; i<m_capture.size(); i++)
    {
        if(m_capture[i][0]->getCardRank() == t_capture[0]->getCardRank() && ((m_capture[i].size()+t_capture.size()) <= 4))
        {
            for(int j = 0; j<t_capture.size();j++)
                m_capture[i].push_back(t_capture[j]);
            
            // Updating score
            m_score++;
            
            return;
        }
    }
    
    // No existing pair, so we add a new capture pile
    m_capture.push_back(t_capture);
    m_score++;
    
}

/* *********************************************************************
Function Name: playStockPile
Purpose: Method to play stock pile after completing hand pile move and captured remaining stacked pairs.
 Calls getStackedPairs function to capture stacked pairs from the turn.
Parameters:
 a_round Round* Current round of the game being played.
 a_matches short The number of matches of card a_card in the layout from the previous move played from the hand card.
 This determines how the stock card is played.
 a_help bool Flag for help mode for user and move details for computer player.
Return Value: NA
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
Algorithm:
 Draws from stock pile and checks matches. Depending on the case, H0/H1/H2/H2
 it processes the move by calling function processMove and then captures stacks from the layout
 by using getStackedPairs method.
Assistance Received: NA
********************************************************************* */
void Player::playStockPile(Round* a_round, short a_matches, bool a_help)
{
    cout<<"Drawing top card from stock pile................"<<endl;
    sleep(1);
    // Variable to hold number of matches on the layout with drawn card:
    short t_matches;
    
    // Drawing card from the stock pile
    Card* t_stockCard = a_round->drawStockCard();
    cout<<"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"<<endl;
    cout<<"You drew: "<<a_round->getCardInformation(t_stockCard->getCardRank(), t_stockCard->getCardSuit())<<" from the stock pile!"<<endl;
    cout<<"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"<<endl;

    // Previous hand case was H0 or H3:
    if(a_matches == 0 || a_matches >=3)
    {
        // Checking layout for # of matches on the layout
        // and performing the move:
        t_matches = checkLayout(a_round, t_stockCard, a_help);
        processMove(a_round, t_stockCard, t_matches);
        
        // Capture stacked pairs on the layout:
        getStackedPairs(a_round);
    }
    // Previous hand case was H1 or H2
    else
    {
        // Flag for checking if drawn card matches a stacked pair in the layout,
        // This results in no capture and a triple stack on the layout
        bool a_stackMatch = false;
        
        // Get layout of the round:
        vector<vector<Card*>> t_layout = a_round->getLayout();
        
        // Variable to hold the index of the stacked pair
        // that matches with the drawn stock card
        // Only initialized if a_stackMatch is true.
        int t_cardIdx = -1;
        
        // Check to see if drawn card matches stacked pair:
        for(int i = 0; i<t_layout.size(); i++)
        {
            if(t_layout[i][0]->getCardRank() == t_stockCard->getCardRank()
               && t_layout[i].size() == 2)
            {
                a_stackMatch = true;
                t_cardIdx = i;
            }
        }
   
        // Checking layout for # of matches on the layout:
        t_matches = checkLayout(a_round, t_stockCard, a_help);
        // Card does not match a stacked pair
        if(a_stackMatch == false)
        {
            // Process move normally
            processMove(a_round, t_stockCard, t_matches);
            // Get stacked pair from previous move
            getStackedPairs(a_round);
        }
        // Card matches stacked pair:
        else
        {
            // If drawn card only matches stacked pair
            // and index of stacked pair is initialized:
            if(t_matches == 0 && t_cardIdx != -1)
            {
                // A triple stack is created and no capture is made
                a_round->addToLayout(t_stockCard, t_cardIdx);
                return;
            }
            else
            {
                // There are other options to place the card:
                // Process move normally
                // Function processMove
                processMove(a_round, t_stockCard, t_matches);
                // Get stacked pair from previous move
                getStackedPairs(a_round);
            }
        }
    }
    
}

/* *********************************************************************
Function Name: getStackedPairs
Purpose:  Method to find and capture stacked pairs at the end of each player's turn
Parameters:
 a_round Round* Current round of the game being played.
Return Value: NA
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
 t_capture vector<Card*> Vector to hold the captured cards
Algorithm:
 Captures stacked pairs from the layout at the end of players turn.
 Iterates the layout to check for pairs and adds to capture pile checking for any face matches
 that might form a complete set of 4. Updates score when a pair of 4 is formed.
Assistance Received: NA
********************************************************************* */
void Player::getStackedPairs(Round* a_round)
{
    // Get layout of the round:
    vector<vector<Card*>> t_layout = a_round->getLayout();
    // Boolean to hold if the stack has been added
    bool t_added = false;
    // Variable to hold number of cards removed.
    // Used to update index because:
    // If i elements are removed, the index is less by i
    // on the class member variable
    int t_actions = 0;
    
    // Checking layout for stacked cards:
    for(int i = 0; i<t_layout.size(); i++)
    {
        // Stacked pair:
        if(t_layout[i].size() == 2)
        {
            // Check for existing stacked pairs that don't have 4 cards in capture pile
            for(int j = 0; j<m_capture.size(); j++)
            {
                // Matching face:
                if(m_capture[j][0]->getCardRank() == t_layout[i][0]->getCardRank())
                {
                    // Checking if stacked pairs together make 4.
                    if((m_capture[j].size()+t_layout[i].size()) <= 4)
                    {
                        // Pushing stacked pair to existing pair, making it a complete stack
                        for(int k = 0; k<t_layout[i].size();k++)
                            m_capture[j].push_back(t_layout[i][k]);
                        // Setting flag
                        t_added = true;
                        // Updating score
                        m_score++;
                    }
                }
            }
            // Checking flag and adding stack to capture if it has not been added
            if(t_added==false)
                m_capture.push_back(t_layout[i]);
            
            // Removing from layout and updating action counter:
            // This works because we are iterating through the layout
            // in increasing order of indices
            a_round->removeFromLayout(i-t_actions);
            t_actions++;
        }
        
        // Resetting flag
        t_added=false;
    }
}

/* *********************************************************************
Function Name: sortCapturePile
Purpose: Method to sort capture pile
Parameters:
 a_round Round* Current round of the game being played.
Return Value: NA
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
 t_capture vector<Card*> Vector to hold the captured cards
Algorithm:
 Uses a map to sort the capture pile. Renames face 10 as A and King as Z,
 so that the sorting is -> X, J, Q, K.
 Records faces in a map, which holds the face value in a sorted manner.
 Using the map, the capture pile is remade according to the sorted order.
Assistance Received: NA
********************************************************************* */
void Player::sortCapturePile(Round* a_round)
{
    // Map to hold the pile faces
    map<char,int> t_pileMap;
    map<char,int>::iterator it;

    // Temp capture pile variable to hold the sorted pile
    vector<vector<Card*>> t_sortedPile;
    // Temp vector to hold stacked piles
    vector<Card*> t_temp;
    
    // Recording pile faces, map sorts the face value
    for(int i = 0; i<m_capture.size(); i++)
    {
        // Renaming for 10 and King face so that the map has them in the card face order:
        if(a_round->getGUIchar(m_capture[i][0]->getCardRank(), m_capture[i][0]->getCardSuit())[0] == 'X')
        {
            t_pileMap['A']++;
        }
        else if(a_round->getGUIchar(m_capture[i][0]->getCardRank(), m_capture[i][0]->getCardSuit())[0] == 'K')
        {
            t_pileMap['Z']++;
        }
        else
            t_pileMap[a_round->getGUIchar(m_capture[i][0]->getCardRank(), m_capture[i][0]->getCardSuit())[0]]++;
    }
    
    // Variable to hold the face to compare
    char t_comp;
    // Iterating through the sorted faces
    for(it = t_pileMap.begin(); it!=t_pileMap.end(); it++)
    {
        t_comp = it->first;
        // Reverting face names
        if(t_comp=='A')
            t_comp = 'X';
        if(t_comp=='Z')
            t_comp = 'K';
            
        // Finding matching face from the capture pile
        for(int i = 0; i<m_capture.size(); i++)
        {
            // If it matches the face, we push the stack from the pile
            if(a_round->getGUIchar(m_capture[i][0]->getCardRank(), m_capture[i][0]->getCardSuit())[0] == t_comp)
            {
                for(int j = 0; j<m_capture[i].size();j++)
                    t_temp.push_back(m_capture[i][j]);
                t_sortedPile.push_back(t_temp);
                t_temp.clear();
            }
        }
    }
    
    // Reassigning the new sorted pile
    m_capture.clear();
    m_capture = t_sortedPile;
    
}

/* *********************************************************************
Function Name: sortHandPile
Purpose: Method to sort player hand pile.
Parameters:
 a_round Round* Current round of the game being played.
Return Value: NA
Local Variables:
 t_hand vector<Card*> Vector to hold the cards in hand
Algorithm:
 Uses a map to sort the hand pile. Renames face 10 as A and King as Z,
 so that the sorting is -> X, J, Q, K.
 Records faces in a map, which holds the face value in a sorted manner.
 Using the map, the capture pile is remade according to the sorted order.
Assistance Received: NA
********************************************************************* */
void Player::sortHandPile(Round* a_round)
{
    // Map to hold the pile faces
    map<char,int> t_pileMap;
    map<char,int>::iterator it;

    // Temp vector to hold hand pile
    vector<Card*> t_temp;
    
    // Recording pile faces, map sorts the face value
    for(int i = 0; i<m_hand.size(); i++)
    {
        // Renaming for 10 and King face so that the map has them in the card face order:
        if(a_round->getGUIchar(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())[0] == 'X')
        {
            t_pileMap['A']++;
        }
        else if(a_round->getGUIchar(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())[0] == 'K')
        {
            t_pileMap['Z']++;
        }
        else
            t_pileMap[a_round->getGUIchar(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())[0]]++;
    }
    
    // Variable to hold the face to compare
    char t_comp;
    // Iterating through the sorted faces
    for(it = t_pileMap.begin(); it!=t_pileMap.end(); it++)
    {
        t_comp = it->first;
        // Reverting face names
        if(t_comp=='A')
            t_comp = 'X';
        if(t_comp=='Z')
            t_comp = 'K';
            
        // Finding matching face from the capture pile
        for(int i = 0; i<m_hand.size(); i++)
        {
            // If it matches the face, we push the card from the pile
            if(a_round->getGUIchar(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())[0] == t_comp)
            {
                t_temp.push_back(m_hand[i]);
            }
        }
    }
    
    // Reassigning the new sorted pile
    m_hand.clear();
    m_hand = t_temp;
    
}


/* *********************************************************************
Function Name: arrangePile
Purpose: Method to sort capture pile and make appropriate stacks when loading from config file.
Parameters:
 a_round The round the players are playing currently.
Return Value: NA
Local Variables: NA
Algorithm: Check to return the card rank.
Assistance Received: NA
********************************************************************* */
void Player::arrangePile(Round* a_round)
{
    // Temp capture pile variable to hold the new pile
    vector<vector<Card*>> t_sortedPile;
    // Temp vector to hold stacked piles
    vector<Card*> t_temp;

    // Iterating through capture file to find stack matches
    for(int i=0; i<m_capture.size();i++)
    {
        if(m_capture[i].empty())
            continue;
        
        for(int j=i; j<m_capture.size();j++)
        {
            if(m_capture[j].empty())
                continue;
            
            // Pushing first card
            if(j==i)
            {
                t_temp.push_back(m_capture[i][0]);
                m_capture[i].clear();
                continue;
            }
            
            // When a set of 4 is created it is pushed to the main temp stockpile
            // and the buffer is cleared for reuse
            if(t_temp.size()==4)
            {
                t_sortedPile.push_back(t_temp);
                m_score++;
                t_temp.clear();
            }
            
            // Pushing cards with same faces to make sets
            if((a_round->getGUIchar(t_temp[0]->getCardRank(), t_temp[0]->getCardSuit()))[0]
               == (a_round->getGUIchar(m_capture[j][0]->getCardRank(), m_capture[j][0]->getCardSuit()))[0])
            {
                // Check if a set of <4 can be created
                if(m_capture[j].size()+t_temp.size()<=4)
                {
                    t_temp.push_back(m_capture[j][0]);
                    m_capture[j].clear();
                }
                // Else we need push and clear the card buffer
                else
                {
                    t_sortedPile.push_back(t_temp);
                    t_temp.clear();
                    t_temp.push_back(m_capture[j][0]);
                    m_capture[j].clear();
                }
            }
        }
        // Pushing found stacks
        t_sortedPile.push_back(t_temp);
        t_temp.clear();
    }
      
    // Reassigning the new sorted pile
    m_capture.clear();
    m_capture = t_sortedPile;
}


/* *********************************************************************
Function Name: ~Player
Purpose: Player class destructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Player class destructor. Releases used memory. Non virtual.
Assistance Received: NA
********************************************************************* */
Player::~Player()
{
}

/* *********************************************************************
**************************** TRASH METHODS *****************************
********************************************************************* */
//
///* *********************************************************************
//Function Name: playHand
//Purpose: Method to pick card from player's hand. Used by Computer player to pick the best card
// and by User to get help picking a card.
//Parameters:
// a_round Round* Current round of the game being played.
//Return Value:
// Card* Best card to play from the hand.
//Local Variables:
// t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
//Algorithm:
//
//Assistance Received: NA
//********************************************************************* */
//Card* Player::playHand(Round* a_round)
//{
//    vector<vector<Card*>> t_layout = a_round->getLayout();
//
//    // Variable to record ranks and occurences of each card
//    map<int, int> t_layoutMap;
//    map<int, int> t_handMap;
//    map<int, int> t_captureMap;
//    map<int, int>::iterator it;
//
//    // Recording ranks and occurences of cards on the layout
//    for(int i = 0; i<t_layout.size(); i++)
//    {
//        for(int j = 0; j<t_layout[i].size();j++)
//            t_layoutMap[t_layout[i][j]->getCardRank()]++;
//    }
//
//    for(it=t_layoutMap.begin(); it!=t_layoutMap.end(); it++)
//        cout<<it->first<<":  "<<it->second<<endl;
//
//    return NULL;
//
//}


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Player.cpp
*  Implementation of Player.hpp
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
