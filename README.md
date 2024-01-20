# Firebase Cloud Messaging migration approach with Spring project

Repository to show example of the approaches which able to use when you decided to move away from legacy API before June 2024

## Blog post:

* [TH] [Spring WebFlux] Migrate legacy FCM APIs แล้วไปใช้ Firebase Admin SDK หรือ HTTP v1 ก่อนหัวจะไหม้
* [EN] Coming soon...


# Brief description

So in this project will lead you to have newly setup to handling the policy of API Migration before June 2024

## Approach for injecting service account

* **By file**: In this approach, You can place service-account.json in your server or using Vault to injecting it to container or Kubernetes pod
* **By environment variable with base64**: You are able to handling how you can encrypt/sealing the data before injecting

## Approach for sending push notifications

* **By Firebase Admin SDK** - The approach to using Admin SDK if Firebase team already provided the supported programming languages - One of benefit is able to support sending multiple device
* **By HTTP v1** - The approach in programming agnostic but you need to using Google Auth for get access token in order to send push notifications - Limitation is this new HTTP v1 able to send only 1 token at a time

# Project structure

* **MyFCMApp**
  * Jetpack Compose project which decided to really simple to get FCM device token purpose
* **push-notification**
  * Spring WebFlux project created by JDK 21 and Spring WebFlux (Based on Spring Boot 3.2)
* **Migrate FCM from legacy.postman_collection.json**
  * Postman collection for everyone to import and playaround in your Postman 😆