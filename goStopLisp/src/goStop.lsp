; /* *********************************************
; Source Code for starting Go Stop
; ********************************************* */
(DEFUN GoStop ()
  (TERPRI)
  (TERPRI)
  (PRINC "********************* ")
  (PRINC " Welcome to the game of Go Stop ")
  (PRINC " *********************")
  (TERPRI)
  (WRITE-LINE "MENU OPTIONS: ")
  (WRITE-LINE "1: START NEW GAME")
  (WRITE-LINE "2: LOAD SAVED GAME")
  (PRINC "********************* ")
  (PRINC "--------------------------------")
  (PRINC " *********************")
  (TERPRI)
  (PRINC ":")
  (LET (
         (menuInput (validateInput (READ) 1 2)))
    (COND
      ( (= menuInput 1)
        (TERPRI)
        (WRITE-LINE "Starting new game...")
        (TERPRI)
        (startGame (LIST 1) 0))
      ( (= menuInput 2)
        (TERPRI)
        (WRITE-LINE "Loading saved game...")
        (TERPRI)
        (startGame (loadSavedGame) 1)))))


; /* *********************************************
; Source Code User Input Validation
; ********************************************* */
(DEFUN validateInput(num lBound uBound)
  (COND
    ( (LISTP num)
      (WRITE-LINE "INVALID INPUT! Input cannot be a list.")
      (LET ( (userInput (READ)))
        (validateInput userInput lBound uBound)))
    ( (NOT (NUMBERP num))
      (WRITE-LINE "INVALID INPUT! Number input required.")
      (LET ( (userInput (READ)))
        (validateInput userInput lBound uBound)))
    ( (< num lBound)
      (WRITE-LINE "INVALID INPUT! Number below range.")
      (LET ( (userInput (READ)))
        (validateInput userInput lBound uBound)))
    ( (> num uBound)
      (WRITE-LINE "INVALID INPUT! Number above range.")
      (LET ( (userInput (READ)))
        (validateInput userInput lBound uBound)))
    ( t num)))

; /* *********************************************
; Source Code to load saved game from config file.
; ********************************************* */
(DEFUN loadSavedGame()
  (TERPRI)
  (WRITE-LINE "Enter name of config file to load game from: ")
  (LET*
    ( (in (OPEN (READ) :if-does-not-exist NIL))
      (data ( COND
              ( IN (READ IN))
              (t
                (WRITE-LINE "CANNOT OPEN FILE. PLEASE TRY AGAIN.")
                (loadSavedGame)))))
    (COND
      ( (NULL IN)
        NIL)
      ( t
        (CLOSE IN)))
    data))

; /* *************************************************
; Source Code to start a new game or load saved game.
; **************************************************** */
(DEFUN startGame (aGame aOption)
  (COND
    ; New game.
    ( (= aOption 0)
      (LET*
        ( (newGame (initiateRound aGame))
          (playerTurn (determineFirstPlayer newGame))
          (board1 (setCompTotalScore newGame 0))
          (board2 (setUserTotalScore board1 0))
          (mGame (setNextPlayer board2 playerTurn)))
        (COND
        ; Modulo suit. Restarting round.
          ( (= playerTurn 2)
            (displayBoard mGame)
            (startGame aGame))
          ( t
            (startRound mGame playerTurn)))))

    ; Loading saved game with loaded board.
    ( (= aOption 1)
      (LET*
        ( (board1 (processSavedGame aGame))
          (board2 (setCompRoundScore board1 (calculatePlayerScore (getComputerCapture board1))))
          (mGame (setUserRoundScore board2 (calculatePlayerScore (getUserCapture board2))))
          (playerTurn (getPlayerTurn mGame)))
        (TERPRI)
        (WRITE-LINE "Game loaded!")
        (WRITE-LINE "Scores from previous game:")
        (PRINC "Computer Total: ")
        (PRINC (getCompTotalScore mGame))
        (TERPRI)
        (PRINC "User Total: ")
        (PRINC (getUserTotalScore mGame))
        (TERPRI)
        (startRound mGame playerTurn)))

    ; Consecutive rounds.
    ( (= aOption 2)
      (LET*
        ( (turnWscore (getHigherScorePlayer aGame))
          (newGame (initiateRound (LIST (+ (getRoundNumber aGame) 1))))
          (board (setCompTotalScore newGame (getCompTotalScore aGame)))
          (mGame (setUserTotalScore board (getUserTotalScore aGame))))
        (COND
          ; Same score
          ( (= turnWscore 2)
            (LET
              ( (turnWhand (determineFirstPlayer mGame)))
              (startRound (setNextPlayer mGame turnWhand) turnWhand)))
          ( t
            (startRound (setNextPlayer mGame turnWscore) turnWscore)))))))



; Function to initiate a round of Go Stop
(DEFUN initiateRound(mGame)
  (LET* ( (mDeck (shuffle_deck(make_deck 2)))
  ; Distributing cards in interwoven pattern
         (comp1 (handCard 5 mDeck))
         (user1 (handCard 5 (removeHandedCards 5 mDeck)))
         (layout1 (handCard 4 (removeHandedCards 10 mDeck)))
         (mComputer (APPEND comp1 (handCard 5 (removeHandedCards 14 mDeck))))
         (mUser (APPEND user1 (handCard 5 (removeHandedCards 19 mDeck))))
         (mLayout (APPEND layout1 (handCard 4 (removeHandedCards 24 mDeck)))))
      ( APPEND
        mGame
        (LIST mComputer () (LIST 0) (LIST 0))
        (LIST mUser () (LIST 0) (LIST 0))
        (LIST mLayout)
        (LIST (removeHandedCards 28 mDeck))
        (LIST ()))))

