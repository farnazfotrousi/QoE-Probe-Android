[[qoe-probe-for-android]]
QoE Probe for Android
---------------------

[[table-of-content]]
Table of content
~~~~~~~~~~~~~~~~

\1. Introduction

1.1. How to use the Github

1.2. Copyright and Licensing Information

1.3. Contact information

\2. How the QoE probe works

\3. Installation and Integration instructions

3.1. Installation

3.2. Integration

3.3. Experimentation

3.4. Configuration

3.4.1. Accept Rules For Participation

3.4.2. Set Questionnaire Likelihood

3.4.3. Set Data Submission Interval

3.5. How to use the collected data

Appendix I: Java-Doc

Appendix II: Alternative integration solutions

Appendix III: User Interfaces of the QoE probe

[[introduction]]
Introduction
~~~~~~~~~~~~

The QoE Probe Android application is an android mobile application,
which is integrated with another application in order to continuously
collect data relevant to quality of services (QoS) and quality of user
experiences (QoE). This application collects data to study the specific
relationships between QoS and the impact of QoS on QoE. An understanding
of this relationship can then be used to specify the right quality level
by deciding about acceptable levels of QoE.

[[how-to-use-the-github]]
How to use the Github
~~~~~~~~~~~~~~~~~~~~~

Files uploaded in Github are two parts:

* “QoE probe” includes files relevant to the QoE-probe APK
* “QoE-Library” includes files used as the library to connect a second
application to QoE-probe APK.
1.  [[copyright-and-licensing-information]]
Copyright and Licensing Information
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The software is using GNU LGPL License. More information is available in
the “LICENSE” file**.**

The terms and conditions that has been also included in the software, is
available in “Term-and-conditions.md”

[[contact-information]]
Contact information
~~~~~~~~~~~~~~~~~~~

For technical questions, or reporting troubles and bugs, please contact
Farnaz Fotrousi (Farnaz.Fotrousi at bth.se).

[[how-the-qoe-probe-works]]
How the QoE probe works
~~~~~~~~~~~~~~~~~~~~~~~

Developers integrate the QoE probe with their own target application
before the release. Then by installation the applications, data
collection will happen continuously based on calls triggered through the
interaction of the end-user with that main application. To achieve this
goal, developers will implement the interfaces of the QoE probe library
relevant to the following actions:

__________________________________________________________________________________________________________________________________________________________________________
*Register Application:* To initialize the connection of the  probe with
an application the relevant method is called to identify which
application is using the QoE probe.

*Log Feature Start:* The timestamp relevant to the start of a feature
use is logged.

*Log User Input:* The timestamp of a user interaction such as pressing a
button relevant to a specific feature is logged.

*Log Application Output:* The timestamp of a user interaction such as
displaying a screen for a specific feature is logged.

*Log Feature Completed:* The timestamp relevant to the end of a feature
use is logged.

*Log Fire Questionnaire:* The questionnaire is fire up and the relevant
timestamp is logged.

*Input:* The timestamp of a user interaction such as pressing a button
relevant to a specific feature is logged.

*Log Application Output:* The timestamp of a user interaction such as
displaying a screen for a specific feature is logged.

*Log Feature Completed:* The timestamp relevant to the end of a feature
use is logged.

*Log Fire Questionnaire:* The questionnaire is fire up and the relevant
timestamp is logged.
__________________________________________________________________________________________________________________________________________________________________________

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

_____________________________
image:media/image1.png[image]
_____________________________

Figure : Questionnaire User-Interface

The log file (Figure 3) sent to the back-end application, contains
several collected records that each includes the following data in order
(separated by ;):

application name, device_id (hashed data), time-stamp, event (e.g.
starting feature, questionnaire results, etc), feature name, action
name, the numerical rate for selected radio button (5 :excellent ..
1:bad) , text of selected radio-button and user’s comment.

_____________________________
image:media/image2.png[image]
_____________________________

Figure : Generated Log-file

To enable data collection, the QoE probe will provide the interfaces to
application developers that are explained in the section 4.

1.  [[installation-and-integration-instructions]]
Installation and Integration instructions
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1.  [[installation]]
Installation
~~~~~~~~~~~~

___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
Downloading the QoE probe (QoE.apk) application as well as QoE library
(qoe-lib.jar). Then integrate your application with the QoE probe
(section **Error! Reference source not found.**) and follow experiment
instruction in section **Error! Reference source not found.**. Multi
integration on one mobile device supports simultaneous use of the QoE
enabler.
___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

