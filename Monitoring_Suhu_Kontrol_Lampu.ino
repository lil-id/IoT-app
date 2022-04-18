#include <WiFi.h>
#include "DHT.h"
#define DHTPIN 13
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

#include <FirebaseESP32.h>
#define WIFI_SSID ""
#define WIFI_PASSWORD ""
#define FIREBASE_HOST ""
#define FIREBASE_AUTH ""

FirebaseData iot;
FirebaseJson json;

void printResult(FirebaseData &data);
unsigned long wsb = 0;
unsigned long interval = 1000;
int led1 = 14;

void setup() {
  // put your setup code here, to run once:

  pinMode (14, OUTPUT);
  pinMode (13, INPUT);
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  dht.begin();
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED);
  {
    Serial.print(".");
    delay(300);
  }

  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

}

void loop() {

  String path = "/suhu/";
  int hum = dht.readHumidity();
  int temp = dht.readTemperature();
  unsigned long ws = millis();

  if (Firebase.getInt(iot, "/lampu")) {
    int nilai = iot.intData();
    if (nilai == 1) {
      digitalWrite(led1, HIGH);
      Serial.println("lampu nyala");
    } else if (nilai == 0) {
      digitalWrite(led1, LOW);
      Serial.println("lampu mati");
    } else if (nilai == 2) {
      digitalWrite(led1, HIGH);
      delay(500);
      digitalWrite(led1, LOW);
      Serial.println("lampu kedip");
    }
  }

  if (ws - wsb >= interval) {
    Serial.print("Suhu: ");
    Serial.print(temp);
    Serial.print("H: ");
    Serial.println(hum);
    json.set("suhu", temp);
    json.set("hum", hum);
    Firebase.setJSON(iot, path, json);
    wsb = ws;
  }

}
