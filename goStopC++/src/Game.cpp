/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/23/2020                                        *
************************************************************
*/

#include "Game.hpp"
#include "User.hpp"
#include "Computer.hpp"

/* *********************************************************************
Function Name: Game
Purpose: Game class constructor.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Constructs a Game object by initializing member variables.
Assistance Received: NA
********************************************************************* */
Game::Game()
{
    // Initializing game score
    m_compScore = 0;
    m_userScore = 0;
    
    // Initializing rounds
    m_rounds = 0;
    
    // Initializing the players of the game. One computer and user.
    m_user = new User();
    m_computer = new Computer();
}


/* *********************************************************************
Function Name: startGoStop
Purpose: Start a game of Go stop. Main game handler method.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Determine first player by calling determineFirstPlayer method and by checking the scores of each player.
 Then initiates the round according to who the first player is.
Assistance Received: NA
********************************************************************* */
void Game::startGoStop()
{
    // Variable to hold the player turn:
    short t_turn;
    // Variable to hold the round:
    Round* t_round;
    // Variable to ask user for another round of Go Stop
    char t_playAgain = 'Y';
    // Variable to ask user if a previous game needs to be loaded
    string t_prevGame;
    
    cout<<"GO STOP"<<endl;
    
    // Checking if previous instance needs to be loaded
    cout<<"Main menu options:"<<endl;
    cout<<"1: Load a saved game."<<endl;
    cout<<"2: Load a new game."<<endl;
    cout<<"----------------------"<<endl;
    // Input validation and integer check:
    string t_temp;
    int t_menuChoice;
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
    } while (t_menuChoice<1 || t_menuChoice>2);
    if(t_menuChoice == 1)
        loadFromFile();
    
    
    // Main rounds loop for playing again:
    do
    {
        // Updating round counter:
        m_rounds++;
        
        // Determining first player:
        // Looping round construction in case of modulo suits.
        do
        {
            // Initializing a round of go stop
            t_round = new Round(this);
            // Get player turn according to game rule:
            t_turn = t_round->determineFirstPlayer();
            // Modulo Suit case
            if(t_turn == 3)
            {
                cerr<<"Encountered a modulo suit."<<endl;
                cerr<<"Reshuffling deck and starting a new round."<<endl;
            }
        } while (t_turn==3);
        
        // Modulo suits are skipped by this point.
        // Initial round or a tie score:
        if(m_rounds == 1 || m_userScore == m_compScore)
        {
            /*Correct player is already initialized by determineFirstPlayer*/
            if(t_turn == 1)
            {
                cout<<"***************************************************"<<endl;
                cout<<"User has more higher rank cards. First player: User"<<endl;
                cout<<"***************************************************"<<endl;
            }
            else
            {
                cout<<"***********************************************************"<<endl;
                cout<<"Computer has more higher rank cards. First player: Computer"<<endl;
                cout<<"***********************************************************"<<endl;
            }
        }
        // Player with higher score plays on subsequent rounds
        else
        {
            if(m_userScore < m_compScore)
            {
                t_turn = 2;
                cout<<"***********************************************************"<<endl;
                cout<<"Computer has higher score than user. First player: Computer"<<endl;
                cout<<"***********************************************************"<<endl;
            }
            else
            {
                t_turn = 1;
                cout<<"***************************************************"<<endl;
                cout<<"User has higher score than user. First player: User"<<endl;
                cout<<"***************************************************"<<endl;
            }
        }
        
        // Sorting the hand pile
        m_user->sortHandPile(t_round);
        m_computer->sortHandPile(t_round);

        // Start playing the round:
        t_round->startRound(t_turn);
        
        // Updating total game score:
        m_userScore += m_user->getPlayerScore();
        m_compScore += m_computer->getPlayerScore();
        
        displayGameStats();
        
        // Asking user if they want to play again
        // Input validate?
        cout<< "Do you want to play another round of Go Stop?"<<endl;
        cout<<"1: Play another game."<<endl;
        cout<<"2: End Go Stop."<<endl;
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
        
    } while (t_playAgain == 1);
        
    // End game
    endGoStop();

}


