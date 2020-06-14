Name: Winnie Wan
BB ID: wwan5
wwan5@u.rochester.edu

Name: Xiaoxiang Liu
BB ID: xliu102
xliu102@u.rochester.edu

Date: March 9, 2019
Project 2

The compressed folder should contain README.txt (this one) and HuffmanSubmit.java,
which is the homework. Put target files into the project folder, and run HuffmanSubmit
to check the compressed file. Necessary comments are put in the code, which should help
you understand the code without difficulty.

Short summary:
Encode:
I stored everything in a hashmap, which records the frequency. I use that
hashmap to put it in a priority queue, which enables me to create a tree within it.
Then I used another hashmap to record all the leaves/characters and use that
hashmap to reproduce an encoded string in which i write onto a new file.

Decode:
I use the freq file and I store that into my hashmap, stores it in priority queue
which then creates a new tree. I traverse that tree to find each character recursively, each
time ending with a leaf. Then storing the decoded into a string and writing it back out.