/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#include "Round.hpp"
#include "Game.hpp"

/* *********************************************************************
Function Name: Round
Purpose: Round class constructor.
Parameters:
 a_game Game* The main game of each round.
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Round object by initializing member variables.
Assistance Received: NA
********************************************************************* */
Round::Round(Game* a_game)
{
    // Recording game pointer
    m_game = a_game;

    // Temp variable to hold layout cards
    vector<Card*> t_layout;

    // Initializing the two decks of cards and shuffling them for the round
    m_stockpile = new Deck(2);
    m_stockpile->shuffleDeck();

    // Clearing players hands:
    a_game->m_user->clearPile();
    a_game->m_computer->clearPile();

    /*
     Dealing card as per the rules of the game:
         5 cards are dealt to the human player;
         5 cards are dealt to the computer player;
         4 cards are placed face up in the layout;
         5 cards are dealt to the human player;
         5 cards are dealt to the computer player;
         4 cards are placed face up in the layout;
     */
    dealCards(a_game->m_user, 5);
    dealCards(a_game->m_computer, 5);
    for(int i = 0; i<4; i++)
    {
        t_layout.push_back(m_stockpile->getNewCard());
        m_layout.push_back(t_layout);
        t_layout.clear();
    }
    dealCards(a_game->m_user, 5);
    dealCards(a_game->m_computer, 5);
    for(int i = 0; i<4; i++)
    {
        t_layout.push_back(m_stockpile->getNewCard());
        m_layout.push_back(t_layout);
        t_layout.clear();
    }
    // Stock pile now has the remaining cards after dealing.

    // Debugging:
//    for(int i = 0; i<2; i++)
//    {
//        t_layout.push_back(new Card(m_layout[0][0]->getCardRank(), m_layout[0][0]->getCardSuit()));
//        m_layout.push_back(t_layout);
//        t_layout.clear();
//    }
//    m_layout[0].push_back(new Card(m_layout[0][0]->getCardRank(), m_layout[0][0]->getCardSuit()));
//    m_layout[0].push_back(new Card(m_layout[0][0]->getCardRank(), m_layout[0][0]->getCardSuit()));
//    cout<<"Layout: "<<endl;
//    for(int i = 0; i<m_layout.size(); i++)
//    {
//        for(int j = 0; j<m_layout[i].size();j++)
//           cout<<i<<"-"<<j<<"Rank: "<<m_layout[i][j]->getCardRank()<<" SUIT: "<<m_layout[i][j]->getCardSuit()<<endl;
//
//    }
//    cout<<"User: "<<endl;
//    a_game->m_user->printCards();
//    cout<<"Computer: "<<endl;
//    a_game->m_computer->printCards();

}

/* *********************************************************************
Function Name: Round
Purpose: Parameterized round class constructor to initiate round from config file
Parameters:
 a_game Game* The main game of the round.
 a_deck vector<Card*> The deck to load for the game from the config file
 a_layout vector<vector<Card*>> The layout from th config file to load
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Round object by initializing member variables according to the config file.
Assistance Received: NA
********************************************************************* */
Round::Round(Game* a_game, vector<Card*> a_deck, vector<vector<Card*>> a_layout)
{
    // Recording game pointer
    m_game = a_game;

    // Temp variable to hold layout cards
    vector<Card*> t_layout;

    // Initializing the two decks of cards and shuffling them for the round
    m_stockpile = new Deck(a_deck);

    // Initializing layout
    m_layout = a_layout;

}

/* *********************************************************************
Function Name: dealCards
Purpose: Method to deal cards in the game.
Parameters:
 a_player Player* A player of the game. Can be the computer or user.
 a_cards int Number of cards to deal to a_player.
Return Value: NA
Local Variables: NA
Algorithm: Deals cards to the player by utilizing Deck class getNewCard method.
Assistance Received: NA
********************************************************************* */
void Round::dealCards(Player* a_player, int a_cards)
{
    for(int i = 0; i<a_cards; i++)
        a_player->handPlayerCard(m_stockpile->getNewCard());

}

