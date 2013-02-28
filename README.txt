ICeePot 

main goal:
- periodically measuring the moisture (or any other) condition of plants in a pot with the use of moisture sensors
- accessing & managing that information remotely

hierarchy:
Client-Server hierarchy with an arduino ethernet board acting as a server which reads the moisture condition from analog pins & post this information on demand

folders:
- documentation: contains useful notes, diagrams about the project
  - notes: about tools used,code explanations etc 
  - diagrams: flows, schematics, class diagrams etc
- software: the project's code organized in the following way:
   - ICeePotServer: the sketch to be uploaded & executed on the arduino acting as a server
   - ICeePotPC: java swing project which implements the client sending time requests and showing results returned in a graphic way
- hardware: schematic files, projects related to the hardware used for this solution
