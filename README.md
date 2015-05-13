# Spam-Filter-from-Scratch
A school project for Santa Clara University's Big Data class

The goal is is to incrementally create an email spam filter from scratch using Spark Streaming.

The email data set can be downloaded here:
http://www.cs.cmu.edu/~enron/

When the data set is decompressed, these files can then be combined into one big file using the following UNIX shell command inside the 'maildir' folder:
find . type f -exec cat {} \; > dataset.dat
