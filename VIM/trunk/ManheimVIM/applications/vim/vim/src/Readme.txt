Vim Application is used to check-in and checkout images as a batch

Vim.jar takes 3 command line parameters: 

1) Import (-i) or Export (-e) parameter that tells vim if it needs to import or 
   export content This is the provider specific file that contains custom 
   configuration information for each image provider.
2) Manifest configuration XML file path (-m). This file will have all 
   metadata fields and their values that are used while importing content into UCM
3) Common configuration XML file path (-c) that is common for all providers.
   This file contains general information like
   a) The number of threads to use 
   b) List of Server instances that are used to import content
   c) Maximum execution time, etc


Example execution commands to execute vim.jar from command line:

    Export:  java -jar vim.jar -e export-config.xml -c vim-config.xml
    Import:  java -jar vim.jar -i import-config.xml -c vim-config.xml -m manifest.xml";

    
VimMain is the entry point into the application. This class sets and verifies 
the command line options and their corresponding values. It is also responsible for 
parsing the XML files into Java objects using JavaBeans. If all all required parameters 
are passed in, it initiates import or export sequence based on the first parameter.


IMPORT:
-------
Key notes for Import are:

-- ImageLoader gets all configuration objects from VimMain class
-- ImageLoader reads the configuration files and divides the work into smaller tasks 
   and creates a thread for each task
-- Each task is handled by an individual thread that is responsible for 
   communicating with the UCM server via a DAO, getting back the response,
   and logging the appropriate response headers for each image.
-- If an exception occurred in the thread, it writes to a log and fails silently. 
   or tried to check-in that particular image again for the specified number of times
   before failing or succeeding. All other threads continue on their execution paths.
-- The number of concurrent threads that are allowed to execute at a given point in 
   time is based off the configuration information retrieved from the XML files.
-- Every time the application runs, it creates a new log file (named by the current 
   system date) for every image provider (only if specified in the configuration). 
   It logs to the console if otherwise.
-- After all threads complete execution, a batch completion termination file is 
   created in the present base directory path.

 For further details, please refer to the comments in the source.

EXPORT:
-------
Key notes for export are:

-- ImageExporter gets all configuration objects from vimMain class.
-- ImageExporter reads the vimConfig object and creates a thread pool.
   An individual thread in this pool is responsible for executing a query with UCM server
   via a Dao and getting back the metadata of the list of images.
-- If an exception occurred while executing the query, the thread writes the query to a 
   log and retries to execute the query for the specified number of times before the 
   thread dies.
-- after all threads are done executing the queries, Image exporter will create a 
   new thread pool
-- Each thread in this pool is responsible for communicating with UCM server via a dao, 
   exporting the image file to specified base directory path and writing the metadata to 
   manifest file.
-- If an exception occurred while exporting the images, the thread writes the image name to a 
   log and retries to export the image for the specified number of times before the 
   thread dies.
-- The application creates a new log file (named by the current system date) for every 
   image provider only if specified in the configuration. otherwise It logs to the console.
-- After all threads complete execution, the manifest file is generated and a batch 
   completion termination file is created in the present base directory path.

 For further details, please refer to the comments in the source.



