# Refactoring List
(We write tests for reinforce, startup and fortify phase before refactoring)

1. ### remove duplicated showCommands   
    At build 1, showCommands such as showPlayer(), showAllPlayer(), showMap() exist in every controller including mapLoaderController, gameController, startUpGameController, reinforceGameController and fortifyGameController.

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")

    At build 2, we extract all showCommand methods and make them a static method in the MapDisplayUtils.class to avoid duplication. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")


2. ### move player state and corresponding logics (set and get current player, add and remove player, switch players) from the gameController to model named PlayerService.class. 

    At build 1
    
    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")

    At build 2

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")


3. ### remove game controller, unnecessary boolean values and unnecessary dependency of other controllers on gameController. This improves the readability and extensibility of the code. 
    
    At build 1, game controller has references of other controllers. It also has a lot of boolean values to determine which controller to call based on these boolean values. 
    
    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")
    
    At build 2, we remove game controller, its dependency on other controllers. We remove some of boolean values, instead we  use gamestate to determine which controller to call from the view that receives user's input. 
    
    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")

4. ### remove logic relating to reinforce(), fortify(), exchangeCards() from corresponding reinforceGameController, fortifyGameController to the Player.class. The use of these controllers is to validate the commands received from the view and call methods in the model. 

    At build 1   

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

    At build 2
    
    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

5.  ### add GameView and Controller as the interface
    At build 1, we have only CommandPromptView to receive users commands, and sent it to different controllers according to the game state. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

    At build 2, we have three different views, phaseView, dominationView and exchangeCardView. We make these three views implement GameViewInterface. This would make the system more flexible as we could replace these views in the future. The same case for the controllers, we would be able to replace these controllers easily in the future based on changing requirements. 

     ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

6. ### Refactor the logic in reinforcement
   At build 1, the validation and reinforce logic are all in the ReinforceGameController. The methods are long and complex, which makes them hard to understand and maintain.  

   ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

    At build 2, we split the validation and reinforce logic. We put the command validation in ReinforceGameController and the reinforce logic in the model. For the method in ReinforceController, we split the method based on the logic to make each function small and maintainable. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")


7. ### add observables to PlayerService and mapService.  
   At build 1, these model classes have a reference of view. When it modifies the data, it will call view.displayMethod() to display the changes in the model. 


   ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")


    At build 2, we use observer pattern. We make PlayerService and mapService extends from Observable, and attach the views as the observers. If there is the update in the model, it will notify the views and update the new changes to the view. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")
    
    