/* **********************s***********************************************
Function Name: determineFirstPlayer
Purpose: Method to check who will go first according the rule of the game and for modulo suit.
Parameters:
Return Value: short
 1 User goes first.
 2 Computer goes first.
 3 A modulo suit. (Same cards in hands of both players)
Local Variables:
 t_userHandMap, t_compHandMap Two hash map to record the cards in hands of the two players.
Algorithm: Player with the most Kings plays first. If there is a tie, the player with the most Queens plays first. And so on down to the most Aces. If there is a tie in the number of Aces also, it means the two players have exactly the same set of cards (modulo suit).
 Iterate through the hands of both computer and user player and update two separate hash maps
 with the card ranks. The cards ranks are flipped because we need to check from Kings -> Aces.
 Check values and determine the first player.
Assistance Received: NA
********************************************************************* */
short Round::determineFirstPlayer()
{
    // Variable to record ranks and occurences of each hand
    map<int, int> t_userHandMap;
    map<int, int> t_compHandMap;

    // Recording hands of each player in temp variables
    vector<Card*> t_userHand = m_game->m_user->getHandPile();
    vector<Card*> t_compHand = m_game->m_computer->getHandPile();

    // Variable to hold the highest ranking card:
    // 12 == Aces, 0 == Kings
    int t_minVal = 12;

    // Temp value holding variables
    int t_userCardVal;
    int t_compCardVal;

    // Recording card ranks in a flipped manner using respective hashmaps.
    for(int i = 0; i<t_userHand.size(); i++)
    {
        // Recording rank value:
        t_userCardVal = 12 - t_userHand[i]->getCardRank();
        t_compCardVal = 12 - t_compHand[i]->getCardRank();

        // Updating hash map
        t_userHandMap[t_userCardVal]++;
        t_compHandMap[t_compCardVal]++;

        // Updating the highest rank card:
        if(t_userCardVal < t_minVal) t_minVal = t_userCardVal;
        if(t_compCardVal < t_minVal) t_minVal = t_compCardVal;
    }

    // Checking for modulo suit and determining first player
    // Checking cards from highest rank to lowest rank:
    for(int i = t_minVal; i<13; i++)
    {
        // If both hands don't have the card:
        if(t_compHandMap.find(i) == t_compHandMap.end()
           && t_userHandMap.find(i) == t_userHandMap.end())
            continue;

        // Check if computer has higher rank card and user does not:
        // Computer goes first
        else if(t_compHandMap.find(i) != t_compHandMap.end()
           && t_userHandMap.find(i) == t_userHandMap.end())
            return 2;


        // Check if user has higher rank card and computer does not:
        // User goes first
        else if(t_userHandMap.find(i) != t_userHandMap.end()
           && t_compHandMap.find(i) == t_compHandMap.end())
            return 1;


        // If both have the higher rank card:
        else
        {
            // Check who has more of the higher rank card:
            // User
            if(t_userHandMap.find(i)->second > t_compHandMap.find(i)->second)
                return 1;
            // Computer
            else if(t_compHandMap.find(i)->second > t_userHandMap.find(i)->second)
                return 2;

            // Equal number of higher rank cards
            else
                continue;
        }
    }
    // If function reaches here, there is a modulo suit:
    cout<<"Modulo suit! Reshuffling cards and restarting the round..."<<endl;
    return 3;
}

/* **********************s***********************************************
Function Name: startRound
Purpose: Method to start the round of the game:
Parameters:
 a_turn short The starting turn of the player for the round (1 for user 2 for computer)
Return Value: NA
Local Variables: NA
Algorithm:
 Displays the board with current cards. Checks for first player, which is passed as an argument.
 Calls play method for each player acccordingly and loops until the hands of both players are empty.
Assistance Received: NA
********************************************************************* */
void Round::startRound(short a_turn)
{
    displayBoard();

    // Variable to hold number of turns in a round. Reinitialized on each round.
    int t_turnCounter = 1;

    // Variable to hold quit flag for user
    bool t_resume = 1;

    // Playing the round according to the decided first player:
    if(a_turn == 1)
    {
        // User as first player
        do
        {
            cout<<endl<<"Round: "<<m_game->getRoundNumber()<<endl;
            cout<<"Turn: "<<t_turnCounter<<endl;
            cout<<"--------"<<endl;
            t_resume = m_game->m_user->play(this);
            // Checking if user quit the game:
            if(t_resume == 0)
                m_game->endGoStop();
            displayBoard();
            if(m_game->m_computer->getHandPile().empty())
                break;
            t_resume = m_game->m_computer->play(this);
            // Checking if user quit the game:
            if(t_resume == 0)
                m_game->endGoStop();
            t_turnCounter++;
            displayBoard();
        } while (!m_game->m_user->getHandPile().empty() || !m_game->m_computer->getHandPile().empty());
    }
    else
    {
        // Computer as first player
        do
        {
            cout<<endl<<"Round: "<<m_game->getRoundNumber()<<endl;
            cout<<"Turn: "<<t_turnCounter<<endl;
            cout<<"--------"<<endl;
            t_resume = m_game->m_computer->play(this);
            // Checking if user quit the game:
            if(t_resume == 0)
                m_game->endGoStop();
            displayBoard();
            if(m_game->m_user->getHandPile().empty())
                break;
            t_resume = m_game->m_user->play(this);
            // Checking if user quit the game:
            if(t_resume == 0)
                m_game->endGoStop();
            t_turnCounter++;
            displayBoard();
        } while (!m_game->m_user->getHandPile().empty() || !m_game->m_computer->getHandPile().empty());

    }

}


