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

To check that you did this correctly, do "sha1sum mail.dat" and compare
with mail.dat.sha1sum in this directory.

Since the above linked dataset is subject to change based on removal
requests from the e-mail participants, the specific data we use can be
downloaded at this address:

http://students.engr.scu.edu/~vtai/files/enron/

"enron_mail_20150507.tar.xz" is the original tarball (but gunzip'd and
then recompressed with xz to reduce download size).

"mail.dat.xz" is the xz-compressed null-separated concatenation of all
files in the tarball, as produced by the above procedure.

Use "xz -d *.xz" to decompress.  We will use mail.dat as our
"official" dataset.

In the Enron data set, the original folder structure stores all of the emails by the user and then by the directory that the user organized the mail in. Since the directories (or at least some of them) are organized by the users themselves, it is a reasonable assumption that the user does not consider them to be spam. By concatenating many of these folders, we can slowly build the amount of data we need to train our Bayesian filter.

This seems to be several folders that each user has in common. These are the ones we can eliminate from our concatenation:

    _sent_mail
    all_documents
    contacts
    deleted_items
    discussion_threads
    inbox
    notes_inbox
    sent
    sent_items


The following Linux shell command will delete all of the above folders from the data set, you can then use the concatenation again in order to create a training data set. Be sure to concatenate the entire data set first before running the command below.

    find maildir -type d -name "_sent_mail" -o -name "all_documents" -o -name "contacts" -o -name "deleted_items" -o -name "discussion_threads" -o -name "inbox" -o -name "notes_inbox" -o -name "sent" -o -name "sent_items" | xargs rm -r 
