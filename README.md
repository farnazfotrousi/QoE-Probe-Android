QoE Probe for Android
=====================

Table of content
----------------

1\. Introduction 1

1.1. How to use the Github 1

1.2. Copyright and Licensing Information 1

1.3. Contact information 1

2\. How the QoE probe works 2

3\. Installation and Integration instructions 3

3.1. Installation 3

3.2. Integration 4

3.3. Experimentation 4

3.4. Configuration 5

3.4.1. Accept Rules For Participation 5

3.4.2. Set Questionnaire Likelihood 5

3.4.3. Set Data Submission Interval 5

3.5. How to use the collected data 6

Appendix I: Java-Doc 7

Appendix II: Alternative integration solutions 15

Integration: 15

Experimentation 15

Introduction
------------

The QoE Probe Android application is an android mobile application,
which is integrated with another application in order to continuously
collect data relevant to quality of services (QoS) and quality of user
experiences (QoE). This application collects data to study the specific
relationships between QoS and the impact of QoS on QoE. An understanding
of this relationship can then be used to specify the right quality level
by deciding about acceptable levels of QoE.

How to use the Github
---------------------

Files uploaded in Github are two parts:

-   “QoE probe” includes files relevant to the QoE-probe APK

-   “QoE-Library” includes files used as the library to connect a second
    application to QoE-probe APK.

    1.  Copyright and Licensing Information
        -----------------------------------

The software is using GNU LGPL License. More information is available in
the “LICENSE” file**.**

The terms and conditions that has been also included in the software, is
available in “Term-and-conditions.md”

Contact information
-------------------

For technical questions, or reporting troubles and bugs, please contact
Farnaz Fotrousi (Farnaz.Fotrousi at bth.se).

How the QoE probe works
-----------------------

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

> ![](media/image1.png)

<span id="_Ref273607540" class="anchor"></span>Figure : Questionnaire
User-Interface

The log file (Figure 3) sent to the back-end application, contains
several collected records that each includes the following data in order
(separated by ;):

application name, device\_id (hashed data), time-stamp, event (e.g.
starting feature, questionnaire results, etc), feature name, action
name, the numerical rate for selected radio button (5 :excellent ..
1:bad) , text of selected radio-button and user’s comment.

Application A;152b2c711552c3dd;20150118033947951;Starting
Feature;AddType;;;;;

Application A;152b2c711552c3dd;20150118033957831;User
Input;AddType;Press OK in AddType;;;;

Application A;152b2c711552c3dd;20150118033958351;Completing
Feature;AddType;;;;;

Application A;152b2c711552c3dd;20150118033959052; Fire
Questionnaire;AddType;;;;;

Application
A;152b2c711552c3dd;20150118034058380;Questionnaire;AddFieldText;;5;Excellent;;

Application A;e8ccce3d565abe8b;20150118034259231;Starting
Feature;SymbolDelete;;;;;

Application A;e8ccce3d565abe8b;20150118034259333;Application
Output;SymbolDelete; Confirmation message;;;;

Application A;e8ccce3d565abe8b;20150118034259943;Completing
Feature;SymbolDelete;;;;;

Application A;e8ccce3d565abe8b;20150118034157041;Starting
Feature;Zooming;;;;;

Application A;e8ccce3d565abe8b;20150118034157544;Completing
Feature;Zooming;;;;;

Application A;e8ccce3d565abe8b;20150118034157544; Fire
Questionnaire;Zooming;;;;;

> Application
> A;e8ccce3d565abe8b;20150118034409159;Questionnaire;Zooming;;2;Poor;
> Not adjusted to small screens;

<span id="_Ref271293093" class="anchor"></span>Figure : Generated
Log-file

To enable data collection, the QoE probe will provide the interfaces to
application developers that are explained in the section 4.

1.  Installation and Integration instructions
    -----------------------------------------

    1.  Installation
        ------------

