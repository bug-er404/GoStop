/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#include "User.hpp"
#include "Round.hpp"

/* *********************************************************************
Function Name: User
Purpose: User class constructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Constructs a User object by initializing member variables.  Calls player class for construction.
Assistance Received: NA
********************************************************************* */
User::User() : Player()
{
    
}

/* *********************************************************************
Function Name: User
Purpose: Parameterized User class constructor used for initiating game from a config file.
Parameters:
 a_hand vector<Card*> cards in hand of the User
 a_capture vector<vector<Card*>> Cards in the capture pile of the User
 a_score unsigned int Score from config file
Return Value: NA
Local Variables: NA
Algorithm: Constructs a User object by initializing member variables. Calls player class for construction.
Assistance Received: NA
********************************************************************* */
User::User(vector<Card*> a_hand, vector<vector<Card*>> a_capture, unsigned int a_score) : Player((vector<Card*>) a_hand, (vector<vector<Card*>>) a_capture, (unsigned int) a_score)
{

}

/* *********************************************************************
Function Name: play
Purpose: Overloaded method to play a turn for user player.
Parameters:
 a_round Round* Pointer to the current round.
Return Value:
 bool 0 if the player wants to stop playing.
Local Variables: NA
Algorithm: Gets input from user to perform the selected menu choice.
Assistance Received: NA
********************************************************************* */
bool User::play(Round* a_round)
{
    // Variable to hold menu choice of user
    short t_menuChoice;
    
    cout<<"User Turn"<<endl;
    
    // User menu option:
    cout<<"Menu:"<<endl;
    cout<<"1. Save the game"<<endl;
    cout<<"2. Make a move"<<endl;
    cout<<"3. Ask for help"<<endl;
    cout<<"4. Quit the game "<<endl;
    
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
    } while (t_menuChoice<1 || t_menuChoice>4);
    
    // Performing menu option
    switch(t_menuChoice)
    {
        case 1:
            a_round->saveGame(1);
            break;
        case 2:
            makeMove(a_round, false);
            break;
        case 3:
            movePickHelp(a_round);
            makeMove(a_round, true);
            break;
        case 4:
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
Purpose: Method to process a user move.
Parameters:
 a_round Round* Pointer to the current round.
 a_help bool Flag for help mode or not.
Return Value: NA
Local Variables: NA
Algorithm:
 Gets card choice from user and validates input. Checks layout for number of matches and
 processses the move by calling function processMove. Makes captures, updates hand
 and then plays from the stock pile.
Assistance Received: NA
********************************************************************* */
void User::makeMove(Round* a_round, bool a_help)
{
    // Variable to hold card index choice:
    short t_cardIdx;
    
    // Variable to hold number of matches on the layout with picked card:
    short t_matches;
    
    // ************************
    // PLAYING FROM HAND PILE:
    // ************************
    // Getting card choice from user:
    cout<<"Which card from your hand do you want to add to the layout?"<<endl;
    cout<<"(Pick a card position number written below the card detail)"<<endl;
    cout<<"Your Hand: "<<endl;
    for(int i = 0; i<m_hand.size(); i++)
        cout<<a_round->getGUIchar(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())<<" ";
    cout<<endl;
    for(int i = 0; i<m_hand.size(); i++)
        cout<<i+1<<"  ";
    cout<<endl;

    // Input validation and integer check:
    string t_temp;
    do
    {
        cout<<":";
        cin>>t_temp;
        try {
            t_cardIdx = stoi(t_temp);
        } catch (exception e) {
            cout<<"Invalid input"<<endl;
            t_cardIdx = -1;
        }
    } while (t_cardIdx<1 || t_cardIdx>m_hand.size());
    
    // Checking layout for # of matches on the layout
    // and performing the move:
    t_matches = checkLayout(a_round, m_hand[t_cardIdx-1], false);
    processMove(a_round, m_hand[t_cardIdx-1], t_matches);
    
    // Removing card from hand
    m_hand.erase(m_hand.begin()+(t_cardIdx-1));
    
    // Sorting and displaying board
    sortHandPile(a_round);
    sortCapturePile(a_round);
    a_round->displayBoard();
    
    // ************************
    // PLAYING FROM STOCK PILE:
    // ************************
    
    // Waiting for user input:
    // Asking user if they want to draw from stock pile
    cout<< "Menu:"<<endl;
    cout<<"1: Draw card from stock pile"<<endl;
    string temp;
    int t_playAgain;
    do
    {
        cout<<":";
        cin>>temp;
        try {
            t_playAgain = stoi(temp);
        } catch (exception e) {
            cout<<"Invalid input"<<endl;
            t_playAgain = -1;
        }
    } while (t_playAgain != 1);
    

    playStockPile(a_round, t_matches, a_help);
    sortHandPile(a_round);
    sortCapturePile(a_round);

    
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
Assistance Received: NA
********************************************************************* */
short User::checkLayout(Round* a_round, Card* a_card, bool a_print)
{
    // Variable to count number of matches:
    short t_count = 0;
    
    // Get layout of the round:
    vector<vector<Card*>> t_layout = a_round->getLayout();
    
    
    // Checking the layout for matches:
    if(a_print)
    {
    cout<<"Picked card: "<<a_round->getGUIchar(a_card->getCardRank(), a_card->getCardSuit())<<endl;;
    cout<<"Recommended layout captures: ";
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
void User::processMove(Round* a_round, Card* a_card, short a_matches)
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
        int t_cardIdx;
        
        // Get layout of the round:
        vector<vector<Card*>> t_layout = a_round->getLayout();
        // Map to hold index of matching cards on the layout
        map<int, Card*> t_cardMatch;
        map<int, Card*>::iterator itr;
        
        for(int i = 0; i<t_layout.size(); i++)
        {
            // Check for matching faces and ignoring stacked pairs:
            if(t_layout[i][0]->getCardRank() == a_card->getCardRank() && t_layout[i].size() != 2)
                t_cardMatch[i] = t_layout[i][0];
        }
        
        // Getting card choice from user:
        cout<<"Which card on the layout do you want stack your card?"<<endl;
        cout<<"(Pick a card position number written below the card detail)"<<endl;
        cout<<"Layout cards with same face: "<<endl;
        for(itr = t_cardMatch.begin(); itr!=t_cardMatch.end(); itr++)
            cout<<a_round->getGUIchar(itr->second->getCardRank(), itr->second->getCardSuit())<<" ";
        cout<<endl;
        for(itr = t_cardMatch.begin(); itr!=t_cardMatch.end(); itr++)
            cout<<itr->first<<"  ";
        cout<<endl;
        
        // Input validation and integer check:
        string t_temp;
        do
        {
            cout<<":";
            cin>>t_temp;
            try {
                t_cardIdx = stoi(t_temp);
            } catch (exception e) {
                cout<<"Invalid input"<<endl;
                t_cardIdx = -1;
            }
        } while(t_cardMatch.find(t_cardIdx) == t_cardMatch.end());
        
        // Adding to layout
        a_round->addToLayout(a_card, t_cardIdx);
        
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
Purpose:  Method to get move recommendation. Used as a move helper for user.
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
int User::movePickHelp(Round* a_round)
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
            cout<<"You can chose to play "<<a_round->getCardInformation(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())<<" because it can capture three cards or a triple stack and give you a point."<<endl;
            checkLayout(a_round, m_hand[i], true);
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
                    cout<<"You can chose to play "<<a_round->getCardInformation(m_hand[i]->getCardRank(), m_hand[i]->getCardSuit())<<" because it can capture a stacked pair and build a captured pair to earn a point."<<endl;
                    checkLayout(a_round, m_hand[i], true);
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
        cout<<"You can chose to play "<<a_round->getCardInformation(m_hand[t_cardIdx]->getCardRank(), m_hand[t_cardIdx]->getCardSuit())<<" because it can build and capture a stacked pair."<<endl;
        checkLayout(a_round, m_hand[t_cardIdx], true);
        return t_cardIdx;
    }

    // If no stacked card can be created, a random card is picked:
    else
    {
        srand((int)time(NULL));
        int t_rand = rand()%m_hand.size();
        cout<<"You can chose to play "<<a_round->getCardInformation(m_hand[t_rand]->getCardRank(), m_hand[t_rand]->getCardSuit())<<" because you have no card that matches a card on the layout."<<endl;
        cout<<"You can pick any card for this turn."<<endl;
        return t_rand;
    }
}

/* *********************************************************************
Function Name: ~User
Purpose: User class destructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: User class destructor. Releases used memory.
Assistance Received: NA
********************************************************************* */
User::~User()
{
    for(int i = 0; i<m_hand.size(); i++)
        m_hand[i]->~Card();
    
    for(int i = 0; i<m_capture.size(); i++)
    {
        for(int j = 0; j<m_capture[i].size(); j++)
            m_capture[i][j]->~Card();
    }
}


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  User.cpp
*  Implementation of User.hpp
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
