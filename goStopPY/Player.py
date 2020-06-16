class Player:
    """ Player class to create and instantiate a Player type for the game.

    Class to create players for the game. Main class further inherited by Computer and User class.

    Attributes:


        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_num):
        self.__mTotalScore = 0
        self.__mRoundScore = 0
        self.__mHand = []
        self.__mCapture = []

    # Accessors
    def getRoundScore(self):
        return self.__mRoundScore

    def getTotalScore(self):
        return self.__mTotalScore

    def getHandPile(self):
        return self.__mHand

    def getCapturePile(self):
        return self.__mCapture

    def clearPile(self):
        """ Method to clear player attributes

        Clears player's hand, capture piles and the score for new round.

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        self.__mHand.clear()
        self.__mCapture.clear()
        self.__mRoundScore = 0

    def handPlayerCard(self, a_card):
        self.__mHand.append(a_card)