> Downloading the QoE probe (QoE.apk) application as well as QoE library
> (qoe-lib.jar). Then integrate your application with the QoE probe
> (section **Error! Reference source not found.**) and follow experiment
> instruction in section **Error! Reference source not found.**. Multi
> integration on one mobile device supports simultaneous use of the QoE
> enabler.

Integration
-----------

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

1.  Extends your MeteredActivity for an Activity of your application:

    **public** **class** MainActivity **extends** MeteredActivity

    Note: if you are using ActionBarActivity instead of Activity, you
    can extend from MeteredActionBarActivity instead and add relevant
    library. Be sure “appcompat-v7 support library” has been configured
    for your project in advance.

    1.  Experimentation
        ---------------

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

1.  Configuration
    -------------

    1.  ### Accept Rules For Participation

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

### Set Questionnaire Likelihood

> Questionnaire likelihood is the probability that a QoE questionnaire
> will be fired. This probability can be set in range of 0 to 100. As an
> example, if the likelihood is set to 20, it means that the probability
> of firing the questionnaire in completion of the feature use would be
> 20 percent. This parameter can be configured by developers through the
> following method’s call:
>
> setQuestionnaireLikelihood(likelihood);

### Set Data Submission Interval 

> The “data submission interval” configures the maximal timespan to wait
> before the user is requested to share QoE and QoS data with the
> QoE/QoS back-end application. When the end-users are not interested to
> submit the questionnaire, the collected data on the mobile phone will
> be submitted automatically and the log file in the mobile device will
> be reset. The default value for this parameter is 15 days, which can
> be extended to 90 days as well. This parameter can be configured by
> developers through the following method’s call:

setDataCollectionInterval(time\_interval);

How to use the collected data
-----------------------------

The generated logfile on the server is downloadable through the
following URL using your application\_name as the paramter:

https://comsrv1.comproj.bth.se:8443/QoEAnalyticsServer/?app=application\_name&token=Received\_token

By calling the above URL, a window for confirmation of username and
password will be presented. The admin of QoE probe will provide you a
username and password. After a successful authentication, you can
download all records relevant to your application\_name (Figure 4).

> ![](media/image2.png)

<span id="_Ref274921999" class="anchor"></span>Figure : Opening logfile
based on application\_name

An example of downloaded file has been presented in Figure 3. For
analysis you can import the file to an excel-sheet in order to have data
in different columns.

Appendix I: Java-Doc
--------------------

Package: com.bth.qoe

**Class MeteredActivity**

