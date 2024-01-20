# Firebase Cloud Messaging migration approach with Spring project

Repository to show example of the approaches which able to use when you decided to move away from legacy API before June 2024

## Blog post:

* [TH] [Spring WebFlux] Migrate legacy FCM APIs แล้วไปใช้ Firebase Admin SDK หรือ HTTP v1 ก่อนหัวจะไหม้
* [EN] Coming soon...

# Project structure

* MyFCMApp
  * Jetpack Compose project which decided to really simple to get FCM device token purpose
* push-notification
  * Spring WebFlux project created by JDK 21 and Spring WebFlux (Based on Spring Boot 3.2)
* Migrate FCM from legacy.postman_collection.json
  * Postman collection for everyone to import and playaround in your Postman 😆