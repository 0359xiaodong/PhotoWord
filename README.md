PhotoWord
=========
## Android Project

### Config project
> You must register on the respective sites to create your app and get the API's keys.
-   [Facebook developers](http://developers.facebook.com)
-   [Twitter developers](https://dev.twitter.com/)
-   [Aviary developers](developers.aviary.com)


1. In **src\com\dacopancm\photoword\helpers\Const.java** replace your API's keys:
       -       `public static String CONSUMER_KEY = "YOUR_CONSUMER_KEY";`
       -        `public static String CONSUMER_SECRET = "YOUR_CONSUMER_SECRET";`
  
2. In PhotoWord Project **AndroidManifest.xml** replace with your Aviary api key
    `<meta-data
            android:name="com.aviary.android.feather.v1.API_KEY"
            android:value="YOUR_AVIARY_API_KEY"/>`
  
3. In **res\values\strings.xml** replace your Facebook AppID:
      -        `<string name="face_appid">YOUR_FACEBOOK_APPID</string>`

=========