[[integration]]
Integration
~~~~~~~~~~~

______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
To integrate an application with QoE probe Android application,
developers can easily extend the _MeteredActivity_ class for the
Activity classes of their application as explained through the
instructions below. However the developers can make an instance of
_Metered_ class as an alternative solution and follow the instructions
in Appendix instead.
______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

1.  Install the QoE.apk in an android mobile where you test your mobile
application and the integration with the QoE probe.
2.  Add the qoe-lib.jar to the libs folder of your project
3.  Import the MeteredActivity class

___________________________________
import com.bth.qoe.MeteredActivity;
___________________________________

1.  Extends your MeteredActivity for an Activity of your application:
+
*public* *class* MainActivity *extends* MeteredActivity
+
Note: if you are using ActionBarActivity instead of Activity, you can
extend from MeteredActionBarActivity instead and add relevant library.
Be sure “appcompat-v7 support library” has been configured for your
project in advance.
1.  [[experimentation]]
Experimentation
~~~~~~~~~~~~~~~

_______________________________________________________________________________________________________________________________________________________________________________________________________________
During the experimentation, developers should tag the code lines that
the features are started or completed. They also identify user input and
application output places cross the feature as below instructed:
_______________________________________________________________________________________________________________________________________________________________________________________________________________

1.  Log start of the feature: In the line that the feature is started in
your application call logFeatureStart method.
+
logFeatureStart("feature_name");
2.  Log completion of the feature. In the line that the feature is
completed in your application, call logFeatureCompleted method with the
feature_name as the parameter.
+
logFeatureCompleted("feature_name");
3.  Log fire questionnaire. In the line the questionnaire is fired.
Please call it after the feature completed, call logFireQuestionnaire
method with the feature_name as the parameter.
+
logFireQuestionnaire("feature_name");
4.  Log user Input. In the line that the user perform an action in your
application call logUserInput method with the action_name as the
parameter.
+
logUserInput("Action_name 1");
5.  Log application output. In the line that the application provides an
output for the users, call logApplicationOutput method with the
action_name as the parameter.
+
logApplicationOutput("Action_name 2");

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
*Note:* No action is required for application registration. The
application registration is automatically performed when you follow the
above instruction with a default value for the application name. The
default value is set by the package name, however if you are interested
to make the generated logs more readable you can use the following
method to pass the name of your application before starting feature
command (before step 1):
____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

registerApplication("application_name");

1.  [[configuration]]
Configuration
~~~~~~~~~~~~~
1.  [[accept-rules-for-participation]]
Accept Rules For Participation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted. While the application is
registered for the first time, in the first use, the terms and condition
user-interface will be shown to the end users. In the case of rejection,
no data collection will be performed unless in _preferences_ menu of the
QoE enabler, data collection is activated by the end-users. However
developers can also accept the term&conditions by calling the following
method:

setAccceptRule(**true**);
__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

[[set-questionnaire-likelihood]]
Set Questionnaire Likelihood
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
Questionnaire likelihood is the probability that a QoE questionnaire
will be fired. This probability can be set in range of 0 to 100. As an
example, if the likelihood is set to 20, it means that the probability
of firing the questionnaire in completion of the feature use would be 20
percent. This parameter can be configured by developers through the
following method’s call:

setQuestionnaireLikelihood(likelihood);
________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

[[set-data-submission-interval]]
Set Data Submission Interval
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________
The “data submission interval” configures the maximal timespan to wait
before the user is requested to share QoE and QoS data with the QoE/QoS
back-end application. When the end-users are not interested to submit
the questionnaire, the collected data on the mobile phone will be
submitted automatically and the log file in the mobile device will be
reset. The default value for this parameter is 15 days, which can be
extended to 90 days as well. This parameter can be configured by
developers through the following method’s call:
__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

setDataCollectionInterval(time_interval);

[[how-to-use-the-collected-data]]
How to use the collected data
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The generated logfile on the server is downloadable through the
following URL using your application_name as the paramter:

https://comsrv1.comproj.bth.se:8443/QoEAnalyticsServer/?app=application_name&token=Received_token

By calling the above URL, a window for confirmation of username and
password will be presented. The admin of QoE probe will provide you a
username and password. After a successful authentication, you can
download all records relevant to your application_name (Figure 4).