/* *********************************************************************
Function Name: loadFromFile
Purpose: Method to load game from a text file
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm:
 Gets user input for file to load from and validates it. Reads all lines till the end of file is reached and checks for all the elements required to
 reintialize the game. Constructs the game using parameterized constructors for Round, User and Computer. After the end of the round, the function
 returns to the main loop and continues.
Assistance Received: NA
********************************************************************* */
void Game::loadFromFile()
{
    // Variable to hold filename
    string t_fileName;
    // Flag to see if the file is loaded
    bool t_load = 1;
    // Stream to read file
    ifstream inf;
    // Buffer variable to store each line and line element.
    string t_buffer;
    string t_element;
    // Temp buffer to read stack of cards
    vector<Card*> t_cardBuffer;
    
    // Variables to hold previous game details:
    // Round number that was last played
    int t_roundNum = 0;
    // Computer and user score
    // Total score
    unsigned int t_compScore=0;
    unsigned int t_userScore=0;
    // Round score
    unsigned int t_compRoundScore = 0;
    unsigned int t_userRoundScore = 0;
    // Computer and user hand pile and capture pile
    vector<Card*> t_userHand;
    vector<Card*> t_compHand;
    vector<vector<Card*>> t_userCapture;
    vector<vector<Card*>> t_compCapture;
    // Game layout
    vector<vector<Card*>> t_layout;
    // Deck
    vector<Card*> t_deck;
    // Player turn: 2 for computer, 1 for user
    short t_turn=0;
    // Counter variable
    unsigned int counter = 0;
    // flag for hyphenated stacks
    bool t_hyphens = false;
    
    // Getting file name input from user
    do
    {
        cout<<"Please enter the name of file to load from"<<endl;
        cin>>t_fileName;
    
        // Open file and check if it opened correctly.
        inf = ifstream(t_fileName);
        if(!inf)
        {
            cerr << "Could not open the file: " << t_fileName << endl;
            t_load = 0;
        }
        else
            t_load = 1;
            
    } while (t_load == 0);

    cout<<"Details loaded from previous game:"<<endl;
    // Getting all records from the file
    while(!inf.eof())
    {
        // Get each line
        getline(inf,t_buffer);

        // Ignoring empty lines.
        if(t_buffer.empty() || t_buffer == " ")
            continue;
        
        // Get Round information
        if(t_buffer.find("Round") != string::npos)
        {
//            // Previous implementation
//            istringstream line(t_buffer.c_str(), (int)t_buffer.size());
//            while(line>>t_element)
//            {
//                if(isdigit(t_element[0]))
//                    t_roundNum = atoi(t_element.c_str());
//            }
            t_roundNum = atoi((t_buffer.substr(t_buffer.find(':') + 1)).c_str());
            cout<<"Round: "<<t_roundNum<<endl;
        }
        
        // Get Computer player details
        if(t_buffer.find("Computer") != string::npos
           && t_buffer.find("Next Player") == string::npos)
        {
            // Get score from next line
            getline(inf, t_buffer);
            if(t_buffer.find("Score") != string::npos)
                t_compScore = atoi((t_buffer.substr(t_buffer.find(':')+1)).c_str());
            cout<<"Computer Total Score: "<<t_compScore<<endl;
            
            // Get computer hand from next line
            getline(inf, t_buffer);
            if(t_buffer.find("Hand") != string::npos)
                t_buffer = t_buffer.substr(t_buffer.find(':')+1);
            // Parsing using space delimiter and updating computer hand:
            istringstream line(t_buffer.c_str(), (int)t_buffer.size());
            while(line>>t_element)
            {
                t_compHand.push_back(new Card(asciiToRank(t_element),asciiToSuit(t_element)));
            }
            
            // Get computer capture pile from next line
            // Variable to hold stacked cards in pile
            string t_pile;
            getline(inf, t_buffer);
            if(t_buffer.find("Capture Pile") != string::npos)
                t_buffer = t_buffer.substr(t_buffer.find(':')+1);
            // Parsing using space delimiter and updating computer capture:
            line = istringstream(t_buffer.c_str(), (int)t_buffer.size());
            // Get all cards (individual and stacked)
            while(line>>t_element)
            {
                // Stacked cards
                if(t_element.find('-') != string::npos)
                {
                    t_hyphens = true;
                    // Reset counter
                    counter = 0;
                    stringstream stackStream(t_element);
                    while(stackStream.good())
                    {
                        // Checking for empty parses
                        if(stackStream.rdbuf()->in_avail() == 0)
                            continue;
                        getline(stackStream, t_pile, '-');
                        t_cardBuffer.push_back(new Card(asciiToRank(t_pile), asciiToSuit(t_pile)));
                        // Check how many cards are in the stack
                        counter++;
                    }
                    if(counter==4)
                        t_compRoundScore++;
                }
                else
                    t_cardBuffer.push_back(new Card(asciiToRank(t_element), asciiToSuit(t_element)));
                
                // Pushing the buffer vector of cards and clearing it
                t_compCapture.push_back(t_cardBuffer);
                t_cardBuffer.clear();
            }
        }
            
        // Get User player details
        if(t_buffer.find("Human") != string::npos
           && t_buffer.find("Next Player") == string::npos)
        {
            // Get score from next line
            getline(inf, t_buffer);
            if(t_buffer.find("Score") != string::npos)
                t_userScore = atoi((t_buffer.substr(t_buffer.find(':')+1)).c_str());
            cout<<"User Total Score: "<<t_userScore<<endl;
            
            // Get user hand from next line
            getline(inf, t_buffer);
            if(t_buffer.find("Hand") != string::npos)
                t_buffer = t_buffer.substr(t_buffer.find(':')+1);
            // Parsing using space delimiter and updating user hand:
            istringstream line(t_buffer.c_str(), (int)t_buffer.size());
            while(line>>t_element)
            {
                t_userHand.push_back(new Card(asciiToRank(t_element),asciiToSuit(t_element)));
            }
            
            // Get user capture pile from next line
            // Variable to hold stacked cards in pile
            string t_pile;
            getline(inf, t_buffer);
            if(t_buffer.find("Capture Pile") != string::npos)
                t_buffer = t_buffer.substr(t_buffer.find(':')+1);
            // Parsing using space delimiter and updating computer capture:
            line = istringstream(t_buffer.c_str(), (int)t_buffer.size());
            // Get all cards (individual and stacked)
            while(line>>t_element)
            {
                // Stacked cards
                if(t_element.find('-') != string::npos)
                {
                    t_hyphens = true;
                    
                    // Reset counter
                    counter = 0;
                    
                    stringstream stackStream(t_element);
                    while(stackStream.good())
                    {
                        // Checking for empty parses
                        if(stackStream.rdbuf()->in_avail() == 0)
                            continue;
                        getline(stackStream, t_pile, '-');
                        t_cardBuffer.push_back(new Card(asciiToRank(t_pile), asciiToSuit(t_pile)));
                        counter++;
                    }
                    if(counter == 4)
                        t_userRoundScore++;
                }
                // Individual card
                else
                    t_cardBuffer.push_back(new Card(asciiToRank(t_element), asciiToSuit(t_element)));
                
                // Pushing the buffer vector of cards and clearing it
                t_userCapture.push_back(t_cardBuffer);
                t_cardBuffer.clear();
            }
        }
        
        // Get layout
        if(t_buffer.find("Layout") != string::npos)
        {
            // Variable to hold stacked cards in pile
            string t_pile;

            // Skip after :
            t_buffer = t_buffer.substr(t_buffer.find(':')+1);
                      
            // Parsing using space delimiter and updating computer capture:
            istringstream line(t_buffer.c_str(), (int)t_buffer.size());
            // Get all cards (individual and stacked)
            while(line>>t_element)
            {
            // Stacked cards
            if(t_element.find('-') != string::npos)
            {
                stringstream stackStream(t_element);
                              
                while(stackStream.good())
                {
                    // Checking for empty parses
                    if(stackStream.rdbuf()->in_avail() == 0)
                        continue;
                    getline(stackStream, t_pile, '-');
                    t_cardBuffer.push_back(new Card(asciiToRank(t_pile), asciiToSuit(t_pile)));
                }
            }
            // Individual card
            else
                t_cardBuffer.push_back(new Card(asciiToRank(t_element), asciiToSuit(t_element)));
                          
            // Pushing the buffer vector of cards and clearing it
            t_layout.push_back(t_cardBuffer);
            t_cardBuffer.clear();
            }
        }
        
        // Get stock pile, main deck:
        if(t_buffer.find("Stock Pile") != string::npos)
        {
            // Skip after :
            t_buffer = t_buffer.substr(t_buffer.find(':')+1);
            // Parsing using space delimiter and updating computer hand:
            istringstream line(t_buffer.c_str(), (int)t_buffer.size());
            while(line>>t_element)
            {
                t_deck.push_back(new Card(asciiToRank(t_element),asciiToSuit(t_element)));
            }
        }
        
        // Get player turn
        if(t_buffer.find("Next Player") != string::npos)
        {
            if(t_buffer.find("Computer") != string::npos)
                t_turn = 2;
            else
                t_turn = 1;
        }
    }
    /*
     END OF READING FROM GAME CONFIG FILE.
     READY TO INITIALIZE GAME ACCORDING TO CONFIG FILE
     */
    // Checking if initialization completed:
    if(t_userHand.size()==0 && t_compHand.size()==0 && t_layout.size()==0)
    {
        cerr<<"Unable to load game from the provided file."<<endl;
        cerr<<"Please try a configuration file with the saved game. Thank you."<<endl;
        exit(1);
    }
    

    // CONSTRUCTING GAME:
    // Player reconstruction
    m_user = new User(t_userHand, t_userCapture, t_userRoundScore);
    m_computer = new Computer(t_compHand, t_compCapture, t_compRoundScore);
    // Round initialization
    m_rounds = t_roundNum;
    // Score initialization:
    m_compScore = t_compScore;
    m_userScore = t_userScore;
    
    // Initializing round class and starting the round:
    Round* t_round = new Round(this, t_deck, t_layout);
    
    // Check if loaded game is a modulo suit:
    short t_temp = t_round->determineFirstPlayer();
    if(t_temp == 3)
    {
        cerr<<"The loaded game has a modulo suit."<<endl;
        cerr<<"Reshuffling deck and starting a new round."<<endl;
        return;
    }
    
    // Arranging and sorting capture piles:
    // If it is not a hyphenated config file
    if(t_hyphens == false)
    {
        m_user->arrangePile(t_round);
        m_computer->arrangePile(t_round);
    }
    m_user->sortCapturePile(t_round);
    m_computer->sortCapturePile(t_round);
    // Sorting hand pile
    m_user->sortHandPile(t_round);
    m_computer->sortHandPile(t_round);
    
    t_round->startRound(t_turn);
    
    // Updating total game score:
    m_userScore += m_user->getPlayerScore();
    m_compScore += m_computer->getPlayerScore();
 
    displayGameStats();

    // Asking user if they want to play again
    // Input validate?
    string temp;
    cout<< "Do you want to play another round of Go Stop?"<<endl;
    cout<<"1: Play another game."<<endl;
    cout<<"2: End Go Stop."<<endl;
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
    } while (t_playAgain<1 || t_playAgain>2);
    
    if(t_playAgain == 1)
        return;
    else
        endGoStop();

}