-   android.app. Activity

    -   com.bth.qoe.MeteredActivity

  Method Summary
  ------------------- --------------------------------------------------------------------------------------------------------------------
  Modifier and Type
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void

  Methods inherited from class java.lang.Object
  ---------------------------------------------------------------------------
  equals, getClass, hashCode, notify, notifyAll, toString, wait, wait, wait

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Method Details
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  OnResume

  protected void OnResume()

  Start the QoEService

  stopQoEService

  protected void onStop()

  Stop the QoE Service

  startQoEService

  private void startQoEService(Context context)

  Start the service by creating an intent parameter and bind the activity to the right service

  Parameters:

  context -- the reference to the activity context

  stopQoEService

  Private void stopQoEService(Context context)

  Unbind the connection

  Parameters:

  context -- the reference to the activity context

  registerApplication

  private void registerApplication()

  It registers application to identify which application is using the QoE application. The method calculate the mobile\_id as the user\_id

  registerApplication

  public void registerApplication(java.lang.String application)

  It registers application to identify which application is using the QoE application. The method calculate the mobile\_id as the user\_id

  Parameters:

  application - name of the application

  logFeatureStart

  public void logFeatureStart(java.lang.String feature\_name)

  Log starting of the feature

  Parameters:

  feature\_name - the name of feature that is going to be started

  logUserInput

  public void logUserInput(java.lang.String action\_name)

  Log user actions

  Parameters:

  action\_name - name of the user action (e.g. "submit login form")

  logApplicationOutput

  public void logApplicationOutput(java.lang.String action\_name)

  Log application output

  Parameters:

  action\_name - name of the application output (e.g. Display error message )

  logFeatureCompleted

  public void logFeatureCompleted(java.lang.String feature\_name)

  Log the completion of the feature

  Parameters:

  feature\_name - name of the feature

  logFireQuestionnaire

  public void logFireQuestionnaire(java.lang.String feature\_name)

  Log fire questionnaire for the relevant feature

  Parameters:

  feature\_name - name of the feature

  setQuestionnaireLikelihood

  public void setQuestionnaireLikelihood(int likelihood)

  Parameters:

  likelihood - the probability that a QoE questionnaire will be fired. This probability can be set in range of 0 to 100. As an example, if the likelihood is set to 20, it means that the probability of firing the questionnaire in completion of the feature use would be 20 percent. The user can set it through the preferences menu, but it can be also implemented.

  setDataCollectionInterval

  public void setDataCollectionInterval(int interval)

  Parameters:

  interval - configures the maximal time-span to wait before the user is requested to share QoE and QoS data with the QoE/QoS back-end application. When the end-users are not interested to submit the questionnaire, the collected data on the mobile phone will be submitted automatically and the log file in the mobile device will be reset. The default value for this parameter is 15 days, which can be extended to 90 days as well. This parameter is configured by the end-user as well as by a code implementation.

  setAccceptRule

  public void setAccceptRule(boolean accepted\_terms)

  To perform QoE/QoS data sharing within the trusted zone, the informed consent for data sharing should be accepted. This acceptance or rejection can be also done by code.

  Parameters:

  accepted\_terms - true of the informed consent is accepted, otherwise false.
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Package: com.bth.qoe

**Class MeteredActionBarActivity**

-   support.v7.app.ActionBarActivity

    -   com.bth.qoe.MeteredActionBarActivity

public class MeteredActionBar extends support.v7.app.ActionBarActivity

  Method Summary
  ------------------- --------------------------------------------------------------------------------------------------------------------
  Modifier and Type
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void

  Methods inherited from class java.lang.Object
  ---------------------------------------------------------------------------
  equals, getClass, hashCode, notify, notifyAll, toString, wait, wait, wait

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Method Details
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  OnResume

  protected void OnResume()

  Start the QoEService

  stopQoEService

  protected void onStop()

  Stop the QoE Service

  startQoEService

  private void startQoEService(Context context)

  Start the service by creating an intent parameter and bind the activity to the right service

  Parameters:

  context -- the reference to the activity context

  stopQoEService

  Private void stopQoEService(Context context)

  Unbind the connection

  Parameters:

  context -- the reference to the activity context

  registerApplication

  private void registerApplication()

  It registers application to identify which application is using the QoE application. The method calculate the mobile\_id as the user\_id

  registerApplication

  public void registerApplication(java.lang.String application)

  It registers application to identify which application is using the QoE application. The method calculate the mobile\_id as the user\_id

  Parameters:

  application - name of the application

  logFeatureStart

  public void logFeatureStart(java.lang.String feature\_name)

  Log starting of the feature

  Parameters:

  feature\_name - the name of feature that is going to be started

  logUserInput

  public void logUserInput(java.lang.String action\_name)

  Log user actions

  Parameters:

  action\_name - name of the user action (e.g. "submit login form")

  logApplicationOutput

  public void logApplicationOutput(java.lang.String action\_name)

  Log application output

  Parameters:

  action\_name - name of the application output (e.g. Display error message )

  logFeatureCompleted

  public void logFeatureCompleted(java.lang.String feature\_name)

  Log the completion of the feature

  Parameters:

  feature\_name - name of the feature

  logFireQuestionnaire

  public void logFireQuestionnaire(java.lang.String feature\_name)

  Log fire questionnaire for the relevant feature

  Parameters:

  feature\_name - name of the feature

  setQuestionnaireLikelihood

  public void setQuestionnaireLikelihood(int likelihood)

  Parameters:

  likelihood - the probability that a QoE questionnaire will be fired. This probability can be set in range of 0 to 100. As an example, if the likelihood is set to 20, it means that the probability of firing the questionnaire in completion of the feature use would be 20 percent. The user can set it through the preferences menu, but it can be also implemented.

  setDataCollectionInterval

  public void setDataCollectionInterval(int interval)

  Parameters:

  interval - configures the maximal time-span to wait before the user is requested to share QoE and QoS data with the QoE/QoS back-end application. When the end-users are not interested to submit the questionnaire, the collected data on the mobile phone will be submitted automatically and the log file in the mobile device will be reset. The default value for this parameter is 15 days, which can be extended to 90 days as well. This parameter is configured by the end-user as well as by a code implementation.

  setAccceptRule

  public void setAccceptRule(boolean accepted\_terms)

  To perform QoE/QoS data sharing within the trusted zone, the informed consent for data sharing should be accepted. This acceptance or rejection can be also done by code.

  Parameters:

  accepted\_terms - true of the informed consent is accepted, otherwise false.
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Package: com.bth.qoe

