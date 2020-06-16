/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#include "Computer.hpp"
#include "Round.hpp"

/* *********************************************************************
Function Name: Computer
Purpose: Computer class constructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Computer object by initializing member variables. Calls player class for construction.
Assistance Received: NA
********************************************************************* */
Computer::Computer() : Player()
{
    
}

/* *********************************************************************
Function Name: Computer
Purpose: Parameterized Computer class constructor used for initiating game from a config file.
Parameters:
 a_hand vector<Card*> cards in hand of the Computer
 a_capture vector<vector<Card*>> Cards in the capture pile of the Computer
 a_score unsigned int Score from config file
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Computer object by initializing member variables. Calls player class for construction.
Assistance Received: NA
********************************************************************* */
Computer::Computer(vector<Card*> a_hand, vector<vector<Card*>> a_capture, unsigned int a_score) : Player((vector<Card*>) a_hand, (vector<vector<Card*>>) a_capture, (unsigned int) a_score)
{

}


/* *********************************************************************
Function Name: play
Purpose: Overloaded method to play a turn for computer player.
Parameters:
 a_round Round* Pointer to the current round.
Return Value:
  bool 0 if the player wants to stop playing.
Local Variables: NA
Algorithm:
 Gets menu choice for computer and performs the menu action.
Assistance Received: NA
********************************************************************* */
bool Computer::play(Round* a_round)
{
    // Variable to hold menu choice of user
    short t_menuChoice;
    
    cout<<"Computer Turn"<<endl;
    
    // User menu option:
    cout<<"Menu:"<<endl;
    cout<<"1. Save the game"<<endl;
    cout<<"2. Make a move"<<endl;
    cout<<"3. Quit the game "<<endl;
    
    // Input validation and integer check:
    string t_temp;
    do
    {
        cout<<":";
        cin>>t_temp;
        try {
            t_menuChoice = stoi(t_temp);
        } catch (exception e) {
            cout<<"Invalid input"<<endl;
            t_menuChoice = -1;
        }
    } while (t_menuChoice<1 || t_menuChoice>3);
    
    // Performing menu option
    switch(t_menuChoice)
    {
        case 1:
            a_round->saveGame(0);
            break;
        case 2:
            makeMove(a_round, true);
            break;
        case 3:
            return 0;
            break;
        default:
            cerr<<"User menu input invalid. Should be between 1-4"<<endl;
            exit(1);
    }
    
    return 1;
}

/* *********************************************************************
Function Name: makeMove
Purpose: Method to process a computer move.
Parameters:
 a_round Round* Pointer to the current round.
 a_help Flag to print computer move details. Default as true.
Return Value: NA
Local Variables: NA
Algorithm:
 Uses function movePickHelp that gives the best card pick for the current state.
 Uses the card value to check the layout for matchees and proceesses the move, making captures if possible
 and updating the player's hand. Then, plays from stock pile.
 Makes sure the capture pile is sorted by calling sortCapturePile function.
Assistance Received: NA
********************************************************************* */
void Computer::makeMove(Round* a_round, bool a_help=true)
{
    // Variable to hold card index choice:
    int t_cardIdx;
    
    // Variable to hold number of matches:
    int t_matches;
    
    // Get move recommendation and process move:
    t_cardIdx = movePickHelp(a_round);
    t_matches = checkLayout(a_round, m_hand[t_cardIdx], true);
        
    processMove(a_round, m_hand[t_cardIdx], t_matches);
    
    // Removing card from hand
    m_hand.erase(m_hand.begin()+(t_cardIdx));
    
    // Sorting piles and displaying board
    sortHandPile(a_round);
    sortCapturePile(a_round);
    a_round->displayBoard();
    
    // ************************
    // PLAYING FROM STOCK PILE:
    // ************************
    playStockPile(a_round, t_matches, true);
    // Sorting after play
    sortCapturePile(a_round);
    sortHandPile(a_round);

}

