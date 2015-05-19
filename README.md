# Spam-Filter-from-Scratch
A school project for Santa Clara University's Big Data class

The goal is is to incrementally create an email spam filter from scratch using Spark Streaming.

The email data set can be downloaded here:
http://www.cs.cmu.edu/~enron/

When the data set is decompressed, these files can then be combined into one
big file, separated by null bytes, using the following UNIX shell command:
  find maildir -type f | xargs ./nullcat > mail.dat

This requires that the "nullcat" program has been built and is in the
current directory.  To build nullcat, just type "make" in its directory.
