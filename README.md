# Firebase Cloud Messaging migration approach with Spring project

Repository to show example of the approaches which able to use when you decided to move away from legacy API before June 2024

## Blog post:

* [TH] [[Spring WebFlux] Migrate legacy FCM APIs แล้วไปใช้ Firebase Admin SDK หรือ HTTP v1 ก่อนหัวจะไหม้](https://tpbabparn.medium.com/แนวทาง-migrate-legacy-fcm-apis-แล้วไปใช้-firebase-admin-sdk-หรือ-http-v1-ก่อนหัวจะไหม้-a39418fd0544)
* [EN] Coming soon...


# Brief description

Reference to Firebase document [Migrate from legacy FCM APIs to HTTP v1](https://firebase.google.com/docs/cloud-messaging/migrate-v1)

```
Apps using the deprecated FCM legacy APIs for HTTP and XMPP should migrate to the HTTP v1 API at the earliest opportunity. Sending messages (including upstream messages) with those APIs was deprecated on June 20, 2023, and will be removed in June 2024.
```

Therefore, We need to migrate the way to hitting Firebase API in order to send push notification **before June 2024**

So in this project will lead you to have newly setup to handling the policy of API Migration.

## Approach for injecting service account

* **By file**: In this approach, You can place service-account.json in your server or using Vault to injecting it to container or Kubernetes pod
* **By environment variable with base64**: You are able to handling how you can encrypt/sealing the data before injecting

## Approach for sending push notifications

* **By Firebase Admin SDK** - The approach to using Admin SDK if Firebase team already provided the supported programming languages - One of benefit is able to support sending multiple device
* **By HTTP v1** - The approach in programming agnostic but you need to using Google Auth for get access token in order to send push notifications - Limitation is this new HTTP v1 able to send only 1 token at a time

# Project structure

* **MyFCMApp**
  * Jetpack Compose project which decided to really simple to get FCM device token purpose

![This image show simple application to display device token from Firebase Cloud Messaging](/images/Screenshot_20240120-184010.png "Display device token from Firebase Cloud Messaging")

* **push-notification**
  * Spring WebFlux project created by JDK 21 and Spring WebFlux (Based on Spring Boot 3.2)

* **Migrate FCM from legacy.postman_collection.json**
  * Postman collection for everyone to import and playaround in your Postman 😆

  * For geek person who love curl
    ```shell
    curl --location 'http://localhost:8080/push/firebase-admin' \
      --header 'Content-Type: application/json' \
      --data '{
          "tokens": [ "{{token1}}", "{{token2}}"],
          "notification": {
              "title": "[ADMIN] Let'\''s go buy latest book",
              "body": "New book from technology category!"
          },
          "data": {
              "body": "Body of Your Notification in data",
              "title": "Title of Your Notification in data",
              "key1": "Value for key1",
              "key2": "Value for key1"
          }
      }'
    ```

    ```shell
    curl --location 'http://localhost:8080/push/google' \
      --header 'Content-Type: application/json' \
      --data '{
          "tokens": [ "{{token1}}", "{{token2}}"],
          "notification": {
              "title": "[OAuth2.0] Let'\''s go buy latest book",
              "body": "New book from technology category!"
          },
          "data": {
              "body": "Body of Your Notification in data",
              "title": "Title of Your Notification in data",
              "key1": "Value for key1",
              "key2": "Value for key1"
          }
      }'
    ```

    In case you want to test OAuth 2.0 with Google OAuth 2.0 playgroud

    Note: You need device token from Firebase Cloud Messaging in order you can publish new push notification

    ```shell
    curl --location --globoff 'https://fcm.googleapis.com/v1/projects/{{FIREBASE_PROJECT_ID}}/messages:send' \
      --header 'Authorization: Bearer {{ACCESS_TOKEN}}' \
      --header 'Content-Type: application/json' \
      --data '{
          "message": {
              "token": {{token}},
              "notification": {
                  "title": "Let'\''s go buy latest book",
                  "body": "New book from technology category!"
              },
              "data": {
                  "body": "Body of Your Notification in data",
                  "title": "Title of Your Notification in data",
                  "key1": "Value for key1",
                  "key2": "Value for key1"
              }
          }
      }'
    ```

# How to setup

Please kindly read above blog, below is for quickly setup 🙇‍♂️

* Jetpack Compose project - Put google-services.json under **app**

  ![Place google-services.json under app directory for Jetpack Compose project](/images/CleanShot%202567-01-21.jpg)

* Spring - Put service-account.json under resources (for able to use with ClassPathResource)

  ![Place service-account.json under resources](</images/CleanShot 2567-01-21 at 08.55.47@2x.jpg>)

* If related to environment variables, Using Intelij to injecting value from the IDE like below image

  ![Adding more environment variables in IntelijIDEA for Spring project](</images/CleanShot 2567-01-20 at 17.41.38@2x.jpg>)

  relate command (Don't forget to delete base64.txt)
  ```
  base64 -i service-account.json > base64.txt
  ```