; Function to process a saved game configurations
(DEFUN processSavedGame (aGame)
  (LET*
    ( (round (LIST (FIRST aGame)))
      (compScore (LIST (FIRST (REST aGame))))
      (compHand (LIST (FIRST (REST (REST aGame)))))
      (compCapture (LIST (FIRST (REST (REST (REST aGame))))))
      (userScore (LIST (FIRST (REST (REST (REST (REST aGame)))))))
      (userHand (LIST (FIRST (REST (REST (REST (REST (REST aGame))))))))
      (userCapture (LIST (FIRST (REST (REST (REST (REST (REST (REST aGame)))))))))
      (layout (LIST (FIRST (REST (REST (REST (REST (REST (REST (REST aGame))))))))))
      (stockPile (LIST (FIRST (REST (REST (REST (REST (REST (REST (REST (REST aGame)))))))))))
      (nextPlayerName (FIRST (REST (REST (REST (REST (REST (REST (REST (REST (REST aGame)))))))))))
      (nextPlayer (COND
                      ( (STRING-EQUAL nextPlayerName "Human")
                        (LIST 1))
                      ( t
                        (LIST 0)))))

    ; Get score by looking at capture piles of each player
    (APPEND
      round compHand compCapture (LIST 0) compScore userHand userCapture (LIST 0) userScore layout stockPile nextPlayer)))

