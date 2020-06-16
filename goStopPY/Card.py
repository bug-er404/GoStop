class Card(object):
    """Card class to create card objects.

    Class to define cards and its behaviors.

    Attributes:
        __mSuit: The suit of the card.
        __mRank: The rank of the card.
        CARDSUIT: A dictionary of all possible card suits.
        CARDRANK: A dictionary of all possible card ranks.

        Created by Salil Maharjan on 02/07/20.
        Copyright © 2019 Salil Maharjan. All rights reserved.
    """

    # CARD SUIT TYPES
    CARDSUIT = {
        1: 'HEARTS',
        2: 'CLUBS',
        3: 'SPADES',
        4: 'DIAMONDS'
    }

    # CARD RANKS
    CARDRANK = {
        1: 'ACE',
        2: 'TWO',
        3: 'THREE',
        4: 'FOUR',
        5: 'FIVE',
        6: 'SIX',
        7: 'SEVEN',
        8: 'EIGHT',
        9: 'NINE',
        10: 'TEN',
        11: 'JACK',
        12: 'QUEEN',
        13: 'KING'
    }

    def __init__(self, a_rank, a_suit):
        """ Card Class instantiation function

        Card class construction by member variable initialization according to
        the passed arguments.

        Args:
            a_rank: The rank of the card to be constructed.
            a_suit: The suit of the card to be constructed.

        Returns:
            NA

        Raises:
            NA
        """
        self.__mRank = self.CARDRANK[a_rank]
        self.__mSuit = self.CARDSUIT[a_suit]

    # Accessors
    def getRank(self):
        return self.__mRank

    def getSuit(self):
        return self.__mSuit

    def getCardName(self):
        return self.__mRank + self.__mSuit

    def displayImage(self):
        pass




    """Fetches rows from a Bigtable.

    Retrieves rows pertaining to the given keys from the Table instance
    represented by big_table.  Silly things may happen if
    other_silly_variable is not None.

    Args:
        big_table: An open Bigtable Table instance.
        keys: A sequence of strings representing the key of each table row
            to fetch.
        other_silly_variable: Another optional variable, that has a much
            longer name than the other args, and which does nothing.

    Returns:
        A dict mapping keys to the corresponding table row data
        fetched. Each row is represented as a tuple of strings. For
        example:

        {'Serak': ('Rigel VII', 'Preparer'),
         'Zim': ('Irk', 'Invader'),
         'Lrrr': ('Omicron Persei 8', 'Emperor')}

        If a key from the keys argument is missing from the dictionary,
        then that row was not found in the table.

    Raises:
        IOError: An error occurred accessing the bigtable.Table object.
    """

    # ************************************************************
    # * Name:  Salil Maharjan                                    *
    # * Project:  5 goStop Python                                *
    # * Class: CMPS366 Organization of Programming Languages     *
    # * Date:  02/07/2020                                        *
    # ************************************************************
    # *  Card class.
    # *
    # *  Created by Salil Maharjan on 02/07/20.
    # *  Copyright © 2019 Salil Maharjan. All rights reserved.

