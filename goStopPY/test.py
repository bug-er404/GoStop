import tkinter
import tkinter.messagebox
from tkinter import *
from PIL import ImageTk, Image
import os

root = Tk()
root.geometry("1600x1000")

miFrame = Frame(root, width=1600, height=1000)
miFrame.grid(row=0,column=0)

# can = Canvas(root, width=90, height=130)
image = Image.open("img/TWODIAMONDS.jpg")
photo = ImageTk.PhotoImage(image)
# can.create_image(90, 130, image=photo)

boton = Button(miFrame, image=photo, border=0)
boton.config(bg="red")
boton.grid(row=0, column=0)

root.mainloop()

# Label(frame, text="Number of users playing the game: ").grid(row=0, column=0)
# userPlayers = Spinbox(frame, values=(1, 2, 3, 4), state="readonly")
# userPlayers.grid(row=0, column=1)
# Label(frame, text="Number of decks to use: ").grid(row=1, column=0)
# numDecks = Spinbox(frame, values=(2, 3, 4), state="readonly")
# numDecks.grid(row=1, column=1)
# Button(frame, text="Start game!", command=lambda: getValues(userPlayers, numDecks)).grid(row=3)
#
#
#


# entry_widget = tkinter.Entry(frame, text="Text entry widget")
#
# frame_widget = tkinter.Frame(frame)
#
# label_widget = tkinter.Label(frame, text="Label widget")
#
# # You will first create a division with the help of Frame class and align them on TOP and BOTTOM with pack() method.
# top_frame = tkinter.Frame(frame).pack()
# bottom_frame = tkinter.Frame(frame).pack(side="bottom")
#
# # Once the frames are created then you are all set to add widgets in both the frames.
# btn1 = tkinter.Button(top_frame, text="Button1",
#                       foreground="red").pack()  # 'fg or foreground' is for coloring the contents (buttons)
#
# btn2 = tkinter.Button(top_frame, text="Button2", foreground="green").pack()
#
# btn3 = tkinter.Button(bottom_frame, text="Button3", foreground="purple").pack(
#     side="left")  # 'side' is used to left or right align the widgets
#
# btn4 = tkinter.Button(bottom_frame, text="Button4", foreground="orange").pack(side="left")

# CheckVar1 = IntVar()
# CheckVar2 = IntVar()
# tkinter.Checkbutton(top, text = "Machine Learning",variable = CheckVar1,onvalue = 1, offvalue=0).grid(row=0,sticky=W)
# tkinter.Checkbutton(top, text = "Deep Learning", variable = CheckVar2, onvalue = 0, offvalue =1).grid(row=1,sticky=W)
# top.mainloop()


# frame.mainloop()