; /* *************************************************
; Source Code to determine first player according to
; the specification and check for modulo suits.
; **************************************************** */
(DEFUN determineFirstPlayer(aGame)
  (LET*
    ( (cardOrder (LIST 'KH 'QH 'JH 'XH '9H '8H '7H '6H '5H '4H '3H '2H '1H))
      (playerTurn (getHigherRankPlayer cardOrder aGame)))
    (COND
      ; Computer First player
      ( (= (FIRST playerTurn) 0)
        (WRITE-LINE "---------------------------------")
        (WRITE-LINE "FIRST PLAYER: COMPUTER!")
        (PRINC "Determining Card: ")
        (PRINC (CHAR (STRING (NTH 1 playerTurn)) 0))
        (TERPRI)
        (WRITE-LINE "---------------------------------")
        0)
      ; User First player
      ( (= (FIRST playerTurn) 1)
        (WRITE-LINE "---------------------------------")
        (WRITE-LINE "FIRST PLAYER: USER!")
        (PRINC "Determining Card: ")
        (PRINC (CHAR (STRING (NTH 1 playerTurn)) 0))
        (TERPRI)
        (WRITE-LINE "---------------------------------")
        1)
      ; Modulo suit.
      ( (= (FIRST playerTurn) 2)
        (WRITE-LINE "---------------------------------")
        (WRITE-LINE "MODULO SUIT FOUND.")
        (WRITE-LINE "RESHUFFLING CARDS...")
        (WRITE-LINE "RESTARTING ROUND......")
        (WRITE-LINE "---------------------------------")
        2))))

; Function to get higher rank player
; 0 for computer, 1 for user, 2 for modulo suit
(DEFUN getHigherRankPlayer (aOrder aGame)
  (LET
    ( (compHandCount (countCards (FIRST aOrder) (NTH 1 aGame)))
      (userHandCount (countCards (FIRST aOrder) (NTH 5 aGame))))
    (COND
      ( (< compHandCount userHandCount)
        (LIST 1 (FIRST aOrder)))
      ( (> compHandCount userHandCount)
        (LIST 0 (FIRST aOrder)))
      ( t
        (getHigherRankPlayer (REST aOrder) aGame)))))

; Function to determine player with higher score
(DEFUN getHigherScorePlayer (aGame)
  (COND
  ; User higher score. First player.
    ( (> (getUserTotalScore aGame) (getCompTotalScore aGame))
      1)
  ; Computer higher score. First player.
    ( (< (getUserTotalScore aGame) (getCompTotalScore aGame))
      0)
  ; Same score. First player according to specification new game.
    ( t
      2)))

; Function to count 'aCard' in 'aPile'
(DEFUN countCards (aCard aPile)
  (COND
    ( (< (LENGTH aPile) 1)
      0)
    ( t
      (+ (checkCard aCard (FIRST aPile)) (countCards aCard (REST aPile))))))

; Function to check if cards match.
; * Only checks face value *
(DEFUN checkCard (aCard1 aCard2)
  (COND
    ( (EQ (CHAR (STRING aCard1) 0) (CHAR (STRING aCard2) 0))
      1)
    (t
      0)))

; Function to check both face and suit of a card
; if they are matching.
(DEFUN checkSuitFace (aCard1 aCard2)
  (COND
    ( ( AND (EQ (CHAR (STRING aCard1) 0) (CHAR (STRING aCard2) 0)) (EQ (CHAR (STRING aCard1) 1) (CHAR (STRING aCard2) 1)))
      1)
    ( t
      0)))

; /* ***************************************************
; Source Code to play each round in the game
; until the user quits or both player runs out of cards.
; ****************************************************** */
(DEFUN startRound(aGame aPlayerTurn)
  (COND
    ( (AND (= 0 (LENGTH (getComputerHand aGame))) (= 0 (LENGTH (getUserHand aGame))))
      (displayBoard aGame)
      (TERPRI)
      (WRITE-LINE "End of round....")
      (WRITE-LINE "Players have no more cards on hand.")
      (LET*
        ( (updateGame1 (setCompTotalScore aGame (+ (getCompTotalScore aGame) (getCompRoundScore aGame))))
          (updatedGame (setUserTotalScore updateGame1 (+ (getUserTotalScore updateGame1) (getUserRoundScore updateGame1)))))
        (gameStats updatedGame)
        (WRITE-LINE "Do you want play another game?")
        (WRITE-LINE "1: Play Again!")
        (WRITE-LINE "2: Quit Game.")
        (COND
          ( ( = (validateInput (READ) 1 2) 1)
            (startGame updatedGame 2))
          ( t
            (endGame)))))
    ( t
      (displayBoard aGame)
      (playTurn aGame aPlayerTurn))))

; Function to end game.
(DEFUN endGame ()
  (TERPRI)
  (WRITE-LINE "************************************************************************************")
  (WRITE-LINE "------------------------  Thank you for playing Go Stop! ---------------------------")
  (WRITE-LINE "************************************************************************************")
  (TERPRI))


; Function to display game statistics
(DEFUN gameStats (aGame)
  (TERPRI)
  (WRITE-LINE ">>>>>>>>>>>>>>>>>>>>>>>>>>")
  (WRITE-LINE "GAME SUMMARY...")
  (WRITE-LINE "On last round:")
  (PRINC "User score: ")
  (PRINC (getUserRoundScore aGame))
  (TERPRI)
  (PRINC "Computer score: ")
  (PRINC (getCompRoundScore aGame))
  (TERPRI)
  (WRITE-LINE "..........................")
  (WRITE-LINE "Game total:")
  (PRINC "User score: ")
  (PRINC (getUserTotalScore aGame))
  (TERPRI)
  (PRINC "Computer score: ")
  (PRINC (getCompTotalScore aGame))
  (TERPRI)
  (WRITE-LINE ">>>>>>>>>>>>>>>>>>>>>>>>>>")
  (COND
    ( (> (getUserTotalScore aGame) (getCompTotalScore aGame))
      (WRITE-LINE "USER PLAYER WINS THE GAME!!!"))
    ( (< (getUserTotalScore aGame) (getCompTotalScore aGame))
      (WRITE-LINE "COMPUTER PLAYER WINS THE GAME!"))
    ( t
      (WRITE-LINE "GAME IS TIED...")))
  (WRITE-LINE ">>>>>>>>>>>>>>>>>>>>>>>>>>")
  (TERPRI))

; Function to play a turn by 'aPlayer'
(DEFUN playTurn (aGame aPlayerTurn)
  (COND
    ; Computer turn
    ( (= aPlayerTurn 0)
      (LET
        ( (userInput (getMenuInput aPlayerTurn)))
        (COND
          ( (= userInput 1)
            (playHand aGame aPlayerTurn))
          ( (= userInput 2)
            (saveGame aGame))
          ( (= userInput 3)
            (gameStats aGame)
            (endGame)))))
      ; Play from stockpile
      ; Make necessary updates: score piles.

    ; User turn
    ( (= aPlayerTurn 1)
      (LET
        ( (userInput (getMenuInput aPlayerTurn)))
        (COND
          ( (= userInput 1)
            (playHand aGame aPlayerTurn))
          ( (= userInput 2)
            (saveGame aGame))
          ( (= userInput 3)
            (LET
              ( (getHelp (pickSuggestion (getUserHand aGame) (getLayout aGame) (getUserCapture aGame))))
              (playHand aGame aPlayerTurn)))
          ( (= userInput 4)
            (gameStats aGame)
            (endGame)))))))

; Function for menu option input for each round
(DEFUN getMenuInput (aPlayerTurn)
  (COND
    ( (= aPlayerTurn 0)
      (WRITE-LINE "--------------")
      (WRITE-LINE "COMPUTER TURN")
      (WRITE-LINE "--------------")
      (WRITE-LINE "1: PLAY TURN")
      (WRITE-LINE "2: SAVE GAME")
      (WRITE-LINE "3: QUIT GAME")
      (WRITE-LINE "--------------")
      (validateInput (READ) 1 3))
    ( (= aPlayerTurn 1)
      (WRITE-LINE "--------------")
      (WRITE-LINE "USER TURN")
      (WRITE-LINE "--------------")
      (WRITE-LINE "1: PLAY TURN")
      (WRITE-LINE "2: SAVE GAME")
      (WRITE-LINE "3: HELP MODE")
      (WRITE-LINE "4: QUIT GAME")
      (WRITE-LINE "--------------")
      (validateInput (READ) 1 4))))

; /* ***************************************************
; Source Code to save the game
; ****************************************************** */
(DEFUN saveGame(aGame)
  (WRITE-LINE "Game saved successfully! ")
  (endGame)
  (LET*
    ( (nextPlayer (COND
                    ( (= (getPlayerTurn aGame) 1)
                      (LIST "Human"))
                    ( t
                      (LIST "Computer"))))
      (config (APPEND (LIST(getRoundNumber aGame)) (LIST (getCompTotalScore aGame)) (LIST(getComputerHand aGame)) (LIST(getComputerCapture aGame)) (LIST (getUserTotalScore aGame)) (LIST(getUserHand aGame)) (LIST(getUserCapture aGame)) (LIST(getLayout aGame)) (LIST(getStockPile aGame)) nextPlayer)))
    (WITH-OPEN-FILE (OUTPUT "./save_file.txt" :direction :output :if-exists :supersede)
      (FORMAT OUTPUT "~a" config))))

; /* ***************************************************
; Source Code to play card from hand.
; ****************************************************** */
; Function to check layout and get best hand card choice
(DEFUN playHand (aGame aPlayerTurn)
  (COND
    ; Computer play
    ( (= aPlayerTurn 0)
      (LET*
        ( (compMove (pickSuggestion (getComputerHand aGame) (getLayout aGame) (getComputerCapture aGame)))
          (board1 (addToLayout aGame compMove aPlayerTurn))
          (board2 (setComputerHand board1 (removeCard (+ (getCardIndexStrict (FIRST compMove) (getComputerHand board1)) 1) (getComputerHand board1))))
          (display (displayBoard board2))
          (board3 (playStockPile board2 compMove aPlayerTurn))
          (board4 (setCompRoundScore board3 (calculatePlayerScore (getComputerCapture board3)))))
        (startRound (setNextPlayer board4 1) 1)))
    ; User play
    ( (= aPlayerTurn 1)
      (LET*
        ( (cardOrder (LIST 'KH 'QH 'JH 'XH '9H '8H '7H '6H '5H '4H '3H '2H '1H))
          (userCard (getUserCardChoice (sortPile (getUserHand aGame) cardOrder) 1 (LENGTH (getUserHand aGame)) (sortPile (getUserHand aGame) cardOrder)))
          (userMove (APPEND (LIST userCard) (LIST (countCards userCard (getLayout aGame)))))
          (board1 (addToLayout aGame userMove aPlayerTurn))
          (board2 (setUserHand board1 (removeCard (+ (getCardIndexStrict (FIRST userMove) (getUserHand board1)) 1) (getUserHand board1))))
          (display (displayBoard board2))
          (userInput (getDrawInput aGame))
          (board3 (playStockPile board2 userMove aPlayerTurn))
          (board4 (setUserRoundScore board3 (calculatePlayerScore (getUserCapture board3)))))
        (startRound (setNextPlayer board4 0) 0)))))

; Function to get drawing input from user to draw from stock pile
(DEFUN getDrawInput(aGame)
  (WRITE-LINE "MENU OPTIONS: ")
  (WRITE-LINE "1: DRAW FROM STOCK PILE")
  (WRITE-LINE "2: SAVE & QUIT GAME")
  (COND
     ( (= (validateInput (READ) 1 2) 1)
       1)
    ( t
      (saveGame aGame)
      (QUIT))))

; /* ***************************************************
; Source Code to get suggestion for picking card.
; ****************************************************** */
(DEFUN pickSuggestion(aHand aLayout aCapture)
  (LET
    ( (draw (handCardHelp aHand aLayout aCapture))
      (randomDraw (LIST (NTH (RANDOM (LENGTH aHand) (MAKE-RANDOM-STATE t)) aHand) 0)))
    (COND
      ( (= (LENGTH draw) 1)
        (WRITE-LINE "There are no matches on the layout from your hand. You can choose to play any card.")
        (PRINC "A recommendation: ")
        (PRINC (FIRST randomDraw))
        (TERPRI)
        randomDraw)
      ( t
        (COND
          ( (OR (= (LENGTH draw) 3) (>= (NTH 1 draw) 3))
            draw)
          ( t
            (WRITE-LINE "You can create a stacked pair.")
            (PRINC "Recommended Card: ")
            (PRINC (FIRST draw))
            (TERPRI)
            draw))))))

; Function to get suggestion for hand card play.
(DEFUN handCardHelp (aHand aLayout aCapture)
  (LET
    ( (matchCount (countCards (FIRST aHand) aLayout)))
    (COND
      ; Exhaustion case
      ( (= (LENGTH aHand) 0)
        (LIST 0))
      ; Triple capture case
      ( (>= matchCount 3)
        (WRITE-LINE "TRIPLE CAPTURE FOUND!")
        (PRINC "RECOMMENDED CARD: ")
        (PRINC (FIRST aHand))
        (TERPRI)
        (LIST (FIRST aHand) matchCount))
      ; Two/one match case
      ( (OR (= matchCount 2) (= matchCount 1))
        (COND
          ; Complete a set of capture case
          ( (= (MOD (countCards (FIRST aHand) aCapture) 4) 2)
            (WRITE-LINE "A STACK CAN BE COMPLETED ON YOUR CAPTURE!")
            (PRINC "RECOMMENDED CARD: ")
            (PRINC (FIRST aHand))
            (TERPRI)
            (LIST (FIRST aHand) matchCount 1))
          ; Checking rest of cards in hand
          ( (LET
              ( (rMatches (handCardHelp (REST aHand) aLayout aCapture)))
              (COND
                ; No other matches
                ( (= (LENGTH rMatches) 1)
                  (LIST (FIRST aHand) matchCount))
                ; Other matches found
                ( t
                  ( COND
                    ; If triple stack or stack that completes set on capture is found:
                    ( (OR (> (LENGTH rMatches) 2) (> (NTH 1 rMatches) 2))
                      rMatches)
                    ( t
                      ; (WRITE-LINE "YOU CAN BUILD A STACKED PAIR!")
                      ; (PRINC "RECOMMENDED CARD: ")
                      ; (PRINC (FIRST aHand))
                      ; (TERPRI)
                      (LIST (FIRST aHand) matchCount)))))))))
      ; No match case
      ( (= matchCount 0)
        (handCardHelp (REST aHand) aLayout aCapture)))))

; /* ***************************************************
; Function to add 'aDraw' card to layout from a 'aPile'
; ****************************************************** */
; Capture is made from front to end. Stacked cards are appended at the end but
; during the time of the capture, the reverse of the list is passed so the capture is made.
; Triple stacks are immediately captured in the same fashion.
(DEFUN addToLayout(aGame aDraw aPlayerTurn)
  (TERPRI)
  (WRITE-LINE "......................")
  (PRINC "PICKED CARD: ")
  (PRINC (FIRST aDraw))
  (TERPRI)
  (PRINC "LAYOUT MATCHES: ")
  (PRINC (NTH 1 aDraw))
  (TERPRI)
  (PRINC "POSSIBLE CAPTURES: ")
  (PRINC (getCardMatchList (FIRST aDraw) (getLayout aGame)))
  (TERPRI)
  (WRITE-LINE "......................")
  (COND
      ; Adding to layout. No matches or an empty layout.
    ( (OR (NULL (getLayout aGame)) (= (NTH 1 aDraw) 0))
      (setLayout aGame (APPEND (getLayout aGame) (LIST (FIRST aDraw)))))
      ; Creating stack pair. One match.
    ( (= (NTH 1 aDraw) 1)
      (LET
        ( (aLayout (APPEND (getLayout aGame) (APPEND (LIST (NTH (getCardIndex (FIRST aDraw) (getLayout aGame)) (getLayout aGame))) (LIST (FIRST aDraw))))))
        (setLayout aGame (removeCard (+ (getCardIndex (FIRST aDraw) aLayout) 1) aLayout))))
      ; Two matches:
    ( (= (NTH 1 aDraw) 2)
      (COND
        ( (= aPlayerTurn 0)
          (LET
            ( (aLayout (APPEND (getLayout aGame) (APPEND (LIST (NTH (getCardIndex (FIRST aDraw) (getLayout aGame)) (getLayout aGame))) (LIST (FIRST aDraw))))))
            (setLayout aGame (removeCard (+ (getCardIndex (FIRST aDraw) aLayout) 1) aLayout))))
        ( (= aPlayerTurn 1)
          (WRITE-LINE "Pick a card to pair with on the layout: ")
          (LET*
            (
              (userCard (getUserCardChoice (getCardMatchList (FIRST aDraw) (getLayout aGame)) 1 2 (getCardMatchList (FIRST aDraw) (getLayout aGame))))
              (aLayout (APPEND (getLayout aGame) (APPEND (LIST userCard) (LIST (FIRST aDraw))))))
            (setLayout aGame (removeCard (+ (getCardIndex userCard aLayout) 1) aLayout))))))
      ; Three or more matches
      ; H3 Capture and make a complete set of 4
    ( t
      (LET
        ( (newGame (setLayout aGame (CONS (FIRST aDraw) (getLayout aGame)))))
        (captureFromLayout newGame (getCardMatchList (FIRST aDraw) (getLayout newGame)) 4 aPlayerTurn)))))
    ; MAKE CAPTURES FROM END-----

; /* ***************************************************
; Function to play from stock pile.
; ****************************************************** */
(DEFUN playStockPile (aGame aHandMove aPlayerTurn)
  ; Draw card from stock pile and update stockpile.
  (LET*
    ( (draw (handCard 1 (getStockPile aGame)))
      (newStockPile (removeHandedCards 1 (getStockPile aGame)))
      (matchCount (countCards (FIRST draw) (getLayout aGame))))
    (TERPRI)
    (WRITE-LINE "............................")
    (WRITE-LINE "DRAWING FROM STOCKPILE....")
    (PRINC "PLAYER DREW: ")
    (PRINC (FIRST draw))
    (TERPRI)
    (PRINC "LAYOUT MATCHES: ")
    (PRINC matchCount)
    (TERPRI)
    (PRINC "POSSIBLE CAPTURES: ")
    (PRINC (getCardMatchList (FIRST draw) (getLayout aGame)))
    (TERPRI)
    (PRINC ".................................")
    (TERPRI)
    (COND
        ; No matches - Adding to layout.
      ( (= matchCount 0)
        (COND
          ; Capturing previous stacked pairs in H1/H2 case:
          ( (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2))
            (LET
              ( (newGame (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile)))
              (captureFromLayout newGame (getCardMatchList (FIRST aHandMove) (REVERSE (getLayout newGame))) 2 aPlayerTurn)))
          ( t
            (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile))))

        ; One match.
      ( (= matchCount 1)
        (COND
          ; Checking for previous stacked pairs H1/H2 case:
          ( (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2))
            (LET*
              ( (stockUpdatedGame (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile))
                (newGame (captureFromLayout stockUpdatedGame (getCardMatchList (FIRST draw) (REVERSE (getLayout stockUpdatedGame))) 2 aPlayerTurn)))
              (captureFromLayout newGame (getCardMatchList (FIRST aHandMove) (REVERSE (getLayout newGame))) 2 aPlayerTurn)))
          ( t
            (LET
              ( (newGame (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile)))
              (captureFromLayout newGame (getCardMatchList (FIRST draw) (REVERSE (getLayout newGame))) 2 aPlayerTurn)))))

        ; Two matches.
      ( (= matchCount 2)
        (COND
          ; Checking for H1/H2 case:
          ; The match is only with a stacked pair - a triple stack is left on the layout
          ( (AND (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2)) (= (checkCard (FIRST draw) (FIRST aHandMove)) 1))
            (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile))
          ( t
            (COND
              ( (= aPlayerTurn 0)
                ; Append matches to layout - capture them - then capture H1/H2 matches with different faces.
                (LET*
                  ( (newLayout (APPEND (getLayout aGame) (APPEND (LIST (NTH (getCardIndex (FIRST draw) (getLayout aGame)) (getLayout aGame))) draw)))
                    (stockUpdatedGame (setStockPile (setLayout aGame (removeCard (+ (getCardIndex (FIRST draw) newLayout) 1) newLayout)) newStockPile))
                    (newGame (captureFromLayout stockUpdatedGame (getCardMatchList (FIRST draw) (REVERSE (getLayout stockUpdatedGame))) 2 aPlayerTurn)))
                  ; (displayBoard stockUpdatedGame)
                  ; (displayBoard newGame)
                  (COND
                    ( (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2))
                      (PRINC (getLayout newGame))
                      (captureFromLayout newGame (getCardMatchList (FIRST aHandMove) (REVERSE(getLayout newGame))) 2 aPlayerTurn))
                    ( t
                      newGame))))
              ( (= aPlayerTurn 1)
                ; Append matches to layout - capture them - then capture H1/H2 matches with different faces.
                (LET*
                  ( (userCard (getUserCardChoice (getCardMatchList (FIRST draw) (getLayout aGame)) 1 2 (getCardMatchList (FIRST draw) (getLayout aGame))))
                    (newLayout (APPEND (getLayout aGame) (APPEND (LIST userCard) draw)))
                    (stockUpdatedGame (setStockPile (setLayout aGame (removeCard (+ (getCardIndex userCard newLayout) 1) newLayout)) newStockPile))
                    (newGame (captureFromLayout stockUpdatedGame (getCardMatchList (FIRST draw) (REVERSE (getLayout stockUpdatedGame))) 2 aPlayerTurn)))
                  ; (displayBoard stockUpdatedGame)
                  ; (displayBoard newGame)
                  (COND
                    ( (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2))
                      (captureFromLayout newGame (getCardMatchList (FIRST aHandMove) (REVERSE(getLayout newGame))) 2 aPlayerTurn))
                    ( t
                      newGame))))))))

        ; Three or more matches
      ( t
        (COND
            ; Same card face.
          ( (= (checkCard (FIRST draw) (FIRST aHandMove)) 1)
            (LET
              ( (stockUpdatedGame (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile)))
              (captureFromLayout stockUpdatedGame (getCardMatchList (FIRST draw) (REVERSE (getLayout stockUpdatedGame))) 4 aPlayerTurn)))
            ; Different face from earlier capture:
          ( t
            (LET*
              ( (stockUpdatedGame (setStockPile (setLayout aGame (APPEND (getLayout aGame) draw)) newStockPile))
                (newGame (captureFromLayout stockUpdatedGame (getCardMatchList (FIRST draw) (REVERSE (getLayout stockUpdatedGame))) 4 aPlayerTurn)))
              (COND
                ( (OR (= (NTH 1 aHandMove) 1) (= (NTH 1 aHandMove) 2))
                  (captureFromLayout newGame (getCardMatchList (FIRST aHandMove) (REVERSE(getLayout newGame))) 2 aPlayerTurn))
                ( t
                  newGame)))))))))


