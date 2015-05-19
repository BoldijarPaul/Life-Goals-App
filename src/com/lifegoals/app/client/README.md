## **Client API Documentation**

To consume the api you must:

 1. Get the Client and Entities package to your project.
 2. Resolve any package issues.
 
 
 For most of the requests you need an access token, you get it after logging in.
 You can set the token like this:
 
 ```Context.setToken(token); ``` 
 
 
 ###Trying to login

```LoginResult loginResult = ClientUserManagement.login(info);``` 
    
To check if the login was successful or not check the success value from the LoginResult class.
 
 

**User management**


###Adding a new user
This request does not require a token.

```ClientUserManagement.addUser(user);``` 
    
This returns the user if it was successfully added.

###Getting all users
This request does require a token.

```ClientUserManagement.getAllUsers();``` 
    
This returns a list of users if success.

#**Goals management**


###Adding a new goal
This request does require a token.
Don't forget to set all the goal fields.

```ClientGoalManagement.addGoal(goal);``` 
    
This returns the goal if it was successfully added.

###Deleting a goal
This request does require a token.

```ClientGoalManagement.deleteGoal(goal);``` 
    
This returns the goal if it was successfully deleted.

###Getting all goals
This request does not require a token.

```ClientGoalManagement.getAllGoals();``` 
    
This returns a list of goals if success.

#**Saved goals management**
A user can have more saved goals, a saved goal will be a goal that has a boolean done, which is true or false.
This can be viewed only by the user.

###Adding a new saved goal
This request does require a token.
Don't forget to set all the goal fields.

```ClientSavedGoalManagement.addSavedGoal(savedGoal);``` 
    
This returns the saved goal if it was successfully added.

###Deleting a goal
This request does require a token.

```ClientSavedGoalManagement.deleteSavedGoal(savedGoal);``` 
    
This returns the saved goal if it was successfully deleted.

###Getting all goals
This request does  require a token.

```ClientSavedGoalManagement.getUserSavedGoals(user);``` 
    
This returns a list of saved goals if success.

