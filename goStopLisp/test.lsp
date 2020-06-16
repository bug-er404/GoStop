; CODE FOR RANDOM NUMBER GENERATION
;(PRINC (RANDOM 1000 (MAKE-RANDOM-STATE t)))

;(PRINT (RANDOM (LENGTH (LIST '1C '1D '1E '2C '2D '2E)) (MAKE-RANDOM-STATE t)))

;(PRINC (substitute 0 1 '(1 2 3 1 3 2 1 2 1 2 1) :start 6 :count 1))


(DEFUN sift (numbers)
  (COND ( (= (LENGTH numbers) 0)
          ())
        ( (>= (FIRST numbers) 0)
          (CONS (FIRST numbers) (sift (REST numbers))))
        ( t
          (sift (REST numbers)))))

(PRINC(sift '( 5 0 -5 -3 3)))