/* **********************s***********************************************
Function Name: displayBoard
Purpose: Method to display the board.
Parameters:
Return Value: NA
Local Variables: NA
Algorithm: Displays the board with current cards.
Assistance Received: NA
********************************************************************* */
void Round::displayBoard()
{
    // Get both player's hands and capture pile:
    vector<Card*> t_compHand = m_game->m_computer->getHandPile();
    vector<Card*> t_userHand = m_game->m_user->getHandPile();
    vector<vector<Card*>> t_compCapture = m_game->m_computer->getCapturePile();
    vector<vector<Card*>> t_userCapture = m_game->m_user->getCapturePile();

    cout<<"---------------------------------"<<endl;
    cout<<"Computer Score: "<<m_game->m_computer->getPlayerScore()<<endl;
    cout<<"Computer Hand: "<<endl;
    for(int i = 0; i<t_compHand.size(); i++)
    {
        cout<<getGUIchar(t_compHand[i]->getCardRank(), t_compHand[i]->getCardSuit())<<" ";
    }
    cout<<endl;
    cout<<"Computer Capture: "<<endl;
    for(int i = 0; i<t_compCapture.size(); i++)
    {
        for(int j = 0; j<t_compCapture[i].size(); j++)
            cout<<getGUIchar(t_compCapture[i][j]->getCardRank(), t_compCapture[i][j]->getCardSuit())<<"|";
        cout<<endl;
    }
    cout<<"---------------------------------"<<endl;


    cout<<"Layout: "<<endl;
    for(int i = 0; i<m_layout.size(); i++)
    {
        for(int j = 0; j<m_layout[i].size();j++)
            cout<<getGUIchar(m_layout[i][j]->getCardRank(), m_layout[i][j]->getCardSuit())<<"|";
        cout<<endl;
    }
    cout<<"Stock pile: "<<getGUIchar(m_stockpile->viewTopCard()->getCardRank(), m_stockpile->viewTopCard()->getCardSuit()) <<"||||......."<<endl;
    cout<<"---------------------------------"<<endl;
    cout<<"User Score: "<<m_game->m_user->getPlayerScore()<<endl;
    cout<<"User Hand: "<<endl;
    for(int i = 0; i<t_userHand.size(); i++)
    {
        cout<<getGUIchar(t_userHand[i]->getCardRank(), t_userHand[i]->getCardSuit())<<" ";
    }
    cout<<endl;
    cout<<"User Capture: "<<endl;
    for(int i = 0; i<t_userCapture.size(); i++)
    {
        for(int j = 0; j<t_userCapture[i].size(); j++)
            cout<<getGUIchar(t_userCapture[i][j]->getCardRank(), t_userCapture[i][j]->getCardSuit())<<"|";
        cout<<endl;
    }
    cout<<"---------------------------------"<<endl;
}

/* **********************s***********************************************
Function Name: getGUIchar
Purpose: Method to translate card value to GUI character representation for printing purposes.
Parameters:
 a_rank CardRank Rank of the card
 a_suit CardSuit Suit of the card
Return Value:
 string The GUI character representation of the card value
Local Variables: NA
Algorithm: Converts the cardrank and cardsuit to a string for GUI representation
Assistance Received: NA
********************************************************************* */
string Round::getGUIchar(CardRank a_rank, CardSuit a_suit)
{
    // Variable to hold the character representation
    string t_code = "";

    switch(a_rank)
    {
        case ACE:
            t_code += "1";
            break;
        case TWO:
            t_code += "2";
            break;
        case THREE:
            t_code += "3";
            break;
        case FOUR:
            t_code += "4";
            break;
        case FIVE:
            t_code += "5";
            break;
        case SIX:
            t_code += "6";
            break;
        case SEVEN:
            t_code += "7";
            break;
        case EIGHT:
            t_code += "8";
            break;
        case NINE:
            t_code += "9";
            break;
        case TEN:
            t_code += "X";
            break;
        case JACK:
            t_code += "J";
            break;
        case QUEEN:
            t_code += "Q";
            break;
        case KING:
            t_code += "K";
            break;
    }

    switch(a_suit)
    {
        case HEARTS:
            t_code += "H";
            break;
        case CLUBS:
            t_code += "C";
            break;
        case SPADES:
            t_code += "S";
            break;
        case DIAMONDS:
            t_code += "D";
            break;
    }

    return t_code;
}

