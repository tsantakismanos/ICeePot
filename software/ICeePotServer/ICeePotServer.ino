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
#include <NTP.h>


#define anl_pins_counter 1
#define interval_in_min 60 //time interval (in minutes) between measurementσ
#define max_months_in_sd 12
#define debug_mode false

//helper functions declaration
void get_measurements();
void store_row_to_sd(String row, time_t now_in_secs);
void send_file_rows_to_client(EthernetClient client, char* date);
void cfg_pins_array();

//analog pins array
short anl_pins[anl_pins_counter];

//variable for periodic measurement
time_t last_measur_time = 0;
boolean isSynchronized = false;

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

//this server's IP
byte ip[] = { 192, 168, 1, 20 };    //the inner IP of the server

IPAddress ntp_server_ip(64, 90, 182, 55); // time.nist.gov NTP server

//server instantiation
EthernetServer server(3629);


//basic initiating setup
void setup() {
  
  #ifdef debug_mode
    Serial.begin(9600);
  #endif
  
  //network & ICeePot server initialization
  Ethernet.begin(mac, ip);
  server.begin();
  
  isSynchronized = false;
  
  //set to now
  //setTime(23,30,00,4,2,2013);
  unsigned long now_in_seconds = synchronize_time(ntp_server_ip);
  if(now_in_seconds != 0){
    setTime(now_in_seconds);
    isSynchronized = true;
    #ifdef debug_mode
      Serial.println("Time synchronized: "+now_in_seconds);
      Serial.flush();
    #endif
  }
  else{
    #ifdef debug_mode
      Serial.println("Time not synchronized");
      Serial.flush();
    #endif
  }

  cfg_pins_array();
  
  #ifdef debug_mode
    Serial.println("Ethernet and server set");
    Serial.flush();
  #endif


  //SD neccessary calls
  pinMode(10, OUTPUT);
  if(SD.begin(4) == false){
    #ifdef debug_mode
      Serial.println("Error in SD initialization");
      Serial.flush();
    #endif
    return;
  }
  
}



//forever
void loop() {
  
  //recheck for time synchronization
  if(isSynchronized == false){
    unsigned long now_in_seconds = synchronize_time(ntp_server_ip);
    if(now_in_seconds != 0){
      setTime(now_in_seconds);
      isSynchronized = true;
      #ifdef debug_mode
        Serial.println("Time synchronized: "+now_in_seconds);
        Serial.flush();
      #endif
    }
  }
  
  get_measurements();
  
  char server_msg[20];
  byte srv_msg_cnt;
  
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client){
    
    #ifdef debug_mode
      Serial.println("Client connected");
      Serial.flush();
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
            Serial.flush();
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


/* helper method that gets the moisture values from all pins
   if a specified ammount of time (defined in mins) has passed
*/
void get_measurements(){
  
  //time value to be kept in each row
  //unsigned long now_in_millis = millis();
  
  //time value to be compared for interval & file creation
  time_t now_in_secs = now();
  
  String row;
  int anl_value = 0;
  
  if((now_in_secs - last_measur_time) > (60 * interval_in_min)){
   
      #ifdef debug_mode
        Serial.println("Time for measurement "+String(now_in_secs));
        Serial.flush();
      #endif
    
    for(byte i=0; i<anl_pins_counter; i++){
      
      anl_value = analogRead(anl_pins[i]);
      //row = String(day(now_in_secs)) + "|" + String(hour(now_in_secs))  + ":" + String(minute(now_in_secs)) + ":" + String(second(now_in_secs)) + "|" + String(anl_pins[i]) + "|" + String(anl_value);
      row = String(now_in_secs) + "|" + String(anl_pins[i]) + "|" + String(anl_value);
      store_row_to_sd(row, now_in_secs);
      
      //delay between each writing      
      delay(500);
    }
    last_measur_time = now_in_secs;
  }
}


/* helper method that writes a given row to the SD output file
*/
void store_row_to_sd(String row, time_t now_in_secs){
  
  File file;
  
  short bytes_written = 0;
     
  //construct the appropriate filename
  byte now_month = month(now_in_secs); //TODO: add 7200 seconds
  int now_year = year(now_in_secs);  //TODO: add 7200 seconds
  String filename_str = String(now_month) + String(now_year) + ".txt";
  
  #ifdef debug_mode
        Serial.println("File to write row to: "+filename_str);
        Serial.flush();
  #endif
  
  char filename[filename_str.length() + 1];
  filename_str.toCharArray(filename, filename_str.length() + 1);

  //open the file  
  file = SD.open(filename, FILE_WRITE);
 
  //format the row to be written
  char row_in_chars[row.length()+1];
  row.toCharArray(row_in_chars,row.length()+1);
  
  bytes_written = file.println(row_in_chars);

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
            Serial.flush();
      }
    #endif
  
  if(!file){
     //client.println("Measurements not present yet...");
     //client.flush();
     
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
    Serial.flush();
  #endif

}



