# dropout2

## Concept, Gameplay and Controls
Sequel to DROPOUT written in Java.

The game takes place in a zombie infested world.

The goal of the game is to eliminate all the zombies in order to advance to the next level. Zombies are enemies that detect and chase the player if they get close enough. If they reach the player, they will attack until the player dies.

The player has vertical and horizontal mobility (using the WASD keys) and can use two short-range attacks (Q and E) or long-range attacks with a power-up. These attacks make the player invulnerable and allow them to pass through obstacles.

The player also controls a support medic character (using the UP, DOWN, LEFT, RIGHT keys) who cannot attack but can revive players through physical contact if they are immobilized.

After eliminating all zombies on a level, the player advances to the next. The game is won after all the zombies on the final level are defeated.

![image](https://github.com/user-attachments/assets/bf30fe28-744f-4a91-b14b-2962ebfec19c)


## Technology Used

For this project I used Java AWT (for the gameplay part), Java Swing (for the menus, obviously) and SQLite for managing the "SAVES" database.

## Class Diagram w/ notable classes

![image1](https://github.com/user-attachments/assets/9a531c92-8ec7-4126-a695-6193b21ca200)



## Notable Design Patterns Used

## State

Everything, from the **GAME** class itself to the **MENU-type** classes and the **ENTITY-type** classes (players and enemies) uses the State pattern. It's pretty self explainatory as to why I need to use it on the player and enemies. The menus make use of it to be able to switch between them more easily, and manipulate the state of the **GAME** class itself.

![image](https://github.com/user-attachments/assets/833661dc-2360-4287-ace4-209037b2a01b)


### Singleton

Most menus (most notably the LOAD GAME MENU) are singletons. This allows me to "initialize" them only when the game starts, and that makes the transition when calling them smoother as they always exist, and when I change the state of the game I simply change what is displayed.

### (Abstract) Factory

### Observer

### Strategy

A particular type of Zombie changes the 

