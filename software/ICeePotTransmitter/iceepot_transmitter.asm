    list        p=12f675        ; list directive to define processor
    #include    <p12f675.inc>   ; processor specific variable definitions
    
    errorlevel  -302            ; suppress message 302 from list file

; ---------------------------------------------------------------------
; Configuration Bits (Section 9.1 Configuration Bits)
; ---------------------------------------------------------------------
;
; Data Memory Code Protection bit:
; _CPD_ON = Enabled
; _CPD_OFF = Disabled
;
; Program Memory Code protection:
; _CP_ON = Enabled
; _CP_OFF = : Disabled
;
; Brown-out Detection Enable bit:
; _BODEN_ON = Enabled
; _BODEN_OFF = Disabled
;
; GP3/MCLR pin function select:
; _MCLRE_ON = GP3/MCLR pin function is /MCLR
; _MCLRE_OFF = GP3/MCLR pin function is digital I/O, 
;              /MCLR internally tied to Vdd
;
; Power-up Timer Enable bit:
; _PWRTE_ON = Enabled
; _PWRTE_OFF = Disabled
;
; Watchdog Timer Enable bit:
; _WDT_ON = Enabled
; _WDT_OFF = Disabled
;
; Oscillator Selction bits:
; _EXTRC_OSC_NOCLKOUT = CLKOUT function on GP4 pin, RC on GP5 pin.
; _EXTRC_OSC_CLKOUT = I/O function on GP4 pin, RC on GP5 pin.
; _INTRC_OSC_CLKOUT =  Internal oscillator, CLKOUT function on GP4 pin,
;                      I/O function on GP5 pin.
; _INTRC_OSC_NOCLKOUT = Internal oscillator, I/O function on GP4 and GP5 pins.
; _EC_OSC = I/O function on GP4 pin, CLKIN on GP5 pin.
; _HS_OSC = High speed crystal/resonator on GP4 and GP5 pins.
; _XT_OSC = Crystal/resonator on GP4 and GP5 pins.
; _LP_OSC = Low power crystal on GP4 and GP5 pins.
;
;
; ---------------------------------------------------------------------

    __CONFIG    _CPD_OFF & _CP_OFF & _BODEN_OFF & _MCLRE_ON & _PWRTE_OFF & _WDT_OFF & _INTRC_OSC_NOCLKOUT

;----------------------------------------------------------------------
; Variables  (Section 2.2 Data Memory Organization)
;----------------------------------------------------------------------

    ; Data Memory Organization (Section 2.2)
    ;
    ; Register locations 0x20 to 0x5F (64 bytes) are General Purpose 
    ; registers, implemented as static RAM and are mapped across both
    ; banks. 

	cblock  0x20

        w_temp                      ; used for context saving 
        status_temp                 ; used for context saving

        TEMP                        ; General Purpose Temporary register
        TEMP2                       ; General Purpose Temporary register

		MSG_LENGTH					; Length
		MSG_TYPE					; Type
		MSG_ADDRESS_LOW				; Address Low
		MSG_ADDRESS_HIGH			; Address High
		MSG_VALUE_LOW				; Value Low
		MSG_VALUE_HIGH				; Value High
		CRC_LOW						; CRC Low
		CRC_HIGH					; CRC High


        CSR0                        ; TX buffer shift register
        CSR1
        CSR2
        CSR3
        CSR4
        CSR5
        CSR6
        CSR7
        CSR8
        CSR9
        CSR10
        CSR11
        CSR12
        CSR13
        CSR14
        CSR15
        CSR16
        CSR17
        CSR18
        CSR19
        CSR20
        CSR21
        CSR22
        CSR23

        count_inner_100
        count_inner_500
        BitCount

        FuncBits                    ; Function Bits
	endc

;----------------------------------------------------------------------
; Defines
;----------------------------------------------------------------------

    ; Set up GPIO Port (Section 3.0)
    ; Function of GPIO pins depend on:
    ;   Configuration Bits (CONFIG) (Section 9.1)
    ;   Weak Pull-up Register (WPU) (Section 3.2.1)
    ;   Interrupt-on-change GPIO Register (IOCB) (Section 3.2.2)
    ;   Option Register (OPTION_REG) (Register 4-1)
    ;   TIMER1 Control Register (T1CON) (Register 5-1)
    ;   Comparator Control Register (CMCON) (Section 6.0)
    ;   A/D Control Register (ADCON0) (Section 7.0) (PIC12F675 Only)

