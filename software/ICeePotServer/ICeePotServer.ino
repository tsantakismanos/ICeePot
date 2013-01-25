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
#define interval_in_min 1  //time interval (in minutes) between measurement
#define months_in_SD 24

//helper functions declaration
String get_moist_from_sensor_in_row(short anl_pin);
void get_measurements();
void store_row_to_sd(String row);
void send_all_rows_to_client(EthernetClient client);
void configure_anl_pins();
String get_filename_based_on_now();

//analog pins array
short anl_pins[anl_pins_counter];

//variable for periodic measurement
unsigned long last_measur_time = 0;

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

//this server's IP
IPAddress server_ip(192,168,1,20);

//server instantiation
EthernetServer server(3629);

String filenames[months_in_SD];
short int filenames_count=0;

void setup() 
{
  //TODO: implement synchronize_time();
  
  configure_anl_pins();
  
  //network & ICeePot server initialization
  Ethernet.begin(mac, server_ip);
  server.begin();

  //SD neccessary calls
  pinMode(10, OUTPUT);
  if(SD.begin(4) == false)
  {
    return;
  }
  
}

void loop() 
{
  get_measurements();
  
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client){
    
    while (client.connected())
    {
      if (client.available())
      {
        char c = client.read();
        if (c == 'g')
        {
            send_all_rows_to_client(client);
            
            client.flush();
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
String get_value_from_sensor_in_row(short anl_pin)
{
  int anl_val = analogRead(anl_pin);
  //unsigned long time = millis(); //the elapsed time in millis from the arduino powered on moment
  
  time_t now_in_millis = now();

  int now_second = second(now_in_millis);
  int now_minute = minute(now_in_millis);
  int now_hour = hour(now_in_millis);
  int now_day = day(now_in_millis);
  int now_month = month(now_in_millis);
  int now_year = year(now_in_millis);
  
  String res = String(now_hour) + ":" + String(now_minute) + ":" + String(now_second) + "-" + String(now_day) + "/" + String(now_month) + "/" + String(now_year) + "|" + String(anl_pin) + "|" + String(anl_val);
  
  return res;
}


/* helper method that gets the moisture values from all pins
   if a specified ammount of time (defined in mins) has passed
*/
void get_measurements()
{
  unsigned long now = millis();
  if((now - last_measur_time) > (60000 * interval_in_min)){ 
    
    for(int i=0; i<anl_pins_counter; i++){
      String row = get_value_from_sensor_in_row(anl_pins[i]);
      store_row_to_sd(row);
            
      last_measur_time = now;
    }
    delay(1000);
    
  }
}

/*helper method which creates & returns a string
 containing the filename which is produced from the 
 day/month/year concatenation */
String get_filename_based_on_now()
{
  time_t now_in_millis = now();

  int now_day = day(now_in_millis);
  int now_month = month(now_in_millis);
  int now_year = year(now_in_millis);
  
  //String filename_str = String(now_day) + "_" + String(now_month) + "_" + String(now_year) + ".txt";
  String filename_str = String(now_day) + String(now_month) + String(now_year) + ".txt";
  
  return filename_str;
}

/* helper method that writes a given row to the SD output file
*/
void store_row_to_sd(String row)
{
  //get the appropriate filename
  String filename_str=get_filename_based_on_now();
  
  char filename[filename_str.length() + 1];
 
  filename_str.toCharArray(filename, filename_str.length() + 1);
  
  
  //if it is the first record of the month
  if(SD.exists(filename) == false){
    
    filenames[filenames_count]= filename_str;
    filenames_count++;
  }
  
  File file = SD.open(filename, FILE_WRITE);
  
  char row_in_chars[row.length()+1];
  
  row.toCharArray(row_in_chars,row.length()+1);
  
  int bytes_written = file.println(row_in_chars);
  
  file.flush();
  file.close();
}

/* helper method that sends the content of the SD file
  to the client given
*/
void send_all_rows_to_client(EthernetClient client)
{
  String filename_str;
  
  //no measurement yet
  if(filenames_count ==  0)
    client.println("Measurements not present yet...");
    
  //all files - all months
  for(int i=0; i<filenames_count; i++){
    
    filename_str = filenames[i];
  
    char filename[filename_str.length() + 1];
 
    filename_str.toCharArray(filename, filename_str.length() + 1);
        
    File file = SD.open(filename);
    char d,c;
    
    
    while(file.available()>0)
    {
      c = file.peek();
      d = file.read();
      client.write(d);
      client.flush();
                
    }
    
    file.close();
  }
  
}

/* helper method configuring the analog input pins*/
void configure_anl_pins()
{
  anl_pins[0] = 0;
}


