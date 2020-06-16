from tkinter import *
import tkinter.messagebox

import Round
import GUI
from User import User
from Computer import Computer


class Game:
    """ Game class to create and start a game of Go Stop.

    Main class to instantiate the game.

    Attributes:
        __mCompScore Total game score of computer player
        __mUserScore Total game score of user player
        __mRounds Total rounds played in the game


        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_mainframe):
        """ Game class instantiation method.

        Game class construction. Default behaviour only.

        Args:
            mainframe The main frame of the GUI.

        Returns:
            NA

        Raises:
            NA
        """
        # Round number initialization
        self.__mRounds = 0

        # Game player variable to hold the players objects
        self.__mPlayers = []

        # Initializing GUI window
        self.__mainWindow = GUI.GUI(a_mainframe)

        # Total score of each player

    # Accessors:
    def getRoundNumber(self):
        return self.__mRounds

    def getPlayers(self):
        return self.__mPlayers

    # def getUserScore(self):
    #     return self.__mUserScore
    #
    # def getCompScore(self):
    #     return self.__mCompScore

    def startGoStop(self):
        """ Method to start game of Go Stop.

        Start a game of Go stop. Main game handler method.
        Gives option to start a new game or to load a previous instance of game.

        Args:
            a_frame Main GUI window

        Returns:
            NA

        Raises:
            NA
        """
        # Get user input for game instance values
        # (# user/computer players and decks to use for the game)
        self.__mainWindow.initializeGame()
        self.startNewGame()

    def startNewGame(self):
        """ Method to start a new round of Go Stop.

        Start a new round of Go stop. Initializes the frame and players for the game.
        Determine first player by calling determineFirstPlayer method and by checking the scores of each player.
        Then initiates the round according to who the first player is.

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        # Open game window
        self.__mainWindow.openGameWindow()
        # Initializing user and computer players
        tPlayers = [User(i) for i in range(0, int(self.__mainWindow.getUserPlayers()))]
        for i in range(0, int(self.__mainWindow.getCompPlayers())):
            tPlayers.append(Computer(i))
        self.__mPlayers = tuple(tPlayers)

        # TESTING
        t_round = Round.Round(self, self.__mainWindow)

        # # Main round loop for playing again
        # while True:
        #     self.__mRounds += 1
        #
        #     # Clearing players hands and capture piles
        #     for i in self.__mPlayers:
        #         i.clearPile()
        #
        #     # Constructing round and skipping modulo suits
        #     while True:
        #         t_round = Round.Round(self)
        #         # Determine first player
        #         t_turn = t_round.determineFirstPlayer()
        #         t_turn = 1
        #         # Modulo suits check
        #         if t_turn == 3:
        #             continue
        #         else:
        #             break
        #
        # if self.__mRounds == 1 or self.__mUserScore == self.__mCompScore:
        #     # Initial round or tied score case, first player predetermined above:
        #     if t_turn == 1:
        #         tkinter.messagebox.showinfo(title="Player Decision",
        #                                     message="User has more higher rank cards. First player: User")
        #     else:
        #         tkinter.messagebox.showinfo(title="Player Decision",
        #                                     message="Computer has more higher rank cards. First player: Computer")
        # else:
        #     # Player with higher score plays in subsequent rounds
        #     if self.__mCompScore > self.__mUserScore:
        #         tkinter.messagebox.showinfo(title="Player Decision",
        #                                     message="Computer has higher score than user. First player: Computer")
        #     else:
        #         tkinter.messagebox.showinfo(title="Player Decision",
        #                                     message="User has higher score than user. First player: User")
        #
        #     # Starting round
        #     t_round.startRound(t_turn)
        #
        #     # Updating scores from the round
        #     self.__mUserScore += self.__mUser.getPlayerScore()
        #     self.__mCompScore += self.__mComputer.getPlayerScore()
        #
        #     self.displayGameStats();
        #
        #     # Asking user if they want to play again
        #     t_playAgain = tkinter.messagebox.askyesno(title="End of round...",
        #                                               message="Do you want to play another game?")
        #     if not t_playAgain:
        #         break;
        #
        # # End game
        # self.endGoStop()

    def displayGameStats(self):
        """ Method to display game statistics.

        End game result displaying method. Displays Round score and
        Total score of all players.

        Args:
            NA

        Returns:
            NA

        Raises:
            NA
        """
        pass
        # resultFrame = tkinter.Tk(className="Game Statistics")
        # resultFrame.geometry("500x250+450+250")
        # tkinter.Message(resultFrame, text="Game Summary: ", width=200, bg="red").grid(row=0, column=0)
        # tkinter.Message(resultFrame, text="On this round...", width=200, font="bold").grid(row=1, column=1)
        # tkinter.Message(resultFrame, text="User score: " + str(self.__mUser.getPlayerScore()), width=200).grid(row=2,
        #                                                                                                        column=2)
        # tkinter.Message(resultFrame, text="Computer score: " + str(self.__mComputer.getPlayerScore()), width=200).grid(
        #     row=3, column=2)
        # tkinter.Message(resultFrame, text="In the game... ", width=200, font="bold").grid(row=4, column=1)
        # tkinter.Message(resultFrame, text="Total User score: " + str(self.__mUserScore), width=200).grid(row=5,
        #                                                                                                  column=2)
        # tkinter.Message(resultFrame, text="Total Computer score: " + str(self.__mCompScore), width=200).grid(row=6,
        #                                                                                                      column=2)
        # tkinter.Message(resultFrame, text="Thank you for playing Go Stop!", width=200, font="italic").pack()
        #
        # resultFrame.mainloop()

    def endGoStop(self):
        """ Method to declare winner and end the game.
        Calls displayGameStats method to display the final scores before quiting program.

        Args:
            NA

        Returns:
            NA

        Raises:
            NA
        """
        pass
        # finalFrame = tkinter.Tk(className="End Game Result")
        # finalFrame.geometry("250x250+450+250")
        # if self.__mCompScore > self.__mUserScore:
        #     tkinter.Message(finalFrame, text=" YOU WIN THIS GAME OF GO STOP! CONGRATULATIONS! ", width=500,
        #                     bg="green").pack()
        # elif self.__mCompScore == self.__mUserScore:
        #     tkinter.Message(finalFrame, text=" THE GAME IS TIED! ", width=500, bg="yellow").pack()
        # else:
        #     tkinter.Message(finalFrame, text=" COMPUTER WINS! BETTER LUCK NEXT TIME. ", width=500, bg="red").pack()
        #
        # self.displayGameStats()
        #
        # quit()

    def loadSavedGame(self):
        """ Method to load a saved game of Go Stop.

        Start a saved round of Go stop.

        Args:
            NA

        Returns:
            NA

        Raises:
            NA
        """
        pass
