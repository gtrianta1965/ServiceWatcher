![missing logo](https://raw.githubusercontent.com/gtrianta1965/ServiceWatcher/master/ServiceWatcher/src/main/resources/images/sw.png)

# Service Watcher

<div align="center">

## Maven Deployment

</div>

***

_**`./rebuild.sh `**_ 

The above script cleans, compiles and fetches projects' dependencies using Maven.

***

_**`./deploy.sh <path> `**_ 

 * Takes a path as a parameter (relative or full-path)
 * Runs rebuild.sh
 * Creates a folder structure at the specified path
   * \<path>/ServiceWatcher/target/lib
 * Finally inserts all the needed files in order to run the programme.


```
<path>
   └── ServiceWatcher # run scripts, *.properties files, sw.log and email template.
        └── target # ServiceWatcher compiled jar file.
             └── lib # all dependencies are located here.
```
***

<div align="center">
  
## List of properties 

</div>

[more](https://github.com/gtrianta1965/ServiceWatcher/wiki/Configuration)

* concurrentThreads
* smtpSendMailOnSuccess
* smtpSendActivityEmailInterval
* httpResponseTimeout
* ldapResponseTimeout
* socketDieInterval
* sendMailUpdates
* smtpHost
* smtpPort
* smtpUsername
* smtpPassword
* autoRefreshIntervals
* to
