# Refactoring List
(We write tests for reinforce and startup and fortify phase before refactoring)

1. ### remove duplicated showCommand   
    At build 1, showCommand in every controller including mapLoaderController, gameController, startUpGameController, reinforceGameController and fortifyGameController.

    ![alt text](https://github.com/Nithish58/RISC/blob/master/images/build1/showPlayerAllCountriesFortification().png "showPlayer()")

    At build 2, we extract all showCommand methods and make them a static method in the MapDisplayUtils.class to avoid duplication. 

    ![alt text](https://github.com/Nithish58/RISC/blob/master/images/build2/showMap().png "showPlayer()")


2. ### move player state and corresponding logics (list of players, current player, add players, remove players, switch players) from the gameController to model named PlayerService.class. 

    Build 1
    
    ![alt text](https://github.com/Nithish58/RISC/blob/master/images/build1/switchPlayers().png "switch player")

    At build 2

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build two")


3. ### remove game controller and dependency of controllers on each other. This improves the readability and extensibility of the code. 
    At build 1 
    At build 2 

4. ### remove logic relating to reinforce(), fortify(), exchangeCards(), to the Player.class 
   At build 1, we put the logic reinforce() and exchangeCards() in ReinforceGameController, fortify() in FortifyGameController.   

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

    At build 2, we move these logic in the Player.class. The use of controllers is to check and validate the commands from the view. if the command is valid, call corresponding method in the model to implement the logics. 


5.  ### add GameView and Controller as the interface
    At build 1, we have only CommandPromptView to receive users commands, and sent it to different controllers according to the game state. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

    At build 2, we have three different views, phaseView, dominationView and exchangeCardView. We make these three views implement GameViewInterface. This would make the system more flexible if we would like to replace view in the future. The same case for the controllers, we would be able to replace these controllers easily in the future based on changing requirements. 

     ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")

6. ### Refactor the logic in reinforcement
   At build 1, the reinforceGameController method is large, making the code hard to read and maintain. 

   ![alt text](https://github.com/Nithish58/RISC/blob/master/images/build1/reinforce()%20build1.png "reinforce build 1")

    At build 2, We split the function based on logic to make each function small and maintainable. 

    ![alt text](https://github.com/Nithish58/RISC/blob/master/images/build2/reinforceRefactoring.png "reinforce build 2")


7. ### add observables to PlayerService and mapService.  
   At build 1, these model classes have a reference of view. When it modifies the data, it will call view.displayMethod() to display the changes in the model. 


   ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")


    At build 2, we use observer pattern. We make PlayerService and mapService extends from Observable, and attach the views as the observers. If there is the update in the model, it will notify the views and update the new changes to the view. 

    ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "build one")
    
    