**Class Metered**

-   java.lang.Object

    -   com.bth.qoe.Metered

public class Metered extends java.lang.Object<span
id="nested.class.summary" class="anchor"></span>

  Nested Class Summary
  ---------------------- ------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Modifier and Type
  class 

<span id="method.summary" class="anchor"></span>

  Method Summary
  ------------------- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Modifier and Type
  static *Metered*
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void
  void

<span id="methods.inherited.from.class.java.lang.O"
class="anchor"></span>

  Methods inherited from class java.lang.Object
  ---------------------------------------------------------------------------
  equals, getClass, hashCode, notify, notifyAll, toString, wait, wait, wait

<span id="stopQoEService-Context-" class="anchor"></span>

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Method Details
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  getInstance

  public static *Metered* getInstance()

  Returns:

  a static instance of the class

  startQoEService

  public void startQoEService(Context context)

  Start the service by creating an intent parameter and bind the activity to the right service

  Parameters:

  context -- the reference to the activity context

  stopQoEService

  public void stopQoEService(Context context)

  Unbind the connection

  Parameters:

  context -- the reference to the activity context

  <span id="registerApplication-ContentResolver-java" class="anchor"></span>registerApplication

  public void registerApplication(ContentResolver context, java.lang.String application)

  It registers application to identify which application is using the QoE application. The method calculate the mobile\_id as the user\_id

  Parameters:

  context – the reference to the Content Resolver

  application - name of the application

  <span id="logFeatureStart-java.lang.String-" class="anchor"></span>logFeatureStart

  public void logFeatureStart(java.lang.String feature\_name)

  Log starting of the feature

  Parameters:

  feature\_name - the name of feature that is going to be started

  <span id="logFeatureStart-java.lang.String-View-" class="anchor"></span>logFeatureStart

  public void logFeatureStart(java.lang.String feature\_name, View view)

  Log starting of the feature

  Parameters:

  feature\_name - name of the feature

  view - the view of the relevant activity

  <span id="logUserInput-java.lang.String-java.lang." class="anchor"></span>logUserInput

  public void logUserInput(java.lang.String action\_name)

  Log user actions

  Parameters:

  action\_name - name of the user action (e.g. "submit login form")

  <span id="logApplicationOutput-java.lang.String-ja" class="anchor"></span>logApplicationOutput

  public void logApplicationOutput(java.lang.String action\_name)

  Log application output

  Parameters:

  action\_name - name of the application output (e.g. Display error message )

  <span id="logFeatureCompleted-java.lang.String-" class="anchor"></span>logFeatureCompleted

  public void logFeatureCompleted(java.lang.String feature\_name)

  Log the completion of the feature

  Parameters:

  feature\_name - name of the feature

  logFireQuestionnaire

  public void logFireQuestionnaire(java.lang.String feature\_name)

  Log fire questionnaire for the relevant feature

  Parameters:

  feature\_name - name of the feature

  <span id="setQuestionnaireLikelihood-int-" class="anchor"></span>setQuestionnaireLikelihood

  public void setQuestionnaireLikelihood(int likelihood)

  Parameters:

  likelihood - the probability that a QoE questionnaire will be fired. This probability can be set in range of 0 to 100. As an example, if the likelihood is set to 20, it means that the probability of firing the questionnaire in completion of the feature use would be 20 percent. The user can set it through the preferences menu, but it can be also implemented.

  <span id="setDataCollectionInterval-int-" class="anchor"></span>setDataCollectionInterval

  public void setDataCollectionInterval(int interval)

  Parameters:

  interval - configures the maximal time-span to wait before the user is requested to share QoE and QoS data with the QoE/QoS back-end application. When the end-users are not interested to submit the questionnaire, the collected data on the mobile phone will be submitted automatically and the log file in the mobile device will be reset. The default value for this parameter is 15 days, which can be extended to 90 days as well. This parameter is configured by the end-user as well as by a code implementation.

  <span id="setAccceptRule-boolean-" class="anchor"></span>setAccceptRule

  public void setAccceptRule(boolean accepted\_terms)

  To perform QoE/QoS data sharing within the trusted zone, the informed consent for data sharing should be accepted. This acceptance or rejection can be also done by code.

  Parameters:

  accepted\_terms - true of the informed consent is accepted, otherwise false.
  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Appendix II: Alternative integration solutions