/* *********************************************************************
Function Name: checkLayout
Purpose: Method to check layout for matching card faces with a_card and return number of matches.
Parameters:
 a_round Round* Current round of the game being played.
 a_card Card* Card to check on the layout
 a_print bool Flag to print match statistics
Return Value:
 short The number of card face matches on the layout with the passed card a_card.
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
Algorithm:
 Function does not count stacked pairs. They are handled differently as they are a special case in the second draw
 if the previous draw was H1/H2 case where a stacked pair is left on the layout.
 Checks layout for number of matches with a_card by iteration and returns the number of matches.
Assistance Received: NA
********************************************************************* */
short Computer::checkLayout(Round* a_round, Card* a_card, bool a_print)
{
    // Variable to count number of matches:
    short t_count = 0;
    
    // Get layout of the round:
    vector<vector<Card*>> t_layout = a_round->getLayout();
    
    
    // Checking the layout for matches:
    if(a_print)
    {
    cout<<"Picked card: "<<a_round->getGUIchar(a_card->getCardRank(), a_card->getCardSuit())<<endl;;
    cout<<"Layout matches: ";
    }
    for(int i = 0; i<t_layout.size(); i++)
    {
        for(int j = 0; j<t_layout[i].size();j++)
        {
            // Not counting if it is a stacked pair:
            // Stack pairs on the layout are only possible on the stock pile draw and if the previous match case was
            // H1 or H2.
            // Stacked pairs are played differently. See function seeStockPile
            if (t_layout[i].size()==2)
                continue;
            
            if(t_layout[i][j]->getCardRank() == a_card->getCardRank())
            {
                if(a_print)
                    cout<<a_round->getGUIchar(t_layout[i][j]->getCardRank(), t_layout[i][j]->getCardSuit())<<" ";
                
                t_count++;
                // Triple stack notification
                if(j==2 && a_print)
                    cout<<"- A triple stack! - ";
            }
        }
    }


    if(t_count==0 && a_print)cout<<"NA"<<endl;
    else
    {
        if(a_print)
            cout<<endl;
    }
    
    if(a_print)
        cout<<"Number of matches: "<<t_count<<endl;
    
    return t_count;
}


/* *********************************************************************
Function Name: processMove
Purpose: Method to put card on the layout and perform the required captures
 Defined as H0, H1, H2 AND H2 in the game description
Parameters:
 a_round Round* Current round of the game being played.
 a_card Card* Card to check on the layout
 a_matches short The number of matches of card a_card in the layout. Generated by method checkLayout.
Return Value: NA
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
Algorithm:
 Processes move according the match case. Makes captures and adds cards to layout and removes them.
Assistance Received: NA
********************************************************************* */
void Computer::processMove(Round* a_round, Card* a_card, short a_matches)
{
    // H0 CASE
    // H0: card matches no card in the layout, the card is added to the layout.
    if (a_matches == 0)
        a_round->addToLayout(a_card, (int)a_round->getLayout().size());
    
    // H1 CASE
    // H1: card matches one card in the layout, the player creates a stack pair of the two cards and leaves it in the layout.
    else if (a_matches == 1)
    {
        // Get layout of the round:
        vector<vector<Card*>> t_layout = a_round->getLayout();
        
        for(int i = 0; i<t_layout.size(); i++)
        {
            // Check for matching faces and ignoring stacked pairs:
            if(t_layout[i][0]->getCardRank() == a_card->getCardRank() && t_layout[i].size() != 2)
                a_round->addToLayout(a_card, i);
        }
    }
    // H2 CASE
    // H2: card matches two cards in the layout, the player picks one of the two cards and creates a stack pair with it and the card played from the hand, leaving the stack pair in the layout.
    else if (a_matches == 2)
    {
        // Variable to hold card placement index:
        int t_cardIdx = -1;
        
        // Get layout of the round:
        vector<vector<Card*>> t_layout = a_round->getLayout();

        for(int i = 0; i<t_layout.size(); i++)
        {
            // Check for matching faces and ignoring stacked pairs:
            if(t_layout[i][0]->getCardRank() == a_card->getCardRank() && t_layout[i].size() != 2)
            {
                t_cardIdx = i;
                break;
            }
        }
        
        // Add card to the first matching card on the layout
        if(t_cardIdx != -1)
            a_round->addToLayout(a_card, t_cardIdx);
        else
            cerr<<"Error choosing card on the layout. Computer::processMove"<<endl;
        
    }
    // 3 or more, H3 CASE. (TRIPLE STACK IS PRIORITIZED AUTOMATICALLY)
    // H3: card matches three cards in the layout or triple stack, the player captures all four cards, i.e., adds them to their capture pile.
    else
    {
        // Get layout of the round:
        vector<vector<Card*>> t_layout = a_round->getLayout();
        
        // Vector to hold indeces of matching cards in the layout
        vector<int> t_cardIdx;
        
        // Checking for matching faces
        for(int i = 0; i<t_layout.size(); i++)
        {
            // Checking for triple stack and capturing if found:
            if(t_layout[i][0]->getCardRank() == a_card->getCardRank() && t_layout[i].size()==3)
            {
                a_round->addToLayout(a_card, i);
                captureStack(a_round, i);
                a_round->removeFromLayout(i);
                return;
            }
            // Check for matching faces and ignoring stacked pairs:
            if(t_layout[i][0]->getCardRank() == a_card->getCardRank() && t_layout[i].size() != 2)
                t_cardIdx.push_back(i);
        }
        
        // If function reaches here, no triple stack was found.
        // But, there are 3 cards on the layout that has the same face.
        // Capturing all cards:
        if(t_cardIdx.size()>=3)
        {
            // Leave the card from your hand on the first card
            // then capture all 4 cards:
            a_round->addToLayout(a_card, t_cardIdx[0]);
            // You can only capture 3 cards from the layout
            // Even if there are more.
            // The first 3 matching cards are captured.
            for(int i = 0; i<3; i++)
                captureStack(a_round, t_cardIdx[i]);
            // Removing the cards captured from the layout
            for(int i = 0; i<3; i++)
            {
                // If i elements are removed, the index is less by i
                if(i>0)
                    t_cardIdx[i] -= i;
                a_round->removeFromLayout(t_cardIdx[i]);
            }
            // Updating score as the score increments three times on function call
            m_score -= 2;
                
        }
    }
}


