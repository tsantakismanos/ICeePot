/*

 Udp NTP Client
 
 Get the time from a Network Time Protocol (NTP) time server
 Demonstrates use of UDP sendPacket and ReceivePacket 
 For more on NTP time servers and the messages needed to communicate with them, 
 see http://en.wikipedia.org/wiki/Network_Time_Protocol
 
 created 4 Sep 2010 
 by Michael Margolis
 modified 9 Apr 2012
 by Tom Igoe
 modified 21 Jul 2012
 by Tomas Galan
 modified 15 Jan 2013
 by Manos Tsantakis
 
 This code is in the public domain.

 */


#include <Ethernet.h>
#include <EthernetUdp.h>
#include <SPI.h>
#include "NTP.h"
#include <Time.h>

  byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

  IPAddress time_server_ip(192, 43, 244, 18);
  
  EthernetUDP Udp;

  const int NTP_PACKET_SIZE= 48;

  byte packetBuffer[NTP_PACKET_SIZE];
  
  const int timeZoneOffset= +1; // GMT zone;


void synchronize_time(){



  unsigned int localPort = 8888;      // local port to listen for UDP packets

  
  while(Ethernet.begin(mac) == 0){
    //until connection is established
  }
  
  Udp.begin(localPort);
  
  setSyncProvider(getNtpTime);
  
  while(timeStatus()== timeNotSet)   
     ;
  
}

unsigned long getNtpTime()
{
  sendNTPpacket(time_server_ip); // send an NTP packet to a time server
  delay(1000);
  if ( Udp.parsePacket() ) {  
    // We've received a packet, read the data from it
    Udp.read(packetBuffer,NTP_PACKET_SIZE);  // read the packet into the buffer

    //the timestamp starts at byte 40 of the received packet and is four bytes,
    // or two words, long. First, esxtract the two words:

    unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
    unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);  
    // combine the four bytes (two words) into a long integer
    // this is NTP time (seconds since Jan 1 1900):
    unsigned long secsSince1900 = highWord << 16 | lowWord; 
    const unsigned long seventyYears = 2208988800UL;     
    // subtract seventy years and add the time zone:
    unsigned long epoch = secsSince1900 - seventyYears + (timeZoneOffset * 3600L);
    return epoch;
  }
  return 0;
}

// send an NTP request to the time server at the given address 
unsigned long sendNTPpacket(IPAddress& address)
{
  // set all bytes in the buffer to 0
  memset(packetBuffer, 0, NTP_PACKET_SIZE); 
  // Initialize values needed to form NTP request
  // (see URL above for details on the packets)
  packetBuffer[0] = 0b11100011;   // LI, Version, Mode
  packetBuffer[1] = 0;     // Stratum, or type of clock
  packetBuffer[2] = 6;     // Polling Interval
  packetBuffer[3] = 0xEC;  // Peer Clock Precision
  // 8 bytes of zero for Root Delay & Root Dispersion
  packetBuffer[12]  = 49; 
  packetBuffer[13]  = 0x4E;
  packetBuffer[14]  = 49;
  packetBuffer[15]  = 52;

  // all NTP fields have been given values, now
  // you can send a packet requesting a timestamp: 		   
  Udp.beginPacket(address, 123); //NTP requests are to port 123
  Udp.write(packetBuffer,NTP_PACKET_SIZE);
  Udp.endPacket(); 
}
