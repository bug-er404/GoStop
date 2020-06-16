from tkinter import *
import tkinter.messagebox
from PIL import ImageTk, Image

import Game
import Card


class GUI:
    """ GUI class to display graphical interface for a game of Go Stop.

    Main game window class.

    Attributes:


        Created by Salil Maharjan on 02/20/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    def __init__(self, a_mainframe):
        # Initializing GUI. Welcome screen.
        self.__mainframe = a_mainframe
        self.__mainframe.geometry("500x500+300+300")
        self.__mainframe.title("Go Stop by Salil Maharjan")
        Label(self.__mainframe, text="Welcome to the card game:\n Go Stop", font="Helvetica 16 bold", height=20).pack()

        # Game initializing variables
        self.__mTotalPlayers = 0
        self.__mUserPlayers = 0
        self.__mCompPlayers = 0
        self.__mNumOfDecks = 0

    # Accessors
    def getUserPlayers(self):
        return self.__mUserPlayers

    def getCompPlayers(self):
        return self.__mCompPlayers

    def getTotalPlayers(self):
        return self.__mTotalPlayers

    def getNumOfDecks(self):
        return self.__mNumOfDecks

    def initializeGame(self):
        """ Game beginning menu option method.

        Method to ask user if they want to start a new game or
        load a saved game from a config file.

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        Button(self.__mainframe, text="Start a new game!",
               command=lambda: self.totalPlayerInitializer()).pack()
        Button(self.__mainframe, text="Load game from file.",
               command=lambda: self.loadSavedGame()).pack()
        self.__mainframe.mainloop()

    def totalPlayerInitializer(self):
        """ GUI for getting total players in the game

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        # Clearing mainframe
        self.__mainframe.destroy()
        self.__mainframe = Tk(className="Go Stop")
        self.__mainframe.geometry("+300+300")
        self.__mainframe.title("Go Stop by Salil Maharjan")
        Label(self.__mainframe, text="Total players in the game: ").grid(row=0, column=0)
        totalPlayers = Spinbox(self.__mainframe, values=(2, 3, 4), state="readonly")
        totalPlayers.grid(row=0, column=1)
        Button(self.__mainframe, text="Next", command=lambda: self.userPlayerInitializer(totalPlayers.get())).grid(
            row=1)

    def userPlayerInitializer(self, a_totalPlayers):
        """ GUI for getting 2 input for number of decks to use
        and number of users playing the game.


        Args:
            a_totalPlayers Total players in the game

        Returns:
            NA

        Raises:
            NA
        """
        self.__mTotalPlayers = int(a_totalPlayers)
        # Get possible number of user player values
        userRange = []
        for i in range(1, (int(a_totalPlayers) + 1)):
            userRange.append(i)

        # Get new game configurations
        Label(self.__mainframe, text="Number of users playing the game: ").grid(row=0, column=0)
        userPlayers = Spinbox(self.__mainframe, values=userRange, state="readonly")
        userPlayers.grid(row=0, column=1)
        Label(self.__mainframe, text="Number of decks to use: ").grid(row=1, column=0)
        numDecks = Spinbox(self.__mainframe, values=(2, 3, 4), state="readonly")
        numDecks.grid(row=1, column=1)
        Button(self.__mainframe, text="Start game!", command=lambda: self.getSpinboxValues(userPlayers, numDecks)).grid(
            row=3)

    def getSpinboxValues(self, a_players, a_decks):
        """ Getter to get final Spinbox values.

        Gets the number of user players and number of decks
        to use for the game.

        Args:
            a_players Total players in the game
            a_user Total user players in the game
            a_decks Number of decks in the game

        Returns:
            NA

        Raises:
            NA
        """
        self.__mUserPlayers = a_players.get()
        self.__mCompPlayers = (self.__mTotalPlayers - int(a_players.get()))
        self.__mNumOfDecks = a_decks.get()
        self.__mainframe.destroy()

    def openGameWindow(self):
        """ Method to open a new game window of Go Stop.

        Start a new round of Go stop. Initializes the frame for the round.

        Args:

        Returns:
            NA

        Raises:
            NA
        """
        self.__mainframe = Tk(className="Go Stop")
        self.__mainframe.geometry("1600x1000")
        tkinter.messagebox.showinfo(title="Loading game", message="Please wait for the game to load...")

    def displayBoard(self, a_game):
        # Image folder
        imgFolder = "img/"

        # Each player's frame on the GUI
        playerFrame = []

        # Iterators for row/column grid indexing
        i = 0
        j = 0

        # Display hand and capture for each player:
        for player in (a_game.getPlayers()):
            # Player Details
            Label(self.__mainframe, text="Player: " + player.getName()).grid(row=i, column=j, ipadx=2)
            Label(self.__mainframe, text="Round Score: " + str(player.getRoundScore())).grid(row=i + 1, column=j,
                                                                                             ipadx=2)
            Label(self.__mainframe, text="Total Score: " + str(player.getTotalScore())).grid(row=i + 2, column=j,
                                                                                             ipadx=2, pady=2)

            j += 2
            # Player Hand
            Label(self.__mainframe, text="Hand Pile").grid(row=i, column=j, padx=2)
            i += 1
            for card in (player.getHandPile()):
                t_frame = Frame(self.__mainframe, width=65, height=95)
                t_frame.grid(row=i, column=j)
                # Card Image
                imgDir = imgFolder + card.getCardName() + ".jpg"
                image = Image.open(imgDir)
                photo = ImageTk.PhotoImage(image)

                # Card Button
                t_button = Button(t_frame, image=photo, border=0)
                t_button.grid(row=i, column=j)

                # Next column\
                t_frame.image = photo
                j += 1

                playerFrame.append(t_frame)

            # Player Capture
            j += 2
            Label(self.__mainframe, text="Capture Pile").grid(row=i - 1, column=j, padx=2)
            scrollFrame = Frame(self.__mainframe, width=65, height=95)
            scrollFrame.grid(row=i, column=j)

            captureScroll = Scrollbar(scrollFrame, orient=HORIZONTAL) #, command=scrollCanvas.xview

            captureList = Listbox(scrollFrame)  # xscrollcommand=captureScroll.set

            # scrollCanvas = Canvas(scrollFrame, width=65, height=95)
            # scrollCanvas.grid(row=i, column=j)

            captureScroll.pack(side=BOTTOM, fill=Y)
            captureList.pack(side=TOP, fill=BOTH)
            captureScroll.config(command=captureList.xview)
            captureList.config(xscrollcommand=captureScroll.set)

            for card in (player.getHandPile()):
                t_frame = Frame(captureList, width=65, height=95)
                t_frame.grid(row=i, column=j)
                # Card Image
                imgDir = imgFolder + card.getCardName() + ".jpg"
                image = Image.open(imgDir)
                photo = ImageTk.PhotoImage(image)

                # Card Button
                t_button = Button(t_frame, image=photo, border=0)
                t_button.grid(row=i, column=j)
                captureList.insert(END, t_frame)

                t_frame.image = photo
                # Next column
                j += 1

            for card in (player.getHandPile()):
                t_frame = Frame(captureList, width=65, height=95)
                t_frame.grid(row=i, column=j)
                # Card Image
                imgDir = imgFolder + card.getCardName() + ".jpg"
                image = Image.open(imgDir)
                photo = ImageTk.PhotoImage(image)

                # Card Button
                t_button = Button(t_frame, image=photo, border=0)
                t_button.grid(row=i, column=j)
                captureList.insert(END, t_frame)

                t_frame.image = photo
                # Next column
                j += 1

            # scrollCanvas.config(xscrollcommand=captureScroll.set)
            # Reinitializing column and moving to next row
            i += 3
            j = 0