; /* ***************************************************
; Function to make appropriate captures from the layout.
; ****************************************************** */
; Function to capture 'aNum' cards in aCards from the layout of aGame and add to the hand of the player with turn aPlayerTurn
(DEFUN captureFromLayout (aGame aCards aNum aPlayerTurn)
  ;(TERPRI)
  ;(WRITE-LINE "CAPTURE FROM LAYOUT")
  ;(displayBoard aGame)
  ;(PRINC aCards)
  ;(TERPRI)
  ;(PRINT aNum)
  (COND
    ( (= aNum 2)
      (COND
        ( (= aPlayerTurn 0)
          (LET*
            ( (cardsToCapture (SUBSEQ aCards 0 2))
              (newLayout (removeCardsFromPile cardsToCapture (getLayout aGame))))
            (COND
                ; Merging with existing stacked pair
              ( (= (MOD (countCards (FIRST aCards) (getComputerCapture aGame)) 4) 2)
                (PRINC "A stacked pair set is complete. You gain a point.")
                (setComputerCapture (setLayout aGame newLayout) (APPEND (getComputerCapture aGame) cardsToCapture)))
              ( t
                (setComputerCapture (setLayout aGame newLayout) (APPEND (getComputerCapture aGame) cardsToCapture))))))
        ( (= aPlayerTurn 1)
          (LET*
            ( (cardsToCapture (SUBSEQ aCards 0 2))
              (newLayout (removeCardsFromPile cardsToCapture (getLayout aGame))))
            (COND
                ; Merging with existing stacked pair
              ( (= (MOD (countCards (FIRST aCards) (getUserCapture aGame)) 4) 2)
                (PRINC "A stacked pair set is complete. You gain a point.")
                (setUserCapture (setLayout aGame newLayout) (APPEND (getUserCapture aGame) cardsToCapture)))
              ( t
                (setUserCapture (setLayout aGame newLayout) (APPEND (getUserCapture aGame) cardsToCapture))))))))
    ( (= aNum 4)
      (COND
        ( (= aPlayerTurn 0)
          (LET*
            ( (cardsToCapture (SUBSEQ aCards 0 4))
              (newCompCapture (APPEND (getComputerCapture aGame) cardsToCapture))
              (newLayout (removeCardsFromPile cardsToCapture (getLayout aGame))))
              ;(newGame (setLayout aGame newLayout)))
            ;(setComputerCapture newGame newCompCapture)))
            (setComputerCapture (setLayout aGame newLayout) newCompCapture)))
        ( (= aPlayerTurn 1)
          (LET*
            ( (cardsToCapture (SUBSEQ aCards 0 4))
              (newUserCapture (APPEND (getUserCapture aGame) cardsToCapture))
              ;(tem (PRINC (getLayout aGame)))
              (newLayout (removeCardsFromPile cardsToCapture (getLayout aGame))))
              ;(temp (PRINC newLayout))
              ;(newGame (setLayout aGame newLayout)))
            ;(setUserCapture newGame newUserCapture)))))))
            (setUserCapture (setLayout aGame newLayout) newUserCapture)))))))

