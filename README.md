# PhotoWord

### Description
> Do you know which words can be formed with letters of another word? Type a word and you' ll find which others words can be formed with her and share it on your Facebook and Twitter accounts.
Funny, take a picture or choose it from your gallery, you can edit it as desired, enter a word and select a template and a color, discover the result and share it in your social networks.
You can add and display your favorite pictures.

### Video
> This is a small video showing the power of **PhotoWord**. Enjoy! [PhotoWord Promo](http://bit.ly/photowordpromo)

### License
> PhotoWord with its two code projects to Android &amp; Windows Phone has a GNU GENERAL PUBLIC LICENSE v3

### Autor
> Hi I'm Darwin Correa P. and I'm the unique autor of PhotoWord.

***
### Config project
> You must register on the respective sites to create your app and get the API's keys.
-   [Facebook developers](http://developers.facebook.com)
-   [Twitter developers](https://dev.twitter.com/)
-   [Aviary developers](developers.aviary.com).


***
## Android Project
### Version
 Current version: 1.0.2
### Config project
1. In **src\com\dacopancm\photoword\helpers\Const.java** replace your API's keys:
       -       `public static String CONSUMER_KEY = "YOUR_CONSUMER_KEY";`
       -        `public static String CONSUMER_SECRET = "YOUR_CONSUMER_SECRET";`
  
2. In PhotoWord Project **AndroidManifest.xml** replace with your Aviary api key
    `<meta-data
            android:name="com.aviary.android.feather.v1.API_KEY"
            android:value="YOUR_AVIARY_API_KEY"/>`
  
3. In **res\values\strings.xml** replace your Facebook AppID:
      -        `<string name="face_appid">YOUR_FACEBOOK_APPID</string>`

***
## Windows Phone 8.0 Project
> This app has been submitted on the Windows Phone Store 6 months ago and has 7000 + downloads [WindowsPhoneStore-PhotoWord](http://bit.ly/photowordapp).

> This project will be available soon, one bug was found this 23/03/2014 because Facebook changed your API's config.
I 'm working hard to correct it as soon as possible.

### Version
 Current version: 1.0.1
