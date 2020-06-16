import random

import Card


class Deck(object):
    """Card class to create card objects.

    Class to define cards and its behaviors.

    Attributes:
        __mDeck: A deck of cards.
        __mTopCaard: The index of the top card of the deck
        __mDeckSize: The total deck size. (Using a standard DECKSIZE of 52)

        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    # Constant variable to hold standard deck size
    DECKSIZE = 52

    def __init__(self, a_deckNum, a_deck=None):
        """Deck class instantiation function.

        Deck class construction by member variable initialization according to
        the passed arguments.

        Args:
            a_deckNum: The number of decks to construct.
            a_deck: The list of cards in the deck to initialize as.
                    Passes when loading a configuration file.

        Returns:
            NA

        Raises:
            NA
        """
        # If config file is not passed, a new deck is created
        if a_deck is None:
            # Index of the "top card"
            self.__mTopCard = 0
            # Initializing the final deck and its size
            self.__mDeck = []
            self.__mDeckSize = int(a_deckNum) * self.DECKSIZE

            # Constructing a sorted deck
            for i in range(int(a_deckNum)):
                for j in range(self.DECKSIZE):
                    self.__mDeck.append(Card.Card((j % 13) + 1, (j % 4) + 1))

            random.shuffle(self.__mDeck)
        else:
            # Initialize top card, deck size and the deck:
            self.__mTopCard = 0
            self.__mDeckSize = len(a_deck)
            self.__mDeck = a_deck

    # Accessors
    def getDeckSize(self):
        return self.__mDeckSize

    def getTopCardIndex(self):
        return self.__mTopCard

    def getDeck(self):
        return self.__mDeck

    # Accessor to view the top card
    def viewTopCard(self):
        return self.__mDeck[self.__mTopCard]

    def getNewCard(self):
        """ Method to draw a new card.

        Draw new card from the top of the stock pile deck.
        The index of the top card is updated in each call.

        Args:
            NA

        Returns:
            Card The card on top of the deck

        Raises:
            NA
        """
        assert (self.__mTopCard < self.__mDeckSize)
        self.__mTopCard += 1
        return self.__mDeck[self.__mTopCard - 1]