/* *********************************************************************
Function Name: displayGameStats
Purpose: Method to display game statistics.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Simple display function.
Assistance Received: NA
********************************************************************* */
void Game::displayGameStats()
{
    cout<<"Game Summary: "<<endl;
    cout<<"On this round..."<<endl;
    cout<<"User score: "<<m_user->getPlayerScore()<<endl;
    cout<<"Computer score: "<<m_computer->getPlayerScore()<<endl;
    cout<<"--------------------------"<<endl;
    cout<<"Total User score: "<<m_userScore<<endl;
    cout<<"Total Computer score: "<<m_compScore<<endl;
    
}

/* *********************************************************************
Function Name: asciiToRank
Purpose: Method to get card rank from its ASCII representation.
Parameters:
 a_string string ASCII representation of card
Return Value:
 CardRank The rank of the card
Local Variables: NA
Algorithm: Check to return the card rank.
Assistance Received: NA
********************************************************************* */
CardRank Game::asciiToRank(string a_string)
{
    char t_rank = a_string[0];
    switch(t_rank)
    {
        case '1':
            return ACE;
        case '2':
            return TWO;
        case '3':
            return THREE;
        case '4':
            return FOUR;
        case '5':
            return FIVE;
        case '6':
            return SIX;
        case '7':
            return SEVEN;
        case '8':
            return EIGHT;
        case '9':
            return NINE;
        case 'X':
            return TEN;
        case 'J':
            return JACK;
        case 'Q':
            return QUEEN;
        case 'K':
            return KING;
            
    }
    cerr<<"Cannot determine Card Rank from the file. Method asciiToRank"<<endl;
    exit(1);
}