_____________________________
image:media/image3.png[image]
_____________________________

Figure : Opening logfile based on application_name

An example of downloaded file has been presented in Figure 3. For
analysis you can import the file to an excel-sheet in order to have data
in different columns.

[[section]]

[[appendix-i-java-doc]]
Appendix I: Java-Doc
~~~~~~~~~~~~~~~~~~~~

Package: com.bth.qoe

*Class MeteredActivity*

* android.app. Activity
** com.bth.qoe.MeteredActivity

[cols="",options="header",]
|=======================================================================
|Method Summary
|Modifier and Type |*Method and Description*

|void a|
__logApplicationOutput__(java.lang.String feature_name,
java.lang.String action_name)

Log application output

|void a|
__logFeatureCompleted__(java.lang.String feature_name)

Log the completion of the feature

|void a|
__logFeatureStart__(java.lang.String feature_name)

Log starting of the feature

|void a|
_logFireQuestionnaire_ (java.lang.String feature_name)

Log fire the questionnaire about the feature

|void a|
__logUserInput__(java.lang.String action_name)

Log user actions

|void a|
__OnResume__()

Start QoE Service

| a|
__OnStop__()

Stop QoE Service

|void a|
__registerApplication__(java.lang.String application)

It registers application to identify which application is using the QoE
application.

|void a|
__registerApplication__()

It registers application to identify which application is using the QoE
application.