/* **********************s***********************************************
Function Name: getCardInformation
Purpose: Method to translate card value to detailed card description. Used for printing.
Parameters:
 a_rank CardRank Rank of the card
 a_suit CardSuit Suit of the card
Return Value:
 string The Card Information as a string.
Local Variables: NA
Algorithm: Converts the cardrank and cardsuit to a string.
Assistance Received: NA
********************************************************************* */
string Round::getCardInformation(CardRank a_rank, CardSuit a_suit)
{
    // Variable to hold the character representation
    string t_code = "";

    switch(a_rank)
    {
        case ACE:
            t_code += "ACE of ";
            break;
        case TWO:
            t_code += "2 of ";
            break;
        case THREE:
            t_code += "3 of ";
            break;
        case FOUR:
            t_code += "4 of ";
            break;
        case FIVE:
            t_code += "5 of ";
            break;
        case SIX:
            t_code += "6 of ";
            break;
        case SEVEN:
            t_code += "7 of ";
            break;
        case EIGHT:
            t_code += "8 of ";
            break;
        case NINE:
            t_code += "9 of ";
            break;
        case TEN:
            t_code += "X of ";
            break;
        case JACK:
            t_code += "JACK of ";
            break;
        case QUEEN:
            t_code += "QUEEN of ";
            break;
        case KING:
            t_code += "KING of ";
            break;
    }

    switch(a_suit)
    {
        case HEARTS:
            t_code += "Hearts";
            break;
        case CLUBS:
            t_code += "Clubs";
            break;
        case SPADES:
            t_code += "Spades";
            break;
        case DIAMONDS:
            t_code += "Diamonds";
            break;
    }

    return t_code;
}

/* **********************s***********************************************
Function Name: addToLayout
Purpose: Method to add cards to the layout on given index. Used by players.
Parameters:
 a_card Card* Pointer to the card to be added
 a_index int Index position of where to place the card on the layout
Return Value: NA
Local Variables: NA
Algorithm: Adds the card at the given layout. If it is placed in a new position, a new vector of card is created and pushed.
Assistance Received: NA
********************************************************************* */
void Round::addToLayout(Card* a_card, int a_index)
{
    // Temp card vector
    vector<Card*> t_card;

    // If it is a new card in the layout
    if(a_index == m_layout.size())
    {
        t_card.push_back(a_card);
        m_layout.push_back(t_card);
    }
    else
    {
        assert(a_index >= 0 && a_index<m_layout.size());
        m_layout[a_index].push_back(a_card);
    }
}

/* **********************s***********************************************
Function Name: removeFromLayout
Purpose: Method to remove cards to the layout. Used by players.
Parameters:
 a_index int Index position of where to place the card on the layout
Return Value: NA
Local Variables: NA
Algorithm: Removes card from index from the layout.
Assistance Received: NA
********************************************************************* */
void Round::removeFromLayout(int a_index)
{
    m_layout.erase(m_layout.begin()+a_index);
}