#define SENSOR  GPIO, 0             ; (Analog Input) Sensor input
#define BATTERY GPIO, 1             ; (Analog Input) Battery measurement
#define TXD     GPIO, 2             ; (Output) Transmit Data
#define PB3     GPIO, 3             ; (Input Only) Reset switch
#define POWER   GPIO, 4             ; (Output) Give power for the analog read for the battery and sensor
#define RFENA   GPIO, 5             ; (Output) RF Enable

    ; Define for TRISIO Register (Section 3.1)
    ;
    ; GPIO is an 6-bit wide, bi-directional port. The corresponding data
    ; direction register is TRISIO. Setting a TRISIO bit (= 1) will make
    ; the corresponding GPIO pin an input. Clearing a TRISIO bit (= 0)
    ; will make the corresponding GPIO pin an output. The exception is
    ; GP3, which is input only and its TRIS bit will always read as a '1'.

    ; GPIO Pins = xx543210
#define GPTRIS  B'00001011'




;----------------------------------------------------------------------
; Program Memory
;----------------------------------------------------------------------

    ; Program Memory Organization (Section 2.1)

        ORG     0x000               ; RESET Vector

        nop                         ; for ICD use
        goto    INITIALIZE          ; goto INITALIZE


        ORG     0x004               ; Interrupt Vector

        movwf   w_temp              ; save W register
        swapf   STATUS, W           ; swap status to be saved into W
        bcf     STATUS, RP0         ; ---- Select Bank 0 -----
        movwf   status_temp         ; save STATUS register

;----------------------------------------
; Interrupt Service Routine (ISR)
;
; Description: 
;   
;----------------------------------------

; Interrupt-on-change (Section 3.2.2 and 9.4.3)
;
; An input change on GPIO change sets the GPIF bit. The interrupt can
; be enabled/disabled be setting/clearing the GPIE bit. Individual pins
; are configured through the IOC register (see INITIALIZATION below).
;
; Clear the IOC interrupt by:
;  a) Any read or write of GPIO. This will end the mismatch condition.
;  b) Clear the flag bit GPIF

        movfw   GPIO                ; read GPIO
        bcf     INTCON, GPIF


;----------------------------------------

        swapf   status_temp, W      ; swap status_temp into W, sets bank to original state
        movwf   STATUS              ; restore STATUS register
        swapf   w_temp, F
        swapf   w_temp, W           ; restore W register

        retfie



;----------------------------------------------------------------------
; Subroutine READ_ANALOG_AN0; READ_ANALOG_AN1
;
; Description: Read analog channel 0 (AN0) or 1 (AN1).
;   
; Constants: none
;   
; Global Variables: none
;   
; Initialization: none
;   
; Output: ADRESH and ADRESL contain 10-bit A/D result justified 
;   according to ADCON0, ADFM bit.
;   
;----------------------------------------------------------------------

READ_ANALOG_AN0

        bcf     ADCON0, CHS1        ; select analog channel AN0
        bcf     ADCON0, CHS0

        goto    READ_ANALOG

READ_ANALOG_AN1

        bcf     ADCON0, CHS1        ; select analog channel AN1
        bsf     ADCON0, CHS0

READ_ANALOG

		bsf		POWER				; Put power to the sensor and for battery measurement
        bsf     ADCON0, ADON        ; Turn on ADC module

        ; After selecting a new channel, allow for sufficent sample time.
        ; The amount of sample time depends on the charging time of the
        ; internal charge holding capacitor (Section 7.2).

        movlw   D'6'                ; At 4 MHz, a 22us delay
        movwf   TEMP                ; (22us = 2us + 6 * 3us + 1us)
        decfsz  TEMP, F
        goto    $-1

        bsf     ADCON0, GO          ; start A/D conversion

        btfsc   ADCON0, GO          ; has A/D conversion completed?
        goto    $-1

        bcf     ADCON0, ADON        ; Turn off ADC module (consumes no operating current)
		bcf 	POWER				; Clear the power

        return


