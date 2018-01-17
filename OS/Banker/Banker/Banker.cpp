#include<iostream>
#include<stdio.h>
using namespace std;
#define MAX_SIZE 100
int nowRs[MAX_SIZE], nowRs1[MAX_SIZE];
int avaiRs[MAX_SIZE][MAX_SIZE], avaiRs1[MAX_SIZE][MAX_SIZE];
int MaxRs[MAX_SIZE][MAX_SIZE], MaxRs1[MAX_SIZE][MAX_SIZE];
int needRs[MAX_SIZE][MAX_SIZE], needRs1[MAX_SIZE][MAX_SIZE];
int flag[MAX_SIZE];
int ans[MAX_SIZE];
int process, resource;
int n=0;
void toCalculate(int nump){
	int tempnow[200];
	for (int i = 0; i<resource; i++){
		tempnow[i] = nowRs[i];
	}
	for (int i = 0; i < process; i++){
		int j = 0;
		if(flag[i]==1)
            continue;
		for (j = 0; j < resource; j++){
			if (nowRs[j] < needRs[i][j])
				break;
		}
		if (j == resource){
		    for(int tempi=0; tempi<resource; tempi++){
                nowRs[tempi] +=avaiRs[i][tempi];
            }
		    ans[nump] = i;
			if (nump == process - 1){
				n++;
				cout<<"安全序列"<<n<<endl;
				for (int ind = 0; ind < process; ind++){
					printf("P%d ", ans[ind]);
				}
				printf("\n");
				break;
			}
			else{
                flag[i] = 1;
				toCalculate(nump + 1);
				flag[i] = 0;
				for(int tempi=0;tempi<resource;tempi++){
                    nowRs[tempi] = tempnow[tempi];
                }

			}
		}
	}

}
int main()
{
	cout<<"*********   操作系统银行家算法   *********\n";
	int c = 0;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>c;
	if(c == 1){
		FILE *fread = fopen("Banker.txt","r");
		fscanf(fread,"%d",&process);
		fscanf(fread,"%d",&resource);
		cout<<"进程数："<<process<<"    "<<"资源个数："<<resource<<endl;
		cout<<"Allocation矩阵"<<endl;
		for (int i = 0; i<process; i++){
			for (int j = 0; j<resource; j++){
				fscanf(fread,"%d",&avaiRs[i][j]);
				avaiRs1[i][j] = avaiRs[i][j];
				cout<<avaiRs[i][j]<<" ";
			}
			printf("\n");
		}
		cout<<"Max矩阵"<<endl;
		for (int i = 0; i<process; i++){
			for (int j = 0; j<resource; j++){
				fscanf(fread,"%d",&MaxRs[i][j]);
				MaxRs1[i][j] = MaxRs[i][j];
				cout<<MaxRs[i][j]<<" ";
				needRs[i][j] = MaxRs[i][j] - avaiRs[i][j];
			    needRs1[i][j] = needRs[i][j];
			}
			printf("\n");
		}
		cout<<"Available可用资源矩阵"<<endl;
		for (int i = 0; i<resource; i++){
			fscanf(fread,"%d",&nowRs[i]);
			nowRs1[i] = nowRs[i];
			cout<<nowRs[i]<<" ";
		}
		cout<<endl;
	}
	else{
		printf("输入进程的个数，输入资源的个数：\n");
		cin>> process >> resource;

		for (int ind = 0; ind<2; ind++){
			switch (ind){
			case 0:
				printf("输入Allocation矩阵\n");
				break;
			case 1:
				printf("输入Max矩阵\n");
				break;
			}
			for (int i = 0; i<process; i++){
				for (int j = 0; j<resource; j++){
					if (ind == 0){
						cin >> avaiRs[i][j];
						avaiRs1[i][j] = avaiRs[i][j];
					}
					if (ind == 1){
						cin >> MaxRs[i][j];
						MaxRs1[i][j] = MaxRs[i][j];
						needRs[i][j] = MaxRs[i][j] - avaiRs[i][j];
						needRs1[i][j] = needRs[i][j];
					}
				}
			}
		}
		printf("输入Available可用资源矩阵\n");
		for (int i = 0; i<resource; i++){
			cin >> nowRs[i];
			nowRs1[i] = nowRs[i];
		}
	}
	printf("根据Max = Allocation + Need计算得出Need矩阵\n");
	for (int i = 0; i<process; i++){
		for (int j = 0; j<resource; j++){
			printf("%d ", needRs[i][j]);
		}
		printf("\n");
	}
	toCalculate(0);
	int choose = 0;
    cout<<"是否设置预分配资源？ 1---是  2---否"<<endl;
	cin>>choose;
	if(choose == 1){
		n =0;
		printf("输入预分配资源\n");
		int indn;
		int request[200];
		cin>>indn;
		bool x = true;
		for(int i=0; i<resource; ++i){
			nowRs[i] = nowRs1[i];
		}
		for(int i=0; i<process; ++i){
			for(int j=0; j<resource; ++j){
				avaiRs[i][j] = avaiRs1[i][j];
				MaxRs[i][j] = MaxRs1[i][j];
				needRs[i][j] = needRs1[i][j];
		    }
		}
		for(int i=0;i<resource&&x;i++){
			cin>>request[i];
			if(nowRs[i]<request[i])
				x=false;
			nowRs[i]-=request[i];
			avaiRs[indn][i]+=request[i];
            needRs[indn][i]-=request[i];
		}
		if(x==false){
				cout<<"预分配失败"<<endl;
				system("pause");
				return 0;
		}
		for(int i=0; i<MAX_SIZE; ++i){
			flag[i] = 0;
		}
		toCalculate(0);
	}
	system("pause");
}
