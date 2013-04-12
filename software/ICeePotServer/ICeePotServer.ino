/* 
 author: tsantakis manos
 date: 16/01/2013
 licence: GPL
 ******************************  
 description:
 server sketch to be uploaded & run on an arduino board
 with ethernet interface and SD card support.
 This server performs up to 4 tasks:
 - taking periodical measurements of analog input taken from arduino analog pins 
 - receiving measure messages from an RF transmitter placed in a pot
 - writing this information to the SD card in files of type: <month><year>.txt 
 - responding to a client's month requests  to the given port & IP 
 & sending the contents of the appropriate SD file
 in packets of the form: <time><type><pot id><value>
 */

#include <SD.h>
#include <Ethernet.h>
#include <SPI.h>
#include <Time.h>
#include <NTP.h>
#include <VirtualWire.h>


#define interval_in_min 30 //time interval (in minutes) between measurementσ
//#define debug_mode 
//#define wireless_enabled
#define wired_enabled
#define server_mode

//helper functions declaration
void get_wired_values(short wired_pin);
void get_wireless_values();
void save_values(time_t time, uint8_t *type_id_valye);
void save_values(time_t time, uint8_t type, uint16_t id, uint16_t value);
void send_file_rows_to_client(EthernetClient client, char* date);

//variable for periodic measurement
time_t last_measur_time = 0;
boolean isSynchronized = false;

byte mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

//this server's IP
byte ip[] = { 
  192, 168, 1, 20 };    //the inner IP of the server

IPAddress ntp_server_ip(216, 171, 112, 36); // time.nist.gov NTP server

#ifdef server_mode
//server instantiation
EthernetServer server(3629);
#endif

//basic initiating setup
void setup() {

#ifdef debug_mode
  Serial.begin(9600);
#endif

  //network & ICeePot server initialization
  Ethernet.begin(mac, ip);

#ifdef server_mode
  server.begin();
#ifdef debug_mode
    Serial.println("ServerOK");
#endif
#endif

  isSynchronized = false;

  //set to now
  unsigned long now_in_seconds = synchronize_time(ntp_server_ip);
  if(now_in_seconds != 0){
    setTime(now_in_seconds);
    isSynchronized = true;
#ifdef debug_mode
    Serial.println("TimeOK: "+now_in_seconds);
#endif
  }
  else{
#ifdef debug_mode
    Serial.println("TimeNotOK");
#endif
  }


  //SD neccessary calls
  pinMode(10, OUTPUT);
  if(SD.begin(4) == false){
#ifdef debug_mode
    Serial.println("SDNotOK");
#endif
    return;
  }

#ifdef wireless_enabled
  //Virtual Wire setup
  // Initialise the IO and ISR
  vw_set_ptt_inverted(true); // Required for DR3100
  vw_setup(1980);	 // Bits per sec

  vw_set_rx_pin(3);

  vw_rx_start();       // Start the receiver PLL running
#endif
}



//forever
void loop() {

#ifdef server_mode
  char server_msg[15];
  byte srv_msg_cnt;
#endif


  //recheck for time synchronization
  if(isSynchronized == false){
    unsigned long now_in_seconds = synchronize_time(ntp_server_ip);
    if(now_in_seconds != 0){
      setTime(now_in_seconds);
      isSynchronized = true;
#ifdef debug_mode
      Serial.println("TimeOK: "+now_in_seconds);
#endif
    }
    else{
#ifdef debug_mode
      Serial.println("TimeNotOK");
#endif
    }
  }

#ifdef wireless_enabled
  get_wireless_values();
#endif

#ifdef wired_enabled
  get_wired_values(0);
#endif

#ifdef server_mode
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client){

#ifdef debug_mode
    Serial.println("ClientOK");
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
          Serial.println("Msg: "+s);
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
#endif
}


//helper method definitions

/* helper method that gets the moisture values from all pins
 if a specified ammount of time (defined in mins) has passed
 */
void get_wired_values(short wired_pin){

  //time value to be compared for interval & file creation
  time_t now_in_secs = now();

  String row;
  short anl_value = 0;

  if((now_in_secs - last_measur_time) > (60 * interval_in_min)){

#ifdef debug_mode
    Serial.println("WdTime "+String(now_in_secs));
#endif

    anl_value = analogRead(wired_pin);

    save_values(now_in_secs, 0, wired_pin, anl_value);

    last_measur_time = now_in_secs;
  }
}


void get_wireless_values(){

  uint8_t buf[VW_MAX_MESSAGE_LEN];
  uint8_t buflen = VW_MAX_MESSAGE_LEN;

  if (vw_get_message(buf, &buflen)) // Non-blocking
  {
    time_t now_in_secs = now();

    save_values(now_in_secs, buf);
  }
}


void save_values(time_t time, uint8_t type, uint16_t id, uint16_t value){

  File file;

  String filename_str = String(month(time)) + String(year(time));

#ifdef debug_mode
  Serial.println("Filename: "+filename_str);
#endif

  char filename[filename_str.length() + 1];
  filename_str.toCharArray(filename, filename_str.length() + 1);

  //open the file  
  file = SD.open(filename, FILE_WRITE);

  uint8_t packet[9];

  *((uint32_t*)packet) = time;
  *((uint8_t*)(packet+4)) = type;
  *((uint16_t*)(packet+5)) = id;
  *((uint16_t*)(packet+7)) = value;

  short bytes = file.write(packet,9);
  
  file.flush();
  file.close();

#ifdef debug_mode
  Serial.println("Bytes written: "+String(bytes));
#endif

  
}



void save_values(time_t time, uint8_t *type_id_valye){

  File file;

  String filename_str = String(month(time)) + String(year(time));

#ifdef debug_mode
  Serial.println("Filename: "+filename_str);
#endif

  char filename[filename_str.length() + 1];
  filename_str.toCharArray(filename, filename_str.length() + 1);

  //open the file  
  file = SD.open(filename, FILE_WRITE);

  uint8_t packet[9];

  *((uint32_t*)packet) = time;                             //put time into packet
  *((uint8_t*)packet+4) = *((uint8_t*)type_id_valye);      //put type into packet
  *((uint8_t*)packet+5) = *((uint8_t*)(type_id_valye+1));  //put id into packet (first byte)
  *((uint8_t*)packet+6) = *((uint8_t*)(type_id_valye+2));  //put id into packet (second byte)
  *((uint8_t*)packet+7) = *((uint8_t*)(type_id_valye+3));  //put value into packet (first byte)
  *((uint8_t*)packet+8) = *((uint8_t*)(type_id_valye+4));  //put value into packet (second byte)


  short bytes = file.write(packet,9);
  
  file.flush();
  file.close();

#ifdef debug_mode
  Serial.println("Bytes written: "+bytes);
#endif

  
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
    Serial.println("FileNotOpened: "+s);
  }
#endif


  while(file.available()>0){
    d = file.read();
    client.write(d);
    client.flush();
  }

  file.close();  

}
