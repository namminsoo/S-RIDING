#include <Thread.h>
#include <ThreadController.h>
#include <SoftwareSerial.h>

SoftwareSerial BTSerial(11, 12); //Connect HC-06. Use your (TX, RX) settings

// ThreadController that will controll all threads
ThreadController controll = ThreadController();

//My Thread (as a pointer)
Thread* ultra_Thread_A = new Thread();
Thread ultra_Thread_B = Thread();
Thread ultra_Thread_C = Thread();

struct _ultra {
  int Trig_pin;
  int Echo_pin;
  int distance;
};
typedef struct _ultra Ultra;

int buzzPin = 3;     //Connect Buzzer on Digital Pin3
int distance_A, distance_B, distance_C;
Ultra ultra_A, ultra_B, ultra_C;

Ultra UltrasonicMeasure(Ultra  s); //선언부
void makeSoundBuzz();

// callback for ultra_Thread_A
void ultra_A_Callback() {
  ultra_A = UltrasonicMeasure(ultra_A);
  
  if (ultra_A.distance <= 400 && ultra_A.distance >= 340) {
     // Serial –> Data –> BT
    if (Serial.available()) {
      BTSerial.write("s");
      Serial.println("wow..");
    }
        makeSoundBuzz();
  }
}

// callback for ultra_Thread_B
void ultra_B_Callback() {
  ultra_B = UltrasonicMeasure(ultra_B);

  if (ultra_B.distance <= 400 && ultra_B.distance >= 340) {
    // Serial –> Data –> BT
    if (Serial.available()) {
      BTSerial.write("s");
      Serial.println("wow..");
    }
        makeSoundBuzz();
  }
}

// callback for ultra_Thread_C
void ultra_C_Callback() {
  ultra_C = UltrasonicMeasure(ultra_C);
  
  if (ultra_C.distance <= 400 && ultra_C.distance >= 340) {
    BTSerial.write("a");
    // Serial –> Data –> BT
        if (Serial.available()) {
          BTSerial.write("s");
          Serial.println("wow..");
        }
       makeSoundBuzz();
  }
  else
  {
    BTSerial.write("r");
  }
}

void setup() {
  Serial.begin(9600);

  // Configure ultra_Thread_A
  ultra_Thread_A->onRun(ultra_A_Callback);
  // Configure ultra_Thread_B
  ultra_Thread_B.onRun(ultra_B_Callback);
  // Configure ultra_Thread_C
  ultra_Thread_C.onRun(ultra_C_Callback);

  // Adds both threads to the controller
  controll.add(ultra_Thread_A);
  controll.add(&ultra_Thread_B); // & to pass the pointer to it
  controll.add(&ultra_Thread_C); // & to pass the pointer to it

  ultra_A = { 4, 5, 0 };
  ultra_B = { 6, 7, 0 };
  ultra_C = { 8, 9, 0 };

  pinMode(buzzPin, OUTPUT);     //digital buuzer는 d3핀

  pinMode(ultra_A.Trig_pin, OUTPUT); //트리거 핀을 출력으로 설정
  pinMode(ultra_A.Echo_pin, INPUT); //에코핀을 입력으로 설정
  pinMode(ultra_B.Trig_pin, OUTPUT); //트리거 핀을 출력으로 설정
  pinMode(ultra_B.Echo_pin, INPUT); //에코핀을 입력으로 설정
  pinMode(ultra_C.Trig_pin, OUTPUT); //트리거 핀을 출력으로 설정
  pinMode(ultra_C.Echo_pin, INPUT); //에코핀을 입력으로 설정

  BTSerial.begin(9600);  // set the data rate for the BT port
}

void loop() {
  // run ThreadController
  // this will check every thread inside ThreadController,
  // if it should run. If yes, he will run it;
  controll.run();
}


Ultra UltrasonicMeasure(Ultra  s)    //초음파 센서로 거리를 출력하기 위한 함수
{
  int duration;
  digitalWrite(s.Trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(s.Trig_pin, HIGH);
  delayMicroseconds(10);
  digitalWrite(s.Trig_pin, LOW);
  s.distance = pulseIn(s.Echo_pin, HIGH) / 29 / 2;
  return  s;
}

void makeSoundBuzz() // 후방 접근시 버저의 소리를 출력
{
  digitalWrite(buzzPin, HIGH);
  delay(500);
  digitalWrite(buzzPin, LOW);
  delay(500);
}