; Function to calculate score according to captures made.
; Round scores.
(DEFUN calculatePlayerScore (aCapture)
  (COND
    ( (= (LENGTH aCapture) 0)
      0)
    ( t
      (COND
        ( (>= (countCards (FIRST aCapture) aCapture) 4)
          (+ (calculatePlayerScore (removeCardsFromPile (getCardMatchList (FIRST aCapture) aCapture) aCapture)) 1))
        ( t
          (calculatePlayerScore (removeCardsFromPile (getCardMatchList (FIRST aCapture) aCapture) aCapture)))))))


; Function to remove aCards from aPile
(DEFUN removeCardsFromPile (aCards aPile)
  (COND
    ( (= (LENGTH aCards) 0)
      aPile)
    ( t
      (removeCardsFromPile (REST aCards)(removeCard (+ (getCardIndex (FIRST aCards) aPile) 1) aPile)))))


; Function to get user card choice
(DEFUN getUserCardChoice (aMatches aCount aLength aMatchCopy)
  (COND
    ( (= (LENGTH aMatches) 0)
      (WRITE-LINE "--------------------")
      (NTH (- (validateInput (READ) 1 aLength) 1) aMatchCopy))
    ( t
      (PRINC aCount)
      (PRINC ": ")
      (PRINC (FIRST aMatches))
      (TERPRI)
      (getUserCardChoice (REST aMatches) (+ aCount 1) aLength aMatchCopy))))


