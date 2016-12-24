#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <stdint.h>
#include <termios.h>
#include <android/log.h>
#include <sys/ioctl.h>
#include "com_topeet_serialtest_serial.h"

#undef  TCSAFLUSH
#define TCSAFLUSH  TCSETSF
#ifndef _TERMIOS_H_
#define _TERMIOS_H_
#endif

int fd=0;
int fd1=0;
/*
 * Class:     com_topeet_serialtest_serial
 * Method:    Open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_topeet_serialtest_serial_Open
  (JNIEnv *env, jobject obj, jint Port, jint Rate,jint flag)
{
  if(fd <= 0||fd1<=0)
  {
		if(0 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttySAC0");
			if(flag==0){
			   fd=open("/dev/ttySAC0",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
			   fd1=open("/dev/ttySAC0",O_RDWR|O_NDELAY|O_NOCTTY);
			}

		}
		else if(1 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttySAC1");
			if(flag==0){
			   fd=open("/dev/ttySAC1",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
			   fd1=open("/dev/ttySAC1",O_RDWR|O_NDELAY|O_NOCTTY);
			}

		}
		else if(2 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttySAC2");
			if(flag==0){
				fd=open("/dev/ttySAC2",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
				fd1=open("/dev/ttySAC2",O_RDWR|O_NDELAY|O_NOCTTY);
			}

		}
		else if(3 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttySAC3");
			if(flag==0){
			   fd=open("/dev/ttySAC3",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
			   fd1=open("/dev/ttySAC3",O_RDWR|O_NDELAY|O_NOCTTY);
			}
		}
		else if(4 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttyUSB0");
			if(flag==0){
			   fd=open("/dev/ttyUSB0",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
			   fd1=open("/dev/ttyUSB0",O_RDWR|O_NDELAY|O_NOCTTY);
			}
		}
		else if(5 == Port)
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "open fd /dev/ttyUSB1");
			if(flag==0){
			  fd=open("/dev/ttyUSB1",O_RDWR|O_NDELAY|O_NOCTTY);
			}else{
			  fd1=open("/dev/ttyUSB1",O_RDWR|O_NDELAY|O_NOCTTY);
			}
		}
		else
		{
			__android_log_print(ANDROID_LOG_INFO, "serial", "Parameter Error serial not found");
			if(flag==0){
			  fd = 0;
			}else{
			  fd1= 0;
			}
			return -1;
		}
#if 1	
		if(fd > 0||fd1>0)
		{	
			if(flag==0){
			__android_log_print(ANDROID_LOG_INFO, "serial", "serial open ok fd=%d", fd);                
			}else{
			__android_log_print(ANDROID_LOG_INFO, "serial", "serial open ok fd1=%d", fd1);
			}
			// disable echo on serial ports                    
			struct termios  ios;
			if(flag==0){
			 tcgetattr( fd, &ios );
			}else{
			 tcgetattr( fd1, &ios );
			}
			ios.c_oflag &=~(INLCR|IGNCR|ICRNL);
	  	    ios.c_oflag &=~(ONLCR|OCRNL);
			ios.c_iflag &= ~(ICRNL | IXON);			
			ios.c_iflag &= ~(INLCR|IGNCR|ICRNL);
			ios.c_iflag &=~(ONLCR|OCRNL);
			if(flag==0){
			 tcflush(fd,TCIFLUSH);
			}else{
			 tcflush(fd1,TCIFLUSH);
			}
			
			if(Rate == 2400){cfsetospeed(&ios, B2400);  cfsetispeed(&ios, B2400);}
			if(Rate == 4800){cfsetospeed(&ios, B4800);  cfsetispeed(&ios, B4800);}
			if(Rate == 9600){cfsetospeed(&ios, B9600);  cfsetispeed(&ios, B9600);}
			if(Rate == 19200){cfsetospeed(&ios, B19200);  cfsetispeed(&ios, B19200);}
			if(Rate == 38400){cfsetospeed(&ios, B38400);  cfsetispeed(&ios, B38400);}
			if(Rate == 57600){cfsetospeed(&ios, B57600);  cfsetispeed(&ios, B57600);}
			if(Rate == 115200){cfsetospeed(&ios, B115200);  cfsetispeed(&ios, B115200);}
			
			ios.c_cflag |= (CLOCAL | CREAD);
			ios.c_cflag &= ~PARENB;
			ios.c_cflag &= ~CSTOPB;
			ios.c_cflag &= ~CSIZE; 
			ios.c_cflag |= CS8; 
			ios.c_lflag = 0;  
			if(flag==0){
			  tcsetattr( fd, TCSANOW, &ios );
			}else{
			  tcsetattr( fd1, TCSANOW, &ios );
			}
		}
#endif
  }
   if(flag==0){
	return fd;
   }else{
	return fd1;
   }
}

/*
 * Class:     com_topeet_serialtest_serial
 * Method:    Close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_topeet_serialtest_serial_Close
  (JNIEnv *env, jobject obj,jint flag)
  {  if(flag==0){
	  if(fd > 0)close(fd);
     }else{
      if(fd1 > 0)close(fd1);
     }
  }

  
/*
 * Class:     com_topeet_serialtest_serial
 * Method:    Read
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_topeet_serialtest_serial_Read
  (JNIEnv *env, jobject obj,jint flag)
 {
		unsigned char buffer[2048];
		int BufToJava[2048];
		int len = 0, i = 0;
		
		memset(buffer, 0, sizeof(buffer));
		memset(BufToJava, 0, sizeof(BufToJava));
		if(flag==0){
	    	len=read(fd, buffer, 2048);
		}else{
			len=read(fd1, buffer, 2048);
		}
	
		if(len <= 0){
			__android_log_print(ANDROID_LOG_INFO, "serial","Data null");
			return NULL;
		}
	
		for(i=0; i<len; i++)
		{
			printf("%x", buffer[i]);
			BufToJava[i] = buffer[i];
		}
		
		jintArray array = (*env)-> NewIntArray(env, len); 
		(*env)->SetIntArrayRegion(env, array, 0, len, BufToJava);
		return array;	
  }
  
/*
 * Class:     com_topeet_serialtest_serial
 * Method:    Read
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_topeet_serialtest_serial_Write
  (JNIEnv *env, jobject obj, jintArray buf, jint buflen,jint flag)
 {
	jsize len = buflen;
	
	if(len <= 0) 
		return -1;
	jintArray array = (*env)-> NewIntArray(env, len);  
	if(array == NULL){array=NULL;return -1;}

	jint *body = (*env)->GetIntArrayElements(env, buf, 0);

	jint i = 0;
	unsigned char num[len];
	
	for (; i <len; i++) 
		num[i] = body[i];
	if(flag==0){
		write(fd, num, len);
	}else{
		write(fd1, num, len);
	}
	array = NULL;

	return 0;	
  }
  


