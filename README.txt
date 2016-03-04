ICeePot 

GOAL
ICeePot solution is a way to help remotely accessing and managing pot moisture information. It combines several server-client technologies to gather, organize and provide moisture information in clients PC and mobile phones.

HIERARCHY
ICeePot follows the server-client architecture with the one (or several) pot server performing and saving moisture measurements and one or several mobile or desktop applications responsible for presenting moisture graphs per pot.

PROJECTS:
ICeePot/software folder contains the various projects implementing the Solutions functionality:
 - ICeePotServer: This is basically the source code of an arduino ethernet board. It reads the moisture condition from analog     pins &        saves this information on its SD card.
 - ICeePotLib: This is a library project and cointains the entities and tools used in the client Projects. It's a java project that uses        gradle to add itself in the local repository. 
 - ICeePotPC: This is a java application acting as an ICeePot client. It provides a UI to display moisture information and has dependencies    on ICeePotLib
 - ICeePotMobile: This is another ICeePot client implemented as an android application. It depens on ICeePotLib as well and provides           moisture aggregated information.
 - ICeePotWeb: This is soon to be another ICeePot client aimed to act as both a web UI and a API of the ICeePotServer. It will provide a REST API for ICeePotPC and ICeePotMobile clients which now contact directly ICeePotServer with the use of ICeePotLib


DOCUMENTATION:
ICeePot/documentation folder contains useful notes, diagrams about the project
  - notes: about tools used, code explanations, etc 
  - diagrams: flows, schematics, class diagrams, etc
