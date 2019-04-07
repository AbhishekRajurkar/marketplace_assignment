To Run the Application- 
Java -jar simple-word-search-0.0.1-SNAPSHOT.jar 'directory path'

To exit the application - Type exit. 

To solve the problem, a map of word - FileMeta Map<String, FileMeta> object is created.
FileMeta object holds the file name and number of occurrences of a word for a given file.
Rate is calculated based on , occurrences of search string in each file.  

Known Bug-
Files with same name within directory structure hierarchy causes the wrong calculation.

Known improvements
-- Some of the code elements can be read from properties files.   


Assumption-
I have restricted the input file to maximum 10 files. 
stop words e.g. this, is, the and so on.. and punctuation are eliminated from indexing. 
Used lower case words as keys, also search string is converted to lowercase before using it for search.

