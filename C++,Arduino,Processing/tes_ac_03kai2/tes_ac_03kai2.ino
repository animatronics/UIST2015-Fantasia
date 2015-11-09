const int bufferLength= 5;//Meanフィルタ用バッファの長さ
const int threshold = 80; //センサが振られたしきい値
const int sensorX = 3; //X軸のアナログピン
const int sensorY = 4;
const int sensorZ = 5;
const int ledPin = 9; //LED接続したピンの番号
int buffer[bufferLength]; //Meanフィルタ用バッファ
int index = 0; //バッファにデータを書き込むインデックス
float intensity = 0; //LEDの輝度
unsigned long NT;
int d=100;
int light=0;
int i;
long x,y,z;
int n=100; 

void setup()
{
  pinMode(ledPin,OUTPUT); //LED接続したピンのモードを出力にセット
   //Meanフィルタ用バッファを中央値で初期化
  for(int i=0;i<bufferLength;i++){
    buffer[i]=525;
  }
  Serial.begin(9600) ;// シリアルモニターの初期化をする
  NT=millis()+d;
}

void loop()
{
if (NT <= millis()) {
    NT += d;  // Set Next analyze time
    
//100回センサ値を読み込んで平均を算出
x=y=z=0;
for (i=0 ; i <n ; i++) {
x = x + analogRead(3) ; // Ｘ軸
y = y + analogRead(4) ; // Ｙ軸
z = z + analogRead(5) ; // Ｚ軸
}
x = x / n ;
y = y / n ;
z = z / n ;
int rotateX = (x-264)/2.62 - 90; //角度を求める式
int rotateY = (y-287)/2.62 - 90;

  int raw = analogRead(sensorY) ; //Y軸のアナログピンでセンサの値を読み取る
  int smoothed = processSample(raw); //スムージング あとで関数
  int diff= abs(raw - smoothed);

  if(diff>threshold){
    intensity = 255;
    light =1;
  }else{
    intensity = intensity * 0.8;
    light=0;
  }
  analogWrite(ledPin,round(intensity)); //LEDの輝度セット

Serial.write('H');
Serial.write(highByte(x));
Serial.write(lowByte(x));
Serial.write(highByte(y));
Serial.write(lowByte(y));
Serial.write(light);
}
}



int processSample(int raw){
  buffer[index]=raw;
  index = (index+1)%bufferLength;
  long sum=0;

  for(int i=0;i<bufferLength;i++){
    sum+=buffer[i];
  }
  return (int)(sum/bufferLength);
}

