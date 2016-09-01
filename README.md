# sle2016

Description:
============
  Proof-of-concept implementation of the DSL monitoring pattern

Recommended versions of software:
=================================
   - Eclipse Version: Mars.2 Release (4.5.2)
   - Configured with JavaSE-1.8
   - Including DSProfile, Version 2.11

Installing the prototype:
=========================
  The easiest way to install and test the paper's code is to do it within Eclipse.
  However it is independent of any Eclipse underlying technology, so it can be as well run
  in command line with proper classpath configuration. We describe the eclipse way here.
     - run Eclipse
     - import the directory 'monitorLambda' as a Java Project inside Eclipse
       (however it is independent of any Eclipse underlying API)
     - Configure the Java project properties (right-click on monitorLambda > Properties)
         (i)  Set Java Compiler to JavaSE-1.8
         (ii) Set Java Build path:
              * Add DSProfile (dsprofile_2.11-0.4.0.jar available in the project's directory 'libs')
              * Add Scala library (scala-library.jar available in the project's directory 'libs')

Run the tests listed in the paper:
=================================
  The main program is in MainTester in package 'examples.lambda.concrete'
  (right-click on MainTester.java > Run As > JavaApplication).
  It runs without any argument configuration : all available tests are executed sequentially.
    
Note about the dependencies:
---------------------------
  The jars are available in the libs directory at the root of the project
  You can also download the jar of dsprofile, version 2.11
  Available at: https://bitbucket.org/inkytonik/dsprofile
  

  
