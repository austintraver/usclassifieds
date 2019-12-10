# USClassifieds

## Authors

* [Austin Traver](https://github.com/austintraver)
* [Charlie Pyle](https://github.com/charliepyle)
* [Cameron Durham](https://github.com/camerondurham)
* [John Tanner](https://github.com/johntannerofficial)
* [Julian Zhang](https://github.com/julianzhang98)


## New Features and Bug Fixes

After finishing project testing, we have added new features to the application
to fulfill customer requests. Below are the changes in app functionality:

* Allow users to search only the listings from friends
* Add bought and sold metrics on user profiles
* Fixed bugs to allow users to add and cancel friend requests
* Mark items as sold allows seller to select who bought the item
* Allow better text readability on listing, profile, and home pages
* Fixed bugs where the **Back** button signed out users
* Fixed map loading bug causing **Map View** to crash when clicked

## Getting Started

In order to run the application in an emulator, you will need to send John Tanner the value of the `SHA1` portion returned by the following command. This is necessary in order to whitelist your key as the application is not running in a production environment. Once he has whitelisted the key, the application will allow you to sign in.

Command to run:

  ```sh
  keytool -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore
  ```

Sample output:

  ```txt
  Valid from: Mon Sep 30 10:26:19 PDT 2019 until: Wed Sep 22 10:26:19 PDT 2049
  Certificate fingerprints:
  	 SHA1: A1:CC:42:83:ED:1E:B9:63:AD:A6:57:A6:55:06:8B:81:EE:27:18:14
  	 SHA256: 6D:0D:84:91:52:C7:C6:B8:E8:8D:29:51:4F:7C:20:91:D9:81:4C:C8:E8:37:17:FF:F2:3E:2C:CA:EE:E3:97:43
  Signature algorithm name: SHA1withRSA
  Subject Public Key Algorithm: 2048-bit RSA key
  Version: 1
  ```

Once more, it is the certificate fingerprint value of `SHA1` that needs to be sent, and it should be sent to John Tanner, who has administrator access on the developer console.

## Launching the Application

Simply open this project in Android Studio (version `3.5` or higher), add a new configuration for an Android Application, and select a device. This application is designed to work with multiple devices in mind, but was particularly optimized for the `Pixel 3` (API 29), so selecting this advice will provide the most optimal user experience.

If an error occurs when running the `Make Project` command, please run the `Sync Project with Gradle Files` command first, which typically resolves problems faced by the end-user.
