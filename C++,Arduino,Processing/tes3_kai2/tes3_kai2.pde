import processing.serial.*;
Serial myPort;
PrintWriter output1,output2,output3;
int inByte;
float diameter;
int[]   dataX,dataY;
String[] s1={""};
String[] s2={""};
String[] s3={""};

void setup(){
    size(400,250);
    dataX = new int [width];
    dataY = new int [width];
    println(Serial.list());
    myPort=new Serial(this,Serial.list()[0],9600);
    myPort.clear();
    output1 = createWriter("light.txt");
    output2 = createWriter("x.txt");
    output3 = createWriter("y.txt");
}

void draw(){

  background(0);
  strokeWeight(2); 
  stroke(0, 255, 0);
  for (int i=0; i<dataX.length-1; i++) {
    line( i, convToGraphPoint(dataX[i]), i+1, convToGraphPoint(dataX[i+1]) );
  }
  
  stroke(255, 0, 0);
  for (int i=0; i<dataY.length-1; i++) {
    line( i, convToGraphPoint(dataY[i]), i+1, convToGraphPoint(dataY[i+1]) );
  } 
}

void serialEvent(Serial port){ 
 if ( port.available() >= 6 ) {//
    if ( port.read() == 'H' ) {
      int highX = port.read();
      int lowX = port.read();
      int highY = port.read();
      int lowY = port.read();
      int light = port.read();
      int inByteX= highX*256 + lowX;  // 上位・下位を合体させる
      int inByteY= highY*256 + lowY;
      println(inByteY);
      s1[0] = String.valueOf(light);
      s2[0] = String.valueOf(inByteX);
      s3[0] = String.valueOf(inByteY);
      saveStrings("x.txt", s2);
      saveStrings("light.txt", s1);
      saveStrings("y.txt", s3);
      /*output1.println(s1);
      output2.println(s2);
      output3.println(s3);
      output1.flush();
      output2.flush();
      output3.flush();*/
      
      for (int i=0; i<dataX.length-1; i++) {
        dataX[i] = dataX[i+1];
      }
      dataX[dataX.length-1] = inByteX;
      
      for (int i=0; i<dataY.length-1; i++) {
        dataY[i] = dataY[i+1];
      }
      dataY[dataY.length-1] = inByteY;
    }
    
  }

}
void keyPressed(){
    output1.close();
    output2.close();
    output3.close();
    exit();
}
float convToGraphPoint(int value) {
  return (height - value*height/1024.0);
}