/* *********************************************************************
Function Name: asciiToRank
Purpose: Method to get card suit from its ASCII representation.
Parameters:
 a_string string ASCII representation of card
Return Value:
 CardSuit The suit of the card
Local Variables: NA
Algorithm: Check to return the card rank.
Assistance Received: NA
********************************************************************* */
CardSuit Game::asciiToSuit(string a_string)
{
    char t_suit = a_string[1];
    
    switch(t_suit)
    {
        case 'H':
            return HEARTS;
        case 'C':
            return CLUBS;
        case 'S':
            return SPADES;
        case 'D':
            return DIAMONDS;
            
    }
    
    cerr<<"Cannot determine Card Suit from the file. Method asciiToSuit"<<endl;
    exit(1);
    
}


/* *********************************************************************
Function Name: endGoStop
Purpose: Method to end the game and declare winner.
Parameters: NA
Return Value: NA
Local Variables: NA
Algorithm: Checks score and displays the winner. Ends the game.
Assistance Received: NA
********************************************************************* */
void Game::endGoStop()
{
    // End game results:
    cout<<"****************************************************"<<endl;
    
    if(m_userScore>m_compScore)
        cout<<" YOU WIN THIS GAME OF GO STOP! CONGRATULATIONS! "<<endl;
    else if(m_userScore==m_compScore)
        cout<<" ------- THE GAME IS TIED --------"<<endl;
    else
        cout<<" COMPUTER WINS! BETTER LUCK NEXT TIME. "<<endl;
       
    cout<<"****************************************************"<<endl;
    displayGameStats();
    
    cout<<endl<<endl<<"--------------Thank you for playing Go Stop--------------"<<endl;
    
    exit(0);
}

/*
***************
DOXYGEN HEADER
***************
*/

/**
*  Game.cpp
*  Implementation of Game.hpp
*
*
*  Created by Salil Maharjan on 1/23/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/
