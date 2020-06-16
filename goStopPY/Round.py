import Deck
import Game
import Card
import GUI


class Round:
    """ Round class to create and start a round of the game Go stop.

    Class to process rounds instantiated by the Game class.

    Attributes:


        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_game: Game, a_gui):
        """ Round class instantiation method.

        Round class construction.

        Args:
            a_game The main game from where the round was called.

        Returns:
            NA

        Raises:
            NA
        """
        # Recording game instance
        self.__mGame = a_game

        # Recording game frame
        self.__mRoundFrame = a_gui

        # Initializing the initialized number deck of card
        self.__mStockPile = Deck.Deck(self.__mRoundFrame.getNumOfDecks())

        # Initializing layout
        self.__mLayout = []

        # Dealing cards in an interwoven fashion
        # according to the rules of the game
        self.dealCards(self.__mGame.getPlayers(), 5)
        for i in range(0, 4):
            self.__mLayout.append(self.__mStockPile.getNewCard())
        self.dealCards(self.__mGame.getPlayers(), 5)
        for i in range(0, 4):
            self.__mLayout.append(self.__mStockPile.getNewCard())

        self.__mRoundFrame.displayBoard(self.__mGame)

    def dealCards(self, a_player, a_cards):
        """ Method to deal cards in the game.

        Deals a_cards number of cards to a_player

        Args:
            a_player Players to deal cards to
            a_cards The number of cards to deal

        Returns:
            NA

        Raises:
            NA
        """
        for player in a_player:
            for i in range(a_cards):
                player.handPlayerCard(self.__mStockPile.getNewCard());

    def determineFirstPlayer(self):
        """ Method to determine first player at the start of each round.

        Each player has 10 cards at the beginning of a round. A single iteration is done to
        record all the instances of cards in a dictionary and then is compared to see who has
        the highest high rank cards.

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        # tCardMap = []
        # temp = {}
        # # variable to hold hand of each players
        # tHand = []
        # # Recording card and instances for each player
        # tNumOfPlayers = len(self.__mGame.getPlayers())
        # for player in (self.__mGame.getPlayers()):
        #     tHand.append(player.getHandPile())
        # for i in range(10):
        #     for hand in tHand:
        pass

    def startRound(self, a_round):
        pass

    # def displayBoard(self):
    #     # Image folder
    #     imgFolder = "img/"
    #
    #     # Each player's frame on the GUI
    #     playerFrame = []
    #
    #     # Iterators for row/column grid indexing
    #     i = 0
    #     j = 0
    #     for player in (self.__mGame.getPlayers()):
    #         Label(self.__mRoundFrame, text="Player: " + player.getName()).pack()
    #         for card in (player.getHandPile()):
    #             t_frame = Frame(self.__mRoundFrame, width=90, height=130)
    #             t_frame.grid(row=i, column=j)
    #             # Card Image
    #             imgDir = imgFolder + card.getCardName() + ".jpg"
    #             print(imgDir)
    #             image = Image.open(imgDir)
    #             photo = ImageTk.PhotoImage(image)
    #
    #             # Card Button
    #             t_button = Button(t_frame, image=photo, border=0)
    #             t_button.grid(row=i, column=j)
    #
    #             # Next column
    #             t_frame.image = photo
    #             j += 1
    #
    #             playerFrame.append(t_frame)
    #
    #         # Reinitializing column and moving to next row
    #         i += 1
    #         j = 0


