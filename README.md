# AugustLockCredentials
Fetches the credentials for your August/Yale lock for use with AugustESP32 library.

You can install from PlayStore or using the APK in this repo, or build directly from this sourcecode.

PlayStore: https://play.google.com/store/apps/details?id=com.stormbird.augustcodereader

Required: Rooted Android phone (will not work unless phone is rooted).

Instructions for use:

1. Setup the lock using the official August or Yale app (can be done on your rooted or normal phone)
2. Install the official August/Yale app on the rooted phone
3. Authenticate the app on the rooted phone to use the lock. Test it by locking/unlocking the lock
4. Now switch off WiFi on the rooted phone
5. Operate the lock a few times, which will fall back to the BlueTooth connection, creating credentials in the app's private data area
6. The Credentials App can then be run on this phone, which will recover all the details you need:

![codereader](https://user-images.githubusercontent.com/12689544/189010890-50114c96-4cec-4c3e-89e9-da2c9842784c.jpg)

7. You will need the Address, BlueTooth Key and BlueTooth Index.

In your ESP32 code you would initialise the AugustESP32 library instance like this (given the results above):

```AugustLock augustLock("00:00:00:00:00:00", "1234567890ABCDEF1234567890ABCDEF", 4);```

See the example in the AugustESP32 library:

https://github.com/JamesSmartCell/AugustESP32/tree/main/examples/Simple

Install the app from the latest release here:

https://github.com/JamesSmartCell/AugustLockCredentials/releases