;----------------------------------------------------------------------
; Subroutine MAP_TO_VW_BYTE
;
; Description: Map the lower nibble (4-bits) of W to thecorresponding VirtualWire byte
;   
; Constants: none
;   
; Global Variables: none
;   
; Initialization: none
;   
; Output: W contain the VirtualWire byte
;   
;----------------------------------------------------------------------

MAP_TO_VW_BYTE

		andlw	0x0F			; Only 4-bit values 0x00 - 0x0F are allowed
		addwf 	PCL, F			; add the value of W to the Program Counter

		retlw 	0x0d			; decode 0
		retlw 	0x0e			; decode 1
		retlw 	0x13			; decode 2
		retlw 	0x15			; decode 3
		retlw 	0x16			; decode 4
		retlw 	0x19			; decode 5
		retlw 	0x1a			; decode 6
		retlw 	0x1c			; decode 7
		retlw 	0x23			; decode 8
		retlw 	0x25			; decode 9
		retlw 	0x26			; decode 10
		retlw 	0x29			; decode 11
		retlw 	0x2a			; decode 12
		retlw 	0x2c			; decode 13
		retlw 	0x32			; decode 14
		retlw 	0x34			; decode 15


;----------------------------------------------------------------------
; Subroutine: DELAY_500
;   
; Description: Wait for 500 usec (ìsec, half ms)
;   
; Constants: none
;   
; Global Variables: none
;   
; Initialization: none
;   
; Output: none
;   
;----------------------------------------------------------------------

DELAY_500
        movlw   D'98'              ; [1]
        movwf   count_inner_500    ; [1]

DELAY_500_INNER
        nop                        ; [1]
        nop                        ; [1]
        decfsz  count_inner_500,F      ; [1]
         goto    DELAY_500_INNER  ; [2]
;                          --------
;                         98 x 5 = 490us

        retlw   0                  ; [2]

;                       total 2 (call) + 2 + 490 + 2 (return)


DELAY_100_ms
        movlw   D'200'             ; [1]
        movwf   count_inner_100    ; [1]

DELAY_100_ms_INNER
		CALL	DELAY_500
        decfsz  count_inner_100,F       ; [1]
         goto    DELAY_100_ms_INNER ; [2]
;                          --------
;                         98 x 5 = 490us

        retlw   0                  ; [2]

SLEEP_1SEC
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		call	DELAY_100_ms
		retlw	0

SLEEP_5SEC
		call	SLEEP_1SEC
		call	SLEEP_1SEC
		call	SLEEP_1SEC
		call	SLEEP_1SEC
		call	SLEEP_1SEC
		retlw	0

;----------------------------------------------------------------------
; Initialize PICmicro (PIC12F675)
;----------------------------------------------------------------------

INITIALIZE

; Disable global interrupts during initialization

        bcf     INTCON, GIE         ; disable global interrupts


;----------------------------------------
; Calibrating the Internal Oscillator (Section 9.2.5.1)
; Oscillator Calibration Register (Section 2.2.2.7)
;
; A calibration instruction is programmed into the last location of
; program memory. This instruction is a RETLW XX, where the literal is
; the calibration value. The literal is placed in the OSCCAL register
; to set the calibration of the internal oscillator.

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        call    0x3FF               ; retrieve factory calibration value
        movwf   OSCCAL              ; update register with factory cal value

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; GPIO Port (Section 3.0)
;
; Store GPTRIS value defined above into the TRISIO direction register

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        movlw   GPTRIS
        movwf   TRISIO              ; Write to TRISIO register

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; Comparator Module (Section 6.0)
;
; The PIC12F629/675 devices have one analog comparator. The inputs to
; the comparator are multiplexed with the GP0 and GP1 pins. There is 
; an on-chip Comparator Voltage Reference that can also be applied to
; an input of the comparator. In addition, GP2 can be configured as
; the comparator output. The Comparator Control Register (CMCON)
; contains bits to control the comparator. The Voltage Reference
; Control Register (VRCON) controls the voltage reference module.


        ; Comparator Configuration (Figure 6-2)
