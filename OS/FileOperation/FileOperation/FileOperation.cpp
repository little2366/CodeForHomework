#include<iostream>
#include <fstream>  
#include <string>  
using namespace std;
#define MAX_SIZE 100
int main(){
    string line;  
	string outLine[MAX_SIZE];
	ifstream infile1;
	infile1.open("B.txt");
	int i = 0;
	//判定文件内容是否为空
	string ch;
	getline(infile1, ch, '\n');
	if(!infile1.eof()){
		while (!infile1.eof()){
			getline(infile1, line, '\n');
			outLine[i] = line;
			i++;
		}
	}
	//cout << i;
	infile1.close();
	ifstream infile2;
    ofstream outfile;
	infile2.open("A.txt");
	outfile.open("B.txt");
	if(infile2 && outfile){  
		int type = 0;
		cout << "选择文件操作的方式：1---覆盖  2---追加" <<endl;
		cin >> type;
		if(type == 1){
			while (!infile2.eof()){
				getline(infile2, line, '\n');
				outfile << line << "\n";
			}
		}
		else{
			outfile << ch << "\n";
			for(int j=0; j<i; ++j)
				outfile << outLine[j] << "\n";
			while (!infile2.eof()){
				getline(infile2, line, '\n');
				outfile << line << "\n";
			}
		}
	    cout << "复制完成！" << endl;
		infile2.close();
		outfile.close();
		int isDelete = 0;
		cout << "是否删除源文件？ 1---删除  2---不删除" <<endl;
		cin >> isDelete;
		if(isDelete == 1){
            system("DEL /f A.txt");
			cout << "已删除源文件！" <<endl;
		}
    }  
    else
        cout <<"no such file" << endl; 
	system("pause");
	return 0;
}