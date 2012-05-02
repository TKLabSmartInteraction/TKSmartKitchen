// defining the Ports of the sensors
#define SensorA 32
#define SensorB 33
#define SensorC 34
#define SensorD 35
#define SensorE 36
// Mode 
// 0 = "MPoll" = get only sensor data when you poll them
// 1 = "MChanges" = send sensor data when the sensor changes
// 2 = "MAgressive" = send as fast as possible all sensor data
int Mode = 0;

// Saves the last state of the Sensor
int SensorStateA = LOW;
int SensorStateB = LOW;
int SensorStateC = LOW;
int SensorStateD = LOW;
int SensorStateE = LOW;

void setup() {
  pinMode(SensorA, INPUT);
  pinMode(SensorB, INPUT);
  pinMode(SensorC, INPUT);
  pinMode(SensorD, INPUT);
  pinMode(SensorE, INPUT);
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {
    int nextByte = Serial.read();
    // if it could be a lowercase letter convert to
    // an uppercase
    if (nextByte > 96) nextByte = nextByte - 32;
    // react on what you got
    switch (nextByte) {
      // Mode Changes when you get the ASCII
      // representation of the number 0 - 2
      case 48:
        Mode = 0;
        Serial.write("0");
        break;
      case 49:
        // if we are in MPoll and change to MChange we want to
        // start with up to date sensor data so first send all
        // the sensorstates
        if (Mode == 0) sendAll();
        Mode = 1;
        Serial.write("1");
        break;
      case 50:
        Mode = 2;
        Serial.write("2");
        break;
      // if you got an A - E send the data of the sensor
      case 65:
        sendStateA();
        break;
      case 66:
        sendStateB();
        break;
      case 67:
        sendStateC();
        break;
      case 68:
        sendStateD();
        break;
      case 69:
        sendStateE();
        break;
    }
  // when no serial data is available
  } else {
    if (Mode == 1) {
      
      // compare the acutal state with the last state and
      // send the state when its different
      if (digitalRead(SensorA) != SensorStateA) {
        SensorStateA = digitalRead(SensorA);
        sendStateA();
      }
      if (digitalRead(SensorB) != SensorStateB) {
        SensorStateB = digitalRead(SensorB);
        sendStateB();
      }
      if (digitalRead(SensorC) != SensorStateC) {
        SensorStateC = digitalRead(SensorC);
        sendStateC();
      }
      if (digitalRead(SensorD) != SensorStateD) {
        SensorStateD = digitalRead(SensorD);
        sendStateD();
      }
      if (digitalRead(SensorE) != SensorStateE) {
        SensorStateE = digitalRead(SensorE);
        sendStateE();
      }
    }
    // if the mode is Agressive send all the Sensordata
    if (Mode == 2) {
      sendAll();
    }
  }
}

// sends all sensorstates
void sendAll() {
  sendStateA();
  sendStateB();
  sendStateC();
  sendStateD();
  sendStateE();
}
      
// Send an uppercase letter when the Port
// is HIGH and send an lowercase when its LOW
void sendStateA() {
  if (digitalRead(SensorA) == HIGH) {
    Serial.write("A");
  } else {
    Serial.write("a");
  }
}

void sendStateB() {
  if (digitalRead(SensorB) == HIGH) {
    Serial.write("B");
  } else {
    Serial.write("b");
  }
}

void sendStateC() {
  if (digitalRead(SensorC) == HIGH) {
    Serial.write("C");
  } else {
    Serial.write("c");
  }
}

void sendStateD() {
  if (digitalRead(SensorD) == HIGH) {
    Serial.write("D");
  } else {
    Serial.write("d");
  }
}

void sendStateE() {
  if (digitalRead(SensorE) == HIGH) {
    Serial.write("E");
  } else {
    Serial.write("e");
  }
}