/* *********************************************************************
Function Name: saveGame
Purpose:  Method to save game.
Parameters:
 a_player bool Player whose turn is next. 0 for computer and 1 for human.
Return Value: NA
Local Variables: NA
Algorithm:
 Gets text file name to save the game from user or uses a default name "save_file.txt"
 to save game configurations according to the config file template.
Assistance Received: NA
********************************************************************* */
void Round::saveGame(bool a_player)
{
    // Get both player's hands and capture pile:
    vector<Card*> t_compHand = m_game->m_computer->getHandPile();
    vector<Card*> t_userHand = m_game->m_user->getHandPile();
    vector<vector<Card*>> t_compCapture = m_game->m_computer->getCapturePile();
    vector<vector<Card*>> t_userCapture = m_game->m_user->getCapturePile();

    std::fstream t_saveFile;

    // File name input
    string t_fileName = "save_file.txt";
    cout<< "Do you want to enter a custom file name? (Default save file is: save_file.txt)"<<endl;
    cout<<"1: Create custom file name."<<endl;
    cout<<"2: Use default file name."<<endl;
    string t_temp;
    int t_playAgain;
    do
    {
        cout<<":";
        cin>>t_temp;
        try {
            t_playAgain = stoi(t_temp);
        } catch (exception e) {
            cout<<"Invalid input"<<endl;
            t_playAgain = -1;
        }
    } while (t_playAgain<1 || t_playAgain>2);


    if(t_playAgain == 1)
    {
        cout<<"Please enter the file name with the extension you want to create."<<endl;
        cout<<"(Note: Include .txt after file name)"<<endl;
        do
        {
            getline(cin, t_fileName);
        } while(t_fileName.find(".txt") == string::npos ||
                (t_fileName.find(".txt") != string::npos && t_fileName.find(" ") != string::npos));
    }


    t_saveFile.open(t_fileName, fstream::out);
    if(t_saveFile.is_open())
    {
        t_saveFile<<"Round: "<<m_game->getRoundNumber()<<endl<<endl;
        // Computer stats
        t_saveFile<<"Computer:"<<endl;
        t_saveFile<<'\t'<<"Score: "<<m_game->getCompScore()<<endl;
        t_saveFile<<'\t'<<"Hand: ";
        for(int i = 0; i<t_compHand.size(); i++)
            t_saveFile<<getGUIchar(t_compHand[i]->getCardRank(), t_compHand[i]->getCardSuit())<<" ";
        t_saveFile<<endl;
        t_saveFile<<'\t'<<"Capture Pile: ";
        for(int i = 0; i<t_compCapture.size(); i++)
        {
            for(int j = 0; j<t_compCapture[i].size(); j++)
            {
                t_saveFile<<getGUIchar(t_compCapture[i][j]->getCardRank(), t_compCapture[i][j]->getCardSuit());
                if(j != t_compCapture[i].size()-1)
                    t_saveFile<<"-";
            }
            t_saveFile<<" ";
        }
        t_saveFile<<endl;


        // User stats
        t_saveFile<<endl<<"Human:"<<endl;
        t_saveFile<<'\t'<<"Score: "<<m_game->getUserScore()<<endl;
        t_saveFile<<'\t'<<"Hand: ";
        for(int i = 0; i<t_userHand.size(); i++)
            t_saveFile<<getGUIchar(t_userHand[i]->getCardRank(), t_userHand[i]->getCardSuit())<<" ";
        t_saveFile<<endl;
        t_saveFile<<'\t'<<"Capture Pile: ";
        for(int i = 0; i<t_userCapture.size(); i++)
        {
            for(int j = 0; j<t_userCapture[i].size(); j++)
            {
                t_saveFile<<getGUIchar(t_userCapture[i][j]->getCardRank(), t_userCapture[i][j]->getCardSuit());
                if(j != t_userCapture[i].size()-1)
                    t_saveFile<<"-";
            }
            t_saveFile<<" ";
        }
        t_saveFile<<endl;

        // Layout:
        t_saveFile<<endl<<"Layout: ";
        for(int i = 0; i<m_layout.size(); i++)
        {
            for(int j = 0; j<m_layout[i].size(); j++)
            {
                t_saveFile<<getGUIchar(m_layout[i][j]->getCardRank(), m_layout[i][j]->getCardSuit());
                if(j != m_layout[i].size()-1)
                    t_saveFile<<"-";
            }
            t_saveFile<<" ";
        }
        t_saveFile<<endl;

        // Deck of cards from the top card:
        t_saveFile<<endl<<"Stock Pile: ";
        vector<Card*> t_deck = m_stockpile->getDeck();
        for(int i = m_stockpile->topCardIndex(); i<t_deck.size(); i++)
            t_saveFile<<getGUIchar(t_deck[i]->getCardRank(), t_deck[i]->getCardSuit())<<" ";

        t_saveFile<<endl;

        t_saveFile<<endl<<"Next Player: ";
        if(a_player==0)
            t_saveFile<<"Computer"<<endl;
        else
            t_saveFile<<"Human"<<endl;

        t_saveFile.close();

        cout<<"Game saved successfully."<<endl;
        m_game->displayGameStats();
    }
    else
    {
        cerr<<"Error opening file"<<endl;
        exit(1);
    }

    exit(0);
}

/* *********************************************************************
Function Name: ~Round
Purpose: Round class destructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Releases allocated memory.
Assistance Received: NA
********************************************************************* */
Round::~Round()
{
    delete []m_game;
    m_stockpile->~Deck();
    for(int i = 0; i<m_layout.size(); i++)
    {
        for(int j = 0; j<m_layout[i].size(); j++)
            delete []m_layout[i][j];
    }

}


/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Round.cpp
*  Implementation of Round.hpp
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
