/*

 Udp NTP Client
 
 Get the time from a Network Time Protocol (NTP) time server
 Demonstrates use of UDP sendPacket and ReceivePacket 
 For more on NTP time servers and the messages needed to communicate with them, 
 see http://en.wikipedia.org/wiki/Network_Time_Protocol
 
 created 4 Sep 2010 
 by Michael Margolis
 modified 17 Sep 2010
 by Tom Igoe
 
 This code is in the public domain.

 */

       
#include <Ethernet.h>
#include <EthernetUdp.h>



unsigned long synchronize_time(IPAddress ntp_server_ip); 


// send an NTP request to the time server at the given address 
unsigned long sendNTPpacket(IPAddress& address);