;        bcf     CMCON, CINV         ; Comparator Output Inversion: not inverted
;        bcf     CMCON, COUT         ; Comparator Output bit: Vin+ < Vin-
;        bcf     CMCON, CIS          ; Comparator Input Switch: Vin- connectos to Cin-

        ; CM2:CM0 = 111 - Comparator Off (lowest power)
        bsf     CMCON, CM2          ; Comparator Mode bit 2
        bsf     CMCON, CM1          ; Comparator Mode bit 1
        bsf     CMCON, CM0          ; Comparator Mode bit 0

        ; VRCON (Register 6-2)
        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        bcf     VRCON, VREN         ; CVref circuit: powered down, no Idd drain

;        bcf     VRCON, VRR          ; CVref Range Selection: High Range

;        bcf     VRCON, VR3          ; CVref value selection bit 3
;        bcf     VRCON, VR2          ; CVref value selection bit 2
;        bcf     VRCON, VR1          ; CVref value selection bit 1
;        bcf     VRCON, VR0          ; CVref value selection bit 0

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; Analog-to-Digital Converter (A/D) Module (Section 7.0) (PIC12F675 Only)
;
; The analog-to-digital converter (A/D) allows conversion of an analog
; input signal to a 10-bit binary representation of that signal. The
; PIC12F675 has four analog inputs multiplexed into one sample and hold
; circuit. There are two registers to control the functions of the A/D
; module:
;   A/D Control Register (ADCON0)
;   Analog Select Register (ANSEL)
;
; Note: When using GPIO pins as analog inputs, ensure the TRISIO register
;       bits are set (= 1) for input.

        bsf     ADCON0, ADFM        ; A/D Result Formed: right justified
        bcf     ADCON0, VCFG        ; Voltage Reference: Vdd
        bcf     ADCON0, ADON        ; ADC is shut-off and consumes no operating current

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        ; select A/D Conversion Clock Source: Fosc/8
        bcf     ANSEL, ADCS2        ; A/D Conversion Clock Select bit 2
        bcf     ANSEL, ADCS1        ; A/D Conversion Clock Select bit 1
        bsf     ANSEL, ADCS0        ; A/D Conversion Clock Select bit 0

        ; select GPIO pins that will be analog inputs: GP0/AN0, GP1/AN1
        bcf     ANSEL, ANS3         ; Analog Select GP4/AN3: digital I/O
        bcf     ANSEL, ANS2         ; Analog Select GP2/AN2: digital I/O
        bsf     ANSEL, ANS1         ; Analog Select GP1/AN1: analog input
        bsf     ANSEL, ANS0         ; Analog Select GP0/AN0: analog input

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; TIMER1 Module with Gate Control (Section 5.0)
;
; The TIMER1 Control Register (T1CON) is used to enable/disable TIMER1
; and select various features of the TIMER1 module.

        bcf     T1CON, TMR1ON       ; TIMER1: stopped
        bcf     T1CON, TMR1CS       ; TIMER1 Clock Source Select: Internal Clock (Fosc/4)
        bcf     T1CON, NOT_T1SYNC   ; TIMER1 External Clock Input Sync Control: Syncronize external clock input

        ; T1OSCEN only if INTOSC without CLKOUT oscillator is active, else ignored
        bcf     T1CON, T1OSCEN      ; LP Oscillator Enable Control: LP oscillator off

        ; TIMER1 Input Prescale Select: 1:1
        bcf     T1CON, T1CKPS1      ; TIMER1 Input Clock Prescale Select bit 1
        bcf     T1CON, T1CKPS0      ; TIMER1 Input Clock Prescale Select bit 0

        ; TMR1GE only if TMR1ON = 1, else ignored
        bcf     T1CON, TMR1GE       ; TIMER1 Gate Enable: on


;----------------------------------------
; Weak Pull-up Register (WPU) (Section 3.2.1)
;
; Each of the GPIO pins, except GP3, has an individually configurable
; weak internal pull-up. Control bits WPUx enable or disable each 
; pull-up. Refer to Register 3-1. Each weak pull-up is automatically
; turned off when the port pin is configured as an output. The pull-ups
; are disabled on a Power-on Reset by the NOT_GPPU bit (see OPTION_REG below).

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

