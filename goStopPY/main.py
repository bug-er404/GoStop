"""A program in python to play a card game of Go Stop.

Go Stop is a card game played by 2 players. The objective of this game is to score the most points after all the rounds.
The player earns one point for each arrangement of all four matching cards in the player's capture pile when the round ends.

  Typical usage example:

  foo = ClassFoo()
  bar = foo.FunctionBar()
"""
from tkinter import *

import Game

if __name__ == '__main__':

    # deck = Deck.Deck(2)
    # for i in range(deck.getDeckSize()):
    #     card = deck.getNewCard()
    #     print(i, "+", card.getRank(), card.getSuit())

    frame = Tk(className="Go Stop")
    #
    # frame. .showinfo(title="Player Decision",
    #                               message="Computer has higher score than user. First player: Computer")

    # Loading GUI main frame and starting game:
    myGame = Game.Game(frame)
    myGame.startGoStop()
    frame.mainloop()