/* *********************************************************************
Function Name: movePickHelp
Purpose: Method to get move recommendation. Used as a strategy for computer player.
Parameters:
 a_round Round* Current round of the game being played.
Return Value:
 int Index of card on player's hand that is to be picked according to the algorithm.
Local Variables:
 t_layout vector<vector<Card*>> Temporary variable to hold the round layout.
 t_capture vector<Card*> Vector to hold the captured cards
Algorithm:
 Checks player's hand and the layout to check for matches that can give the player a point.
 Priority check is given to a capture of 3 cards, if not found, the algorithm checks the player's
 capture pile to see if any capture can be made to make a stacked pair on the capture pile a pair of 4.
 If no captures can be made at all, a card is chosen at random to add to the layout.
Assistance Received: NA
********************************************************************* */
int Computer::movePickHelp(Round* a_round)
{
    // Variable to hold card index that can create a stacked pair:
    int t_cardIdx = -1;
    
    // Variable to hold number of matches on the layout with picked card:
    int t_matches;
    
    // Map for index of card on user hand and number of matches on the layout (int, int)
    map<int, int> t_matchMap;
    
    for(int i = 0 ; i<m_hand.size(); i++)
    {
        // Check layout for matches
        t_matches = checkLayout(a_round, m_hand[i], false);
        
        // Return if a triple stack is found:
        if(t_matches >= 3)
        {
            cout<<"The computer chose to play "<<a_round->getCardInformation(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())<<" because it could capture three cards or a triple stack."<<endl;
            return i;
        }
        
        // Can create one stacked pair:
        else if (t_matches == 1 || t_matches == 2)
        {
            // Recording index and matches
            // (Even if the the card matches 2 cards, only a single stack can be created,
            // so we initialize the same capture value)
            t_matchMap[i] = 1;
            
            // Checking capture pile for existing stacked pairs of the same face:
            for(int j = 0; j<m_capture.size(); j++)
            {
                // If found, a point can be gained by capturing current pair.
                // The card index is returned:
                if(m_capture[j][0]->getCardRank() == m_hand[i]->getCardRank()
                   && (m_capture[j].size() + 2) == 4)
                {
                    cout<<"The computer chose to play "<<a_round->getCardInformation(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())<<" because it can capture a stacked pair and build a captured pair to earn a point."<<endl;
                    return i;
                }
            }
            // Recording index that can create a stacked pair
            t_cardIdx = i;
        }
        // No matches
        else
            t_matchMap[i] = 0;
    }
    
    // If function reaches here, none of the cards on hand can give a point.
    // So a card that creates a stacked pair is returned.
    if(t_cardIdx != -1)
    {
        cout<<"The computer chose to play "<<a_round->getCardInformation(m_hand[t_cardIdx]->getCardRank(), m_hand[t_cardIdx]->getCardSuit())<<" because it can build and capture a stacked pair."<<endl;
        return t_cardIdx;
    }

    // If no stacked card can be created, a random card is picked:
    else
    {
        srand((int)time(NULL));
        int t_rand = rand()%m_hand.size();
        cout<<"The computer chose to play "<<a_round->getCardInformation(m_hand[t_rand]->getCardRank(), m_hand[t_rand]->getCardSuit())<<" because no card matches a card on the layout."<<endl;
        return t_rand;
    }
}

/* *********************************************************************
Function Name: ~Computer
Purpose: Computer class destructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Computer class destructor. Releases used memory.
Assistance Received: NA
********************************************************************* */
Computer::~Computer()
{
    for(int i = 0; i<m_hand.size(); i++)
        delete []m_hand[i];
    
    for(int i = 0; i<m_capture.size(); i++)
    {
        for(int j = 0; j<m_capture[i].size(); j++)
            delete []m_capture[i][j];
    }
    
}


/*
***************
DOXYGEN HEADER
***************
*/


/**
*  Computer.cpp
*  Implementation of Computer.hpp
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
