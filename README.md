Member added:
* Nithish
* Bikash
* Binsar
* Tirathraj
* Jenny 


-----------------------------------------------------------------------------------------------------------------------------
meeting brief
October 25, 2019
Attendees: Bikash, Binsar, Nitish, Keshav, Jenny
Deadline: by next Monday(Oct 25) 

By next Monday(Oct 25), everyone reviews the grading schema and selects which part you would like to do. Consider carefully how you will implement the part you would like to do. (This would be easier for us to distribute tasks)


Jenny
DONE: 
1. Add view interface that each concrete view will inherite from it. 
2. Refactor the code: remove duplicate showmap() code and extract it to the mapDisplayUtil.class

TODO:
Wait for Keshav refactoring 

Bikash:
Wait for Keshav refactoring 

Keshav
TODO: 
	1. Move the player information to the model (create a PlayerList class in model and put all logic concerning players like store,switch and manger players in this class)
	2. Implement observable to playerList.class and add view as the observer  
	3. Remove unnecessary parameters in controller constructor as controller should be connect to the model and views not the controllers. 
	4. Remove boolean values (this is really hard to manage in the codes. I don't have a great solution for that, but if it were me, I will do like this: 
	Users cannot populate countries if the player has not been added. if user enter populate country command, it will call the populatecountry(), it first will check the playersList, if there is no player, the code will throw an exception. 
	There is a method in your code to catch the exception, if the exception is catched, it will call view.displayMessage("no player at this moment, cannot populate countries");
	
Note: You don't need to finish this part on Monday. This is really a hard task. If you could refactoring only the architecture, we would be able to start working the build 2 next week.
The logic would definitely need to be fixed if you change the architecture. But we can help you with that in the following week. 


Binsar:
 Wait for Keshav refactoring 

Nitish: 
Wait for Keshav refactoring 
------------------------------------------------------------------------------------------------------------------------------



October 4, 2019
Attendees: Bikash, Binsar, Keshav, Jenny 

Bikash: (By October 11th)
Architectural documentation 
Convention of coding 
Have a basic version of Reinforcement/fortification phase 
Add Javadoc for Reinforcement/fortification phase 


Keshav: ( by October 11th)
Architectural documentation 
Coding convention 
A working version of startup phase 
Add Javadoc for startup phase 

Binsar:(by October 11th)
Write tests for maploader, mapService 
Write possible tests for Keshav, and Bikash 
Add Javadoc for Tests

Jenny(By October 11th)
Implemented all commands of map loader phase 
fix the bugs for the maploader 
Add Javadoc for maploader and mapService, country and continent 


Nitish: TBD 
Have a basic version of Reinforcement/fortification phase 
Add Javadoc for Reinforcement/fortification phase 





October 11, 2019
Attendees: Bikash, Binsar, Keshav, Jenny, Nitish 

Bikash: 
DONE: 
Architectural documentation 
Convention of coding

TODO: 
Reinforce countryName num (missing logic for soldier calculation on continent power)
Add java doc 

Keshav: 
DONE:
Architectural documentation 
Convention of coding 
Loadmap filename 
Gmaplayer -add playername -remove playername 
Populatecountries 

TODO:
Showmap 
Placearmy country 
Placeall 
Add javadoc 


Binsar:
DONE: 
Write tests for maploader, mapService 
Add Javadoc for Tests

TODO:
Write tests for Nitish fortify stage 
Write tests for Bikarsh for reinforcement(IMPORTANT)
Write tests for Keshav 



Jenny
Merge code with Keshav 
fix mapservice if bugs report 



Nitish: 
DONE:
Fortify fromcountry tocountry num 
Fortify none

TODO:
Fix possible bugs according to Bisar's tests 
Merge code with Keshav branch 