;     GPIO Pins = xx54x210
        movlw   B'00000000'         ; No pull-up is enabled
        movwf   WPU

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; OPTION Register (OPTION_REG) (Section 2.2.2.2)
; TIMER0 Module (Section 4.0)
;
; The OPTION_REG contains control bits to configure:
;   Weak pull-ups on GPIO (see also WPU Register above)
;   External GP2/INT interrupt
;   TMR0
;   TMR0/WDT prescaler

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        bcf     OPTION_REG, NOT_GPPU  ; GPIO pull-ups: enabled
        bsf     OPTION_REG, INTEDG  ; Interrupt Edge: on rising edge of GP2/INT pin

        bcf     OPTION_REG, T0CS    ; TMR0 Clock Source: internal instruction cycle (CLKOUT)
        bcf     OPTION_REG, T0SE    ; TMR0 Source Edge: increment low-to-high transition on GP2/T0CKI pin
        bcf     OPTION_REG, PSA     ; Prescaler Assignment: assigned to TIMER0

        ; TMR0 Prescaler Rate: 1:2
        bcf     OPTION_REG, PS2     ; Prescaler Rate Select bit 2
        bcf     OPTION_REG, PS1     ; Prescaler Rate Select bit 1
        bcf     OPTION_REG, PS0     ; Prescaler Rate Select bit 0

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; Interrupt-on-Change Register (IOCB) (Section 3.2.2)
;
; Each of the GPIO pins is individually configurable as an interrupt-
; on-change pin. Control bits IOCBx enable or disable the interrupt 
; function for each pin. Refer to Register 3-2. The interrupt-on-change
; is disabled on a Power-on Reset.
;
; Note: Global interrupt enables (GIE and GPIE) must be enabled for
;       individual interrupts to be recognized.

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

    ; GPIO Pins = xx543210
        movlw   b'00000000'
        movwf   IOCB                ; Interrupt-on-change enabled on no input

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; Peripheral Interrupt Enable Register (PIE1) (Section 2.2.2.4)
;
; The PIE1 register contains peripheral interrupt enable bits.
;
; Note: The PEIE bit (INTCON<6>) must be set to enable any 
;       peripheral interrupt.

        bsf     STATUS, RP0         ; ---- Select Bank 1 -----

        bcf     PIE1, EEIE          ; EE Write Complete Interrupt: disabled
        bcf     PIE1, ADIE          ; A/D Converter Interrupt (PIC12F675 Only): disabled
        bcf     PIE1, CMIE          ; Comparator Interrupt: disabled
        bcf     PIE1, TMR1IE        ; TMR1 Overflow Interrupt: disabled

        bcf     STATUS, RP0         ; ---- Select Bank 0 -----


;----------------------------------------
; Interrupt Control Register (INTCON) (Section 2.2.2.3)
;
; The INTCON register contains enable and disable flag bits for TMR0
; register overflow, GPIO port change and external GP2/INT pin 
; interrupts.

        bcf     INTCON, PEIE        ; disable Peripheral Interrupt Enable bit
        bcf     INTCON, T0IE        ; disable TMR0 Overflow Interrupt Enable bit
        bcf     INTCON, INTE        ; disable GP2/INT External Interrupt Enable bit
        bcf     INTCON, GPIE        ; disable Port Change Interrupt Enable bit
        bsf     INTCON, GIE     	; enable global interrupts


;----------------------------------------------------------------------
;----------------------------------------------------------------------
; Main Program
;----------------------------------------------------------------------
;----------------------------------------------------------------------
;
; Value Packet format [in 6-bit packets]   21 bytes
;	6 Preamble:     [010101 010101 010101 010101 010101 010101 ]
;	2 Magic Number: [000111 001101]
;	2 Length: 		[]
;   1 Type:			[tttttt]		; [vvpp] vv: Protocol version, pp: Packet type 
; 	2 Address:      [hhhhhh llllll]
;   3 Value:        [hhhhhh mmmmmm llllll]  Actual 12 bit value
;	4 Checksum:		[aaaaaa bbbbbb cccccc dddddd]
;
;
;
;SubRoutines
; 		READ_ANALOG_AN0
;			read the sensor value on analog channel 0
; 		MAP_TO_VW_BYTE
;			convert one nibbe (4 bit) to one 6-bit value
; BuildPacket
;   loop the packet bytes
;	Convert the bytes to VWBytes
; SendPacket
;	Loop the VWBytes
; SendOne
;	Send the '1' bit RF wave
; SendZero
;	Send the '0' bit RF wave
; Delay500
;	wait for 500ìsec
;
;   
;INITIALIZE
; set flags
;
;
;
;
;
;MAIN
;
; read the supply voltage value
; if value less than nominal
;     send low voltage packet
;
; read the sensor value
; send sensor value packet
;
;
;
;
; prepare for sleep
;	set pin values
;	disable interrupts ?
;
; sleep
; sleep
; sleep
; sleep
; sleep
; sleep
; sleep
;
;
; goto MAIN




