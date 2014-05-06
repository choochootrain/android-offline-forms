Android Offline Form Entry
==========================
An app for queuing and asynchronously submitting form data

Components
----------

###Android app
Provide a JSON file to configure the form (see the [default form.json](https://github.com/choochootrain/android-offline-forms/blob/master/app/src/main/res/raw/form.json) for examples). The application will queue all form submissions until network access is available, and then send them out in batches.

###Test server
Used to intercept form submissions and debug application behavior