; Function to get list of aCards in 'aPile'. Match by Face.
(DEFUN getCardMatchList (aCard aPile)
  (COND
    ( (= (checkCard aCard (FIRST aPile)) 1)
      (APPEND (LIST (FIRST aPile)) (getCardMatchList aCard (REST aPile))))
    ( (< (LENGTH aPile) 1)
      ())
    ( t
      (getCardMatchList aCard (REST aPile)))))

; Function to get index of aCard in 'aPile'. Match by face.
(DEFUN getCardIndex(aCard aPile)
  (COND
    ( ( = (checkCard aCard (FIRST aPile)) 1)
      0)
    ( t
      (+ (getCardIndex aCard (REST aPile)) 1))))

; Function to get index of aCard in 'aPile'. Match by face and suit.
(DEFUN getCardIndexStrict(aCard aPile)
  (COND
    ( (= (checkSuitFace aCard (FIRST aPile)) 1)
      0)
    ( t
      (+ (getCardIndexStrict aCard (REST aPile)) 1))))

; Function to hand 'anum' cards to player/layout from 'aDeck'
(DEFUN handCard(aNum aDeck)
  (COND
    ( (OR (< aNum 1) (= (LENGTH aDeck) 0))
      ())
    ( t
      (CONS (FIRST aDeck) (handCard (- aNum 1) (REST aDeck))))))