MAIN

		call 	READ_ANALOG_AN0				; Read the sensor value in ADRESL, ADRESH.
		movlw	0x01						; Put the message_type in W. 
		call	SEND_MESSAGE				; Transmit message

		call 	READ_ANALOG_AN1				; Read the battery level in ADRESL, ADRESH.
		movlw	0x02						; Put the message_type in W. 
		call	SEND_MESSAGE				; Transmit message

		call	SLEEP_5SEC					; sleep

		goto MAIN		





;----------------------------------------------------------------------
; Subroutine SEND_MESSAGE
;
; Description: Sends a message with the value
;   
; Constants: none
;   
; Global Variables: none
;   
; Initialization: W contains the message_type, ADRESH and ADRESL contain 10-bit A/D result justified
;   
; Output: ADRESH and ADRESL contain 10-bit A/D result justified 
;   according to ADCON0, ADFM bit.
;   
;----------------------------------------------------------------------

SEND_MESSAGE
;----------------------------------
; Build the message
;----------------------------------
		movwf	MSG_TYPE

		movlw	0x08						; Length is 8 bytes (1xLength, 1xType, 2xAddress, 2xValue, 2xCRC)
		movwf	MSG_LENGTH

		movlw	0x08
		movwf	MSG_ADDRESS_LOW
		movlw	0x00						; Transmitter address
		movwf	MSG_ADDRESS_HIGH

        bsf     STATUS, RP0     			; ----- Select Bank 1 -----
        movfw   ADRESL          			; ADRESL Result
        bcf     STATUS, RP0     			; ----- Select Bank 0 -----
        movwf   MSG_VALUE_LOW
        movfw   ADRESH          			; Set the sensor value to the packet
        movwf   MSG_VALUE_HIGH

CALC_CRC
		movlw	0xFF						; Initialize CRC to 0xFFFF
		movwf	CRC_HIGH
		movwf	CRC_LOW

        movlw   MSG_LENGTH					; Pointer to the start of the message
        movwf   FSR

CRC_NEXT_BYTE
		movfw	INDF						; W = data
		xorwf	CRC_LOW, W					; W ^= lo8(crc);	data ^= lo8 (crc);
		movwf	TEMP						; TEMP = (data ^ lo8(crc))

		swapf	TEMP, W						; W swap nibbles 
		andlw	0xf0						; W = TEMP << 4
        xorwf	TEMP,F						; TEMP = TEMP ^ (TEMP<<4)   data ^= data << 4;

		; calculate CRC_LOW  (data >> 4) ^ hi8(crc) ^ (data << 3)
		swapf	TEMP, W						; TEMP = data
		andlw	0x0f						; W = (data >> 4)
		
		xorwf	CRC_HIGH, W
		movwf	CRC_LOW						; CRC_LOW = (data >> 4) ^ hi8(crc)

		movfw	TEMP						; (data << 3)
		andlw	0x1f
		movwf	TEMP2
        bcf     STATUS, C
		rlf		TEMP2, F
		rlf 	TEMP2, F
		rlf		TEMP2, W
		xorwf	CRC_LOW, F					;  CRC_LOW = [(data >> 4) ^ hi8(crc)] ^ (data<<3)
		
		; calculate CRC_HIGH  (data) ^ hi((data << 3))
		movfw	TEMP						; W holds data
		movwf	CRC_HIGH

		rlf		TEMP, F						; 1st goes to carry
		rlf		TEMP, F
		rlf		TEMP, F
		rlf		TEMP, W						; lower 3 bits are the needed bits
		andlw	B'00000111'
		xorwf	CRC_HIGH, F

		; loop the Message Bytes
        incf    FSR, F						
        movlw   MSG_VALUE_HIGH + 1			; calculate checksum until
        xorwf   FSR, W
        andlw   0x1F
        BNZ     CRC_NEXT_BYTE
		
		COMF	CRC_HIGH, F
		COMF	CRC_LOW, F


