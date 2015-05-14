# QoE Probe for Android


## Table of Contents

* [Introduction](#Introduction)
 *   [Github files](##Github files)
 *   [Copyright and Licensing Information](##Copyright and Licensing Information)
 *   [Contact information](## Contact information)
* [How the QoE probe works](#How the QoE probe works)
* [Installation and Integration instructions](#Installation and Integration instructions)
* [Alternative integration solution](###Alternative integration solution)



## Introduction
QoE probe was developed by [BTH - Blekinge Tekniska Högskola](http://www.bth.se) during a European project called [FI-STAR](https://www.fi-star.eu). The iOS version of the QoE probe has been developed by  [LTFE - University of Ljubljana](http://www.ltfe.org) and is accessible in the Github (https://github.com/LTFE/QoE-Probe-iOS) as well.

The QoE Probe Android application is an android mobile application,
which is integrated with another application in order to continuously
collect data relevant to quality of services (QoS) and quality of user
experiences (QoE). This application collects data to study the specific
relationships between QoS and the impact of QoS on QoE. An understanding
of this relationship can then be used to specify the right quality level
by deciding about acceptable levels of QoE.

### Github files

Files uploaded in the Github are four types:
-  "README.md", "LICENSE", and "Terms_and_conditions" are informative files. 

-   “QoE probe” folder includes source files relevant to the QoE-probe APK

-   “QoE-Library” folder includes source files used as the library to connect a second
    application to QoE-probe APK.

-   "QoE.apk" and "qoe-lib.jar" are ready-to-use files  (refer to README.md). 


### Copyright and Licensing Information

The software is using GNU LGPL License. More information is available in
the “LICENSE” file.

The terms and conditions that has been also included in the software, is
available in “Term-and-conditions.md”

### Contact information

For technical questions, or reporting troubles and bugs, please contact
Farnaz Fotrousi (Farnaz.Fotrousi at bth.se).

##How the QoE probe works

Developers integrate the QoE probe with their own target application
before the release. Then by installation the applications, data
collection will happen continuously based on calls triggered through the
interaction of the end-user with that main application. To achieve this
goal, developers will implement the interfaces of the QoE probe library
relevant to the following actions:

> **Register Application:** To initialize the connection of the  probe
> with an application the relevant method is called to identify which
> application is using the QoE probe.
>
> **Log Feature Start:** The timestamp relevant to the start of a
> feature use is logged.
>
> **Log User Input:** The timestamp of a user interaction such as
> pressing a button relevant to a specific feature is logged.
>
> **Log Application Output:** The timestamp of a user interaction such
> as displaying a screen for a specific feature is logged.
>
> **Log Feature Completed:** The timestamp relevant to the end of a
> feature use is logged.
>
> **Log Fire Questionnaire:** The questionnaire is fire up and the
> relevant timestamp is logged.
>
> **Input:** The timestamp of a user interaction such as pressing a
> button relevant to a specific feature is logged.
>
> **Log Application Output:** The timestamp of a user interaction such
> as displaying a screen for a specific feature is logged.
>
> **Log Feature Completed:** The timestamp relevant to the end of a
> feature use is logged.
>
> **Log Fire Questionnaire:** The questionnaire is fire up and the
> relevant timestamp is logged.

The generated log file is initially stored on the device however by
calling “Log feature completed” and then “Log fire the questionnaire”,
the questionnaire(Figure 2) will be presented. Submitting the
questionnaire form will end-up with sending data including questionnaire
data and stored-logfile to the back-end application for further
analysis. The log file will be sent to the server using a JSON protocol.
Through the analysis, the correlation between QoE and QoS data will be
identified in which it will be measured how much the QoS value is
acceptable. The estimated QoS value for maximum impact of QoE will be
identified to determine the good-enough QoS value by the help of
rationale.

![Questionnaire User-Interface](https://github.com/farnazfotrousi/QoE-Probe-Android/blob/master/images/mainpage.jpg)
>Figure 1: Questionnaire User-Interface

The log file (Figure 2) sent to the back-end application, contains
several collected records that each includes the following data in order
(separated by ;):

application name, device\_id (hashed data), time-stamp, event (e.g.
starting feature, questionnaire results, etc), feature name, action
name, the numerical rate for selected radio button (5 :excellent ..
1:bad) , text of selected radio-button and user’s comment.

![Generated Log-file](https://github.com/farnazfotrousi/QoE-Probe-Android/blob/master/images/log.jpg)
>Figure 2: Generated Log-file

To enable data collection, the QoE probe will provide the interfaces to
application developers.

##Installation and Integration instructions

###Installation


> Downloading the QoE probe (QoE.apk) application as well as QoE library
> (qoe-lib.jar). Then follow instructions for integrating your application with the QoE probe
> and experimentation in the following sections. Multi
> integration on one mobile device supports simultaneous use of the QoE
> enabler.

###Integration
   
> To integrate an application with QoE probe Android application,
> developers can easily extend the *MeteredActivity* class for the
> Activity classes of their application as explained through the
> instructions below. However the developers can make an instance of
> *Metered* class as an alternative solution and follow the instructions
> in Appendix instead.

1.  Install the QoE.apk in an android mobile where you test your mobile
    application and the integration with the QoE probe.

2.  Add the qoe-lib.jar to the libs folder of your project

3.  Import the MeteredActivity class

> import com.bth.qoe.MeteredActivity;

4.  Extends your MeteredActivity for an Activity of your application:

    **public** **class** MainActivity **extends** MeteredActivity

    Note: if you are using ActionBarActivity instead of Activity, you
    can extend from MeteredActionBarActivity instead and add relevant
    library. Be sure “appcompat-v7 support library” has been configured
    for your project in advance.

###Experimentation


> During the experimentation, developers should tag the code lines that
> the features are started or completed. They also identify user input
> and application output places cross the feature as below instructed:

1.  Log start of the feature: In the line that the feature is started in
    your application call logFeatureStart method.

    logFeatureStart("feature\_name");

2.  Log completion of the feature. In the line that the feature is
    completed in your application, call logFeatureCompleted method with
    the feature\_name as the parameter.

    logFeatureCompleted("feature\_name");

3.  Log fire questionnaire. In the line the questionnaire is fired.
    Please call it after the feature completed, call
    logFireQuestionnaire method with the feature\_name as the parameter.

    logFireQuestionnaire("feature\_name");

4.  Log user Input. In the line that the user perform an action in your
    application call logUserInput method with the action\_name as the
    parameter.

    logUserInput("Action\_name 1");

5.  Log application output. In the line that the application provides an
    output for the users, call logApplicationOutput method with the
    action\_name as the parameter.

    logApplicationOutput("Action\_name 2");

> **Note:** No action is required for application registration. The
> application registration is automatically performed when you follow
> the above instruction with a default value for the application name.
> The default value is set by the package name, however if you are
> interested to make the generated logs more readable you can use the
> following method to pass the name of your application before starting
> feature command (before step 1):

registerApplication("application\_name");

###Configuration

#### Accept Rules For Participation

> To perform QoE/QoS data sharing within the trusted zone, the informed
> consent for data sharing should be accepted. While the application is
> registered for the first time, in the first use, the terms and
> condition user-interface will be shown to the end users. In the case
> of rejection, no data collection will be performed unless in
> *preferences* menu of the QoE enabler, data collection is activated by
> the end-users. However developers can also accept the term&conditions
> by calling the following method:
>
> setAccceptRule(**true**);

#### Set Questionnaire Likelihood

> Questionnaire likelihood is the probability that a QoE questionnaire
> will be fired. This probability can be set in range of 0 to 100. As an
> example, if the likelihood is set to 20, it means that the probability
> of firing the questionnaire in completion of the feature use would be
> 20 percent. This parameter can be configured by developers through the
> following method’s call:
>
> setQuestionnaireLikelihood(likelihood);

#### Set Data Submission Interval 

> The “data submission interval” configures the maximal timespan to wait
> before the user is requested to share QoE and QoS data with the
> QoE/QoS back-end application. When the end-users are not interested to
> submit the questionnaire, the collected data on the mobile phone will
> be submitted automatically and the log file in the mobile device will
> be reset. The default value for this parameter is 15 days, which can
> be extended to 90 days as well. This parameter can be configured by
> developers through the following method’s call:

setDataCollectionInterval(time\_interval);

###How to use the collected data

The generated logfile on the server is downloadable through the
following URL using your application\_name as the paramter:

https://comsrv1.comproj.bth.se:8443/QoEAnalyticsServer/?app=application\_name&token=Received\_token

By calling the above URL, a window for confirmation of username and
password will be presented. The admin of QoE probe will provide you a
username and password. After a successful authentication, you can
download all records relevant to your application\_name (Figure 3).

![download](https://github.com/farnazfotrousi/QoE-Probe-Android/blob/master/images/download.jpg)
>Figure 3 : Opening logfile based on application\_name

An example of downloaded file has been presented in Figure 3. For
analysis you can import the file to an excel-sheet in order to have data
in different columns.

###Alternative integration solution


For the alternative solution for integration and experimentation of the
QoE probe, developers will make an instance from Metered Class and work
with that instance:

#### Integration:

1.  Install the QoE.apk in an android mobile where you test your mobile
    application and the integration with the QoE probe.

2.  Add the qoe.jar to your project

3.  Import the Metered class

> import com.bth.qoe.Metered;

4.  Create an instance of Metered class:

Metered metered=Metered.getInstance();

5.  In onResume method of your activity call the startQoEService method
    from the Metered class:

> @Override
>
> **protected** **void** onResume(){
>
> **super**.onResume();
>
> metered.startQoEService(**this**);
>
> }

6.  In onStop method of your activity call stopQoEService method:

> @Override
>
> **protected** **void** onStop() {
>
> **super**.onStop(); metered.stopQoEService(**this**);
>
> }

#### Experimentation

1.  Log application Registration. Call the registerApplication method
    with contentResolver and application\_name parameters to register
    your application when you creating your activity:

> metered.registerApplication(getBaseContext(), getContentResolver());
>
> or
> metered.registerApplication(getContentResolver(),"application\_name");

2.  Log start of the feature. In the line that the feature is started in
    your application call logFeatureStart method.

    metered.logFeatureStart("feature\_name");

3.  Log completion of the feature. In the line that the feature is
    completed in your application, call logFeatureCompleted method with
    the feature\_name as the paramter.

    metered.logFeatureCompleted("feature\_name");

4.  Log user Input. In the line that the user perform an action in your
    application call logUserInput method with the action\_name as the
    parameter.

    metered.logUserInput("Action1");

5.  Log application output. In the line that the application provides an
    output for the users, call logApplicationOutput method with the
    action\_name as the parameters.

    metered.logApplicationOutput("Action2");
