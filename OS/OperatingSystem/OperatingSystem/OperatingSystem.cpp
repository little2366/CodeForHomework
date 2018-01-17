#include<iostream>
#include<stdio.h>
#include<stdlib.h>
using namespace std;
#define MAX_SIZE 100
//FCFS 先来先服务
void fcfs(){
	int process,sum_time=0;
    double average_wait,sum_wait=0;
    int burst_time[MAX_SIZE], now_time[MAX_SIZE];
	int choose;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("FCFS.txt","r");
		fscanf(fread,"%d",&process);
		cout<<"进程数："<<process<<endl;
		for(int i=1; i<=process; ++i){
			fscanf(fread,"%d",&burst_time[i]);
			cout<<"P"<<i<<" "<<burst_time[i]<<endl;
			sum_time += burst_time[i];
		}
	}
	else {
		cout<<"请输入进程的个数：";
		cin>>process;
		for(int i=1; i<=process; ++i){
			cout<<"P"<<i<<" ";
			cin>>burst_time[i];
			sum_time += burst_time[i];
		}
	}
    now_time[0] = 0;
    for(int i=1; i<=process; ++i){
	    now_time[i] = now_time[i-1] + burst_time[i];
	    printf("P%d服务时间为%d，当前时间为%d\n",i, burst_time[i], now_time[i]);
    }
    for(int i=0; i<process; ++i)
	    sum_wait += now_time[i];
    average_wait = sum_wait / process;
    printf("总服务时间为%d，平均等待时间为%.2f%\n\n\n",sum_time,average_wait);
}
//SJF 短作业优先
typedef struct SJF{
	int id;
	int arrive_time;
	int burst_time;
	int start_time;
}SJF;
SJF process2[MAX_SIZE];
int comp1(const void*p,const void*q){
	return ((struct SJF *)p)->burst_time > ((struct SJF *)q)->burst_time;    //根据服务时间进行从小到大排序
}
void sjf(){
	int count, nowTime=0, sum_wait = 0;
	double average_wait;
	int choose;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("SRJF.txt","r");
		fscanf(fread,"%d",&count);
		cout<<"进程数："<<count<<endl;
		for(int i=0; i<count; ++i){
			fscanf(fread,"%d%d",&process2[i].arrive_time, &process2[i].burst_time);
			process2[i].id = i+1;
			cout<<"P"<<process2[i].id<<" "<<process2[i].arrive_time<<" "<<process2[i].burst_time<<endl;
		}
	}
	else{
		cout<<"请输入进程的个数：";
		cin>>count;
		cout<<"请输入进程的到达时间和对应的服务时间\n";
		for(int i=0; i<count; i++){
			cout<<"P"<<i+1<<" ";
			process2[i].id = i+1;
			cin>>process2[i].arrive_time>>process2[i].arrive_time;
		}
	}
	nowTime += process2[0].burst_time;
    printf("P%d服务时间为%d，当前时间为%d\n",process2[0].id, process2[0].burst_time, nowTime);
	process2[0].start_time = 0;
	qsort(process2, count, sizeof(struct SJF), comp1);
    for(int i=0; i<count; ++i){
		if(process2[i].id == 1)
			continue;
		process2[i].start_time = nowTime;
		nowTime += process2[i].burst_time;
	    printf("P%d服务时间为%d，当前时间为%d\n",process2[i].id, process2[i].burst_time, nowTime);
    }
    for(int i=0; i<count; ++i){
		sum_wait = sum_wait + (process2[i].start_time - process2[i].arrive_time);
	}
    average_wait = (double)sum_wait / count;
	printf("总服务时间为%d，平均等待时间为%.2f%\n\n\n",nowTime, average_wait);
}
// SRJF 短作业优先
typedef struct SRJF{
	int id;
	int arriveTime;
	int burnTime;
	int endTime;
	int tempBurn;
}SRJF;
SRJF process3[MAX_SIZE];
int comp2(const void*p,const void*q){
	if (((struct SRJF *)p)->tempBurn == ((struct SRJF *)q)->tempBurn)    
		return ((struct SRJF *)p)->arriveTime > ((struct SRJF *)q)->arriveTime;
	return ((struct SRJF *)p)->tempBurn > ((struct SRJF *)q)->tempBurn;
}
void srjf(){
	int choose, count;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("SRJF.txt","r");
		fscanf(fread,"%d",&count);
		cout<<"进程数："<<count<<endl;
		for(int i=0; i<count; ++i){
			fscanf(fread,"%d%d",&process3[i].arriveTime, &process3[i].burnTime);
			process3[i].id = i+1;
			cout<<"P"<<process3[i].id<<" "<<process3[i].arriveTime<<" "<<process3[i].burnTime<<endl;
			process3[i].tempBurn = process3[i].burnTime;
		}
	}
	else{
		cout<<"请输入进程的个数：";
		cin>>count;
		cout<<"请输入进程的到达时间和对应的服务时间\n";
		for(int i=0; i<count; i++){
			cout<<"P"<<i+1<<" ";
			process3[i].id = i+1;
			cin>>process3[i].arriveTime>>process3[i].burnTime;
			process3[i].tempBurn = process3[i].burnTime;
		}
	}
	int nowTime = 0;
	int lastburn = -1;
	int lastid = 1;
	int forTime = 0;
	while (true){
		qsort(process3, count, sizeof(struct SRJF), comp2);
		int i = 0;
		int indi = 0;
		for (indi = 0; indi<count; indi++){

			if (process3[indi].tempBurn != 0)
				break;
		}
		if (nowTime == 27)
			cout << "000" << endl;
		if (indi == count)
			break;
		for (i = 0; i<count; i++){

			if (process3[i].tempBurn == 0)
				continue;
			if (process3[i].arriveTime>nowTime)
				continue;
			break;
		}
		if (i != count){
			if (lastburn == process3[i].burnTime){
				for (int j = 0; j<count; j++){
					if (lastid == process3[j].id){
						i = j;
						break;
					}
				}
			}
			lastburn = process3[i].tempBurn - 1;
			if(process3[i].id != lastid){
				printf("P%d服务时间为%d，当前时间为%d\n",lastid, forTime, nowTime);
				lastid = process3[i].id;
				forTime = 0;
			}
			forTime++;
			process3[i].tempBurn--;
			if (process3[i].tempBurn == 0)
			{
				process3[i].endTime = nowTime;
			}
			nowTime++;
		}
		else
		{
			//printf("no ");
			nowTime++;
		}
	}
	printf("P%d服务时间为%d，当前时间为%d\n",lastid, forTime, nowTime);
	int total = 0;
	for (int i = 0; i<count; i++)
		total += process3[i].endTime - process3[i].burnTime - process3[i].arriveTime + 1;
	printf("总服务时间为%d，平均等待时间为%.2f%\n\n\n",total, total / (double)count);
}
//PS 优先级调度
typedef struct PS {
	int id;
	int time;
	int endTime;
	int waitTime;
	int privilege;
}PS;
PS process4[MAX_SIZE];
int comp3(const void*p,const void*q){
	return ((struct PS *)p)->privilege > ((struct PS *)q)->privilege;
}
void ps(){
	int totalTime = 0;
	double average_waitTime = 0;
	memset(process4,0,sizeof(process4));
	int choose, count;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("RP.txt","r");
		fscanf(fread,"%d",&count);
		cout<<"进程数："<<count<<endl;
		for(int i=0; i<count; ++i){
			fscanf(fread,"%d%d",&process4[i].time, &process4[i].privilege);
			process4[i].id = i+1;
			cout<<"P"<<process4[i].id<<" "<<process4[i].time<<" "<<process4[i].privilege<<endl;
		}
	}
	else{
		cout<<"请输入进程的个数：";
		cin>>count;
		cout<<"请输入进程的服务时间和对应的优先级\n";
		for(int i = 0;i<count;i++) {
			cout<<"P"<<i+1<<" ";
			process4[i].id = i+1;
			cin>>process4[i].time>>process4[i].privilege;
		}
	}
	qsort(process4, count, sizeof(struct PS), comp3);
	for(int i=0;i<count;i++) {
		printf("P%d服务时间为%d，",process4[i].id, process4[i].time);
		totalTime += process4[i].time;
		process4[i].endTime += totalTime;
		process4[i].waitTime = process4[i].endTime-process4[i].time;
		average_waitTime += process4[i].waitTime;
		printf("当前时间为%d\n",process4[i].endTime);
	}
	printf("总服务时间为%d，平均等待时间为%.2f%\n\n\n",totalTime, average_waitTime/count);
}
//RR 时间片轮转
typedef struct RR{
	int id;
	int duration;
	int re;
}RR;
RR process5[MAX_SIZE];
void rr(){
	int count, q, now=0, aver=0;
	int choose;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("RR.txt","r");
		fscanf(fread,"%d",&count);
		cout<<"进程数："<<count<<endl;
		fscanf(fread,"%d",&q);
		cout<<"时间片数："<<q<<endl;
		for(int i=0; i<count; ++i){
			fscanf(fread,"%d",&process5[i].duration);
			process5[i].id = i+1;
			cout<<"P"<<process5[i].id<<" "<<process5[i].duration<<endl;
			process5[i].re = process5[i].duration;
		}
	}
	else{
		cout<<"请输入进程数：";
		cin>>count;
		cout<<"输入时间片：";
		cin>>q;
		printf("输入%d个进程的区间: \n",count);
		for(int i=0;i<count; i++){
			cout<<"P"<<i+1<<" ";
			process5[i].id = i+1;
			cin>>process5[i].duration;
			process5[i].re = process5[i].duration;
		} 
	}
	while(1){
		bool isfinish = true;
		for(int i=0; i<count; i++)
		{
			if(process5[i].re != 0){
				printf("第%d时刻，做第%d个进程", now, i+1); 
				isfinish = false;
				if(process5[i].re <= q){
					now += process5[i].re;
					process5[i].re = 0;
					aver += now-process5[i].duration;
				}
				else {
					now += q;
					process5[i].re -= q;
				}
				cout<<endl;
			}
		}
		if(isfinish){
			printf("总服务时间为%d，", now);
			break;
		}
	}
	printf("平均等待时间是 %.2lf\n\n\n", (double)aver/count);
}
// HRR 最高响应比
typedef struct HRR{
	int id;
	double duration;
	double arrival;
	bool isfinish;
	double RP;
}HRR;
HRR process6[MAX_SIZE];
int cmp_RP(const void*p,const void*q){
	return ((struct HRR *)p)->RP < ((struct HRR *)q)->RP;
}
int cmp_arr(const void*p,const void*q){
	return ((struct HRR *)p)->arrival > ((struct HRR *)q)->arrival;
}
int cmp_id(const void*p,const void*q){
	return ((struct HRR *)p)->id > ((struct HRR *)q)->id;
}
void hrr(){
	int count, last;
    double now=0, aver=0;
	int choose;
	cout<<"是否从文件中读取数据？ 1---是  2---不是"<<endl;
	cin>>choose;
	if(choose == 1){
		FILE *fread = fopen("HRR.txt","r");
		fscanf(fread,"%d",&count);
		cout<<"进程数："<<count<<endl;
		for(int i=0; i<count; ++i){
			fscanf(fread,"%lf%lf",&process6[i].duration, &process6[i].arrival);
			process6[i].id = i+1;
			cout<<"P"<<process6[i].id<<" "<<process6[i].duration<<" "<<process6[i].arrival<<endl;
			process6[i].isfinish = false;
		}
	}
	else{
		cout<<"请输入进程数：";
		cin>>count;
		printf("输入进程的服务时间和对应的到达时间: \n");
		for(int i=0;i<count; i++){
			cout<<"P"<<i+1<<" ";
			process6[i].id = i+1;
			cin>>process6[i].duration;
			cin>>process6[i].arrival; 
			process6[i].isfinish = false;
		} 
	}
	qsort(process6, count, sizeof(struct HRR), cmp_arr);
	now = process6[0].arrival; 
	last = process6[0].id;
	printf("第%.1f时刻，先做P%d进程\n",now, process6[0].id);
	now += process6[0].duration;
	process6[0].isfinish = true;
	for(int i=0; i<count-1; i++){
		printf("以P%d进程为参照物：\n",last);
		qsort(process6, count, sizeof(struct HRR), cmp_id);
		for(int i = 0; i < count; i++){
			if(process6[i].isfinish == false && process6[i].arrival <= now){
				process6[i].RP = (now - process6[i].arrival) / process6[i].duration + 1;
				printf("R%d = %.2lf  ", process6[i].id, process6[i].RP); 
			}
			else process6[i].RP = -1;
		} 
		printf("\n");
		qsort(process6, count, sizeof(struct HRR), cmp_RP);
		printf("R%d最大，做P%d进程\n", process6[0].id, process6[0].id);
		printf("第%.1lf时刻，做P%d进程\n", now, process6[0].id);
		aver += now - process6[0].arrival;
		now += process6[0].duration;
		process6[0].isfinish = true;
		last = process6[0].id;
	}
	printf("总服务时间为%.1f，平均等待时间为%.2f%\n\n\n",now, (double)aver/count);
}
int main(){
	int choose,flag = 1;
	cout<<"*********   操作系统进程调度算法   *********\n";
	while(flag){
		cout<<"1. FCFS 先来先服务\n";
		cout<<"2. SJF 短作业优先（非抢占式）\n";
		cout<<"3. SRJF 短作业优先（抢占式）\n";
		cout<<"4. PR 优先级调度\n";
		cout<<"5. RR 时间片轮转\n";
		cout<<"6. HRR 最高响应比\n";
		cout<<"7. 退出\n";
		cout<<"请输入你的选择：";
		cin>>choose;
		switch(choose){
		   case 1:
			   fcfs();
			   break;
		   case 2:
			   sjf();
			   break;
		   case 3:
			   srjf();
			   break;
		   case 4:
			   ps();
			   break;
		   case 5:
			   rr();
			   break;
		   case 6:
			   hrr();
			   break;
		   case 7:
			   flag = 0;
		}
	}
	system("pause");
	return 0;
}