; Function to remove 'anum' handed cards from 'aDeck'
(DEFUN removeHandedCards (aNum aDeck)
  (COND
    ( (OR (< aNum 1) (= (LENGTH aDeck) 0))
      aDeck)
    ( t
      (removeHandedCards (- aNum 1) (REST aDeck)))))


; /* ***************************************************
; Function to display board.
; ****************************************************** */
(DEFUN displayBoard (aGame)
  (LET
    ( (cardOrder (LIST 'KH 'QH 'JH 'XH '9H '8H '7H '6H '5H '4H '3H '2H '1H)))
    (TERPRI)
    (WRITE-LINE "**********************************************")
    (WRITE-LINE "----------")
    (PRINC "ROUND #")
    (PRINC (getRoundNumber aGame))
    (TERPRI)
    (WRITE-LINE "----------")
    (WRITE-LINE "COMPUTER PLAYER")
    (WRITE-LINE "...............")
    (PRINC "SCORE: ")
    (PRINC (getCompRoundScore aGame))
    (TERPRI)
    (WRITE-LINE "HAND: ")
    (PRINC (sortPile (getComputerHand aGame) cardOrder))
    (TERPRI)
    (WRITE-LINE "CAPTURE: ")
    (printCapturePile (sortPile (getComputerCapture aGame) cardOrder))
    (TERPRI)
    (WRITE-LINE "----------LAYOUT----------")
    (printCapturePile (sortPile (getLayout aGame) cardOrder))
    (TERPRI)
    (TERPRI)
    (PRINC "STOCK PILE: [")
    (PRINC (FIRST (getStockPile aGame)))
    (PRINC "|||||...")
    (PRINC "]")
    (TERPRI)
    (WRITE-LINE "--------------------------")
    (WRITE-LINE "USER PLAYER")
    (WRITE-LINE "...........")
    (PRINC "SCORE: ")
    (PRINC (getUserRoundScore aGame))
    (TERPRI)
    (WRITE-LINE "HAND: ")
    (PRINC (sortPile (getUserHand aGame) cardOrder))
    (TERPRI)
    (WRITE-LINE "CAPTURE: ")
    (printCapturePile (sortPile (getUserCapture aGame) cardOrder))
    (TERPRI)
    (WRITE-LINE "**********************************************")))

; /* ***************************************************
; Function to sort a pile in order.
; ****************************************************** */
(DEFUN sortPile (aPile aOrder)
  (COND
    ( (= (LENGTH aPile) 0)
      aPile)
    ( (= (LENGTH aOrder) 0)
      ())
    ( t
      (APPEND (getCardMatchList (FIRST aOrder) aPile) (sortPile aPile (REST aOrder))))))

; Function to print capture piles in stacks.
(DEFUN printCapturePile (aPile)
  (COND
    ( (NULL aPile)
      ())
    ( t
      (LET*
        ( (matchList (getCardMatchList (FIRST aPile) aPile)))
        (COND
          ( (> (LENGTH matchList) 4)
            (PRINC (SUBSEQ matchList 0 4))
            (TERPRI)
            (printCapturePile (removeCardsFromPile (SUBSEQ matchList 0 4) aPile)))
          ( t
            (PRINC matchList)
            (TERPRI)
            (printCapturePile (removeCardsFromPile matchList aPile))))))))



; /* *********************************************
; Setter functions:
; ********************************************* */
; Function to update round number
(DEFUN setRoundNumber (aGame aRound)
  (SUBSTITUTE aRound (ELT aGame 0) aGame :count 1))

; Function to update layout
(DEFUN setLayout (aGame aLayout)
  ;(TERPRI)
  ;(PRINC "LAYOUT SET IS HERE")
  ;(PRINC aLayout)
  ;(TERPRI)
  (SUBSTITUTE aLayout (ELT aGame 9) aGame :start 9 :count 1))

; Function to update computer hand
(DEFUN setComputerHand (aGame aComputerHand)
  (SUBSTITUTE aComputerHand (ELT aGame 1) aGame :start 1 :count 1))

; Function to update computer capture
(DEFUN setComputerCapture (aGame aComputerCapture)
  ;(TERPRI)
  ;(PRINC "COMPUTER CAPTURE IS HERE")
  ;(PRINC aComputerCapture)
  ;(TERPRI)
  (SUBSTITUTE aComputerCapture (ELT aGame 2) aGame :start 2 :count 1))

; Function to set computer round score.
(DEFUN setCompRoundScore (aGame aRoundScore)
  ;(TERPRI)
  ;(PRINC "COMPUTER ROUND SCORE IS HERE")
  ;(PRINC aRoundScore)
  ;(PRINC (ELT aGame 3))
  ;(TERPRI)
  (SUBSTITUTE aRoundScore (ELT aGame 3) aGame :start 3 :count 1))

; Function to set computer total score.
(DEFUN setCompTotalScore (aGame aTotalScore)
  ;(TERPRI)
  ;(PRINC "COMPUTER TOTAL SCORE IS HERE")
  ;(PRINC aTotalScore)
  ;(TERPRI)
  (SUBSTITUTE aTotalScore (ELT aGame 4) aGame :start 4 :count 1))

; Function to update user hand
(DEFUN setUserHand (aGame aUserHand)
  (SUBSTITUTE aUserHand (ELT aGame 5) aGame :start 5 :count 1))

; Function to update user capture
(DEFUN setUserCapture (aGame aUserCapture)
  ;(TERPRI)
  ;(PRINC "USER CAPTURE IS HERE")
  ;(PRINC aUserCapture)
  ;(TERPRI)
  (SUBSTITUTE aUserCapture (ELT aGame 6) aGame :start 6 :count 1))

; Function to set user round score.
(DEFUN setUserRoundScore (aGame aRoundScore)
  ;(TERPRI)
  ;(PRINC "USER ROUND SCORE IS HERE")
  ;(PRINC aRoundScore)
  (TERPRI)
  (SUBSTITUTE aRoundScore (ELT aGame 7) aGame :start 7 :count 1))

; Function to set user total score.
(DEFUN setUserTotalScore (aGame aTotalScore)
  ;(TERPRI)
  ;(PRINC "USER TOTAL SCORE IS HERE")
  ;(PRINC aTotalScore)
  (TERPRI)
  (SUBSTITUTE aTotalScore (ELT aGame 8) aGame :start 8 :count 1))

; Function to update stock pile
(DEFUN setStockPile (aGame aStockPile)
  (SUBSTITUTE aStockPile (ELT aGame 10) aGame :count 1 :from-end t))

; Function to set current player
; 0 for computer, 1 for user
(DEFUN setNextPlayer (aGame aPlayerTurn)
  (SUBSTITUTE aPlayerTurn (ELT aGame 11) aGame :count 1 :from-end t))

; /* *********************************************
; Getter functions:
; ********************************************* */
; Function to get Round number
(DEFUN getRoundNumber (aGame)
  (ELT aGame 0))

; Function to get user hand
(DEFUN getUserHand (aGame)
  (ELT aGame 5))

;Function to get user capture pile
(DEFUN getUserCapture (aGame)
  (ELT aGame 6))

; Function to get computer hand
(DEFUN getComputerHand (aGame)
  (ELT aGame 1))

;Function to get computer capture pile
(DEFUN getComputerCapture (aGame)
  (ELT aGame 2))

; Function to get layout
(DEFUN getLayout (aGame)
  (ELT aGame 9))

; Function to get the stock pile
(DEFUN getStockPile (aGame)
  (ELT aGame 10))

; Function to get player turn
(DEFUN getPlayerTurn (aGame)
  (ELT aGame 11))

; Function to get computer round score
(DEFUN getCompRoundScore (aGame)
  (ELT aGame 3))

; Function to get user round score
(DEFUN getUserRoundScore (aGame)
  (ELT aGame 7))

; Function to get computer total score
(DEFUN getCompTotalScore (aGame)
  (ELT aGame 4))

; Function to get user total score
(DEFUN getUserTotalScore (aGame)
  (ELT aGame 8))

; /* *********************************************
; Source Code to make a shuffled 'decksize' deck.
; ********************************************* */
; CONSTANTS OF ALL THE FACES OF CARDS
(DEFUN make_deck(decksize)
  (COND ( (= decksize 0)
          ())
        (t
          (APPEND (LIST '1S '1H '1D '1C '2S '2H '2D '2C '3S '3H '3D '3C
                             '4S '4H '4D '4C '5S '5H '5D '5C '6S '6H '6C '6S
                             '7S '7H '7D '7C '8S '8H '8D '8C '9S '9H '9C '9D
                             'XS 'XH 'XD 'XC 'JS 'JH 'JD 'JC 'QS 'QH 'QD 'QC
                             'KS 'KH 'KD 'KC) (make_deck(- decksize 1))))))

; Function to remove card at index 'i'
(DEFUN removeCard(i tDeck)
; Checking if index is in range
  (COND ( (< i 1)
          ())
          ; Check if the first card is being removed
        ( (= i 1)
          (REST tDeck))
          ; Separate first card, pass rest recursively till 'i'
        (t
            (CONS
                (FIRST tDeck)
                (removeCard (- i 1) (REST tDeck))))))

; Function to shuffle card deck
(DEFUN shuffle_deck(deck)
  (COND
    ( (null deck)
      ())
    (t
     (LET*
        (
         (randState (MAKE-RANDOM-STATE t))
         (i (RANDOM (LENGTH deck) randState)))
        (CONS
            (ELT deck i)
            (shuffle_deck (removeCard(+ i 1) deck)))))))


; MAIN EXECUTION ENTRY
(GoStop)
