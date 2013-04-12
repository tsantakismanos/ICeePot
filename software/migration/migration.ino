#include <SD.h>

void setup(){
  
  //SD neccessary calls
  pinMode(10, OUTPUT);
  if(SD.begin(4) == false){
    Serial.println("SDNotOK");
    return;
  }
  
  
  migrate_file("22013.txt", "22013");
  migrate_file("32013.txt", "32013");
 //migrate_file("42013.txt", "42013");
}

void loop(){
  
  
  
}

void migrate_file(char* filename_src, char* filename_trgt){
  
  
  File source = SD.open(filename_src);
  Serial.println("File to read from: "+filename_src):
  
  File target = SD.open(filename_trgt, FILE_WRITE);
  Serial.println("File to write to: "+filename_trgt):

  byte b;
  String row = "";
  String moment_s = "";
  String id_s = "";
  String value_s = "";
  
  uint32_t moment_n;
  uint8_t type_n = 0;
  uint16_t id_n;
  uint16_t value_n;
  
  while(source.available()){
    
    b = (char)source.read();
    row.concat(String(b));
    
    if(b == '\n'){
      
      //row got
      Serial.println("row: " + row);
      
      //split and put to variables
      moment_s = row.substring(0,row.indexOf("|"));
      id_s = row.substring(row.indexOf("|")+1, row.indexOf("|",row.indexOf("|")+1));
      value_s = row.substring(row.indexOf("|",row.indexOf("|"+1)));
      
      Serial.println("text values: " + moment_s + "-" + id_s + "-" + value_s);
      
      char* c;
      moment_s.toCharArray(c, moment_s.length()+1);
      moment_n = (uint32_t)atoi(c);
      
      id_s.toCharArray(c, id_s.length()+1);
      id_n = (uint16_t)atoi(c);
      
      value_s.toCharArray(c, value_s.length()+1);
      value_n = (uint16_t)atoi(c);
      
      Serial.println("numeric values: " + String(moment_n) + "-" + String(id_n) + "-" + String(value_n));
      
      uint8_t packet[9];

      *((uint32_t*)packet) = moment_n;
      *((uint8_t*)(packet+4)) = type_n;
      *((uint16_t*)(packet+5)) = id_n;
      *((uint16_t*)(packet+7)) = value_n;

      short bytes = target.write(packet,9);
  
      target.flush();
      
      Serial.println("bytes written: "+String(bytes));
      Serial.println("---------------------------------");

      //reset row
      row = "";
    }
    
  }
  
  
  target.close();
  source.close();
  
}
