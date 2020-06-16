from Player import Player


class User(Player):
    """ User class to create a user player for the game.

    Class to process User players. Uses Player Class.

    Attributes:


        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_num):
        super().__init__(a_num)
        self.__mName = "User " + str(a_num)

    # Accessors
    def getName(self):
        return self.__mName