MESSAGE_PREAMBLE
        movlw   B'101010'
        movwf   CSR0

        movlw   B'101010' 
        movwf   CSR1

        movlw   B'101010'
        movwf   CSR2

        movlw   B'101010'
        movwf   CSR3

        movlw   B'101010'
        movwf   CSR4

        movlw   B'101010'
        movwf   CSR5


MESSAGE_STARTBYTE
        movlw   B'111000'
        movwf   CSR6

        movlw   B'101100'
        movwf   CSR7


MESSAGE_LENGTH
		swapf	MSG_LENGTH,W		; get the high nibble of the length into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR8

        movfw   MSG_LENGTH			; get the low nibble of the length into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR9

MESSAGE_TYPE
		swapf	MSG_TYPE,W			; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR10

        movfw   MSG_TYPE			; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR11

MESSAGE_ADDRESS_LOW
		swapf	MSG_ADDRESS_LOW,W	; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR12

        movfw   MSG_ADDRESS_LOW	; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR13

MESSAGE_ADDRESS_HIGH
		swapf	MSG_ADDRESS_HIGH,W	; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR14

        movfw   MSG_ADDRESS_HIGH	; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR15

MESSAGE_VALUE_LOW
		swapf	MSG_VALUE_LOW,W	; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR16

        movfw   MSG_VALUE_LOW		; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR17

MESSAGE_VALUE_HIGH
		swapf	MSG_VALUE_HIGH,W	; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR18

        movfw   MSG_VALUE_HIGH		; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR19

MESSAGE_CRC_LOW
		swapf	CRC_LOW,W			; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR20

        movfw   CRC_LOW				; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR21

MESSAGE_CRC_HIGH
		swapf	CRC_HIGH,W			; get the high nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR22

        movfw   CRC_HIGH			; get the low nibble of the type into low nibble of W
		call 	MAP_TO_VW_BYTE		; W contains the bits to be sent
        movwf   CSR23



TXloop
        bsf     RFENA           ; Enable Transmitter
    
        bcf     TXD             ; Send 0 for 1 msec
        call    DELAY_500
        call    DELAY_500


        movlw   CSR0            ; lsb first 
        movwf   FSR
            
TXNextByte
        movlw   D'6'            ; 6 bits per byte to be transmitted
        movwf   BitCount

TXNextBit
        rrf     INDF,W           ; 8 bit rotate
        rrf     INDF,F           ; Carry contain lsb
        BC      Trasm_ONE

Trasm_ZERO
        bcf     TXD             ; ON
        call    DELAY_500
        goto NEXT_BIT

Trasm_ONE
        bsf     TXD             ; ON
        call    DELAY_500

NEXT_BIT
        decfsz  BitCount,F
        goto    TXNextBit       ; loop on bits

        incf    FSR,F
        movlw   CSR23 + 1        ; check if finished
        xorwf   FSR,W
        andlw   0x1F
        BNZ     TXNextByte

; guard time
        bcf     TXD             ; Send 0 for 1 msec
        call    DELAY_500
        call    DELAY_500

        bcf     RFENA           ; Disable Transmitter

        return


;----------------------------------------------------------------------
; Data EEPROM Memory (Section 8.0)
;
; PIC12F629/675 devices have 128 bytes of data EEPROM with address
; range 0x00 to 0x7F.

        ; Initialize Data EEPROM Memory locations

;        ORG 0x2100
;        DE  0x00, 0x01, 0x02, 0x03


;----------------------------------------------------------------------
; Calibrating the Internal Oscillator (Section 9.2.5.1)
; Oscillator Calibration Register (OSCCAL) (Section 2.2.2.7)
;
; The below statements are placed here so that the program can be
; simulated with MPLAB SIM.  The programmer (PICkit or PROMATE II)
; will save the actual OSCCAL value in the device and restore it.
; The value below WILL NOT be programmed into the device.

        org     0x3ff
        retlw    0x80

;----------------------------------------------------------------------
        end                         ; end of program directive
;----------------------------------------------------------------------