|void a|
__setAccceptRule__(boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted.

|void |__setDataCollectionInterval__(int interval) 

|void |__setQuestionnaireLikelihood__(int likelihood) 

|void a|
__startQoEService__()

Start the service by creating an intent parameter and bind the activity
to the right service

|void |__stopQoEService__() 
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Methods inherited from class java.lang.Object
|equals, getClass, hashCode, notify, notifyAll, toString, wait, wait,
wait
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Method Details
|OnResume

a|
protected void OnResume()

Start the QoEService

|stopQoEService

a|
protected void onStop()

Stop the QoE Service

|startQoEService

a|
private void startQoEService(Context context)

Start the service by creating an intent parameter and bind the activity
to the right service

Parameters:

context -- the reference to the activity context

|stopQoEService

a|
Private void stopQoEService(Context context)

Unbind the connection

Parameters:

context -- the reference to the activity context

|registerApplication

a|
private void registerApplication()

It registers application to identify which application is using the QoE
application. The method calculate the mobile_id as the user_id

|registerApplication

a|
public void registerApplication(java.lang.String application)

It registers application to identify which application is using the QoE
application. The method calculate the mobile_id as the user_id

Parameters:

application - name of the application

|logFeatureStart

a|
public void logFeatureStart(java.lang.String feature_name)

Log starting of the feature

Parameters:

feature_name - the name of feature that is going to be started

|logUserInput

a|
public void logUserInput(java.lang.String action_name)

Log user actions

Parameters:

action_name - name of the user action (e.g. "submit login form")

|logApplicationOutput

a|
public void logApplicationOutput(java.lang.String action_name)

Log application output

Parameters:

action_name - name of the application output (e.g. Display error message
)

|logFeatureCompleted

a|
public void logFeatureCompleted(java.lang.String feature_name)

Log the completion of the feature

Parameters:

feature_name - name of the feature

|logFireQuestionnaire

a|
public void logFireQuestionnaire(java.lang.String feature_name)

Log fire questionnaire for the relevant feature

Parameters:

feature_name - name of the feature

|setQuestionnaireLikelihood

a|
public void setQuestionnaireLikelihood(int likelihood)

Parameters:

likelihood - the probability that a QoE questionnaire will be fired.
This probability can be set in range of 0 to 100. As an example, if the
likelihood is set to 20, it means that the probability of firing the
questionnaire in completion of the feature use would be 20 percent. The
user can set it through the preferences menu, but it can be also
implemented.

|setDataCollectionInterval

a|
public void setDataCollectionInterval(int interval)

Parameters:

interval - configures the maximal time-span to wait before the user is
requested to share QoE and QoS data with the QoE/QoS back-end
application. When the end-users are not interested to submit the
questionnaire, the collected data on the mobile phone will be submitted
automatically and the log file in the mobile device will be reset. The
default value for this parameter is 15 days, which can be extended to 90
days as well. This parameter is configured by the end-user as well as by
a code implementation.

|setAccceptRule

a|
public void setAccceptRule(boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted. This acceptance or
rejection can be also done by code.

Parameters:

accepted_terms - true of the informed consent is accepted, otherwise
false.

|=======================================================================

Package: com.bth.qoe

*Class MeteredActionBarActivity*

* support.v7.app.ActionBarActivity
** com.bth.qoe.MeteredActionBarActivity

public class MeteredActionBar extends support.v7.app.ActionBarActivity

[cols="",options="header",]
|=======================================================================
|Method Summary
|Modifier and Type |*Method and Description*

|void a|
__logApplicationOutput__(java.lang.String feature_name,
java.lang.String action_name)

Log application output

|void a|
__logFeatureCompleted__(java.lang.String feature_name)

Log the completion of the feature

|void a|
__logFeatureStart__(java.lang.String feature_name)

Log starting of the feature

|void a|
_logFireQuestionnaire_ (java.lang.String feature_name)

Log fire the questionnaire about the feature

|void a|
__logUserInput__(java.lang.String action_name)

Log user actions

|void a|
__OnResume__()

Start QoE Service

| a|
__OnStop__()

Stop QoE Service

|void a|
__registerApplication__(java.lang.String application)

It registers application to identify which application is using the QoE
application.

|void a|
__registerApplication__()

It registers application to identify which application is using the QoE
application.

|void a|
__setAccceptRule__(boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted.

|void |__setDataCollectionInterval__(int interval) 

|void |__setQuestionnaireLikelihood__(int likelihood) 

|void a|
__startQoEService__()

Start the service by creating an intent parameter and bind the activity
to the right service

|void |__stopQoEService__() 
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Methods inherited from class java.lang.Object
|equals, getClass, hashCode, notify, notifyAll, toString, wait, wait,
wait
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Method Details
|OnResume

a|
protected void OnResume()

Start the QoEService

|stopQoEService

a|
protected void onStop()

Stop the QoE Service

|startQoEService

a|
private void startQoEService(Context context)

Start the service by creating an intent parameter and bind the activity
to the right service

Parameters:

context -- the reference to the activity context

|stopQoEService

a|
Private void stopQoEService(Context context)

Unbind the connection

Parameters:

context -- the reference to the activity context

|registerApplication

a|
private void registerApplication()

It registers application to identify which application is using the QoE
application. The method calculate the mobile_id as the user_id

|registerApplication

a|
public void registerApplication(java.lang.String application)

It registers application to identify which application is using the QoE
application. The method calculate the mobile_id as the user_id

Parameters:

application - name of the application

|logFeatureStart

a|
public void logFeatureStart(java.lang.String feature_name)

Log starting of the feature

Parameters:

feature_name - the name of feature that is going to be started

|logUserInput

a|
public void logUserInput(java.lang.String action_name)

Log user actions

Parameters:

action_name - name of the user action (e.g. "submit login form")

|logApplicationOutput

a|
public void logApplicationOutput(java.lang.String action_name)

Log application output

Parameters:

action_name - name of the application output (e.g. Display error message
)

|logFeatureCompleted

a|
public void logFeatureCompleted(java.lang.String feature_name)

Log the completion of the feature

Parameters:

feature_name - name of the feature

|logFireQuestionnaire

a|
public void logFireQuestionnaire(java.lang.String feature_name)

Log fire questionnaire for the relevant feature

Parameters:

feature_name - name of the feature

|setQuestionnaireLikelihood

a|
public void setQuestionnaireLikelihood(int likelihood)

Parameters:

likelihood - the probability that a QoE questionnaire will be fired.
This probability can be set in range of 0 to 100. As an example, if the
likelihood is set to 20, it means that the probability of firing the
questionnaire in completion of the feature use would be 20 percent. The
user can set it through the preferences menu, but it can be also
implemented.

|setDataCollectionInterval

a|
public void setDataCollectionInterval(int interval)

Parameters:

interval - configures the maximal time-span to wait before the user is
requested to share QoE and QoS data with the QoE/QoS back-end
application. When the end-users are not interested to submit the
questionnaire, the collected data on the mobile phone will be submitted
automatically and the log file in the mobile device will be reset. The
default value for this parameter is 15 days, which can be extended to 90
days as well. This parameter is configured by the end-user as well as by
a code implementation.

|setAccceptRule

a|
public void setAccceptRule(boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted. This acceptance or
rejection can be also done by code.

Parameters:

accepted_terms - true of the informed consent is accepted, otherwise
false.

|=======================================================================

Package: com.bth.qoe

*Class Metered*

* java.lang.Object
** com.bth.qoe.Metered

public class Metered extends java.lang.Object

[cols="",options="header",]
|=======================================================================
|Nested Class Summary
|Modifier and Type |*Class and Description*

|class  a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.ActivityServiceConnection.html[_Metered.ActivityServiceConnection_]

It initializes the connection of the probe with an application.

|=======================================================================

[cols="",options="header",]
|=======================================================================
|Method Summary
|Modifier and Type |*Method and Description*

|static _Metered_
|file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#getInstance--[_getInstance_]() 

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#logApplicationOutput-java.lang.String-java.lang.String-[_logApplicationOutput_](java.lang.String feature_name,
java.lang.String action_name)

Log application output

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#logFeatureCompleted-java.lang.String-[_logFeatureCompleted_](java.lang.String feature_name)

Log the completion of the feature

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#logFeatureStart-java.lang.String-[_logFeatureStart_](java.lang.String feature_name)

Log starting of the feature

|void a|
_logFireQuestionnaire_ (java.lang.String feature_name)

Log fire the questionnaire about the feature

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#logUserInput-java.lang.String-java.lang.String-[_logUserInput_](java.lang.String action_name)

Log user actions

|void a|
__logApplicationOutput__(java.lang.String action_name)

Log application output

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#registerApplication-ContentResolver-java.lang.String-[_registerApplication_](ContentResolver content,
java.lang.String application)

It registers application to identify which application is using the QoE
application.

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#registerApplication-ContentResolver-java.lang.String-[_registerApplication_](Context
context, ContentResolver content)

It registers application to identify which application is using the QoE
application.

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#setAccceptRule-boolean-[_setAccceptRule_](boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted.

|void
|file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#setDataCollectionInterval-int-[_setDataCollectionInterval_](int interval) 

|void
|file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#setQuestionnaireLikelihood-int-[_setQuestionnaireLikelihood_](int likelihood) 

|void a|
file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#startQoEService-Context-[_startQoEService_](Context context)

Start the service by creating an intent parameter and bind the activity
to the right service

|void
|file:///Users/Farnaz/Documents/workspace/UsecaseApp/doc/com/bth/qoe/LogGeneratorarActivity.html#stopQoEService-Context-[_stopQoEService_](Context context) 

| |
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Methods inherited from class java.lang.Object
|equals, getClass, hashCode, notify, notifyAll, toString, wait, wait,
wait
|=======================================================================

[cols="",options="header",]
|=======================================================================
|Method Details
|getInstance

a|
public static __Metered__ getInstance()

Returns:

a static instance of the class

|startQoEService

a|
public void startQoEService(Context context)

Start the service by creating an intent parameter and bind the activity
to the right service

Parameters:

context -- the reference to the activity context

|stopQoEService

a|
public void stopQoEService(Context context)

Unbind the connection

Parameters:

context -- the reference to the activity context

|registerApplication

a|
public void registerApplication(ContentResolver context,
java.lang.String application)

It registers application to identify which application is using the QoE
application. The method calculate the mobile_id as the user_id

Parameters:

context – the reference to the Content Resolver

application - name of the application

|logFeatureStart

a|
public void logFeatureStart(java.lang.String feature_name)

Log starting of the feature

Parameters:

feature_name - the name of feature that is going to be started

|logFeatureStart

a|
public void logFeatureStart(java.lang.String feature_name, View view)

Log starting of the feature

Parameters:

feature_name - name of the feature

view - the view of the relevant activity

|logUserInput

a|
public void logUserInput(java.lang.String action_name)

Log user actions

Parameters:

action_name - name of the user action (e.g. "submit login form")

|logApplicationOutput

a|
public void logApplicationOutput(java.lang.String action_name)

Log application output

Parameters:

action_name - name of the application output (e.g. Display error message
)

|logFeatureCompleted

a|
public void logFeatureCompleted(java.lang.String feature_name)

Log the completion of the feature

Parameters:

feature_name - name of the feature

|logFireQuestionnaire

a|
public void logFireQuestionnaire(java.lang.String feature_name)

Log fire questionnaire for the relevant feature

Parameters:

feature_name - name of the feature

|setQuestionnaireLikelihood

a|
public void setQuestionnaireLikelihood(int likelihood)

Parameters:

likelihood - the probability that a QoE questionnaire will be fired.
This probability can be set in range of 0 to 100. As an example, if the
likelihood is set to 20, it means that the probability of firing the
questionnaire in completion of the feature use would be 20 percent. The
user can set it through the preferences menu, but it can be also
implemented.

|setDataCollectionInterval

a|
public void setDataCollectionInterval(int interval)

Parameters:

interval - configures the maximal time-span to wait before the user is
requested to share QoE and QoS data with the QoE/QoS back-end
application. When the end-users are not interested to submit the
questionnaire, the collected data on the mobile phone will be submitted
automatically and the log file in the mobile device will be reset. The
default value for this parameter is 15 days, which can be extended to 90
days as well. This parameter is configured by the end-user as well as by
a code implementation.

|setAccceptRule

a|
public void setAccceptRule(boolean accepted_terms)

To perform QoE/QoS data sharing within the trusted zone, the informed
consent for data sharing should be accepted. This acceptance or
rejection can be also done by code.

Parameters:

accepted_terms - true of the informed consent is accepted, otherwise
false.

|=======================================================================

[[appendix-ii-alternative-integration-solutions]]
Appendix II: Alternative integration solutions
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For the alternative solution for integration and experimentation of the
QoE probe, developers will make an instance from Metered Class and work
with that instance:

[[integration-1]]
Integration:
++++++++++++

1.  Install the QoE.apk in an android mobile where you test your mobile
application and the integration with the QoE probe.
2.  Add the qoe.jar to your project
3.  Import the Metered class

___________________________
import com.bth.qoe.Metered;
___________________________

1.  Create an instance of Metered class:

Metered metered=Metered.getInstance();

1.  In onResume method of your activity call the startQoEService method
from the Metered class:

__________________________________
@Override

*protected* *void* onResume()\{

**super**.onResume();

metered.startQoEService(**this**);

}
__________________________________

1.  In onStop method of your activity call stopQoEService method:

_____________________________________________________
@Override

*protected* *void* onStop() \{

**super**.onStop(); metered.stopQoEService(**this**);

}
_____________________________________________________

[[experimentation-1]]
Experimentation
+++++++++++++++

1.  Log application Registration. Call the registerApplication method
with contentResolver and application_name parameters to register your
application when you creating your activity:

________________________________________________________________________
metered.registerApplication(getBaseContext(), getContentResolver());

or metered.registerApplication(getContentResolver(),"application_name");
________________________________________________________________________

1.  Log start of the feature. In the line that the feature is started in
your application call logFeatureStart method.
+
metered.logFeatureStart("feature_name");
2.  Log completion of the feature. In the line that the feature is
completed in your application, call logFeatureCompleted method with the
feature_name as the paramter.
+
metered.logFeatureCompleted("feature_name");
3.  Log user Input. In the line that the user perform an action in your
application call logUserInput method with the action_name as the
parameter.
+
metered.logUserInput("Action1");
4.  Log application output. In the line that the application provides an
output for the users, call logApplicationOutput method with the
action_name as the parameters.
+
metered.logApplicationOutput("Action2");

[[section-1]]

[[appendix-iii-user-interfaces-of-the-qoe-probe]]
Appendix III: User Interfaces of the QoE probe
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

[cols=",",options="header",]
|=======================================
a|
_____________________________
image:media/image4.png[image]

Figure : QoE questionnaire
_____________________________

 a|
________________________________________
image:media/image5.png[image]

Figure : Welcome page after installation
________________________________________

a|
[cols=",",options="header",]
|==============================
a|
_____________________________
image:media/image6.png[image]
_____________________________

 |image:media/image7.png[image]
|==============================

Figure : Preferences

a|
_____________________________
image:media/image8.png[image]
_____________________________

Figure : Accept Rules for Participation

 a|
image:media/image9.png[image]

Figure : Set Questionnaire Likelihood

a|
______________________________
image:media/image10.png[image]
______________________________

Figure : Data Submission Interval

 a|
______________________________
image:media/image11.png[image]
______________________________

Figure : About QoE Service

| |
|=======================================
