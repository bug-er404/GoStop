/*
************************************************************
* Name:  Salil Maharjan                                    *
* Project:  1 goStop C++                                   *
* Class: CMPS366 Organization of Programming Languages     *
* Date:  01/22/2020                                        *
************************************************************
*/

/**
*  main.cpp
*  Main entry for goStop project
*
*
*  Created by Salil Maharjan on 1/22/20.
*  Copyright © 2019 Salil Maharjan. All rights reserved.
*/

#include "PrefixHeader.pch"
#include "Game.hpp"

int main()
{
    // Creating instance of the Game and starting it
    Game* myGame = new Game();
    myGame->startGoStop();
}

/*
***************
DOXYGEN HEADER
***************
*/