----------------------------------------------

For the alternative solution for integration and experimentation of the
QoE probe, developers will make an instance from Metered Class and work
with that instance:

#### Integration:

1.  Install the QoE.apk in an android mobile where you test your mobile
    application and the integration with the QoE probe.

2.  Add the qoe.jar to your project

3.  Import the Metered class

> import com.bth.qoe.Metered;

1.  Create an instance of Metered class:

Metered metered=Metered.getInstance();

1.  In onResume method of your activity call the startQoEService method
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

1.  In onStop method of your activity call stopQoEService method:

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

1.  Log start of the feature. In the line that the feature is started in
    your application call logFeatureStart method.

    metered.logFeatureStart("feature\_name");

2.  Log completion of the feature. In the line that the feature is
    completed in your application, call logFeatureCompleted method with
    the feature\_name as the paramter.

    metered.logFeatureCompleted("feature\_name");

3.  Log user Input. In the line that the user perform an action in your
    application call logUserInput method with the action\_name as the
    parameter.

    metered.logUserInput("Action1");

4.  Log application output. In the line that the application provides an
    output for the users, call logApplicationOutput method with the
    action\_name as the parameters.

    metered.logApplicationOutput("Action2");

Appendix III: User Interfaces of the QoE probe
----------------------------------------------

  ----------------------------------------------------------------------------------------------------------------------------------------------------
  > ![](media/image3.png)                                                  > ![](media/image4.png)<span id="_Ref273624038" class="anchor"></span>
  >                                                                        >
  > Figure : QoE questionnaire                                             > Figure : Welcome page after installation
  ------------------------------------------------------------------------ ---------------------------------------------------------------------------
    > ![](media/image5.png)   ![](media/image6.png)
    ------------------------- -----------------------

  <span id="_Ref271287624" class="anchor"></span>Figure : Preferences

  > ![](media/image7.png)                                                  ![](media/image8.png)
                                                                           
  <span id="_Ref271288543" class="anchor"></span>                          <span id="_Ref271291425" class="anchor"></span>
                                                                           
  Figure : Accept Rules for Participation                                  Figure : Set Questionnaire Likelihood

  > ![](media/image9.png)<span id="_Ref271291481" class="anchor"></span>   > ![](media/image10.png)
                                                                           
  Figure : Data Submission Interval                                        <span id="_Ref273629707" class="anchor"></span>Figure : About QoE Service

                                                                           
  ----------------------------------------------------------------------------------------------------------------------------------------------------


