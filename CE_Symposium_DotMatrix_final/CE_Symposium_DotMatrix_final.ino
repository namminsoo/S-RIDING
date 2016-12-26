/*****************************************************************************
*                Copyright:  ChengDu Geeker Tech. Co., Ltd.
* File name:      hello_matrix.pde
* Description:    test the function of rgb matrix
* Author:       wanghui_CD
* Version:      V1.0
* Date:         2012.06.21
* History:      none
*****************************************************************************/
#include <rgb_matrix.h>
#include <SPI.h>

unsigned long time = 0;
unsigned int tick_100ms = 0;
unsigned char counter = 0;
int rearState;
#define N_X 1
#define N_Y 1
int count = 0;
int sendNum = 0;

#define LEFT_SW 2 //왼쪽 버튼
#define RIGHT_SW 3 //오른쪽 버튼 
int leftButtonState =  0; //버튼 상태 저장하기 위한 변수
int rightButtonState = 0; //버튼 상태 저장하기 위한 변수

/*
//Interface shield ShiftOut connector
#define DATA_PIN  9
#define CLK_PIN   3
*/
//Hardware SPI
#define DATA_PIN  11
#define CLK_PIN   13

#define LATCH_PIN 8

rgb_matrix M = rgb_matrix(N_X, N_Y, DATA_PIN, CLK_PIN, LATCH_PIN);

unsigned char cmd[50] = {0}, cmd_num = 0;

void setup()
{
  Serial.begin(9600);
  pinMode(LEFT_SW, INPUT_PULLUP);
  pinMode(RIGHT_SW, INPUT_PULLUP);
  delay(200);
}

/*************************************************************************
*  Description:
*                         display callback function
*        Receive AT comand via serial,and then run the right comand.
*        This function can be run in sweep interval.
*        Reduce delay time at function tail if screen blink.
*        Increase delay time at function tail if screen shows a double image.
* Param:   none
* Retval:  none
**************************************************************************/
void hook()
{
  int i = 0;
  if ((++counter) % 10 == 0)
  {
    if (millis() - time >= 100)
    {
      time = millis();
      tick_100ms ++;
      if (tick_100ms % 2 == 0)
      {
        if (rearState == 0) {
          M.move(LEFT, 1, 0);
        }
        else if (rearState == 1) {
          M.move(RIGHT, 1, 0);
        }
        else  if (rearState == 2) {
          M.move(UP, 1, 0);
        }
        else if (rearState == 4) {
          //default
        }
      }
    }
  }

  leftButtonState = digitalRead(LEFT_SW);
  rightButtonState = digitalRead(RIGHT_SW);

  count++;
  if (rightButtonState == HIGH && leftButtonState == HIGH) {
    straightDirRear();
  }
  else if (leftButtonState == LOW && rightButtonState == LOW) {
    stopRear();
  }
  else if (leftButtonState == LOW) {
    leftDirForRear();
  }
  else if (rightButtonState == LOW) {
    rightDirRear();
  }
}

void leftDirForRear() // 왼쪽 방향 모양을 찍어줄 함수
{
  int i = 0;
  M.clear(); //Matrix Clear
  
  for(i = 0; i <= 3; i++) {
    M.plot( 4 - i, i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 1 + i, 4 + i);
  }  
  for(i = 0; i <= 3; i++) {
      M.plot(5 - i, i);
  }
  for(i = 0; i <= 3; i++) {
      M.plot(2 + i, 4 + i);
  }
}

void rightDirRear() // 오른쪽 방향 모양을 찍어줄 함수
{ 
  int i = 0;
  M.clear(); //Matrix Clear

  for(i = 0; i <= 3; i++) {
    M.plot( 2 + i, i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 5 - i, 4 + i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 3 + i, i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 6 - i, 4 + i);
  }
}

void straightDirRear() // 직진 방향 모양을 찍어줄 함수
{
  int i = 0;
  M.clear(); //Matrix Clear

  for(i = 0; i <= 3; i++) {
    M.plot( i, 2 + i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 4 + i, 5 - i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( i, 3 + i);
  }
  for(i = 0; i <= 3; i++) {
    M.plot( 4 + i, 6 - i);
  }
}


void stopRear() // 정지 모양을 찍어줄 함수
{
  int i = 0;
  M.clear(); //Matrix Clear
  
  int i = 0, j = 0;
  for (i = 0; i < 8; i++)
  {
    for (j = 0; j < 8; j++)
    {
      M.plot(i, j);
    }
  }

}
/*************************************************************************************
*  Description:
*                         loop function
*        Display function must be called.
*        If you wanna do something after display be called,
*    you should give display function a parameter which is a pointer to a function.
* Param:   none
* Retval:  none
**************************************************************************************/
void loop()
{
  int tmp;
  M.add_layer();

  rearState = 0;
  stopRear(); //꽉찬 모양, 기본 값

  M.display(hook);
}
