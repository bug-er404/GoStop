from Player import Player


class Computer(Player):
    """ Computer class to create a computer player for the game.

    Class to process Computer players. Uses Player Class.

    Attributes:


        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_num):
        super().__init__(a_num)
        self.__mName = "Computer " + str(a_num)

    # Accessors
    def getName(self):
        return self.__mName
