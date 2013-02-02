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

#ifndef NTP_h
#define NTP_h

#include <Ethernet.h>
#include <EthernetUdp.h>
#include <SPI.h>

//time server's IP
extern IPAddress time_server_ip; // time.nist.gov NTP server

//ICeePot server's mac
extern byte mac[];

// A UDP instance to let us send and receive packets over UDP
extern EthernetUDP Udp;

extern const int NTP_PACKET_SIZE; // NTP time stamp is in the first 48 bytes of the message
extern byte packetBuffer[]; //buffer to hold incoming and outgoing packets 

extern const int timeZoneOffset; // GMT zone;


void synchronize_time();
unsigned long getNtpTime();
unsigned long sendNTPpacket(IPAddress& address);


#endif