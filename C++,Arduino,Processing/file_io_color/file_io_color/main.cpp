

#include "opencv2\opencv.hpp"
#include "Labeling.h"
using namespace cv;

#include <fstream>

#include <iostream>
using namespace std;


int main(int argc, char* argv[])
{
	// カメラ映像の幅と高さ
	int w = 640;
	int h = 480;
	int count = 0;
	Mat im, hsv, mask;					// 画像オブジェクトの作成
	LabelingBS labeling;				// ラベリング関連の変数
	RegionInfoBS *ri;
	short *mask2 = new short[w * h];//ラベリング出力先
	VideoCapture cap(0);				// カメラのキャプチャ
	if (!cap.isOpened()) return -1;		// キャプチャ失敗時のエラー処理

	Mat mask_noise;
	int count1=0;

	while (1){
		cap >> im;								// カメラから画像を取得
		cvtColor(im, hsv, CV_BGR2HSV);			// 画像をRGBからHSVに変換
		//inRange(hsv, Scalar(0, 0, 129), Scalar(69, 152, 255), mask);	//　白Scalar(0, 0, 129), Scalar(69, 152, 255)
		//inRange(hsv, Scalar(15, 100, 70), Scalar(40, 255, 255), mask);	// 色検出でマスク画像の作成(黄)

		//inRange(hsv, Scalar(150, 70, 70), Scalar(360, 255, 255), mask);
		inRange(hsv, Scalar(162, 112, 105), Scalar(179, 255, 172), mask);
		//inRange(hsv, Scalar(150, 112, 105), Scalar(179, 255, 172), mask);

		while (count1 < 5)
		{
			erode(mask, mask, Mat());
			count1++;
		}

		count1 = 0;

		while (count1 < 5)
		{
			dilate(mask, mask, Mat());
			count1++;
		}

		count1 = 0;

		// 白領域が無い場合のエラー処理
		rectangle(mask, Point(0, 0), Point(1, 1), Scalar(255), -1);
		//ラベリング処理
		labeling.Exec((uchar *)mask.data, mask2, w, h, true, 30);
		//最大の領域を四角で囲む
		ri = labeling.GetResultRegionInfo(0);
		int x1, y1, x2, y2;
		ri->GetMin(x1, y1);
		ri->GetMax(x2, y2);

		//cout << x1 << "," << x2 << "," << y1 << "," << y2 << endl;
		int x3, y3;
		y3 = y2 - y1;
		x3 = x2 - x1;
		if (x3 > 30 && y3 > 30){
			cout << "ok" << endl;
			std::ofstream ofs("test.txt", std::ios::out | std::ios::trunc);
			ofs << 1 << std::endl;  //検出されているときのファイル書き出し
		}
		else {
			cout << "ng" << endl;
			std::ofstream ofs("test.txt", std::ios::out | std::ios::trunc);
			ofs << 0 << std::endl;  //検出されていないときのファイル書き出し
		}

		std::ofstream ofs1("test1.txt", std::ios::out | std::ios::trunc);
		ofs1 << y1 << std::endl;  //検出されているときのファイル書き出し
		std::ofstream ofs2("test2.txt", std::ios::out | std::ios::trunc);
		ofs2 << x1 << std::endl;  //検出されているときのファイル書き出し
		std::ofstream ofs3("test3.txt", std::ios::out | std::ios::trunc);
		ofs3 << y2 << std::endl;  //検出されているときのファイル書き出し
		std::ofstream ofs4("test4.txt", std::ios::out | std::ios::trunc);
		ofs4 << x2 << std::endl;  //検出されているときのファイル書き出し

		rectangle(im, Point(x1, y1), Point(x2, y2), Scalar(255, 0, 0), 3);
		imshow("Camera", im);					// カメラ映像の表示
		//imshow("Mask", mask);					// マスク画像の作成

		//imshow("noise", mask_noise);
		if (waitKey(30) >= 0){					// 任意のキー入力があれば終了
			break;
		}
	}
	return 0;
}