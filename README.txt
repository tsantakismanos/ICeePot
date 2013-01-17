ICeePot 

main goal:
- periodically measuring the moisture (or any other) condition of plants in a pot with the use of moisture sensors
- accessing & managing that information remotely

hierarchy:
Client-Server hierarchy with an arduino ethernet board acting as a server which reads the moisture condition from analog pins & post this information on demand

folders:
- documentation: contains useful notes, diagrams about the project
- software: the project's code organized in the following way:
   - arduino_server: the code to be uploaded & executed on the arduino acting as a server
   - <whatever_technology_based>_client: any implementation that acts as a client and gets and manages information sent   by the server (e.g: swing_client, js_client, etc)
