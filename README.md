# Life Goals App

#### Tehnologii / programe folosite :

 * limbajul de programare JAVA
 * [Android SDK](https://developer.android.com/sdk/index.html) 
 * [Jersey](https://jersey.java.net) pentru realizarea serviciului web 
 * [Apache Tomcat](http://tomcat.apache.org/) pentru a rula serviciul web  
 * [GSON](https://github.com/google/gson) pentru a crea un client în JAVA pentru a consuma API-ul.
 * diverse librării pentru un design mai plăcut, ele se pot găsi [aici](https://github.com/BoldijarPaul/Life-Goals-App/blob/master/app/build.gradle)
 * [Facebook API v2.3](https://developers.facebook.com/) pentru comunicarea cu o pagina de Facebook
 * [Inkscape](http://inkscape.org/) pentru realizarea logo-ului și altor iconițe, unele dintre ele au fost preluate de pe site-ul [Flaticon](http://www.flaticon.com/) după care au fost editate.
 
 
  #### Descriere
 
 Life Goals App reprezintă o aplicație pentru android unde putem să ne salvăm un calendar de evenimente sau activitați pe care dorim să le realizam pe parcursul vieții, astfel încăt să nu avem vreun moment în care să ne gândim că 'nu avem ce face'.
 După înregistrare putem adăuga , vizualiza, salva, edita aceste activități și le putem confirma realizarea din agenda noastră.
 Pe langă asta putem vedea un timeline de poze pe aceași temă, 'Goals', le putem adăuga la favorite și putem chiar să realizăm un colaj cu ele, și distribui mai departe.

 
 #### API-ul aplicației
 
 Această aplicație consumă un serviciu web scris în Java.
 Sursa se poate găsi [aici](https://github.com/BoldijarPaul/Life-Goals-Api).
 
 Clientul se poate găsi [aici](https://github.com/BoldijarPaul/Life-Goals-Api/tree/master/src/main/java/com/lifegoals/app/client)
 
 ###### Documentație API
 
   **POST /users/login** - se incearcă autentificarea, trebuie trimise [informațiile de logare](https://github.com/BoldijarPaul/Life-Goals-Api/blob/master/src/main/java/com/lifegoals/app/entities/LoginInfo.java), parola fiind criptată pe 512 biți, se returnează un [obiect](https://github.com/BoldijarPaul/Life-Goals-Api/blob/master/src/main/java/com/lifegoals/app/entities/LoginResult.java) ce conține statusul, și tokenul daca autentificarea a avut loc cu succes.
 
 **GET /colors** - un JSON ce conține culorile cu care se poate adăuga o nouă postare, în HEX.
 ```javascript
 [
-431858,
-3211249,
-4179669,
-1337522
]
```
 
  **GET /goals/getall** - un JSON ce conține o listă cu toate postările. În viitor ele vor fi listate pe pagini.
 ```javascript
 [
  {
    color: -2422181,
    id: 458,
    publicGoal: true,
    text: "Get a tatoo",
    userId: 917,
    visible: true
  },
  {
    color: -13330213,
    id: 214,
    publicGoal: true,
    text: "Visit Paris",
    userId: 917,
    visible: true
  },
  
]
```
 
  **POST /goals/getfromuser** - o listă cu toate postările unui user. Userul trebuie trimis ca body. De asemenea este nevoie de un header cu acces token , deoarece un utilizator poate să vadă toate postarile lui, nu și al altor persoane.

 **POST /goals/add** - adaugă o nouă postare, și este returnată daca trece de validări, necesită acces token, goal-ul este trimis ca body.
 
  **DELETE /goals/delete** - șterge o postare, mai bine zis o ascunde, fiindcă este posibil ca alte persoane să fii salvat deja, necesită acces token, iar goal-ul este trimis ca body.
  
   
  **POST /users/add** - adaugă un nou user, adică realizează înregistrarea. Se returnează ca răspuns un [obiect](https://github.com/BoldijarPaul/Life-Goals-Api/blob/master/src/main/java/com/lifegoals/app/entities/RegisterResponse.java) care conține rezultatul înregistrării, success / email existent / nume existent, etc.
  
  **POST /users/getname** - returnează un String cu numele userului, trebuie trimis în body userul , doar id-ul se verifică.
  
   **POST /savedgoals/getall** - returnează toate goal-urile salvate de un user, trebuie trimis userul ca body, și este nevoie de un acces token pentru acest request.
   
   **POST /savedgoals/add** - adaugă un nou goal salvat pentru un user este nevoie de un acces token pentru acest request, entitatea SavedGoal trebuie trimisă ca body.
   
   **DELETE /savedgoals/delete** - șterge un gol salvat, necesită un acces token, goal-ul salvat trebuie trimis ca body.
   
   **PUT /savedgoals/update** - editează un gol salvat, necesită un acces token, goal-ul salvat trebuie trimis ca body.
 
 

 
 

