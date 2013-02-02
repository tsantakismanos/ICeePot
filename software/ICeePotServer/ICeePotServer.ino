/* 
  author: tsantakis manos
  date: 16/01/2013
  licence: GPL
******************************  
  description:
   server sketch to be uploaded & run on an arduino board
   with ethernet interface and SD card support.
   This server performs 2 tasks:
    - taking periodical measurements of analog input taken from arduino analog pins 
      & writing this information to the SD card
    - responding to a client request containing the char g (GET)
      to the given port & IP & sending the contents of the SD card
      in the form: <time>|<analog_pin>|value     
*/

#include <SD.h>
#include <Ethernet.h>
#include <SPI.h>
#include <Time.h>
//#include <NTP.h> TODO: implement 

#define anl_pins_counter 1
#define interval_in_min 15  //time interval (in minutes) between measurement
#define max_months_in_sd 12
//#define debug_mode true

//helper functions declaration
String get_moist_from_sensor_in_row(short anl_pin);
void get_measurements();
void store_row_to_sd(String row);
void send_file_rows_to_client(EthernetClient client, char* date);
void cfg_pins_array();
String get_filename_based_on_date();

//analog pins array
short anl_pins[anl_pins_counter];

//variable for periodic measurement
unsigned long last_measur_time = 0;

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

//this server's IP
IPAddress server_ip(192,168,1,20);

//server instantiation
EthernetServer server(3629);


//basic initiating setup
void setup() {
  
  //TODO: implement synchronize_time();
  #ifdef debug_mode
    Serial.begin(9600);
  #endif
  
  cfg_pins_array();
  
  //network & ICeePot server initialization
  Ethernet.begin(mac, server_ip);
  server.begin();
  
  #ifdef debug_mode
    Serial.println("Ethernet and server set");
  #endif


  //SD neccessary calls
  pinMode(10, OUTPUT);
  if(SD.begin(4) == false){
    #ifdef debug_mode
      Serial.println("Error in SD initialization");
    #endif
    return;
  }
  
}



//forever
void loop() {
  
  get_measurements();
  
  char server_msg[20];
  byte srv_msg_cnt;
  
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client){
    
    #ifdef debug_mode
      Serial.println("Client connected");
    #endif
    
    //new message to be read
    srv_msg_cnt = 0;
    
    while (client.connected()){
      if (client.available()){
        
        char c = client.read();
        
        server_msg[srv_msg_cnt] = c;
        srv_msg_cnt++;
        
        if(c == '\0'){
          
          #ifdef debug_mode
            String s(server_msg);
            Serial.println("Got the message: "+s);
          #endif
    
          send_file_rows_to_client(client,server_msg);

          break;
        }
      }
    }   
    delay(100);
    // close the connection:
    client.stop();
  }
}


//helper method definitions

/* helper method that reads the sensor value from the
*  pin declared and returns a row of the form:
*  time|pin|value
*/
String get_value_from_sensor_in_row(short anl_pin){
  
  int anl_val = analogRead(anl_pin);
  
  //the elapsed time in millis from the arduino powered on moment
  time_t now_in_millis = now();

  String res = String(now_in_millis) + "|" + String(anl_pin) + "|" + String(anl_val);
  
  return res;
}

/* helper method that gets the moisture values from all pins
   if a specified ammount of time (defined in mins) has passed
*/
void get_measurements(){
  
  unsigned long now = millis();
  String row;
  
  if((now - last_measur_time) > (60000 * interval_in_min)){ 
    
    for(byte i=0; i<anl_pins_counter; i++){
      row = get_value_from_sensor_in_row(anl_pins[i]);
      store_row_to_sd(row);
            
      last_measur_time = now;
    }
    delay(1000);
    
  }
}

/*helper method which creates & returns a string
 containing the filename which is produced from the 
 day/month/year concatenation */
String get_filename_based_on_date(){
  
  time_t now_in_millis = now();

  byte now_day = day(now_in_millis);
  byte now_month = month(now_in_millis);
  int now_year = year(now_in_millis);
  
  String filename_str = String(now_day) + String(now_month) + String(now_year) + ".txt";
  
  return filename_str;
}

/* helper method that writes a given row to the SD output file
*/
void store_row_to_sd(String row){
  
  File file;
  
  short bytes_written = 0;
     
  //get the appropriate filename
  String filename_str=get_filename_based_on_date();
  
  #ifdef debug_mode
        Serial.println("File to write row to: "+filename_str);
  #endif
  
  char filename[filename_str.length() + 1];
  filename_str.toCharArray(filename, filename_str.length() + 1);

  //open the file  
  file = SD.open(filename, FILE_WRITE);
  

    #ifdef debug_mode
      if(!file){
        Serial.println("File to write cannot be opened: "+filename_str);
      }
      else{
        Serial.println("File opened for writing: "+filename_str);
      }
    #endif
  
 
  //format the row to be written
  char row_in_chars[row.length()+1];
  row.toCharArray(row_in_chars,row.length()+1);
  
  bytes_written = file.println(row_in_chars);
  
  #ifdef debug_mode
    if(bytes_written == 0){
     Serial.println("problem in writing row value to file");
    }
    else{
      Serial.println("writen to file the row: "+row);
    }
  #endif

  file.flush();
  file.close();
}

/* helper method that sends the content of the SD file
  to the client given
*/
void send_file_rows_to_client(EthernetClient client, char* date){
  
  char d;
  
  File file = SD.open(date);
  
  #ifdef debug_mode
      if(!file){
           String s(date);
            Serial.println("File to read from cannot be opened: "+s);
      }
    #endif
  
  if(!file){
     client.println("Measurements not present yet...");
     client.flush();
     
   }
   
   while(file.available()>0){
      d = file.read();
      client.write(d);
      client.flush();
    }
   
   file.close();  
    
}

/* helper method configuring the analog input pins*/
void cfg_pins_array(){
  
  anl_pins[0] = 0;
  
  #ifdef debug_mode
    Serial.println("Configuring pins done");
  #endif

}

